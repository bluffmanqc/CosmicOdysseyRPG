package com.cosmicodyssey.rpg.activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.cosmicodyssey.rpg.R;
import com.cosmicodyssey.rpg.data.DataManager;
import com.cosmicodyssey.rpg.models.Equipment;
import com.cosmicodyssey.rpg.models.Mount;
import com.cosmicodyssey.rpg.models.Rarity;
import com.cosmicodyssey.rpg.models.Spaceship;
import com.google.android.material.tabs.TabLayout;

import java.util.List;

public class CatalogActivity extends AppCompatActivity {
    private ViewPager2 viewPager;
    private TabLayout tabLayout;
    private DataManager dataManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        dataManager = new DataManager(this);
        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);

        setupTabs();
        setupViewPager();
    }

    private void setupTabs() {
        tabLayout.addTab(tabLayout.newTab().setText("⚔️ Armes"));
        tabLayout.addTab(tabLayout.newTab().setText("🛡️ Armures"));
        tabLayout.addTab(tabLayout.newTab().setText("🐉 Montures"));
        tabLayout.addTab(tabLayout.newTab().setText("🚀 Vaisseaux"));
        tabLayout.addTab(tabLayout.newTab().setText("📦 Cargos"));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }
            @Override public void onTabUnselected(TabLayout.Tab tab) {}
            @Override public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    private void setupViewPager() {
        viewPager.setAdapter(new CatalogPagerAdapter());
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });
    }

    class CatalogPagerAdapter extends androidx.recyclerview.widget.RecyclerView.Adapter<CatalogPagerAdapter.PageViewHolder> {
        @Override
        public PageViewHolder onCreateViewHolder(android.view.ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.page_catalog, parent, false);
            return new PageViewHolder(view);
        }

        @Override
        public void onBindViewHolder(PageViewHolder holder, int position) {
            RecyclerView recycler = holder.itemView.findViewById(R.id.catalogRecycler);
            TextView title = holder.itemView.findViewById(R.id.pageTitle);
            recycler.setLayoutManager(new GridLayoutManager(CatalogActivity.this, 2));

            switch (position) {
                case 0:
                    title.setText("Grimoire des Armes");
                    recycler.setAdapter(new EquipmentAdapter(dataManager.loadEquipmentCatalog()));
                    break;
                case 1:
                    title.setText("Grimoire des Armures");
                    recycler.setAdapter(new EquipmentAdapter(dataManager.loadEquipmentCatalog()));
                    break;
                case 2:
                    title.setText("Bestiaire Cosmique");
                    recycler.setAdapter(new MountAdapter(dataManager.loadMountCatalog()));
                    break;
                case 3:
                    title.setText("Hangar Spatial");
                    recycler.setAdapter(new SpaceshipAdapter(dataManager.loadSpaceshipCatalog()));
                    break;
                case 4:
                    title.setText("Cargos Interstellaires");
                    recycler.setAdapter(new EquipmentAdapter(dataManager.loadEquipmentCatalog()));
                    break;
            }
        }

        @Override public int getItemCount() { return 5; }

        class PageViewHolder extends RecyclerView.ViewHolder {
            PageViewHolder(View itemView) { super(itemView); }
        }
    }

    class EquipmentAdapter extends RecyclerView.Adapter<EquipmentAdapter.ViewHolder> {
        private List<Equipment> items;

        EquipmentAdapter(List<Equipment> items) { this.items = items; }

        @Override
        public ViewHolder onCreateViewHolder(android.view.ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_catalog_card, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Equipment item = items.get(position);
            holder.nameText.setText(item.getName());
            holder.rarityText.setText(item.getRarity().getLabel());
            holder.rarityText.setTextColor(item.getRarity().getColor());
            holder.descriptionText.setText(item.getDescription());
            holder.loreText.setText(item.getLore());
            holder.statsText.setText(String.format("⚔️%d 🛡️%d", item.getDamage(), item.getDefense()));

            Glide.with(CatalogActivity.this)
                    .load(item.getImageUrl())
                    .placeholder(R.drawable.ic_item_placeholder)
                    .into(holder.itemImage);

            holder.itemView.setBackgroundResource(getRarityBackground(item.getRarity()));
        }

        @Override public int getItemCount() { return items.size(); }

        class ViewHolder extends RecyclerView.ViewHolder {
            ImageView itemImage;
            TextView nameText, rarityText, descriptionText, loreText, statsText;

            ViewHolder(View itemView) {
                super(itemView);
                itemImage = itemView.findViewById(R.id.itemImage);
                nameText = itemView.findViewById(R.id.nameText);
                rarityText = itemView.findViewById(R.id.rarityText);
                descriptionText = itemView.findViewById(R.id.descriptionText);
                loreText = itemView.findViewById(R.id.loreText);
                statsText = itemView.findViewById(R.id.statsText);
            }
        }
    }

    class MountAdapter extends RecyclerView.Adapter<MountAdapter.ViewHolder> {
        private List<Mount> items;

        MountAdapter(List<Mount> items) { this.items = items; }

        @Override
        public ViewHolder onCreateViewHolder(android.view.ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_catalog_card, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Mount item = items.get(position);
            holder.nameText.setText(item.getName());
            holder.rarityText.setText(item.getRarity().getLabel());
            holder.rarityText.setTextColor(item.getRarity().getColor());
            holder.descriptionText.setText(item.getDescription());
            holder.loreText.setText(item.getLore());
            holder.statsText.setText(String.format("⚡%d 🎒%d", item.getSpeed(), item.getCargoCapacity()));

            Glide.with(CatalogActivity.this)
                    .load(item.getImageUrl())
                    .placeholder(R.drawable.ic_mount_placeholder)
                    .into(holder.itemImage);

            holder.itemView.setBackgroundResource(getRarityBackground(item.getRarity()));
        }

        @Override public int getItemCount() { return items.size(); }

        class ViewHolder extends RecyclerView.ViewHolder {
            ImageView itemImage;
            TextView nameText, rarityText, descriptionText, loreText, statsText;

            ViewHolder(View itemView) {
                super(itemView);
                itemImage = itemView.findViewById(R.id.itemImage);
                nameText = itemView.findViewById(R.id.nameText);
                rarityText = itemView.findViewById(R.id.rarityText);
                descriptionText = itemView.findViewById(R.id.descriptionText);
                loreText = itemView.findViewById(R.id.loreText);
                statsText = itemView.findViewById(R.id.statsText);
            }
        }
    }

    class SpaceshipAdapter extends RecyclerView.Adapter<SpaceshipAdapter.ViewHolder> {
        private List<Spaceship> items;

        SpaceshipAdapter(List<Spaceship> items) { this.items = items; }

        @Override
        public ViewHolder onCreateViewHolder(android.view.ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_catalog_card, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Spaceship item = items.get(position);
            holder.nameText.setText(item.getName());
            holder.rarityText.setText(item.getRarity().getLabel());
            holder.rarityText.setTextColor(item.getRarity().getColor());
            holder.descriptionText.setText(item.getDescription());
            holder.loreText.setText(item.getLore());
            holder.statsText.setText(String.format("🚀%d 🛡️%d/%d", item.getSpeed(), item.getShields(), item.getMaxShields()));

            Glide.with(CatalogActivity.this)
                    .load(item.getImageUrl())
                    .placeholder(R.drawable.ic_ship_placeholder)
                    .into(holder.itemImage);

            holder.itemView.setBackgroundResource(getRarityBackground(item.getRarity()));
        }

        @Override public int getItemCount() { return items.size(); }

        class ViewHolder extends RecyclerView.ViewHolder {
            ImageView itemImage;
            TextView nameText, rarityText, descriptionText, loreText, statsText;

            ViewHolder(View itemView) {
                super(itemView);
                itemImage = itemView.findViewById(R.id.itemImage);
                nameText = itemView.findViewById(R.id.nameText);
                rarityText = itemView.findViewById(R.id.rarityText);
                descriptionText = itemView.findViewById(R.id.descriptionText);
                loreText = itemView.findViewById(R.id.loreText);
                statsText = itemView.findViewById(R.id.statsText);
            }
        }
    }

    private int getRarityBackground(Rarity rarity) {
        switch (rarity) {
            case COSMIC: return R.drawable.bg_item_cosmic;
            case MYTHIC: return R.drawable.bg_item_mythic;
            case LEGENDARY: return R.drawable.bg_item_legendary;
            case EPIC: return R.drawable.bg_item_epic;
            case RARE: return R.drawable.bg_item_rare;
            case UNCOMMON: return R.drawable.bg_item_uncommon;
            default: return R.drawable.bg_item_common;
        }
    }
}
