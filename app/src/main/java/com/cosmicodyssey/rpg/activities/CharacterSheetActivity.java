package com.cosmicodyssey.rpg.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.cosmicodyssey.rpg.R;
import com.cosmicodyssey.rpg.data.DataManager;
import com.cosmicodyssey.rpg.models.Character;
import com.cosmicodyssey.rpg.models.Equipment;

import java.util.List;

public class CharacterSheetActivity extends AppCompatActivity {
    private ImageView avatarImage;
    private TextView nameText, raceClassText, levelText;
    private TextView hpText, shieldText, energyText, creditsText;
    private ProgressBar hpBar, shieldBar, energyBar;
    private TextView strengthText, dexterityText, constitutionText;
    private TextView intelligenceText, wisdomText, charismaText, luckText;
    private LinearLayout equipmentList;
    private Button inventoryBtn;

    private Character character;
    private DataManager dataManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_sheet);

        dataManager = new DataManager(this);

        character = dataManager.loadCharacter();
        if (character == null) {
            startActivity(new Intent(this, CharacterCreationActivity.class));
            finish();
            return;
        }

        initViews();
        updateUI();
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
        equipmentList = findViewById(R.id.equipmentList);
        inventoryBtn = findViewById(R.id.inventoryBtn);

        inventoryBtn.setOnClickListener(v -> startActivity(new Intent(this, com.cosmicodyssey.rpg.InventoryActivity.class)));
    }

    private int getMod(int stat) {
        return (stat - 10) / 2;
    }

    private void updateUI() {
        nameText.setText(character.getName());
        raceClassText.setText(character.getRace() + " | " + character.getClassName());
        levelText.setText("Niveau " + character.getLevel());

        hpBar.setMax(character.getMaxHitPoints());
        hpBar.setProgress(character.getHitPoints());
        hpText.setText(character.getHitPoints() + "/" + character.getMaxHitPoints());

        shieldBar.setMax(character.getMaxShieldPoints());
        shieldBar.setProgress(character.getShieldPoints());
        shieldText.setText(character.getShieldPoints() + "/" + character.getMaxShieldPoints());

        energyBar.setMax(character.getMaxEnergy());
        energyBar.setProgress(character.getEnergy());
        energyText.setText(character.getEnergy() + "/" + character.getMaxEnergy());

        creditsText.setText(" " + character.getCredits() + " credits");

        strengthText.setText("FOR: " + character.getStats().getStrength() + " (" + getMod(character.getStats().getStrength()) + ")");
        dexterityText.setText("DEX: " + character.getStats().getDexterity() + " (" + getMod(character.getStats().getDexterity()) + ")");
        constitutionText.setText("CON: " + character.getStats().getConstitution() + " (" + getMod(character.getStats().getConstitution()) + ")");
        intelligenceText.setText("INT: " + character.getStats().getIntelligence() + " (" + getMod(character.getStats().getIntelligence()) + ")");
        wisdomText.setText("SAG: " + character.getStats().getWisdom() + " (" + getMod(character.getStats().getWisdom()) + ")");
        charismaText.setText("CHA: " + character.getStats().getCharisma() + " (" + getMod(character.getStats().getCharisma()) + ")");
        luckText.setText("LUCK: " + character.getStats().getLuck());

        updateCharacterImage();
        updateEquipmentList();
    }

    private void updateCharacterImage() {
        try {
            if (character.getAvatarUrl() != null && !character.getAvatarUrl().isEmpty()) {
                Glide.with(getApplicationContext())
                        .load(character.getAvatarUrl())
                        .placeholder(R.drawable.ic_character_placeholder)
                        .error(R.drawable.ic_character_placeholder)
                        .into(avatarImage);
                return;
            }
        } catch (Exception e) {
            avatarImage.setImageResource(R.drawable.ic_character_placeholder);
        }
        // Fallback: image locale
        avatarImage.setImageResource(R.drawable.ic_character_placeholder);
    }

    private void updateEquipmentList() {
        equipmentList.removeAllViews();
        List<Equipment> equipments = character.getEquipments();
        if (equipments.isEmpty()) {
            TextView empty = new TextView(this);
            empty.setText("Aucun equipement");
            empty.setTextColor(Color.parseColor("#888888"));
            empty.setTextSize(14);
            equipmentList.addView(empty);
            return;
        }
        for (Equipment e : equipments) {
            TextView tv = new TextView(this);
            tv.setText("* " + e.getName() + " (" + e.getType() + ")");
            tv.setTextColor(Color.parseColor("#FFFFFF"));
            tv.setTextSize(14);
            tv.setPadding(0, 4, 0, 4);
            equipmentList.addView(tv);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (avatarImage != null) {
            Glide.with(getApplicationContext()).clear(avatarImage);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        character = dataManager.loadCharacter();
        if (character != null) {
            updateUI();
        }
    }
}
