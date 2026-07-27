package com.cosmicodyssey.rpg;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Button;
import android.view.Gravity;
import java.util.List;
import java.util.ArrayList;
import com.cosmicodyssey.rpg.models.Equipment;
import com.cosmicodyssey.rpg.data.DataManager;
import com.cosmicodyssey.rpg.activities.MainActivity;

public class InventoryActivity extends Activity {
    private DataManager dataManager;
    private LinearLayout layout;
    private List<Equipment> equipmentCatalog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataManager = new DataManager(this);
        equipmentCatalog = dataManager.loadEquipmentCatalog();
        if (equipmentCatalog == null) equipmentCatalog = new ArrayList<>();
        setupUI();
        loadInventory();
    }

    private void setupUI() {
        ScrollView scroll = new ScrollView(this);
        layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(40, 60, 40, 40);
        layout.setBackgroundColor(Color.parseColor("#0D0D1A"));
        scroll.addView(layout);
        setContentView(scroll);
        TextView title = new TextView(this);
        title.setText(" INVENTAIRE");
        title.setTextColor(Color.parseColor("#00FF88"));
        title.setTextSize(24);
        title.setGravity(Gravity.CENTER);
        title.setPadding(0, 0, 0, 30);
        layout.addView(title);
    }

    private void loadInventory() {
        addSection(" ARMES", "#FF6B6B", getWeapons());
        addSection(" ARMURES", "#4ECDC4", getArmors());
        addSection(" CASQUES", "#FFE66D", getHelmets());
        addSection(" BOTTES", "#A8E6CF", getBoots());
        addSection(" ACCESSOIRES", "#FF8B94", getAccessories());
        addBackButton();
    }

    private void addSection(String title, String color, List<Equipment> list) {
        TextView t = new TextView(this);
        t.setText(title);
        t.setTextColor(Color.parseColor(color));
        t.setTextSize(18);
        t.setPadding(0, 30, 0, 15);
        t.setTypeface(null, android.graphics.Typeface.BOLD);
        layout.addView(t);
        if (list.isEmpty()) {
            TextView e = new TextView(this);
            e.setText("Aucun équipement");
            e.setTextColor(Color.GRAY);
            e.setPadding(20, 10, 20, 10);
            layout.addView(e);
        } else {
            for (Equipment eq : list) if (eq != null) addItem(eq);
        }
    }

    private void addItem(Equipment eq) {
        LinearLayout item = new LinearLayout(this);
        item.setOrientation(LinearLayout.HORIZONTAL);
        item.setPadding(20, 15, 20, 15);
        item.setBackgroundColor(Color.parseColor("#1A1A2E"));
        TextView txt = new TextView(this);
        txt.setText(" " + eq.getName() + "\n" + eq.getDescription());
        txt.setTextColor(Color.WHITE);
        txt.setTextSize(14);
        txt.setPadding(15, 10, 15, 10);
        txt.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f));
        Button btn = new Button(this);
        btn.setText("Détails");
        btn.setBackgroundColor(Color.parseColor("#00FF88"));
        btn.setTextColor(Color.BLACK);
        btn.setPadding(30, 15, 30, 15);
        btn.setOnClickListener(v -> { android.widget.Toast.makeText(this, eq.getName(), android.widget.Toast.LENGTH_SHORT).show(); });
        item.addView(txt);
        item.addView(btn);
        layout.addView(item);
    }

    private void addBackButton() {
        Button back = new Button(this);
        back.setText(" Retour");
        back.setBackgroundColor(Color.parseColor("#FF6B6B"));
        back.setTextColor(Color.WHITE);
        back.setPadding(40, 20, 40, 20);
        back.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        back.setOnClickListener(v -> { startActivity(new Intent(InventoryActivity.this, MainActivity.class)); finish(); });
        layout.addView(back);
    }

    private List<Equipment> getWeapons() { return filter("Arme", "Weapon"); }
    private List<Equipment> getArmors() { return filter("Armure", "Armor"); }
    private List<Equipment> getHelmets() { return filter("Casque", "Helmet"); }
    private List<Equipment> getBoots() { return filter("Botte", "Boots"); }
    private List<Equipment> getAccessories() { return filter("Accessoire", "Accessory"); }
    private List<Equipment> filter(String f1, String f2) {
        List<Equipment> r = new ArrayList<>();
        for (Equipment e : equipmentCatalog) if (e != null && e.getType() != null && (e.getType().equalsIgnoreCase(f1) || e.getType().equalsIgnoreCase(f2))) r.add(e);
        return r;
    }
}
