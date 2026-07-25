package com.cosmicodyssey.rpg;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SettingsActivity extends AppCompatActivity {
    
    private static final String PREFS_NAME = "CosmicOdysseyPrefs";
    private static final String KEY_OPENROUTER = "openrouter_api_key";
    private static final String KEY_GROQ = "groq_api_key";
    private static final String KEY_ELEVENLABS = "elevenlabs_api_key";
    private static final String KEY_ELEVENLABS_VOICE = "elevenlabs_voice_id";
    private static final String KEY_TTS_FALLBACK = "tts_fallback_enabled";
    
    private EditText openrouterInput, groqInput, elevenlabsInput;
    private Button testOpenRouterBtn, testGroqBtn, testElevenLabsBtn, fetchVoicesBtn, saveButton;
    private TextView openrouterStatus, groqStatus;
    private Spinner voiceSpinner;
    private Switch ttsFallbackSwitch;
    
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());
    
    private final Map<String, String> voiceMap = new HashMap<>();
    private String selectedVoiceId = "";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        
        initViews();
        loadSavedValues();
        setupListeners();
    }
    
    private void initViews() {
        openrouterInput = findViewById(R.id.openrouterKeyInput);
        groqInput = findViewById(R.id.groqKeyInput);
        elevenlabsInput = findViewById(R.id.elevenlabsKeyInput);
        
        testOpenRouterBtn = findViewById(R.id.testOpenRouterButton);
        testGroqBtn = findViewById(R.id.testGroqButton);
        testElevenLabsBtn = findViewById(R.id.testElevenLabsButton);
        fetchVoicesBtn = findViewById(R.id.fetchVoicesButton);
        saveButton = findViewById(R.id.saveButton);
        
        openrouterStatus = findViewById(R.id.openrouterStatus);
        groqStatus = findViewById(R.id.groqStatus);
        
        voiceSpinner = findViewById(R.id.voiceSpinner);
        ttsFallbackSwitch = findViewById(R.id.ttsFallbackSwitch);
    }
    
    private void loadSavedValues() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        openrouterInput.setText(prefs.getString(KEY_OPENROUTER, ""));
        groqInput.setText(prefs.getString(KEY_GROQ, ""));
        elevenlabsInput.setText(prefs.getString(KEY_ELEVENLABS, ""));
        selectedVoiceId = prefs.getString(KEY_ELEVENLABS_VOICE, "");
        ttsFallbackSwitch.setChecked(prefs.getBoolean(KEY_TTS_FALLBACK, true));
    }
    
    private void setupListeners() {
        testOpenRouterBtn.setOnClickListener(v -> testApi("openrouter"));
        testGroqBtn.setOnClickListener(v -> testApi("groq"));
        testElevenLabsBtn.setOnClickListener(v -> testApi("elevenlabs"));
        fetchVoicesBtn.setOnClickListener(v -> fetchElevenLabsVoices());
        
        voiceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String voiceName = parent.getItemAtPosition(position).toString();
                selectedVoiceId = voiceMap.get(voiceName);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        
        saveButton.setOnClickListener(v -> saveSettings());
    }
    
    private void testApi(String api) {
        String key = "";
        String urlStr = "";
        String headerName = "";
        
        switch(api) {
            case "openrouter":
                key = openrouterInput.getText().toString().trim();
                urlStr = "https://openrouter.ai/api/v1/models";
                headerName = "Authorization";
                break;
            case "groq":
                key = groqInput.getText().toString().trim();
                urlStr = "https://api.groq.com/openai/v1/models";
                headerName = "Authorization";
                break;
            case "elevenlabs":
                key = elevenlabsInput.getText().toString().trim();
                urlStr = "https://api.elevenlabs.io/v1/voices";
                headerName = "xi-api-key";
                break;
        }
        
        if (key.isEmpty()) {
            Toast.makeText(this, "Entre une clé API d'abord", Toast.LENGTH_SHORT).show();
            return;
        }
        
        final String finalKey = key;
        final String finalUrl = urlStr;
        final String finalHeader = headerName;
        final String apiName = api;
        
        executor.execute(() -> {
            try {
                URL url = new URL(finalUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(10000);
                conn.setReadTimeout(10000);
                
                if (apiName.equals("elevenlabs")) {
                    conn.setRequestProperty(finalHeader, finalKey);
                } else {
                    conn.setRequestProperty(finalHeader, "Bearer " + finalKey);
                }
                
                int responseCode = conn.getResponseCode();
                final boolean success = (responseCode == 200);
                
                mainHandler.post(() -> {
                    if (apiName.equals("openrouter")) {
                        openrouterStatus.setText(success ? "Connecté" : "Erreur " + responseCode);
                        openrouterStatus.setTextColor(success ? 0xFF00FF41 : 0xFFFF4444);
                    } else if (apiName.equals("groq")) {
                        groqStatus.setText(success ? "Connecté" : "Erreur " + responseCode);
                        groqStatus.setTextColor(success ? 0xFF00FF41 : 0xFFFF4444);
                    } else {
                        Toast.makeText(this, success ? "ElevenLabs OK" : "Erreur " + responseCode, Toast.LENGTH_SHORT).show();
                    }
                });
                
            } catch (Exception e) {
                mainHandler.post(() -> {
                    if (apiName.equals("openrouter")) {
                        openrouterStatus.setText("Erreur");
                        openrouterStatus.setTextColor(0xFFFF4444);
                    } else if (apiName.equals("groq")) {
                        groqStatus.setText("Erreur");
                        groqStatus.setTextColor(0xFFFF4444);
                    } else {
                        Toast.makeText(this, "Erreur: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
    
    private void fetchElevenLabsVoices() {
        String key = elevenlabsInput.getText().toString().trim();
        if (key.isEmpty()) {
            Toast.makeText(this, "Entre ta clé ElevenLabs d'abord", Toast.LENGTH_SHORT).show();
            return;
        }
        
        executor.execute(() -> {
            try {
                URL url = new URL("https://api.elevenlabs.io/v1/voices");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("xi-api-key", key);
                conn.setConnectTimeout(10000);
                conn.setReadTimeout(10000);
                
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) sb.append(line);
                reader.close();
                
                JSONObject json = new JSONObject(sb.toString());
                JSONArray voices = json.getJSONArray("voices");
                
                List<String> voiceNames = new ArrayList<>();
                voiceMap.clear();
                
                for (int i = 0; i < voices.length(); i++) {
                    JSONObject voice = voices.getJSONObject(i);
                    String name = voice.getString("name");
                    String id = voice.getString("voice_id");
                    voiceNames.add(name);
                    voiceMap.put(name, id);
                }
                
                mainHandler.post(() -> {
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, voiceNames);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    voiceSpinner.setAdapter(adapter);
                    
                    if (!selectedVoiceId.isEmpty()) {
                        for (int i = 0; i < voiceNames.size(); i++) {
                            if (voiceMap.get(voiceNames.get(i)).equals(selectedVoiceId)) {
                                voiceSpinner.setSelection(i);
                                break;
                            }
                        }
                    }
                    
                    Toast.makeText(this, voiceNames.size() + " voix chargées", Toast.LENGTH_SHORT).show();
                });
                
            } catch (Exception e) {
                mainHandler.post(() -> Toast.makeText(this, "Erreur chargement voix: " + e.getMessage(), Toast.LENGTH_LONG).show());
            }
        });
    }
    
    private void saveSettings() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        
        editor.putString(KEY_OPENROUTER, openrouterInput.getText().toString().trim());
        editor.putString(KEY_GROQ, groqInput.getText().toString().trim());
        editor.putString(KEY_ELEVENLABS, elevenlabsInput.getText().toString().trim());
        editor.putString(KEY_ELEVENLABS_VOICE, selectedVoiceId);
        editor.putBoolean(KEY_TTS_FALLBACK, ttsFallbackSwitch.isChecked());
        editor.apply();
        
        Toast.makeText(this, "Paramètres sauvegardés !", Toast.LENGTH_SHORT).show();
        finish();
    }
    
    public static String getOpenRouterKey(android.content.Context context) {
        return context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE).getString(KEY_OPENROUTER, "");
    }
    
    public static String getGroqKey(android.content.Context context) {
        return context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE).getString(KEY_GROQ, "");
    }
    
    public static String getElevenLabsKey(android.content.Context context) {
        return context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE).getString(KEY_ELEVENLABS, "");
    }
    
    public static String getElevenLabsVoiceId(android.content.Context context) {
        return context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE).getString(KEY_ELEVENLABS_VOICE, "");
    }
    
    public static boolean isTtsFallbackEnabled(android.content.Context context) {
        return context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE).getBoolean(KEY_TTS_FALLBACK, true);
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        executor.shutdown();
    }
}
