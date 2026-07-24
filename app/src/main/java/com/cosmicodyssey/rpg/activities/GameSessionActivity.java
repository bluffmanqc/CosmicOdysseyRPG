package com.cosmicodyssey.rpg.activities;
import android.widget.ProgressBar;
import android.speech.tts.TextToSpeech;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cosmicodyssey.rpg.CosmicOdysseyApp;
import com.cosmicodyssey.rpg.R;
import com.cosmicodyssey.rpg.ai.GameMasterAI;
import com.cosmicodyssey.rpg.data.DataManager;
import com.cosmicodyssey.rpg.models.Character;
import com.cosmicodyssey.rpg.models.GameMessage;
import com.cosmicodyssey.rpg.models.Equipment;
import com.cosmicodyssey.rpg.models.Party;
import com.cosmicodyssey.rpg.models.Planet;

import java.util.ArrayList;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.List;

public class GameSessionActivity extends AppCompatActivity {
    private static final String PREFS_MESSAGES = "cosmic_messages";
    private static final String KEY_MESSAGE_LIST = "message_list";
    private RecyclerView messageList;
    private ProgressBar loadingProgress;
    private TextToSpeech tts;
    private EditText inputText;
    private ImageButton sendBtn;
    private ImageButton voiceBtn;
    private ImageButton diceBtn;
    private ImageButton mapBtn;
    private ImageButton sheetBtn;
    private ImageButton shopBtn;
    private ImageView sceneImage;
    private LinearLayout choicesContainer;
    private ScrollView scrollView;

    private Character character;
    private Party party;
    private GameMasterAI ai;
    private DataManager dataManager;
    private MessageAdapter messageAdapter;
    private List<GameMessage> messages;
    private boolean ttsEnabled = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_session);

        dataManager = new DataManager(this);
        ai = new GameMasterAI(this);

        character = dataManager.loadCharacter();
        if (character == null) {
            startActivity(new Intent(this, CharacterCreationActivity.class));
            finish();
            return;
        }

        loadOrCreateParty();
        initViews();
        setupButtons();
        loadMessages();
        if (messages.isEmpty()) {
            addSystemMessage("Bienvenue dans Cosmic Odyssey, " + character.getName() + " !");
        addSystemMessage("Tu te trouves sur " + party.getCurrentPlanet() + ", dans le système " + party.getCurrentSystem());
        }
    }

    private void loadOrCreateParty() {
        List<Party> parties = dataManager.loadParties();
        if (!parties.isEmpty()) {
            party = parties.get(0);
        } else {
            party = new Party();
            party.setName("Aventure Cosmique");
            party.setCurrentPlanet("Terra Novus");
            party.setCurrentSystem("Système Sol Prime");
            party.setHostId(character.getId());
            party.getPlayerIds().add(character.getId());
            dataManager.saveParty(party);
        }
    }

    private void initViews() {
        messageList = findViewById(R.id.messageList);
        inputText = findViewById(R.id.inputText);
        sendBtn = findViewById(R.id.sendBtn);
        voiceBtn = findViewById(R.id.voiceBtn);
        diceBtn = findViewById(R.id.diceBtn);
        mapBtn = findViewById(R.id.mapBtn);
        sheetBtn = findViewById(R.id.sheetBtn);
        shopBtn = findViewById(R.id.shopBtn);
        sceneImage = findViewById(R.id.sceneImage);
        choicesContainer = findViewById(R.id.choicesContainer);
        scrollView = findViewById(R.id.scrollView);
        loadingProgress = findViewById(R.id.loadingProgress);

        messages = new ArrayList<>();
        messageAdapter = new MessageAdapter(messages);
        messageList.setLayoutManager(new LinearLayoutManager(this));
        messageList.setAdapter(messageAdapter);
    }

    private void setupButtons() {
        sendBtn.setOnClickListener(v -> sendPlayerAction());
        voiceBtn.setOnClickListener(v -> toggleTTS());
        diceBtn.setOnClickListener(v -> startActivity(new Intent(this, DiceRollActivity.class)));
        mapBtn.setOnClickListener(v -> startActivity(new Intent(this, GalaxyMapActivity.class)));
        sheetBtn.setOnClickListener(v -> startActivity(new Intent(this, CharacterSheetActivity.class)));
        shopBtn.setOnClickListener(v -> startActivity(new Intent(this, ShopActivity.class)));
    }

    private void sendPlayerAction() {
        String action = inputText.getText().toString().trim();
        if (action.isEmpty()) return;

        addPlayerMessage(action);
        inputText.setText("");

        if (!ai.hasApiKey()) {
            addSystemMessage("Configure ta clé API OpenRouter dans Paramètres pour jouer avec le MJ IA.");
            return;
        }

        loadingProgress.setVisibility(View.VISIBLE);
        ai.generateStoryResponse(party, character, action, new GameMasterAI.AIResponseCallback() {
            @Override
            public void onSuccess(GameMasterAI.StoryResponse response) {
                runOnUiThread(() -> {
                    addGMMessage(response.narration, response.voiceText);
                    
                    if (response.sceneImage != null && !response.sceneImage.isEmpty()) {
                        String imageUrl = ai.generateSceneImageUrl(response.sceneImage);
                        loadSceneImage(imageUrl);
                    }

                    if (!response.choices.isEmpty()) {
                        showChoices(response.choices);
                    }

                    if (!response.newItems.isEmpty()) {
                        for (Equipment item : response.newItems) {
                            dataManager.addToCatalog(item);
                            party.getSharedEquipment().add(item);
                            addSystemMessage("Nouvel objet découvert : " + item.getName() + " !");
                        }
                    }

                    if (!response.newPlanets.isEmpty()) {
                        for (Planet planet : response.newPlanets) {
                            party.getDiscoveredPlanets().add(planet);
                            addSystemMessage("Nouvelle planète découverte : " + planet.getName() + " !");
                        }
                    }

                    party.getStoryHistory().add(response.narration);
                    dataManager.saveParty(party);
                });
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                            loadingProgress.setVisibility(View.GONE);
                            addSystemMessage("Erreur MJ : " + error);
                        });
            }
        });
    }

    private void saveMessages() {
        SharedPreferences prefs = getSharedPreferences(PREFS_MESSAGES, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = gson.toJson(messages);
        prefs.edit().putString(KEY_MESSAGE_LIST, json).apply();
    }

    private void loadMessages() {
        SharedPreferences prefs = getSharedPreferences(PREFS_MESSAGES, MODE_PRIVATE);
        String json = prefs.getString(KEY_MESSAGE_LIST, null);
        if (json != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<GameMessage>>(){}.getType();
            ArrayList<GameMessage> savedMessages = gson.fromJson(json, type);
            if (savedMessages != null) {
                messages.clear();
                messages.addAll(savedMessages);
                messageAdapter.notifyDataSetChanged();
                scrollToBottom();
            }
        }
    }

    private void addPlayerMessage(String content) {
        GameMessage msg = new GameMessage();
        msg.setType(GameMessage.Type.DIALOGUE);
        msg.setContent(content);
        msg.setSender(character.getName());
        msg.setPlayer(true);
        messages.add(msg);
        saveMessages();
        messageAdapter.notifyItemInserted(messages.size() - 1);
        scrollToBottom();
    }

    private void addGMMessage(String content, String voiceText) {
        GameMessage msg = new GameMessage();
        msg.setType(GameMessage.Type.NARRATION);
        msg.setContent(content);
        msg.setSender("MJ Cosmique");
        msg.setVoiceText(voiceText);
        messages.add(msg);
        saveMessages();
        messageAdapter.notifyItemInserted(messages.size() - 1);

        if (ttsEnabled && voiceText != null) {
            CosmicOdysseyApp.getInstance().getTTSManager().speak(voiceText);
        }

        scrollToBottom();
    }

    private void addSystemMessage(String content) {
        GameMessage msg = new GameMessage();
        msg.setType(GameMessage.Type.SYSTEM);
        msg.setContent(content);
        msg.setSender("Système");
        messages.add(msg);
        saveMessages();
        messageAdapter.notifyItemInserted(messages.size() - 1);
        scrollToBottom();
    }

    private void loadSceneImage(String url) {
        Glide.with(this)
                .load(url)
                .placeholder(R.drawable.bg_cosmic)
                .into(sceneImage);
    }

    private void showChoices(List<String> choices) {
        choicesContainer.removeAllViews();
        for (String choice : choices) {
            Button btn = new Button(this);
            btn.setText(choice);
            btn.setBackgroundResource(R.drawable.bg_choice_button);
            btn.setTextColor(getResources().getColor(android.R.color.white));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(8, 4, 8, 4);
            btn.setLayoutParams(params);
            btn.setOnClickListener(v -> {
                inputText.setText(choice);
                sendPlayerAction();
                choicesContainer.removeAllViews();
            });
            choicesContainer.addView(btn);
        }
    }

    private void toggleTTS() {
        ttsEnabled = !ttsEnabled;
        voiceBtn.setImageResource(ttsEnabled ? R.drawable.ic_volume_on : R.drawable.ic_volume_off);
        Toast.makeText(this, ttsEnabled ? "Voix activée" : "Voix désactivée", Toast.LENGTH_SHORT).show();
    }

    private void scrollToBottom() {
        scrollView.post(() -> scrollView.fullScroll(View.FOCUS_DOWN));
    }

    class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
        private List<GameMessage> msgs;

        MessageAdapter(List<GameMessage> msgs) { this.msgs = msgs; }

        @Override
        public ViewHolder onCreateViewHolder(android.view.ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_message, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            GameMessage msg = msgs.get(position);
            holder.senderText.setText(msg.getSender());
            holder.contentText.setText(msg.getContent());

                if (msg.getVoiceText() != null && !msg.getVoiceText().isEmpty()) {
                    holder.playVoiceBtn.setVisibility(View.VISIBLE);
                    holder.playVoiceBtn.setOnClickListener(v -> {
                        if (tts != null) {
                            tts.speak(msg.getVoiceText(), TextToSpeech.QUEUE_FLUSH, null, null);
                        }
                    });
                } else {
                    holder.playVoiceBtn.setVisibility(View.GONE);
                }

            if (msg.isPlayer()) {
                holder.itemView.setBackgroundResource(R.drawable.bg_message_player);
            } else if (msg.getType() == GameMessage.Type.SYSTEM) {
                holder.itemView.setBackgroundResource(R.drawable.bg_message_system);
                holder.senderText.setTextColor(getResources().getColor(android.R.color.holo_orange_light));
            } else {
                holder.itemView.setBackgroundResource(R.drawable.bg_message_gm);
            }
        }

        @Override
        public int getItemCount() { return msgs.size(); }

        class ViewHolder extends RecyclerView.ViewHolder {
            ImageButton playVoiceBtn;
            TextView senderText, contentText;
            ViewHolder(View itemView) {
            super(itemView);
            playVoiceBtn = itemView.findViewById(R.id.playVoiceBtn);
            senderText = itemView.findViewById(R.id.senderText);
            contentText = itemView.findViewById(R.id.contentText);
                senderText = itemView.findViewById(R.id.senderText);
                contentText = itemView.findViewById(R.id.contentText);
            }
        }
    }
}
