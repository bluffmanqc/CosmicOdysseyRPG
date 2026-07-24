package com.cosmicodyssey.rpg.activities;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cosmicodyssey.rpg.R;
import com.cosmicodyssey.rpg.ai.GameMasterAI;
import com.cosmicodyssey.rpg.data.DataManager;
import com.cosmicodyssey.rpg.models.Character;
import com.cosmicodyssey.rpg.models.Equipment;

public class CharacterSheetActivity extends AppCompatActivity {

    private ImageView avatarImage;
    private TextView nameText;
    private TextView raceClassText;
    private TextView levelText;
    private ProgressBar hpBar;
    private ProgressBar shieldBar;
    private ProgressBar energyBar;
    private TextView hpText;
    private TextView shieldText;
    private TextView energyText;
    private TextView creditsText;
    private TextView strengthText;
    private TextView dexterityText;
    private TextView constitutionText;
    private TextView intelligenceText;
    private TextView wisdomText;
    private TextView charismaText;
    private TextView luckText;
    private ImageView mountImage;
    private ImageView shipImage;
    private ImageView cargoImage;
    private RecyclerView equipmentList;

    private Character character;
    private DataManager dataManager;
    private GameMasterAI ai;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_sheet);

        dataManager = new DataManager(this);
        ai = new GameMasterAI(this);
        character = dataManager.loadCharacter();

        if (character == null) {
            finish();
            return;
        }

        initViews();
        displayCharacter();
    }

    private void initViews() {
        avatarImage = findViewById(R.id.avatarImage);
        nameText = findViewById(R.id.nameText);
        raceClassText = findViewById(R.id.raceClassText);
        levelText = findViewById(R.id.levelText);
        hpBar = findViewById(R.id.hpBar);
        shieldBar = findViewById(R.id.shieldBar);
        energyBar = findViewById(R.id.energyBar);
        hpText = findViewById(R.id.hpText);
        shieldText = findViewById(R.id.shieldText);
        energyText = findViewById(R.id.energyText);
        creditsText = findViewById(R.id.creditsText);
        strengthText = findViewById(R.id.strengthText);
        dexterityText = findViewById(R.id.dexterityText);
        constitutionText = findViewById(R.id.constitutionText);
        intelligenceText = findViewById(R.id.intelligenceText);
        wisdomText = findViewById(R.id.wisdomText);
        charismaText = findViewById(R.id.charismaText);
        luckText = findViewById(R.id.luckText);
        mountImage = findViewById(R.id.mountImage);
        shipImage = findViewById(R.id.shipImage);
        cargoImage = findViewById(R.id.cargoImage);
        equipmentList = findViewById(R.id.equipmentList);
    }

    private void displayCharacter() {
        nameText.setText(character.getName());
        raceClassText.setText(character.getRace() + " | " + character.getClassName());
        levelText.setText("Niveau " + character.getLevel());

        // Avatar avec mise à jour selon équipement
        String avatarUrl = character.getAvatarUrl();
        if (avatarUrl != null) {
            Glide.with(this).load(avatarUrl).placeholder(R.drawable.ic_character_placeholder).into(avatarImage);
        }

        // Stats bars
        hpBar.setMax(character.getMaxHitPoints());
        hpBar.setProgress(character.getHitPoints());
        hpText.setText(character.getHitPoints() + "/" + character.getMaxHitPoints());

        shieldBar.setMax(character.getMaxShieldPoints());
        shieldBar.setProgress(character.getShieldPoints());
        shieldText.setText(character.getShieldPoints() + "/" + character.getMaxShieldPoints());

        energyBar.setMax(character.getMaxEnergy());
        energyBar.setProgress(character.getEnergy());
        energyText.setText(character.getEnergy() + "/" + character.getMaxEnergy());

        creditsText.setText("💰 " + character.getCredits() + " crédits");

        // Character stats
        strengthText.setText("FOR: " + character.getStats().getStrength() + " (" + character.getStats().getModifier(character.getStats().getStrength()) + ")");
        dexterityText.setText("DEX: " + character.getStats().getDexterity() + " (" + character.getStats().getModifier(character.getStats().getDexterity()) + ")");
        constitutionText.setText("CON: " + character.getStats().getConstitution() + " (" + character.getStats().getModifier(character.getStats().getConstitution()) + ")");
        intelligenceText.setText("INT: " + character.getStats().getIntelligence() + " (" + character.getStats().getModifier(character.getStats().getIntelligence()) + ")");
        wisdomText.setText("SAG: " + character.getStats().getWisdom() + " (" + character.getStats().getModifier(character.getStats().getWisdom()) + ")");
        charismaText.setText("CHA: " + character.getStats().getCharisma() + " (" + character.getStats().getModifier(character.getStats().getCharisma()) + ")");
        luckText.setText("LUCK: " + character.getStats().getLuck());

        // Mount
        if (character.getMount() != null) {
            Glide.with(this)
                    .load(character.getMount().getImageUrl())
                    .placeholder(R.drawable.ic_mount_placeholder)
                    .into(mountImage);
        }

        // Spaceship
        if (character.getSpaceship() != null) {
            Glide.with(this)
                    .load(character.getSpaceship().getImageUrl())
                    .placeholder(R.drawable.ic_ship_placeholder)
                    .into(shipImage);
        }

        // Cargo
        if (character.getCargo() != null) {
            Glide.with(this)
                    .load(character.getCargo().getImageUrl())
                    .placeholder(R.drawable.ic_cargo_placeholder)
                    .into(cargoImage);
        }

        // Equipment grid
        equipmentList.setLayoutManager(new GridLayoutManager(this, 3));
        equipmentList.setAdapter(new EquipmentAdapter(character.getEquipments()));
    }

    class EquipmentAdapter extends RecyclerView.Adapter<EquipmentAdapter.ViewHolder> {
        private java.util.List<Equipment> items;

        EquipmentAdapter(java.util.List<Equipment> items) { this.items = items; }

        @Override
        public ViewHolder onCreateViewHolder(android.view.ViewGroup parent, int viewType) {
            android.view.View view = getLayoutInflater().inflate(R.layout.item_equipment_slot, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Equipment item = items.get(position);
            Glide.with(CharacterSheetActivity.this)
                    .load(item.getImageUrl())
                    .placeholder(R.drawable.ic_item_placeholder)
                    .into(holder.imageView);
            holder.rarityView.setBackgroundColor(item.getRarity().getColor());
        }

        @Override public int getItemCount() { return items.size(); }

        class ViewHolder extends RecyclerView.ViewHolder {
            ImageView imageView;
            android.view.View rarityView;
            ViewHolder(android.view.View itemView) {
                super(itemView);
                imageView = itemView.findViewById(R.id.equipmentImage);
                rarityView = itemView.findViewById(R.id.rarityIndicator);
            }
        }
    }
}
