package com.cosmicodyssey.rpg.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cosmicodyssey.rpg.R;
import com.cosmicodyssey.rpg.data.DataManager;
import com.cosmicodyssey.rpg.models.Party;

import java.util.List;

public class PartyListActivity extends AppCompatActivity {
    private RecyclerView partyList;
    private Button createPartyBtn;
    private Button joinPartyBtn;
    private DataManager dataManager;
    private List<Party> parties;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_party_list);

        dataManager = new DataManager(this);
        partyList = findViewById(R.id.partyList);
        createPartyBtn = findViewById(R.id.createPartyBtn);
        joinPartyBtn = findViewById(R.id.joinPartyBtn);

        partyList.setLayoutManager(new LinearLayoutManager(this));
        loadParties();

        createPartyBtn.setOnClickListener(v -> showCreatePartyDialog());
        joinPartyBtn.setOnClickListener(v -> showJoinPartyDialog());
    }

    private void loadParties() {
        parties = dataManager.loadParties();
        partyList.setAdapter(new PartyAdapter());
    }

    private void showCreatePartyDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Nouvelle partie");

        View view = getLayoutInflater().inflate(R.layout.dialog_create_party, null);
        EditText nameInput = view.findViewById(R.id.partyNameInput);
        EditText descInput = view.findViewById(R.id.partyDescInput);

        builder.setView(view);
        builder.setPositiveButton("Créer", (dialog, which) -> {
            String name = nameInput.getText().toString().trim();
            if (name.isEmpty()) {
                Toast.makeText(this, "Nom requis", Toast.LENGTH_SHORT).show();
                return;
            }

            Party party = new Party();
            party.setName(name);
            party.setDescription(descInput.getText().toString().trim());
            party.setHostId(dataManager.loadCharacter().getId());
            party.getPlayerIds().add(dataManager.loadCharacter().getId());

            dataManager.saveParty(party);
            loadParties();

            Toast.makeText(this, "Partie créée ! Code: " + party.getPartyCode(), Toast.LENGTH_LONG).show();
        });
        builder.setNegativeButton("Annuler", null);
        builder.show();
    }

    private void showJoinPartyDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Rejoindre une partie");

        EditText input = new EditText(this);
        input.setHint("Code à 6 caractères");
        builder.setView(input);

        builder.setPositiveButton("Rejoindre", (dialog, which) -> {
            String code = input.getText().toString().trim().toUpperCase();
            if (code.length() != 6) {
                Toast.makeText(this, "Code invalide", Toast.LENGTH_SHORT).show();
                return;
            }

            // In local mode, search in existing parties
            for (Party p : parties) {
                if (p.getPartyCode().equals(code)) {
                    p.getPlayerIds().add(dataManager.loadCharacter().getId());
                    dataManager.saveParty(p);
                    dataManager.mergeCatalogsFromParty(p);
                    loadParties();
                    Toast.makeText(this, "Rejoint: " + p.getName(), Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            Toast.makeText(this, "Partie non trouvée", Toast.LENGTH_SHORT).show();
        });
        builder.setNegativeButton("Annuler", null);
        builder.show();
    }

    class PartyAdapter extends RecyclerView.Adapter<PartyAdapter.ViewHolder> {
        @Override
        public ViewHolder onCreateViewHolder(android.view.ViewGroup parent, int viewType) {
            android.view.View view = getLayoutInflater().inflate(R.layout.item_party, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Party party = parties.get(position);
            holder.nameText.setText(party.getName());
            holder.codeText.setText("Code: " + party.getPartyCode());
            holder.playersText.setText(party.getPlayerIds().size() + " joueurs");
            holder.descText.setText(party.getDescription());

            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(PartyListActivity.this, GameSessionActivity.class);
                intent.putExtra("party_id", party.getId());
                startActivity(intent);
            });
        }

        @Override public int getItemCount() { return parties.size(); }

        class ViewHolder extends RecyclerView.ViewHolder {
            android.widget.TextView nameText, codeText, playersText, descText;

            ViewHolder(android.view.View itemView) {
                super(itemView);
                nameText = itemView.findViewById(R.id.partyName);
                codeText = itemView.findViewById(R.id.partyCode);
                playersText = itemView.findViewById(R.id.partyPlayers);
                descText = itemView.findViewById(R.id.partyDesc);
            }
        }
    }
}
