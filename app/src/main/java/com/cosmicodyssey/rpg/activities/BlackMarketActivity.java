package com.cosmicodyssey.rpg.activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cosmicodyssey.rpg.R;
import com.cosmicodyssey.rpg.data.DataManager;
import com.cosmicodyssey.rpg.models.Character;
import com.cosmicodyssey.rpg.models.Equipment;
import com.cosmicodyssey.rpg.models.MarketListing;
import com.cosmicodyssey.rpg.models.Mount;
import com.cosmicodyssey.rpg.models.Spaceship;

import java.util.ArrayList;
import java.util.List;

public class BlackMarketActivity extends AppCompatActivity {
    private RecyclerView marketGrid;
    private RecyclerView myListingsGrid;
    private TextView creditsText;
    private Button sellItemBtn;
    private DataManager dataManager;
    private Character character;
    private List<MarketListing> allListings;
    private List<MarketListing> myListings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_black_market);

        dataManager = new DataManager(this);
        character = dataManager.loadCharacter();

        marketGrid = findViewById(R.id.marketGrid);
        myListingsGrid = findViewById(R.id.myListingsGrid);
        creditsText = findViewById(R.id.creditsText);
        sellItemBtn = findViewById(R.id.sellItemBtn);

        marketGrid.setLayoutManager(new GridLayoutManager(this, 2));
        myListingsGrid.setLayoutManager(new GridLayoutManager(this, 2));

        loadMarket();
        updateCredits();

        sellItemBtn.setOnClickListener(v -> showSellDialog());
    }

    private void loadMarket() {
        allListings = dataManager.loadMarketListings();
        myListings = new ArrayList<>();

        for (MarketListing listing : allListings) {
            if (listing.getSellerId().equals(character.getId())) {
                myListings.add(listing);
            }
        }

        marketGrid.setAdapter(new MarketAdapter(allListings, false));
        myListingsGrid.setAdapter(new MarketAdapter(myListings, true));
    }

    private void updateCredits() {
        creditsText.setText("💰 " + character.getCredits() + " crédits");
    }

    private void showSellDialog() {
        if (character.getEquipments().isEmpty()) {
            Toast.makeText(this, "Tu n'as rien à vendre !", Toast.LENGTH_SHORT).show();
            return;
        }

        String[] items = new String[character.getEquipments().size()];
        for (int i = 0; i < character.getEquipments().size(); i++) {
            Equipment e = character.getEquipments().get(i);
            items[i] = e.getName() + " (" + e.getRarity().getLabel() + ")";
        }

        new AlertDialog.Builder(this)
                .setTitle("Vendre un objet")
                .setItems(items, (dialog, which) -> {
                    Equipment selected = character.getEquipments().get(which);
                    showPriceDialog(selected);
                })
                .show();
    }

    private void showPriceDialog(Equipment equipment) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Prix de vente");

        EditText priceInput = new EditText(this);
        priceInput.setHint("Prix en crédits");
        priceInput.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);
        int suggestedPrice = (int)(100 * equipment.getRarity().getMultiplier());
        priceInput.setText(String.valueOf(suggestedPrice));
        priceInput.setTextColor(0xFFFFFFFF);
        priceInput.setHintTextColor(0xFF666666);

        builder.setView(priceInput);
        builder.setPositiveButton("Vendre", (dialog, which) -> {
            String priceStr = priceInput.getText().toString();
            if (priceStr.isEmpty()) return;

            int price = Integer.parseInt(priceStr);
            createListing(equipment, price);
        });
        builder.setNegativeButton("Annuler", null);
        builder.show();
    }

    private void createListing(Equipment equipment, int price) {
        MarketListing listing = new MarketListing();
        listing.setItemId(equipment.getId());
        listing.setItemName(equipment.getName());
        listing.setItemType("equipment");
        listing.setItemImageUrl(equipment.getImageUrl());
        listing.setRarity(equipment.getRarity());
        listing.setPrice(price);
        listing.setSellerId(character.getId());
        listing.setSellerName(character.getName());
        listing.setItem(equipment);

        dataManager.addMarketListing(listing);
        character.removeEquipment(equipment);
        dataManager.saveCharacter(character);

        Toast.makeText(this, "Mis en vente : " + equipment.getName() + " à " + price + " 💰", Toast.LENGTH_SHORT).show();
        loadMarket();
        updateCredits();
    }

    private void buyItem(MarketListing listing) {
        if (character.getCredits() < listing.getPrice()) {
            Toast.makeText(this, "Pas assez de crédits !", Toast.LENGTH_SHORT).show();
            return;
        }

        character.setCredits(character.getCredits() - listing.getPrice());
        Equipment eq = (Equipment) listing.getItem();
        character.addEquipment(eq);
        dataManager.saveCharacter(character);

        dataManager.removeMarketListing(listing);
        loadMarket();
        updateCredits();

        Toast.makeText(this, "Acheté : " + listing.getItemName() + " !", Toast.LENGTH_SHORT).show();
    }

    private void cancelListing(MarketListing listing) {
        Equipment eq = (Equipment) listing.getItem();
        character.addEquipment(eq);
        dataManager.saveCharacter(character);

        dataManager.removeMarketListing(listing);
        loadMarket();
        updateCredits();

        Toast.makeText(this, "Annonce retirée", Toast.LENGTH_SHORT).show();
    }

    class MarketAdapter extends RecyclerView.Adapter<MarketAdapter.ViewHolder> {
        private List<MarketListing> listings;
        private boolean isMyListing;

        MarketAdapter(List<MarketListing> listings, boolean isMyListing) {
            this.listings = listings;
            this.isMyListing = isMyListing;
        }

        @Override
        public ViewHolder onCreateViewHolder(android.view.ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_market_card, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            MarketListing listing = listings.get(position);

            holder.nameText.setText(listing.getItemName());
            holder.priceText.setText(listing.getPrice() + " 💰");
            holder.sellerText.setText("Vendeur: " + listing.getSellerName());
            holder.rarityText.setText(listing.getRarity().getLabel());
            holder.rarityText.setTextColor(listing.getRarity().getColor());

            Glide.with(BlackMarketActivity.this)
                    .load(listing.getItemImageUrl())
                    .placeholder(R.drawable.ic_item_placeholder)
                    .into(holder.itemImage);

            if (isMyListing) {
                holder.actionBtn.setText("❌ Retirer");
                holder.actionBtn.setBackgroundTintList(getResources().getColorStateList(android.R.color.holo_red_dark));
                holder.actionBtn.setOnClickListener(v -> cancelListing(listing));
            } else {
                holder.actionBtn.setText("💰 Acheter");
                holder.actionBtn.setBackgroundTintList(getResources().getColorStateList(android.R.color.holo_green_dark));
                holder.actionBtn.setOnClickListener(v -> buyItem(listing));
            }

            holder.itemView.setBackgroundResource(getRarityBackground(listing.getRarity()));
        }

        @Override public int getItemCount() { return listings.size(); }

        class ViewHolder extends RecyclerView.ViewHolder {
            ImageView itemImage;
            TextView nameText, priceText, sellerText, rarityText;
            Button actionBtn;

            ViewHolder(View itemView) {
                super(itemView);
                itemImage = itemView.findViewById(R.id.itemImage);
                nameText = itemView.findViewById(R.id.nameText);
                priceText = itemView.findViewById(R.id.priceText);
                sellerText = itemView.findViewById(R.id.sellerText);
                rarityText = itemView.findViewById(R.id.rarityText);
                actionBtn = itemView.findViewById(R.id.actionBtn);
            }
        }
    }

    private int getRarityBackground(com.cosmicodyssey.rpg.models.Rarity rarity) {
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
