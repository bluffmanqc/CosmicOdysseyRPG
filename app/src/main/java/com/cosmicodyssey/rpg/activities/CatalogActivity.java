package com.cosmicodyssey.rpg.activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.cosmicodyssey.rpg.R;
import com.cosmicodyssey.rpg.data.DataManager;
import com.cosmicodyssey.rpg.models.Cargo;
import com.cosmicodyssey.rpg.models.Companion;
import com.cosmicodyssey.rpg.models.Equipment;
import com.cosmicodyssey.rpg.models.Mount;
import com.cosmicodyssey.rpg.models.Rarity;
import com.cosmicodyssey.rpg.models.Spaceship;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

public class CatalogActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private DataManager dataManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        dataManager = new DataManager(this);

        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);

        setupTabs();
    }

    private void setupTabs() {
        tabLayout.addTab(tabLayout.newTab().setText("⚔️ Armes"));
        tabLayout.addTab(tabLayout.newTab().setText("🛡️ Armures"));
        tabLayout.addTab(tabLayout.newTab().setText("🐉 Montures"));
        tabLayout.addTab(tabLayout.newTab().setText("🚀 Vaisseaux"));
        tabLayout.addTab(tabLayout.newTab().setText("📦 Cargos"));
        tabLayout.addTab(tabLayout.newTab().setText("👾 Compagnons"));

        CatalogPagerAdapter adapter = new CatalogPagerAdapter();
        viewPager.setAdapter(adapter);

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            switch (position) {
                case 0: tab.setText("⚔️ Armes"); break;
                case 1: tab.setText("🛡️ Armures"); break;
                case 2: tab.setText("🐉 Montures"); break;
                case 3: tab.setText("🚀 Vaisseaux"); break;
                case 4: tab.setText("📦 Cargos"); break;
                case 5: tab.setText("👾 Compagnons"); break;
            }
        }).attach();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }
            @Override public void onTabUnselected(TabLayout.Tab tab) {}
            @Override public void onTabReselected(TabLayout.Tab tab) {}
        });

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });
    }

    class CatalogPagerAdapter extends RecyclerView.Adapter<CatalogPagerAdapter.PageViewHolder> {

        @NonNull
        @Override
        public PageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            RecyclerView recyclerView = new RecyclerView(CatalogActivity.this);
            recyclerView.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));
            recyclerView.setLayoutManager(new GridLayoutManager(CatalogActivity.this, 2));
            return new PageViewHolder(recyclerView);
        }

        @Override
        public void onBindViewHolder(@NonNull PageViewHolder holder, int position) {
            List<CatalogItem> items = new ArrayList<>();

            switch (position) {
                case 0: // Armes
                    for (Equipment e : dataManager.loadEquipmentCatalog()) {
                        if (e.getType() != null && (e.getType().equalsIgnoreCase("Arme") || e.getType().equalsIgnoreCase("Weapon"))) {
                            items.add(new CatalogItem(e.getName(), e.getDescription(), e.getImageUrl(), e.getRarity(), "⚔️ " + e.getDamage()));
                        }
                    }
                    break;
                case 1: // Armures
                    for (Equipment e : dataManager.loadEquipmentCatalog()) {
                        if (e.getType() != null && (e.getType().equalsIgnoreCase("Armure") || e.getType().equalsIgnoreCase("Armor") || e.getType().equalsIgnoreCase("Chest") || e.getType().equalsIgnoreCase("Bouclier") || e.getType().equalsIgnoreCase("Shield"))) {
                            items.add(new CatalogItem(e.getName(), e.getDescription(), e.getImageUrl(), e.getRarity(), "🛡️ " + e.getDefense()));
                        }
                    }
                    break;
                case 2: // Montures
                    for (Mount m : dataManager.loadMountCatalog()) {
                        items.add(new CatalogItem(m.getName(), m.getSpecies(), m.getImageUrl(), m.getRarity(), "🐉 " + m.getSpeed() + " km/h"));
                    }
                    break;
                case 3: // Vaisseaux
                    for (Spaceship s : dataManager.loadSpaceshipCatalog()) {
                        items.add(new CatalogItem(s.getName(), s.getModel(), s.getImageUrl(), s.getRarity(), "🚀 " + s.getSpeed() + " ly/h"));
                    }
                    break;
                case 4: // Cargos
                    items.add(new CatalogItem("Conteneur Standard", "Stockage basique", null, Rarity.COMMON, "📦 50u³"));
                    items.add(new CatalogItem("Coffre Renforcé", "Protection moyenne", null, Rarity.UNCOMMON, "📦 100u³"));
                    break;
                case 5: // Compagnons
                    items.add(new CatalogItem("Drone de Combat", "Petit drone offensif", null, Rarity.COMMON, "👾 Dmg: 5"));
                    items.add(new CatalogItem("Alien Mascotte", "Créature sympathique", null, Rarity.RARE, "👾 CHA +2"));
                    items.add(new CatalogItem("Fantôme Spatial", "Esprit d'un astronaute perdu", null, Rarity.EPIC, "👾 Détecte les pièges"));
                    break;
            }

            if (items.isEmpty()) {
                items.add(new CatalogItem("Objet inconnu", "Aucun objet de cette catégorie découvert", null, Rarity.COMMON, "❓ 0"));
            }

            holder.recyclerView.setAdapter(new CatalogItemAdapter(items));
        }

        @Override
        public int getItemCount() {
            return 6;
        }

        class PageViewHolder extends RecyclerView.ViewHolder {
            RecyclerView recyclerView;
            PageViewHolder(@NonNull View itemView) {
                super(itemView);
                recyclerView = (RecyclerView) itemView;
            }
        }
    }

    class CatalogItem {
        String name, description, imageUrl, statText;
        Rarity rarity;

        CatalogItem(String name, String description, String imageUrl, Rarity rarity, String statText) {
            this.name = name;
            this.description = description;
            this.imageUrl = imageUrl;
            this.rarity = rarity;
            this.statText = statText;
        }
    }

    class CatalogItemAdapter extends RecyclerView.Adapter<CatalogItemAdapter.ViewHolder> {
        private List<CatalogItem> items;

        CatalogItemAdapter(List<CatalogItem> items) {
            this.items = items;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_catalog_card, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            CatalogItem item = items.get(position);
            holder.nameText.setText(item.name);
            holder.descriptionText.setText(item.description);
            holder.statsText.setText(item.statText);
            holder.rarityText.setText(item.rarity != null ? item.rarity.getLabel() : "Commun");

            if (item.imageUrl != null && !item.imageUrl.isEmpty()) {
                Glide.with(CatalogActivity.this)
                        .load(item.imageUrl)
                        .placeholder(R.drawable.ic_item_placeholder)
                        .into(holder.itemImage);
            } else {
                holder.itemImage.setImageResource(R.drawable.ic_item_placeholder);
            }
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            ImageView itemImage;
            TextView nameText, statsText, descriptionText, rarityText;

            ViewHolder(@NonNull View itemView) {
                super(itemView);
                itemImage = itemView.findViewById(R.id.itemImage);
                nameText = itemView.findViewById(R.id.nameText);
                statsText = itemView.findViewById(R.id.statsText);
                descriptionText = itemView.findViewById(R.id.descriptionText);
                rarityText = itemView.findViewById(R.id.rarityText);
            }
        }
    }
}
