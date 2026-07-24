package com.cosmicodyssey.rpg.activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cosmicodyssey.rpg.R;
import com.cosmicodyssey.rpg.data.DataManager;
import com.cosmicodyssey.rpg.models.Rule;

import java.util.ArrayList;
import java.util.List;

public class RulebookActivity extends AppCompatActivity {
    private RecyclerView ruleList;
    private TextView pageIndicator;
    private DataManager dataManager;
    private List<Rule> rules;
    private int currentPage = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rulebook);

        dataManager = new DataManager(this);
        ruleList = findViewById(R.id.ruleList);
        pageIndicator = findViewById(R.id.pageIndicator);

        ruleList.setLayoutManager(new LinearLayoutManager(this));

        loadDefaultRules();
        loadAIGeneratedRules();
        displayRules();

        findViewById(R.id.prevPageBtn).setOnClickListener(v -> prevPage());
        findViewById(R.id.nextPageBtn).setOnClickListener(v -> nextPage());
    }

    private void loadDefaultRules() {
        rules = new ArrayList<>();

        Rule r1 = new Rule();
        r1.setTitle("Création de Personnage");
        r1.setCategory("Base");
        r1.setContent("Chaque joueur crée un personnage en choisissant une race, une classe et un background. Les stats de base sont réparties entre Force, Dextérité, Constitution, Intelligence, Sagesse, Charisme et Chance.");
        r1.setPageNumber(1);
        r1.setOfficial(true);
        rules.add(r1);

        Rule r2 = new Rule();
        r2.setTitle("Système de Combat");
        r2.setCategory("Combat");
        r2.setContent("Le combat se déroule au tour par tour. Chaque action nécessite un jet de dé (D20) + modificateur de stat. Un 20 naturel est un coup critique, un 1 est un échec critique.");
        r2.setPageNumber(2);
        r2.setOfficial(true);
        rules.add(r2);

        Rule r3 = new Rule();
        r3.setTitle("Voyage Spatial");
        r3.setCategory("Exploration");
        r3.setContent("Les joueurs peuvent voyager entre les systèmes stellaires en utilisant leur vaisseau. Chaque saut consomme de l'énergie. Les systèmes non découverts doivent être scannés avant le voyage.");
        r3.setPageNumber(3);
        r3.setOfficial(true);
        rules.add(r3);

        Rule r4 = new Rule();
        r4.setTitle("Psioniques et Technologie");
        r4.setCategory("Pouvoirs");
        r4.setContent("Certaines races possèdent des capacités psioniques. Les compétences technologiques permettent de pirater, réparer et améliorer l'équipement. Chaque utilisation consomme des points d'énergie.");
        r4.setPageNumber(4);
        r4.setOfficial(true);
        rules.add(r4);

        Rule r5 = new Rule();
        r5.setTitle("Commerce et Économie");
        r5.setCategory("Économie");
        r5.setContent("Les crédits galactiques sont la monnaie universelle. Les marchands proposent des objets de différentes raretés. Les prix varient selon la faction dominante et la rareté de l'objet.");
        r5.setPageNumber(5);
        r5.setOfficial(true);
        rules.add(r5);
    }

    private void loadAIGeneratedRules() {
        // Load rules created by AI during gameplay
        // These are stored in the database and shared across parties
    }

    private void displayRules() {
        ruleList.setAdapter(new RuleAdapter());
        updatePageIndicator();
    }

    private void updatePageIndicator() {
        pageIndicator.setText("Page " + (currentPage + 1) + " / " + ((rules.size() / 5) + 1));
    }

    private void prevPage() {
        if (currentPage > 0) {
            currentPage--;
            displayRules();
        }
    }

    private void nextPage() {
        if ((currentPage + 1) * 5 < rules.size()) {
            currentPage++;
            displayRules();
        }
    }

    class RuleAdapter extends RecyclerView.Adapter<RuleAdapter.ViewHolder> {
        @Override
        public ViewHolder onCreateViewHolder(android.view.ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rule_page, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            int index = currentPage * 5 + position;
            if (index >= rules.size()) {
                holder.itemView.setVisibility(View.GONE);
                return;
            }

            Rule rule = rules.get(index);
            holder.itemView.setVisibility(View.VISIBLE);
            holder.titleText.setText(rule.getTitle());
            holder.categoryText.setText("📖 " + rule.getCategory());
            holder.contentText.setText(rule.getContent());
            holder.pageNumber.setText(String.valueOf(rule.getPageNumber()));

            if (rule.isOfficial()) {
                holder.officialBadge.setVisibility(View.VISIBLE);
            } else {
                holder.officialBadge.setVisibility(View.GONE);
            }
        }

        @Override public int getItemCount() { return 5; }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView titleText, categoryText, contentText, pageNumber, officialBadge;

            ViewHolder(View itemView) {
                super(itemView);
                titleText = itemView.findViewById(R.id.ruleTitle);
                categoryText = itemView.findViewById(R.id.ruleCategory);
                contentText = itemView.findViewById(R.id.ruleContent);
                pageNumber = itemView.findViewById(R.id.pageNumber);
                officialBadge = itemView.findViewById(R.id.officialBadge);
            }
        }
    }
}
