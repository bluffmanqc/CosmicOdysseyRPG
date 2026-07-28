package com.cosmicodyssey.rpg.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cosmicodyssey.rpg.R;
import com.cosmicodyssey.rpg.data.DataManager;
import com.cosmicodyssey.rpg.models.Character;
import com.cosmicodyssey.rpg.models.Equipment;
import com.cosmicodyssey.rpg.models.Rarity;
import com.cosmicodyssey.rpg.models.ShopItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ShopActivity extends AppCompatActivity {
    private RecyclerView shopGrid;
    private TextView merchantName;
    private TextView merchantType;
    private ImageView merchantImage;
    private TextView playerCredits;
    private Button refreshBtn;
    private Button backBtn;

    private Character character;
    private DataManager dataManager;
    private List<ShopItem> items;
    private String currentMerchant;
    private ShopAdapter shopAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        dataManager = new DataManager(this);
        character = dataManager.loadCharacter();
        if (character == null) {
            startActivity(new Intent(this, CharacterCreationActivity.class));
            finish();
            return;
        }

        items = new ArrayList<>();

        shopGrid = findViewById(R.id.shopGrid);
        merchantName = findViewById(R.id.merchantName);
        merchantType = findViewById(R.id.merchantType);
        merchantImage = findViewById(R.id.merchantImage);
        playerCredits = findViewById(R.id.playerCredits);
        refreshBtn = findViewById(R.id.refreshBtn);
        backBtn = findViewById(R.id.backBtn);

        shopGrid.setLayoutManager(new GridLayoutManager(this, 2));

        generateRandomMerchant();
        generateShopItems();
        updateCredits();

        refreshBtn.setOnClickListener(v -> {
            generateRandomMerchant();
            generateShopItems();
        });
        backBtn.setOnClickListener(v -> finish());
    }

    private void updateCredits() {
        playerCredits.setText(" " + character.getCredits() + " credits");
    }

    private void generateRandomMerchant() {
        String[] types = {"Armes", "Armures", "Montures", "Vaisseaux", "Cargos", "Generaliste"};
        String[] names = {"Zorblax", "Krynn", "Meebo", "Xylos", "Grakthar", "Nebula"};
        String[] titles = {"le Forgeron", "l'Arsenalier", "le Dresseur", "l'Ingenieur", "le Marchand", "le Collecteur"};

        Random random = new Random();
        currentMerchant = names[random.nextInt(names.length)] + " " + titles[random.nextInt(titles.length)];
        String type = types[random.nextInt(types.length)];

        merchantName.setText(currentMerchant);
        merchantType.setText("Marchand " + type);

        // FIX: Image locale au lieu de generation IA externe
        merchantImage.setImageResource(R.drawable.ic_merchant_placeholder);
    }

    private void generateShopItems() {
        items.clear();
        Random random = new Random();

        for (int i = 0; i < 8; i++) {
            ShopItem item = new ShopItem();
            Rarity[] rarities = Rarity.values();
            Rarity rarity = rarities[random.nextInt(Math.min(rarities.length, 3 + character.getLevel()))];

            Equipment eq = new Equipment();
            eq.setName(generateItemName(random, rarity));
            eq.setType(random.nextBoolean() ? "Arme" : "Armure");
            eq.setRarity(rarity);
            eq.setDamage(random.nextInt(20 * (rarity.ordinal() + 1)) + 5);
            eq.setDefense(random.nextInt(15 * (rarity.ordinal() + 1)) + 3);
            eq.setLevelRequired(character.getLevel() + random.nextInt(3));
            eq.setImageUrl(null);
            eq.setLore("Forge par " + currentMerchant + " dans les profondeurs du cosmos.");

            item.setName(eq.getName());
            item.setDescription(eq.getDescription());
            item.setRarity(rarity);
            item.setPrice((int)(100 * rarity.getMultiplier() * (random.nextDouble() + 0.5)));
            item.setImageUrl(null);
            item.setMerchantName(currentMerchant);
            item.setMerchantImageUrl(null);
            item.setCategory(eq.getType());
            item.setAvailable(true);
            item.setItem(eq);

            items.add(item);
        }

        shopGrid.setAdapter(new ShopAdapter());
    }

    private String generateItemName(Random random, Rarity rarity) {
        String[] prefixes = {"", "Eclatant", "Sombre", "Cosmique", "Quantique", "Nebuleux", "Etoile", "Galactique"};
        String[] suffixes = {"de Puissance", "de Destruction", "de Lumiere", "des Ombres", "du Cosmos", "de l'Infini", "de la Destinee"};
        String[] types = {"Epee", "Pistolet", "Armure", "Bouclier", "Casque", "Gants", "Bottes"};

        String prefix = rarity.ordinal() >= 3 ? prefixes[random.nextInt(prefixes.length)] + " " : "";
        String suffix = rarity.ordinal() >= 4 ? " " + suffixes[random.nextInt(suffixes.length)] : "";
        String type = types[random.nextInt(types.length)];

        return prefix + type + suffix;
    }

    private void buyItem(ShopItem item) {
        if (item.getPrice() > character.getCredits()) {
            Toast.makeText(this, "Pas assez de credits !", Toast.LENGTH_SHORT).show();
            return;
        }
        character.setCredits(character.getCredits() - item.getPrice());
        character.getEquipments().add(item.getItem());
        dataManager.saveCharacter(character);
        updateCredits();
        Toast.makeText(this, "Achat effectue : " + item.getName(), Toast.LENGTH_SHORT).show();
    }

    private int getRarityBorder(Rarity rarity) {
        switch (rarity) {
            case COMMON: return R.drawable.border_common;
            case UNCOMMON: return R.drawable.border_uncommon;
            case RARE: return R.drawable.border_rare;
            case EPIC: return R.drawable.border_epic;
            case LEGENDARY: return R.drawable.border_legendary;
            case MYTHIC: return R.drawable.border_mythic;
            case COSMIC: return R.drawable.border_cosmic;
            default: return R.drawable.border_common;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (merchantImage != null) {
            Glide.with(getApplicationContext()).clear(merchantImage);
        }
    }

    class ShopAdapter extends RecyclerView.Adapter<ShopAdapter.ViewHolder> {
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_shop_card, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            ShopItem item = items.get(position);

            holder.nameText.setText(item.getName());
            holder.priceText.setText(item.getPrice() + " ");
            holder.rarityText.setText(item.getRarity().getLabel());
            holder.rarityText.setTextColor(item.getRarity().getColor());

            // FIX: Image locale au lieu de chargement externe
            holder.itemImage.setImageResource(R.drawable.ic_item_placeholder);

            holder.buyBtn.setOnClickListener(v -> buyItem(item));

            // Rarity glow border
            holder.itemView.setBackgroundResource(getRarityBorder(item.getRarity()));
        }

        @Override
        public int getItemCount() { return items.size(); }

        class ViewHolder extends RecyclerView.ViewHolder {
            ImageView itemImage;
            TextView nameText, priceText, rarityText;
            Button buyBtn;
            ViewHolder(View itemView) {
                super(itemView);
                itemImage = itemView.findViewById(R.id.itemImage);
                nameText = itemView.findViewById(R.id.nameText);
                priceText = itemView.findViewById(R.id.priceText);
                rarityText = itemView.findViewById(R.id.rarityText);
                buyBtn = itemView.findViewById(R.id.buyBtn);
            }
        }
    }
}
