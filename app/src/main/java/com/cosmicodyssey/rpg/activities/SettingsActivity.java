package com.cosmicodyssey.rpg.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.cosmicodyssey.rpg.R;
import com.cosmicodyssey.rpg.ai.GameMasterAI;

public class SettingsActivity extends AppCompatActivity {
    private static final String PREFS_NAME = "CosmicOdysseyPrefs";
    private static final String KEY_OPENROUTER_API = "openrouter_api_key";
    private static final String KEY_TTS_ENABLED = "tts_enabled";

    private EditText openrouterKeyInput;
    private Switch ttsFallbackSwitch;
    private Button saveButton;
    private Button testOpenRouterButton;

    private SharedPreferences prefs;
    private GameMasterAI ai;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        ai = new GameMasterAI(this);

        initViews();
        loadSettings();
        setupListeners();
    }

    private void initViews() {
        openrouterKeyInput = findViewById(R.id.openrouterKeyInput);
        ttsFallbackSwitch = findViewById(R.id.ttsFallbackSwitch);
        saveButton = findViewById(R.id.saveButton);
        testOpenRouterButton = findViewById(R.id.testOpenRouterButton);
    }

    private void loadSettings() {
        openrouterKeyInput.setText(prefs.getString(KEY_OPENROUTER_API, ""));
        ttsFallbackSwitch.setChecked(prefs.getBoolean(KEY_TTS_ENABLED, true));
        updateTtsLabels();
    }

    private void setupListeners() {
        saveButton.setOnClickListener(v -> saveSettings());
        testOpenRouterButton.setOnClickListener(v -> testApiKey());
    }

    private void updateTtsLabels() {
        // TTS labels removed - only fallback switch remains
    }

    private void saveSettings() {
        String apiKey = openrouterKeyInput.getText().toString().trim();
        boolean ttsEnabled = ttsFallbackSwitch.isChecked();

        prefs.edit()
                .putString(KEY_OPENROUTER_API, apiKey)
                .putBoolean(KEY_TTS_ENABLED, ttsEnabled)
                .commit();

        ai.setApiKey(apiKey);

        Toast.makeText(this, "Paramètres sauvegardés !", Toast.LENGTH_SHORT).show();
    }

    private void testApiKey() {
        String apiKey = openrouterKeyInput.getText().toString().trim();
        if (apiKey.isEmpty()) {
            Toast.makeText(this, "Entre une clé API d'abord", Toast.LENGTH_SHORT).show();
            return;
        }

        ai.setApiKey(apiKey);
        Toast.makeText(this, "Test de connexion...", Toast.LENGTH_SHORT).show();

        // Simple test call
        com.cosmicodyssey.rpg.models.Party testParty = new com.cosmicodyssey.rpg.models.Party();
        com.cosmicodyssey.rpg.models.Character testChar = new com.cosmicodyssey.rpg.models.Character();
        testChar.setName("Test");
        testChar.setRace("Humain");
        testChar.setClassName("Pilote");

        ai.generateStoryResponse(testParty, testChar, "Bonjour", new GameMasterAI.AIResponseCallback() {
            @Override
            public void onSuccess(GameMasterAI.StoryResponse response) {
                runOnUiThread(() -> Toast.makeText(SettingsActivity.this, "✅ API OK !", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() -> Toast.makeText(SettingsActivity.this, "❌ Erreur: " + error, Toast.LENGTH_LONG).show());
            }
        });
    }
}
