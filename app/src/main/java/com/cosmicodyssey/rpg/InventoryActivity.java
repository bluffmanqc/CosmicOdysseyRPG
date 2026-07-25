package com.cosmicodyssey.rpg;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ScrollView;
import android.widget.Button;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class InventoryActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScrollView scroll = new ScrollView(this);
        LinearLayout layout = new LinearLayout(this);
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

        List<String> items = getEquippedItems();
        if (items.isEmpty()) {
            TextView empty = new TextView(this);
            empty.setText("Aucun equipement equipe");
            empty.setTextColor(Color.parseColor("#888888"));
            empty.setTextSize(16);
            layout.addView(empty);
        } else {
            for (String item : items) addItemCard(layout, item);
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

    private void addItemCard(LinearLayout parent, String itemName) {
        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setPadding(30, 30, 30, 30);
        GradientDrawable gd = new GradientDrawable();
        gd.setColor(Color.parseColor("#1A1A2E"));
        gd.setCornerRadius(16);
        gd.setStroke(2, Color.parseColor("#00FF88"));
        card.setBackground(gd);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, 16, 0, 0);
        card.setLayoutParams(lp);
        TextView name = new TextView(this);
        name.setText(itemName);
        name.setTextColor(Color.parseColor("#FFFFFF"));
        name.setTextSize(18);
        card.addView(name);
        parent.addView(card);
    }

    private List<String> getEquippedItems() {
        List<String> list = new ArrayList<>();
        return list;
    }
}
