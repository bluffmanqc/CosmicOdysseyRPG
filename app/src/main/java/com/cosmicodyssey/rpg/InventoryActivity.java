package com.cosmicodyssey.rpg;

import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.cosmicodyssey.rpg.data.DataManager;
import com.cosmicodyssey.rpg.models.Equipment;
import com.cosmicodyssey.rpg.models.Mount;
import com.cosmicodyssey.rpg.models.Spaceship;
import com.cosmicodyssey.rpg.models.Vehicle;
import com.cosmicodyssey.rpg.utils.ProgressionConfig;

import java.util.ArrayList;
import java.util.List;

public class InventoryActivity extends AppCompatActivity {

    private DataManager dataManager;
    private LinearLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataManager = new DataManager(this);

        ScrollView scroll = new ScrollView(this);
        layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(40, 60, 40, 40);
        layout.setBackgroundColor(Color.parseColor("#0D0D1A"));
        scroll.addView(layout);
        setContentView(scroll);

        TextView title = new TextView(this);
        title.setText("INVENTAIRE");
        title.setTextSize(26);
        title.setTextColor(Color.parseColor("#00FF88"));
        title.setPadding(0, 0, 0, 30);
        layout.addView(title);

        addSectionTitle("EQUIPEMENT EQUIPE");
        List<Equipment> equipped = getEquippedItems();
        if (equipped.isEmpty()) {
            addEmptyText("Aucun equipement equipe");
        } else {
            for (Equipment e : equipped) addEquipmentCard(layout, e);
        }

        addSectionTitle("ARMES");
        List<Equipment> weapons = getWeapons();
        if (weapons.isEmpty()) {
            addEmptyText("Aucune arme en possession");
        } else {
            for (Equipment e : weapons) addEquipmentCard(layout, e);
        }

        addSectionTitle("ARMURES & BOUCLIERS");
        List<Equipment> armors = getArmors();
        if (armors.isEmpty()) {
            addEmptyText("Aucune armure en possession");
        } else {
            for (Equipment e : armors) addEquipmentCard(layout, e);
        }

        addSectionTitle("CHAPEAUX / CASQUES");
        List<Equipment> hats = getHats();
        if (hats.isEmpty()) {
            addEmptyText("Aucun chapeau ou casque");
        } else {
            for (Equipment e : hats) addEquipmentCard(layout, e);
        }

        addSectionTitle("GANTS");
        List<Equipment> gloves = getGloves();
        if (gloves.isEmpty()) {
            addEmptyText("Aucun gant equipe");
        } else {
            for (Equipment e : gloves) addEquipmentCard(layout, e);
        }

        addSectionTitle("PANTALONS / JAMBIERES");
        List<Equipment> pants = getPants();
        if (pants.isEmpty()) {
            addEmptyText("Aucun pantalon equipe");
        } else {
            for (Equipment e : pants) addEquipmentCard(layout, e);
        }

        addSectionTitle("BOTTES");
        List<Equipment> boots = getBoots();
        if (boots.isEmpty()) {
            addEmptyText("Aucune botte equipee");
        } else {
            for (Equipment e : boots) addEquipmentCard(layout, e);
        }

        addSectionTitle("ANNEAUX");
        List<Equipment> rings = getRings();
        if (rings.isEmpty()) {
            addEmptyText("Aucun anneau equipe");
        } else {
            for (Equipment e : rings) addEquipmentCard(layout, e);
        }

        addSectionTitle("COLLIERS / AMULETTES");
        List<Equipment> necklaces = getNecklaces();
        if (necklaces.isEmpty()) {
            addEmptyText("Aucun collier equipe");
        } else {
            for (Equipment e : necklaces) addEquipmentCard(layout, e);
        }

        addSectionTitle("GRIMOIRES");
        List<Equipment> grimoires = getGrimoires();
        if (grimoires.isEmpty()) {
            addEmptyText("Aucun grimoire en possession");
        } else {
            for (Equipment e : grimoires) addEquipmentCard(layout, e);
        }

        addSectionTitle("MONTURES");
        List<Mount> mounts = dataManager.loadMountCatalog();
        if (mounts.isEmpty()) {
            addEmptyText("Aucune monture possedee");
        } else {
            for (Mount m : mounts) addMountCard(layout, m);
        }

        addSectionTitle("VAISSEAUX");
        List<Spaceship> ships = dataManager.loadSpaceshipCatalog();
        if (ships.isEmpty()) {
            addEmptyText("Aucun vaisseau possede");
        } else {
            for (Spaceship s : ships) addSpaceshipCard(layout, s);
        }

        addSectionTitle("CARGOS");
        addEmptyText("Fonctionnalite cargo a venir");

        addSectionTitle("COMPAGNONS");
        addCompanionCard(layout, "Drone de Combat", "Petit drone offensif automatique", null, "Degats: 5/tour");
        addCompanionCard(layout, "Emplacement vide", "Recrute un compagnon dans l'aventure", null, "Aucun");

        addSectionTitle("VEHICULES");
        List<Vehicle> vehicles = getOwnedVehicles();
        if (vehicles.isEmpty()) {
            addEmptyText("Aucun vehicule possede");
        } else {
            for (Vehicle v : vehicles) addVehicleCard(layout, v);
        }

        Button back = new Button(this);
        back.setText("RETOUR");
        back.setTextColor(Color.parseColor("#0D0D1A"));
        back.setBackgroundColor(Color.parseColor("#00FF88"));
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, 40, 0, 0);
        back.setLayoutParams(lp);
        back.setOnClickListener(v -> finish());
        layout.addView(back);
    }

    private void addSectionTitle(String text) {
        TextView tv = new TextView(this);
        tv.setText(text);
        tv.setTextSize(20);
        tv.setTextColor(Color.parseColor("#00FF88"));
        tv.setPadding(0, 30, 0, 15);
        layout.addView(tv);
    }

    private void addEmptyText(String text) {
        TextView tv = new TextView(this);
        tv.setText(text);
        tv.setTextColor(Color.parseColor("#888888"));
        tv.setTextSize(14);
        tv.setPadding(20, 10, 0, 10);
        layout.addView(tv);
    }

    private void addEquipmentCard(LinearLayout parent, Equipment e) {
        if (e == null) return;
        CardView card = new CardView(this);
        card.setCardBackgroundColor(Color.parseColor("#1A1A2E"));
        card.setRadius(16);
        card.setCardElevation(8);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, 8, 0, 8);
        card.setLayoutParams(lp);

        LinearLayout inner = new LinearLayout(this);
        inner.setOrientation(LinearLayout.HORIZONTAL);
        inner.setPadding(20, 20, 20, 20);

        ImageView img = new ImageView(this);
        img.setLayoutParams(new LinearLayout.LayoutParams(80, 80));
        if (e.getImageUrl() != null && !e.getImageUrl().isEmpty()) {
            Glide.with(this).load(e.getImageUrl()).placeholder(R.drawable.ic_item_placeholder).into(img);
        } else {
            img.setImageResource(R.drawable.ic_item_placeholder);
        }
        inner.addView(img);

        LinearLayout textLayout = new LinearLayout(this);
        textLayout.setOrientation(LinearLayout.VERTICAL);
        textLayout.setPadding(20, 0, 0, 0);
        LinearLayout.LayoutParams textLp = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
        textLayout.setLayoutParams(textLp);

        TextView name = new TextView(this);
        name.setText(e.getName());
        name.setTextColor(Color.parseColor("#FFFFFF"));
        name.setTextSize(16);
        name.setTypeface(null, Typeface.BOLD);
        textLayout.addView(name);

        TextView type = new TextView(this);
        type.setText(e.getType() + " | " + (e.getRarity() != null ? e.getRarity().getLabel() : "Commun"));
        type.setTextColor(Color.parseColor("#00FF88"));
        type.setTextSize(12);
        textLayout.addView(type);

        TextView stats = new TextView(this);
        stats.setText("Dmg: " + e.getDamage() + "  Def: " + e.getDefense());
        stats.setTextColor(Color.parseColor("#AAAAAA"));
        stats.setTextSize(13);
        textLayout.addView(stats);

        inner.addView(textLayout);
        card.addView(inner);
        parent.addView(card);
    }

    private void addMountCard(LinearLayout parent, Mount m) {
        if (m == null) return;
        CardView card = createBaseCard();
        LinearLayout inner = new LinearLayout(this);
        inner.setOrientation(LinearLayout.VERTICAL);
        inner.setPadding(20, 20, 20, 20);

        TextView name = new TextView(this);
        name.setText(m.getName());
        name.setTextColor(Color.parseColor("#FFFFFF"));
        name.setTextSize(16);
        inner.addView(name);

        TextView stats = new TextView(this);
        stats.setText(m.getSpecies() + " | Vitesse: " + m.getSpeed() + " km/h");
        stats.setTextColor(Color.parseColor("#AAAAAA"));
        stats.setTextSize(13);
        inner.addView(stats);

        card.addView(inner);
        parent.addView(card);
    }

    private void addSpaceshipCard(LinearLayout parent, Spaceship s) {
        if (s == null) return;
        CardView card = createBaseCard();
        LinearLayout inner = new LinearLayout(this);
        inner.setOrientation(LinearLayout.VERTICAL);
        inner.setPadding(20, 20, 20, 20);

        TextView name = new TextView(this);
        name.setText(s.getName());
        name.setTextColor(Color.parseColor("#FFFFFF"));
        name.setTextSize(16);
        inner.addView(name);

        TextView stats = new TextView(this);
        stats.setText(s.getModel() + " | Vitesse: " + s.getSpeed() + " ly/h");
        stats.setTextColor(Color.parseColor("#AAAAAA"));
        stats.setTextSize(13);
        inner.addView(stats);

        card.addView(inner);
        parent.addView(card);
    }

    private void addCompanionCard(LinearLayout parent, String name, String desc, String imgUrl, String stats) {
        CardView card = createBaseCard();
        LinearLayout inner = new LinearLayout(this);
        inner.setOrientation(LinearLayout.VERTICAL);
        inner.setPadding(20, 20, 20, 20);

        TextView nameTv = new TextView(this);
        nameTv.setText(name);
        nameTv.setTextColor(Color.parseColor("#FFFFFF"));
        nameTv.setTextSize(16);
        inner.addView(nameTv);

        TextView descTv = new TextView(this);
        descTv.setText(desc);
        descTv.setTextColor(Color.parseColor("#AAAAAA"));
        descTv.setTextSize(13);
        inner.addView(descTv);

        TextView statsTv = new TextView(this);
        statsTv.setText(stats);
        statsTv.setTextColor(Color.parseColor("#FFAA00"));
        statsTv.setTextSize(12);
        inner.addView(statsTv);

        card.addView(inner);
        parent.addView(card);
    }

    private void addVehicleCard(LinearLayout parent, Vehicle v) {
        CardView card = createBaseCard();
        LinearLayout inner = new LinearLayout(this);
        inner.setOrientation(LinearLayout.VERTICAL);
        inner.setPadding(20, 20, 20, 20);

        TextView name = new TextView(this);
        name.setText(v.getName() + " [" + v.getType() + "]");
        name.setTextColor(Color.parseColor("#FFFFFF"));
        name.setTextSize(16);
        inner.addView(name);

        TextView stats = new TextView(this);
        stats.setText("Cargo: " + v.getCargoCapacity() + " | Vitesse: " + v.getSpeed() +
                (v.getArmament() != null ? " | Armement: " + v.getArmament() : ""));
        stats.setTextColor(Color.parseColor("#AAAAAA"));
        stats.setTextSize(13);
        inner.addView(stats);

        card.addView(inner);
        parent.addView(card);
    }

    private CardView createBaseCard() {
        CardView card = new CardView(this);
        card.setCardBackgroundColor(Color.parseColor("#1A1A2E"));
        card.setRadius(16);
        card.setCardElevation(4);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, 8, 0, 8);
        card.setLayoutParams(lp);
        return card;
    }

    private List<Equipment> getEquippedItems() {
        List<Equipment> list = new ArrayList<>();
        return list;
    }

    private List<Equipment> getWeapons() {
        List<Equipment> list = new ArrayList<>();
        for (Equipment e : dataManager.loadEquipmentCatalog()) {
            if (e != null && e.getType() != null && (e.getType().equalsIgnoreCase("Arme") || e.getType().equalsIgnoreCase("Weapon"))) {
                list.add(e);
            }
        }
        return list;
    }

    private List<Equipment> getArmors() {
        List<Equipment> list = new ArrayList<>();
        for (Equipment e : dataManager.loadEquipmentCatalog()) {
            if (e != null && e.getType() != null && (e.getType().equalsIgnoreCase("Armure") || e.getType().equalsIgnoreCase("Armor")
                    || e.getType().equalsIgnoreCase("Chest") || e.getType().equalsIgnoreCase("Bouclier")
                    || e.getType().equalsIgnoreCase("Shield"))) {
                list.add(e);
            }
        }
        return list;
    }

    private List<Equipment> getHats() {
        List<Equipment> list = new ArrayList<>();
        for (Equipment e : dataManager.loadEquipmentCatalog()) {
            if (e != null && e.getType() != null && (e.getType().equalsIgnoreCase("Chapeau") || e.getType().equalsIgnoreCase("Hat")
                    || e.getType().equalsIgnoreCase("Casque") || e.getType().equalsIgnoreCase("Helmet"))) {
                list.add(e);
            }
        }
        return list;
    }

    private List<Equipment> getGloves() {
        List<Equipment> list = new ArrayList<>();
        for (Equipment e : dataManager.loadEquipmentCatalog()) {
            if (e != null && e.getType() != null && (e.getType().equalsIgnoreCase("Gant") || e.getType().equalsIgnoreCase("Glove"))) {
                list.add(e);
            }
        }
        return list;
    }

    private List<Equipment> getPants() {
        List<Equipment> list = new ArrayList<>();
        for (Equipment e : dataManager.loadEquipmentCatalog()) {
            if (e != null && e.getType() != null && (e.getType().equalsIgnoreCase("Pantalon") || e.getType().equalsIgnoreCase("Pants")
                    || e.getType().equalsIgnoreCase("Jambe") || e.getType().equalsIgnoreCase("Leg"))) {
                list.add(e);
            }
        }
        return list;
    }

    private List<Equipment> getBoots() {
        List<Equipment> list = new ArrayList<>();
        for (Equipment e : dataManager.loadEquipmentCatalog()) {
            if (e != null && e.getType() != null && (e.getType().equalsIgnoreCase("Botte") || e.getType().equalsIgnoreCase("Boot"))) {
                list.add(e);
            }
        }
        return list;
    }

    private List<Equipment> getRings() {
        List<Equipment> list = new ArrayList<>();
        for (Equipment e : dataManager.loadEquipmentCatalog()) {
            if (e != null && e.getType() != null && (e.getType().equalsIgnoreCase("Anneau") || e.getType().equalsIgnoreCase("Ring"))) {
                list.add(e);
            }
        }
        return list;
    }

    private List<Equipment> getNecklaces() {
        List<Equipment> list = new ArrayList<>();
        for (Equipment e : dataManager.loadEquipmentCatalog()) {
            if (e != null && e.getType() != null && (e.getType().equalsIgnoreCase("Collier") || e.getType().equalsIgnoreCase("Necklace")
                    || e.getType().equalsIgnoreCase("Amulette") || e.getType().equalsIgnoreCase("Amulet"))) {
                list.add(e);
            }
        }
        return list;
    }

    private List<Equipment> getGrimoires() {
        List<Equipment> list = new ArrayList<>();
        for (Equipment e : dataManager.loadEquipmentCatalog()) {
            if (e != null && e.getType() != null && (e.getType().equalsIgnoreCase("Grimoire") || e.getType().equalsIgnoreCase("Spellbook"))) {
                list.add(e);
            }
        }
        return list;
    }

    private List<Vehicle> getOwnedVehicles() {
        List<Vehicle> owned = new ArrayList<>();
        for (Vehicle v : ProgressionConfig.getAvailableVehicles()) {
            if (v.isOwned()) owned.add(v);
        }
        return owned;
    }
}
