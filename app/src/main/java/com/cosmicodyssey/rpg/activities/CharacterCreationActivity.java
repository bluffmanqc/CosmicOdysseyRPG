package com.cosmicodyssey.rpg.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.cosmicodyssey.rpg.R;
import com.cosmicodyssey.rpg.ai.GameMasterAI;
import com.cosmicodyssey.rpg.data.DataManager;
import com.cosmicodyssey.rpg.utils.ImageUtils;
import com.cosmicodyssey.rpg.models.Character;
import com.cosmicodyssey.rpg.models.CharacterStats;

import java.io.File;

public class CharacterCreationActivity extends AppCompatActivity {
    private static final int PICK_IMAGE = 100;
    private static final int TAKE_PHOTO = 101;
    private static final int MAX_STAT = 18;
    private static final int TOTAL_POINTS = 15;

    private ImageView avatarImage;
    private EditText nameInput;
    private Spinner raceSpinner;
    private Spinner classSpinner;
    private Spinner backgroundSpinner;
    private SeekBar strengthBar;
    private SeekBar dexterityBar;
    private SeekBar constitutionBar;
    private SeekBar intelligenceBar;
    private SeekBar wisdomBar;
    private SeekBar charismaBar;
    private SeekBar luckBar;
    private TextView strengthValue;
    private TextView dexterityValue;
    private TextView constitutionValue;
    private TextView intelligenceValue;
    private TextView wisdomValue;
    private TextView charismaValue;
    private TextView luckValue;
    private TextView pointsRemainingValue;
    private Button createBtn;
    private Button generateAvatarBtn;
    private Button uploadAvatarBtn;
    private Button takePhotoBtn;
    private RadioGroup avatarTypeGroup;
    private RadioButton aiAvatarRadio;
    private RadioButton customAvatarRadio;

    private Character character;
    private GameMasterAI ai;
    private String customAvatarPath;
    private String aiAvatarUrl;
    private boolean isCustomAvatar = false;

    private int[] baseStats = new int[7];
    private int[] allocatedPoints = new int[7];
    private int pointsRemaining = TOTAL_POINTS;

    private static final String[] STAT_NAMES = {"FOR", "DEX", "CON", "INT", "SAG", "CHA", "LUC"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_creation);

        character = new Character();
        ai = new GameMasterAI(this);

        avatarImage = findViewById(R.id.avatarImage);
        nameInput = findViewById(R.id.nameInput);
        raceSpinner = findViewById(R.id.raceSpinner);
        classSpinner = findViewById(R.id.classSpinner);
        backgroundSpinner = findViewById(R.id.backgroundSpinner);
        strengthBar = findViewById(R.id.strengthBar);
        dexterityBar = findViewById(R.id.dexterityBar);
        constitutionBar = findViewById(R.id.constitutionBar);
        intelligenceBar = findViewById(R.id.intelligenceBar);
        wisdomBar = findViewById(R.id.wisdomBar);
        charismaBar = findViewById(R.id.charismaBar);
        luckBar = findViewById(R.id.luckBar);
        strengthValue = findViewById(R.id.strengthValue);
        dexterityValue = findViewById(R.id.dexterityValue);
        constitutionValue = findViewById(R.id.constitutionValue);
        intelligenceValue = findViewById(R.id.intelligenceValue);
        wisdomValue = findViewById(R.id.wisdomValue);
        charismaValue = findViewById(R.id.charismaValue);
        luckValue = findViewById(R.id.luckValue);
        pointsRemainingValue = findViewById(R.id.pointsRemainingValue);
        createBtn = findViewById(R.id.createBtn);
        generateAvatarBtn = findViewById(R.id.generateAvatarBtn);
        uploadAvatarBtn = findViewById(R.id.uploadAvatarBtn);
        takePhotoBtn = findViewById(R.id.takePhotoBtn);
        avatarTypeGroup = findViewById(R.id.avatarTypeGroup);

        setupSpinners();
        setupSeekBars();
        setupButtons();

        applyBaseStats("Humain", "Pilote");
        updatePointsDisplay();
    }

    private void setupSpinners() {
        String[] races = {"Humain", "Xylarien", "Néo-Machine", "Vorak", "Etherean", "Draconien"};
        String[] classes = {"Pilote", "Psionique", "Ingénieur", "Mercenaire", "Explorateur", "Marchand"};
        String[] backgrounds = {"Vétéran de guerre", "Scientifique", "Criminel", "Noble", "Réfugié", "Archéologue"};

        raceSpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, races));
        classSpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, classes));
        backgroundSpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, backgrounds));

        raceSpinner.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, android.view.View view, int position, long id) {
                applyBaseStats(raceSpinner.getSelectedItem().toString(), classSpinner.getSelectedItem().toString());
            }
            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {}
        });

        classSpinner.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, android.view.View view, int position, long id) {
                applyBaseStats(raceSpinner.getSelectedItem().toString(), classSpinner.getSelectedItem().toString());
            }
            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {}
        });
    }

    private void setupSeekBars() {
        SeekBar[] bars = {strengthBar, dexterityBar, constitutionBar, intelligenceBar, wisdomBar, charismaBar, luckBar};
        for (int i = 0; i < bars.length; i++) {
            final int idx = i;
            bars[i].setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if (!fromUser) return;
                    int base = baseStats[idx];
                    int currentAllocated = progress - base;
                    if (currentAllocated < 0) currentAllocated = 0;
                    
                    int otherAllocated = 0;
                    for (int j = 0; j < 7; j++) {
                        if (j != idx) otherAllocated += allocatedPoints[j];
                    }
                    
                    if (currentAllocated + otherAllocated > TOTAL_POINTS) {
                        currentAllocated = TOTAL_POINTS - otherAllocated;
                        if (currentAllocated < 0) currentAllocated = 0;
                    }
                    
                    if (base + currentAllocated > MAX_STAT) {
                        currentAllocated = MAX_STAT - base;
                    }
                    
                    allocatedPoints[idx] = currentAllocated;
                    seekBar.setProgress(base + currentAllocated);
                    updateStatValues();
                    updatePointsDisplay();
                }
                @Override public void onStartTrackingTouch(SeekBar seekBar) {}
                @Override public void onStopTrackingTouch(SeekBar seekBar) {}
            });
        }
    }

    private void updatePointsDisplay() {
        int used = 0;
        for (int p : allocatedPoints) used += p;
        pointsRemaining = TOTAL_POINTS - used;
        pointsRemainingValue.setText("Points restants: " + pointsRemaining);
        if (pointsRemaining < 0) {
            pointsRemainingValue.setTextColor(0xFFFF4444);
        } else {
            pointsRemainingValue.setTextColor(0xFF00FF88);
        }
    }

    private void applyBaseStats(String race, String className) {
        int[] newBase = {10, 10, 10, 10, 10, 10, 10};

        switch (race) {
            case "Humain": break;
            case "Xylarien": newBase[0]=8; newBase[1]=14; newBase[2]=8; newBase[3]=12; newBase[4]=12; newBase[5]=10; newBase[6]=10; break;
            case "Néo-Machine": newBase[0]=12; newBase[1]=8; newBase[2]=14; newBase[3]=12; newBase[4]=8; newBase[5]=6; newBase[6]=8; break;
            case "Vorak": newBase[0]=16; newBase[1]=6; newBase[2]=14; newBase[3]=6; newBase[4]=8; newBase[5]=6; newBase[6]=12; break;
            case "Etherean": newBase[0]=6; newBase[1]=10; newBase[2]=8; newBase[3]=16; newBase[4]=14; newBase[5]=12; newBase[6]=10; break;
            case "Draconien": newBase[0]=14; newBase[1]=8; newBase[2]=12; newBase[3]=10; newBase[4]=10; newBase[5]=12; newBase[6]=8; break;
        }

        switch (className) {
            case "Pilote": newBase[1]+=2; newBase[4]+=1; break;
            case "Psionique": newBase[3]+=2; newBase[5]+=1; break;
            case "Ingénieur": newBase[2]+=1; newBase[3]+=2; break;
            case "Mercenaire": newBase[0]+=2; newBase[2]+=1; break;
            case "Explorateur": newBase[1]+=2; newBase[4]+=1; break;
            case "Marchand": newBase[5]+=2; newBase[6]+=1; break;
        }

        for (int i = 0; i < 7; i++) {
            if (newBase[i] > MAX_STAT) newBase[i] = MAX_STAT;
            baseStats[i] = newBase[i];
        }

        for (int i = 0; i < 7; i++) {
            allocatedPoints[i] = 0;
        }
        pointsRemaining = TOTAL_POINTS;

        strengthBar.setProgress(baseStats[0]);
        dexterityBar.setProgress(baseStats[1]);
        constitutionBar.setProgress(baseStats[2]);
        intelligenceBar.setProgress(baseStats[3]);
        wisdomBar.setProgress(baseStats[4]);
        charismaBar.setProgress(baseStats[5]);
        luckBar.setProgress(baseStats[6]);

        updateStatValues();
        updatePointsDisplay();
    }

    private void updateStatValues() {
        strengthValue.setText("FOR: " + strengthBar.getProgress() + " (base: " + baseStats[0] + ")");
        dexterityValue.setText("DEX: " + dexterityBar.getProgress() + " (base: " + baseStats[1] + ")");
        constitutionValue.setText("CON: " + constitutionBar.getProgress() + " (base: " + baseStats[2] + ")");
        intelligenceValue.setText("INT: " + intelligenceBar.getProgress() + " (base: " + baseStats[3] + ")");
        wisdomValue.setText("SAG: " + wisdomBar.getProgress() + " (base: " + baseStats[4] + ")");
        charismaValue.setText("CHA: " + charismaBar.getProgress() + " (base: " + baseStats[5] + ")");
        luckValue.setText("LUC: " + luckBar.getProgress() + " (base: " + baseStats[6] + ")");
    }

    private void setupButtons() {
        createBtn.setOnClickListener(v -> createCharacter());
        generateAvatarBtn.setOnClickListener(v -> generateAIAvatar());
        uploadAvatarBtn.setOnClickListener(v -> pickImageFromGallery());
        takePhotoBtn.setOnClickListener(v -> takePhoto());
    }

    private void createCharacter() {
        String name = nameInput.getText().toString().trim();
        if (name.isEmpty()) {
            Toast.makeText(this, "Entre un nom !", Toast.LENGTH_SHORT).show();
            return;
        }

        if (pointsRemaining < 0) {
            Toast.makeText(this, "Trop de points alloués !", Toast.LENGTH_SHORT).show();
            return;
        }

        if (character.getAvatarUrl() == null) {
            new AlertDialog.Builder(this)
                .setTitle("Pas d'avatar")
                .setMessage("Générer un avatar IA automatiquement ?")
                .setPositiveButton("Oui", (dialog, which) -> {
                    generateAIAvatar();
                    saveAndContinue(name);
                })
                .setNegativeButton("Non", null)
                .show();
            return;
        }

        saveAndContinue(name);
    }

    private void saveAndContinue(String name) {
        character.setName(name);
        character.setRace(raceSpinner.getSelectedItem().toString());
        character.setClassName(classSpinner.getSelectedItem().toString());
        character.setBackground(backgroundSpinner.getSelectedItem().toString());

        CharacterStats stats = new CharacterStats();
        stats.setStrength(strengthBar.getProgress());
        stats.setDexterity(dexterityBar.getProgress());
        stats.setConstitution(constitutionBar.getProgress());
        stats.setIntelligence(intelligenceBar.getProgress());
        stats.setWisdom(wisdomBar.getProgress());
        stats.setCharisma(charismaBar.getProgress());
        stats.setLuck(luckBar.getProgress());
        character.setStats(stats);

        new DataManager(this).saveCharacter(character);

        Intent intent = new Intent(this, CharacterSheetActivity.class);
        intent.putExtra("character_id", character.getId());
        startActivity(intent);
        finish();
    }

    private void generateAIAvatar() {
        character.setName(nameInput.getText().toString().trim());
        character.setRace(raceSpinner.getSelectedItem().toString());
        character.setClassName(classSpinner.getSelectedItem().toString());

        aiAvatarUrl = ai.generateCharacterImageUrl(character);
        character.setAvatarUrl(aiAvatarUrl);
        character.setAvatarType("ai");

        Glide.with(this)
            .load(aiAvatarUrl)
            .placeholder(R.drawable.ic_character_placeholder)
            .into(avatarImage);

        Toast.makeText(this, "Avatar IA généré !", Toast.LENGTH_SHORT).show();
    }

    private void pickImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE);
    }

    private void takePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File photoFile = new File(getFilesDir(), "avatar_" + System.currentTimeMillis() + ".jpg");
        Uri photoUri = FileProvider.getUriForFile(this, getPackageName() + ".fileprovider", photoFile);
        customAvatarPath = photoFile.getAbsolutePath();
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
        startActivityForResult(intent, TAKE_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == PICK_IMAGE && data != null) {
                Uri selectedImage = data.getData();
                customAvatarPath = selectedImage.toString();
                character.setAvatarUrl(customAvatarPath);
                character.setAvatarType("custom");
                Glide.with(this).load(customAvatarPath).into(avatarImage);
            } else if (requestCode == TAKE_PHOTO) {
                character.setAvatarUrl(customAvatarPath);
                character.setAvatarType("custom");
                Glide.with(this).load(customAvatarPath).into(avatarImage);
            }
        }
    }
}
