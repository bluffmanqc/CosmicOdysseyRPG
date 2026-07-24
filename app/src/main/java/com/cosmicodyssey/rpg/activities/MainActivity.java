package com.cosmicodyssey.rpg.activities;
import com.cosmicodyssey.rpg.SettingsActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.cosmicodyssey.rpg.R;
import com.cosmicodyssey.rpg.data.DataManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView bgImage = findViewById(R.id.bgImage);
        Button btnNewGame = findViewById(R.id.btnNewGame);
        Button btnContinue = findViewById(R.id.btnContinue);
        Button btnJoinParty = findViewById(R.id.btnJoinParty);
        Button btnCatalog = findViewById(R.id.btnCatalog);
        Button btnBlackMarket = findViewById(R.id.btnBlackMarket);
        Button btnSettings = findViewById(R.id.btnSettings);

        Glide.with(this)
                .load("https://image.pollinations.ai/prompt/cosmic%20space%20galaxy%20nebula%20epic%20dramatic%20sci-fi?width=1080&height=1920&nologo=true")
                .placeholder(R.drawable.bg_cosmic)
                .into(bgImage);

        DataManager dataManager = new DataManager(this);

        btnNewGame.setOnClickListener(v -> {
            startActivity(new Intent(this, CharacterCreationActivity.class));
        });

        btnContinue.setOnClickListener(v -> {
            if (dataManager.loadCharacter() != null) {
                startActivity(new Intent(this, GameSessionActivity.class));
            }
        });

        btnJoinParty.setOnClickListener(v -> {
            startActivity(new Intent(this, PartyListActivity.class));
        });

        btnCatalog.setOnClickListener(v -> {
            startActivity(new Intent(this, CatalogActivity.class));
        });

        btnBlackMarket.setOnClickListener(v -> {
            startActivity(new Intent(this, BlackMarketActivity.class));
        });

        btnSettings.setOnClickListener(v -> {
            startActivity(new Intent(this, SettingsActivity.class));
        });
    }
}
