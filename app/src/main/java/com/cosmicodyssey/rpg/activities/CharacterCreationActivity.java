package com.cosmicodyssey.rpg.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.cosmicodyssey.rpg.R;
import com.cosmicodyssey.rpg.ai.GameMasterAI;
import com.cosmicodyssey.rpg.data.DataManager;
import com.cosmicodyssey.rpg.models.Character;
import com.cosmicodyssey.rpg.models.CharacterClass;
import com.cosmicodyssey.rpg.models.Race;

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
    private Race selectedRace = Race.getTerrien();
    private CharacterClass selectedClass = CharacterClass.getMelee();

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
        Race[] races = Race.getAllRaces();
        String[] raceNames = new String[races.length];
        for (int i = 0; i < races.length; i++) {
            raceNames[i] = races[i].getName();
        }
        ArrayAdapter<String> raceAdapter = new ArrayAdapter<>(this, 
            android.R.layout.simple_spinner_item, raceNames);
        raceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        raceSpinner.setAdapter(raceAdapter);
        
        raceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedRace = races[position];
                applyRaceStats();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        CharacterClass[] classes = CharacterClass.getAllClasses();
        String[] classNames = new String[classes.length];
        for (int i = 0; i < classes.length; i++) {
            classNames[i] = classes[i].getName();
        }
        ArrayAdapter<String> classAdapter = new ArrayAdapter<>(this, 
            android.R.layout.simple_spinner_item, classNames);
        classAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        classSpinner.setAdapter(classAdapter);
        
        classSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedClass = classes[position];
                showClassSkills(selectedClass);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        String[] backgrounds = {"Réfugié", "Mercenaire", "Scientifique", "Pirate", "Noble", "Marchand"};
        ArrayAdapter<String> backgroundAdapter = new ArrayAdapter<>(this, 
            android.R.layout.simple_spinner_item, backgrounds);
        backgroundAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        backgroundSpinner.setAdapter(backgroundAdapter);
    }

    private void applyRaceStats() {
        strengthBar.setProgress(selectedRace.getBaseSTR());
        dexterityBar.setProgress(selectedRace.getBaseDEX());
        constitutionBar.setProgress(selectedRace.getBaseCON());
        intelligenceBar.setProgress(selectedRace.getBaseINT());
        wisdomBar.setProgress(selectedRace.getBaseSAG());
        charismaBar.setProgress(selectedRace.getBaseCHA());
        luckBar.setProgress(selectedRace.getBaseLUCK());
        
        strengthBar.setMax(selectedRace.getMaxSTR());
        dexterityBar.setMax(selectedRace.getMaxDEX());
        constitutionBar.setMax(selectedRace.getMaxCON());
        intelligenceBar.setMax(selectedRace.getMaxINT());
        wisdomBar.setMax(selectedRace.getMaxSAG());
        charismaBar.setMax(selectedRace.getMaxCHA());
        luckBar.setMax(selectedRace.getMaxLUCK());
        
        updateStatDisplays();
    }

    private void setupSeekBars() {
        setupStatBar(strengthBar, strengthValue, "FOR", selectedRace.getMaxSTR());
        setupStatBar(dexterityBar, dexterityValue, "DEX", selectedRace.getMaxDEX());
        setupStatBar(constitutionBar, constitutionValue, "CON", selectedRace.getMaxCON());
        setupStatBar(intelligenceBar, intelligenceValue, "INT", selectedRace.getMaxINT());
        setupStatBar(wisdomBar, wisdomValue, "SAG", selectedRace.getMaxSAG());
        setupStatBar(charismaBar, charismaValue, "CHA", selectedRace.getMaxCHA());
        setupStatBar(luckBar, luckValue, "LUCK", selectedRace.getMaxLUCK());
    }

    private void setupStatBar(SeekBar bar, TextView valueText, String statName, int max) {
        bar.setMax(max);
        bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                valueText.setText(statName + ": " + progress);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    private void updateStatDisplays() {
        strengthValue.setText("FOR: " + strengthBar.getProgress());
        dexterityValue.setText("DEX: " + dexterityBar.getProgress());
        constitutionValue.setText("CON: " + constitutionBar.getProgress());
        intelligenceValue.setText("INT: " + intelligenceBar.getProgress());
        wisdomValue.setText("SAG: " + wisdomBar.getProgress());
        charismaValue.setText("CHA: " + charismaBar.getProgress());
        luckValue.setText("LUCK: " + luckBar.getProgress());
    }

    private void showClassSkills(CharacterClass cls) {
        StringBuilder sb = new StringBuilder();
        sb.append("Compétences de ").append(cls.getName()).append(" :\n\n");
        for (CharacterClass.Skill skill : cls.getSkillTree()) {
            sb.append(skill.getLevel()).append(" - ").append(skill.getName())
              .append("\n").append(skill.getEffect()).append("\n\n");
        }
        
        new AlertDialog.Builder(this)
            .setTitle("Arbre de compétences")
            .setMessage(sb.toString())
            .setPositiveButton("OK", null)
            .show();
    }

    private void setupButtons() {
        generateAvatarBtn.setOnClickListener(v -> generateAvatar());
        uploadAvatarBtn.setOnClickListener(v -> uploadAvatar());
        takePhotoBtn.setOnClickListener(v -> takePhoto());
        createBtn.setOnClickListener(v -> createCharacter());
    }

    private void generateAvatar() {
        String prompt = nameInput.getText().toString() + " " + 
                       selectedRace.getName() + " " + selectedClass.getName();
        String imageUrl = "https://image.pollinations.ai/prompt/" + prompt + "?width=512&height=512";
        Glide.with(this).load(imageUrl).into(avatarImage);
        aiAvatarUrl = imageUrl;
    }

    private void uploadAvatar() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE);
    }

    private void takePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, TAKE_PHOTO);
    }

    private void createCharacter() {
        String name = nameInput.getText().toString();
        if (name.isEmpty()) {
            Toast.makeText(this, "Veuillez entrer un nom", Toast.LENGTH_SHORT).show();
            return;
        }

        character.setName(name);
        character.setRace(selectedRace.getName());
        character.setClassName(selectedClass.getName());
        character.setBackground(backgroundSpinner.getSelectedItem().toString());
        
        if (aiAvatarUrl != null) {
            character.setAvatarUrl(aiAvatarUrl);
        }

        dataManager.saveCharacter(character);
        
        Toast.makeText(this, "Personnage créé avec succès !", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == PICK_IMAGE) {
                Uri uri = data.getData();
                avatarImage.setImageURI(uri);
                customAvatarPath = uri.getPath();
            } else if (requestCode == TAKE_PHOTO) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                avatarImage.setImageBitmap(photo);
            }
        }
    }
}
