package com.cosmicodyssey.rpg.activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cosmicodyssey.rpg.R;
import com.cosmicodyssey.rpg.ai.GameMasterAI;
import com.cosmicodyssey.rpg.data.DataManager;
import com.cosmicodyssey.rpg.models.Character;
import com.cosmicodyssey.rpg.models.Equipment;
import com.cosmicodyssey.rpg.models.Rarity;
import com.cosmicodyssey.rpg.models.ShopItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ShopActivity extends AppCompatActivity {
    private ImageView merchantImage;
    private TextView merchantName;
    private TextView merchantType;
    private RecyclerView shopGrid;
    private TextView creditsText;
    private DataManager dataManager;
    private GameMasterAI ai;
    private Character character;
    private List<ShopItem> items;
    private String currentMerchant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        dataManager = new DataManager(this);
        ai = new GameMasterAI(this);
        character = dataManager.loadCharacter();
        items = new ArrayList<>();

        merchantImage = findViewById(R.id.merchantImage);
        merchantName = findViewById(R.id.merchantName);
        merchantType = findViewById(R.id.merchantType);
        shopGrid = findViewById(R.id.shopGrid);
        creditsText = findViewById(R.id.creditsText);

        shopGrid.setLayoutManager(new GridLayoutManager(this, 2));

        generateRandomMerchant();
        generateShopItems();
        updateCredits();

        findViewById(R.id.refreshBtn).setOnClickListener(v -> {
            generateRandomMerchant();
            generateShopItems();
        });
    }

    private void generateRandomMerchant() {
        String[] types = {"Armes", "Armures", "Montures", "Vaisseaux", "Cargos", "Généraliste"};
        String[] names = {"Zorblax", "Krynn", "Meebo", "Xylos", "Grakthar", "Nebula"};
        String[] titles = {"le Forgeron", "l'Arsenalier", "le Dresseur", "l'Ingenieur", "le Marchand", "le Collecteur"};

        Random random = new Random();
        currentMerchant = names[random.nextInt(names.length)] + " " + titles[random.nextInt(titles.length)];
        String type = types[random.nextInt(types.length)];

        merchantName.setText(currentMerchant);
        merchantType.setText("Marchand " + type);

        String merchantImgUrl = ai.generateMerchantImageUrl(type, currentMerchant);
        Glide.with(this)
                .load(merchantImgUrl)
                .placeholder(R.drawable.ic_merchant_placeholder)
                .into(merchantImage);
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
            eq.setImageUrl(ai.generateItemImageUrl(eq.getName(), eq.getType(), rarity));
            eq.setLore("Forgé par " + currentMerchant + " dans les profondeurs du cosmos.");

            item.setName(eq.getName());
            item.setDescription(eq.getDescription());
            item.setRarity(rarity);
            item.setPrice((int)(100 * rarity.getMultiplier() * (random.nextDouble() + 0.5)));
            item.setImageUrl(eq.getImageUrl());
            item.setMerchantName(currentMerchant);
            item.setMerchantImageUrl(ai.generateMerchantImageUrl("Marchand", currentMerchant));
            item.setCategory(eq.getType());
            item.setAvailable(true);
            item.setItem(eq);

            items.add(item);
        }

        shopGrid.setAdapter(new ShopAdapter());
    }

    private String generateItemName(Random random, Rarity rarity) {
        String[] prefixes = {"", "Super", "Ultra", "Cosmique", "Dimensionnel"};
        String[] bases = {"Épée", "Pistolet", "Bouclier", "Armure", "Amulette", "Cristal"};
        String[] suffixes = {"", "de Feu", "de Glace", "des Étoiles", "du Néant", "Quantique"};

        String name = "";
        if (rarity.ordinal() >= Rarity.EPIC.ordinal()) {
            name += prefixes[random.nextInt(prefixes.length)] + " ";
        }
        name += bases[random.nextInt(bases.length)];
        if (rarity.ordinal() >= Rarity.RARE.ordinal()) {
            name += " " + suffixes[random.nextInt(suffixes.length)];
        }
        return name;
    }

    private void updateCredits() {
        creditsText.setText("💰 " + character.getCredits() + " crédits");
    }

    private void buyItem(ShopItem item) {
        if (character.getCredits() < item.getPrice()) {
            Toast.makeText(this, "Pas assez de crédits !", Toast.LENGTH_SHORT).show();
            return;
        }

        character.setCredits(character.getCredits() - item.getPrice());
        Equipment eq = (Equipment) item.getItem();
        character.addEquipment(eq);
        dataManager.saveCharacter(character);
        dataManager.addToCatalog(eq);
        updateCredits();

        Toast.makeText(this, "Acheté : " + item.getName() + " !", Toast.LENGTH_SHORT).show();
    }

    class ShopAdapter extends RecyclerView.Adapter<ShopAdapter.ViewHolder> {
        @Override
        public ViewHolder onCreateViewHolder(android.view.ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shop_card, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            ShopItem item = items.get(position);

            holder.nameText.setText(item.getName());
            holder.priceText.setText(item.getPrice() + " 💰");
            holder.rarityText.setText(item.getRarity().getLabel());
            holder.rarityText.setTextColor(item.getRarity().getColor());

            Glide.with(ShopActivity.this)
                    .load(item.getImageUrl())
                    .placeholder(R.drawable.ic_item_placeholder)
                    .into(holder.itemImage);

            holder.buyBtn.setOnClickListener(v -> buyItem(item));

            // Rarity glow border
            holder.itemView.setBackgroundResource(getRarityBorder(item.getRarity()));
        }

        @Override public int getItemCount() { return items.size(); }

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

    private int getRarityBorder(Rarity rarity) {
        switch (rarity) {
            case LEGENDARY: return R.drawable.border_legendary;
            case EPIC: return R.drawable.border_epic;
            case RARE: return R.drawable.border_rare;
            default: return R.drawable.border_common;
        }
    }
}
