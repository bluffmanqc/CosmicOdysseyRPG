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
    private RadioGroup avatarTypeGroup;
    private Button generateAvatarBtn;
    private Button uploadAvatarBtn;
    private Button takePhotoBtn;
    private Button createBtn;

    private Character character;
    private GameMasterAI ai;
    private DataManager dataManager;
    private String customAvatarPath = null;
    private String aiAvatarUrl = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_creation);

        character = new Character();
        ai = new GameMasterAI(this);
        dataManager = new DataManager(this);

        initViews();
        setupSpinners();
        setupSeekBars();
        setupButtons();
    }

    private void initViews() {
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
        avatarTypeGroup = findViewById(R.id.avatarTypeGroup);
        generateAvatarBtn = findViewById(R.id.generateAvatarBtn);
        uploadAvatarBtn = findViewById(R.id.uploadAvatarBtn);
        takePhotoBtn = findViewById(R.id.takePhotoBtn);
        createBtn = findViewById(R.id.createBtn);
    }

    private void setupSpinners() {
        String[] races = {"Humain", "Xylarien", "Néo-Machine", "Vorak", "Etherean", "Draconien"};
        String[] classes = {"Pilote", "Psionique", "Ingénieur", "Mercenaire", "Explorateur", "Marchand"};
        String[] backgrounds = {"Vétéran de guerre", "Scientifique", "Criminel", "Noble", "Réfugié", "Archéologue"};

        raceSpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, races));
        classSpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, classes));
        backgroundSpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, backgrounds));
    }

    private void setupSeekBars() {
        setupStatBar(strengthBar, strengthValue, "FOR");
        setupStatBar(dexterityBar, dexterityValue, "DEX");
        setupStatBar(constitutionBar, constitutionValue, "CON");
        setupStatBar(intelligenceBar, intelligenceValue, "INT");
        setupStatBar(wisdomBar, wisdomValue, "SAG");
        setupStatBar(charismaBar, charismaValue, "CHA");
        setupStatBar(luckBar, luckValue, "LUCK");
    }

    private void setupStatBar(SeekBar bar, TextView valueText, String statName) {
        bar.setMax(20);
        bar.setProgress(10);
        valueText.setText(statName + ": 10");
        bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                valueText.setText(statName + ": " + progress);
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    private void setupButtons() {
        generateAvatarBtn.setOnClickListener(v -> generateAIAvatar());
        uploadAvatarBtn.setOnClickListener(v -> pickImageFromGallery());
        takePhotoBtn.setOnClickListener(v -> takePhoto());

        avatarTypeGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radioAI) {
                generateAvatarBtn.setEnabled(true);
            } else {
                generateAvatarBtn.setEnabled(false);
            }
        });

        createBtn.setOnClickListener(v -> createCharacter());
    }

    private void generateAIAvatar() {
        character.setName(nameInput.getText().toString());
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
                customAvatarPath = ImageUtils.copyUriToInternalStorage(this, selectedImage, "avatar_" + System.currentTimeMillis() + ".jpg");
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

    private void createCharacter() {
        String name = nameInput.getText().toString().trim();
        if (name.isEmpty()) {
            Toast.makeText(this, "Entre un nom !", Toast.LENGTH_SHORT).show();
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

        dataManager.saveCharacter(character);

        Intent intent = new Intent(this, GameSessionActivity.class);
        intent.putExtra("character_id", character.getId());
        startActivity(intent);
        finish();
    }
}
