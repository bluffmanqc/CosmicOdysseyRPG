package com.cosmicodyssey.rpg.ai;

import android.content.Context;
import android.content.SharedPreferences;
import com.cosmicodyssey.rpg.models.Character;
import com.cosmicodyssey.rpg.models.Equipment;
import com.cosmicodyssey.rpg.models.Mount;
import com.cosmicodyssey.rpg.models.Party;
import com.cosmicodyssey.rpg.models.Planet;
import com.cosmicodyssey.rpg.models.Rarity;
import com.cosmicodyssey.rpg.models.Spaceship;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class GameMasterAI {
    private static final String PREFS_NAME = "CosmicOdysseyPrefs";
    private static final String KEY_OPENROUTER_API = "openrouter_api_key";
    private static final String KEY_FALLBACK_MODEL = "fallback_model";
    private static final String OPENROUTER_URL = "https://openrouter.ai/api/v1/chat/completions";
    private static final String OPENROUTER_MODELS_URL = "https://openrouter.ai/api/v1/models";
    private static final String POLLINATIONS_IMAGE_URL = "https://image.pollinations.ai/prompt/";
    private static final String DEFAULT_MODEL = "meta-llama/llama-3.1-8b-instruct:free";
    
    private final Context context;
    private final OkHttpClient client;
    private final Gson gson;
    private final SharedPreferences prefs;
    private String apiKey;
    private String currentModel;

    public GameMasterAI(Context context) {
        this.context = context;
        this.gson = new Gson();
        this.prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        this.apiKey = prefs.getString(KEY_OPENROUTER_API, "");
        this.currentModel = prefs.getString(KEY_FALLBACK_MODEL, DEFAULT_MODEL);
        this.client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .build();
    }

    public void setApiKey(String key) {
        this.apiKey = key;
        prefs.edit().putString(KEY_OPENROUTER_API, key).apply();
    }

    public boolean hasApiKey() {
        return apiKey != null && !apiKey.isEmpty();
    }

    public void fetchFreeModel(final ModelCallback callback) {
        if (!hasApiKey()) {
            callback.onModelSelected(DEFAULT_MODEL);
            return;
        }
        
        Request request = new Request.Builder()
                .url(OPENROUTER_MODELS_URL)
                .header("Authorization", "Bearer " + apiKey)
                .header("HTTP-Referer", "https://cosmicodyssey.app")
                .header("X-Title", "Cosmic Odyssey RPG")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onModelSelected(currentModel);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    callback.onModelSelected(currentModel);
                    return;
                }
                try {
                    String body = response.body().string();
                    JSONObject json = new JSONObject(body);
                    JSONArray models = json.getJSONArray("data");
                    
                    String selectedModel = currentModel;
                    for (int i = 0; i < models.length(); i++) {
                        JSONObject model = models.getJSONObject(i);
                        String id = model.getString("id");
                        if (id.contains(":free")) {
                            selectedModel = id;
                            break;
                        }
                    }
                    
                    currentModel = selectedModel;
                    prefs.edit().putString(KEY_FALLBACK_MODEL, selectedModel).apply();
                    callback.onModelSelected(selectedModel);
                    
                } catch (Exception e) {
                    callback.onModelSelected(currentModel);
                }
            }
        });
    }

    public void generateStoryResponse(Party party, Character character, String playerAction, 
                                       AIResponseCallback callback) {
        if (!hasApiKey()) {
            callback.onError("Clé API OpenRouter requise. Va dans Paramètres > API Keys.");
            return;
        }

        doGenerateStoryResponse(party, character, playerAction, currentModel, callback);
    }

    private void doGenerateStoryResponse(Party party, Character character, String playerAction,
                                          String model, AIResponseCallback callback) {
        String systemPrompt = buildSystemPrompt(party);
        String userPrompt = buildUserPrompt(character, playerAction, party);

        JsonObject message = new JsonObject();
        message.addProperty("role", "user");
        message.addProperty("content", userPrompt);

        JsonObject systemMessage = new JsonObject();
        systemMessage.addProperty("role", "system");
        systemMessage.addProperty("content", systemPrompt);

        JsonArray messages = new JsonArray();
        messages.add(systemMessage);
        
        for (String hist : party.getStoryHistory()) {
            JsonObject histMsg = new JsonObject();
            histMsg.addProperty("role", "assistant");
            histMsg.addProperty("content", hist);
            messages.add(histMsg);
        }
        messages.add(message);

        JsonObject body = new JsonObject();
        body.addProperty("model", model);
        body.add("messages", messages);
        body.addProperty("temperature", 0.85);
        body.addProperty("max_tokens", 1500);

        Request request = new Request.Builder()
                .url(OPENROUTER_URL)
                .header("Authorization", "Bearer " + apiKey)
                .header("HTTP-Referer", "https://cosmicodyssey.app")
                .header("X-Title", "Cosmic Odyssey RPG")
                .post(RequestBody.create(body.toString(), MediaType.parse("application/json")))
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onError("Erreur réseau: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.code() == 404) {
                    fetchFreeModel(new ModelCallback() {
                        @Override
                        public void onModelSelected(String newModel) {
                            if (!newModel.equals(model)) {
                                doGenerateStoryResponse(party, character, playerAction, newModel, callback);
                            } else {
                                callback.onError("Erreur 404: Modèle non trouvé. Aucun modèle gratuit disponible.");
                            }
                        }
                    });
                    return;
                }
                if (!response.isSuccessful()) {
                    callback.onError("Erreur API: " + response.code());
                    return;
                }
                try {
                    JSONObject json = new JSONObject(response.body().string());
                    String content = json.getJSONArray("choices")
                            .getJSONObject(0)
                            .getJSONObject("message")
                            .getString("content");
                    
                    StoryResponse storyResponse = parseStoryResponse(content);
                    callback.onSuccess(storyResponse);
                } catch (JSONException e) {
                    callback.onError("Erreur parsing: " + e.getMessage());
                }
            }
        });
    }

    private String buildSystemPrompt(Party party) {
        return "Tu es le Maître de Jeu d'un RPG spatial intergalactique appelé Cosmic Odyssey. " +
                "Le jeu se déroule à travers l'univers et d'autres dimensions. " +
                "Tu dois narrer de manière immersive, créer des descriptions visuelles riches, " +
                "gérer les combats au tour par tour, et proposer des choix au joueur. " +
                "Tu peux créer de nouveaux équipements, montures, vaisseaux, planètes et systèmes. " +
                "Utilise un ton épique et mystérieux. " +
                "Format de réponse JSON structuré avec: narration, choices (tableau), " +
                "newItems (tableau d'objets), newPlanets (tableau), diceChecks (tableau), " +
                "sceneImage (description pour image), voiceText (version courte pour TTS).";
    }

    private String buildUserPrompt(Character character, String action, Party party) {
        StringBuilder sb = new StringBuilder();
        sb.append(character.getCharacterPrompt()).append("\\n");
        sb.append("Action du joueur: ").append(action).append("\\n");
        sb.append("Contexte: Planète actuelle ").append(party.getCurrentPlanet())
          .append(", Système ").append(party.getCurrentSystem()).append("\\n");
        sb.append("Réponds en français avec le format JSON demandé.");
        return sb.toString();
    }

    private StoryResponse parseStoryResponse(String content) {
        StoryResponse response = new StoryResponse();
        try {
            int jsonStart = content.indexOf("{");
            int jsonEnd = content.lastIndexOf("}");
            if (jsonStart >= 0 && jsonEnd > jsonStart) {
                String jsonStr = content.substring(jsonStart, jsonEnd + 1);
                JSONObject json = new JSONObject(jsonStr);
                
                response.narration = json.optString("narration", content);
                response.voiceText = json.optString("voiceText", response.narration.substring(0, 
                        Math.min(300, response.narration.length())));
                response.sceneImage = json.optString("sceneImage", "");
                
                JSONArray choices = json.optJSONArray("choices");
                if (choices != null) {
                    for (int i = 0; i < choices.length(); i++) {
                        response.choices.add(choices.getString(i));
                    }
                }
                
                JSONArray newItems = json.optJSONArray("newItems");
                if (newItems != null) {
                    for (int i = 0; i < newItems.length(); i++) {
                        response.newItems.add(parseEquipmentFromJson(newItems.getJSONObject(i)));
                    }
                }
                
                JSONArray newPlanets = json.optJSONArray("newPlanets");
                if (newPlanets != null) {
                    for (int i = 0; i < newPlanets.length(); i++) {
                        response.newPlanets.add(parsePlanetFromJson(newPlanets.getJSONObject(i)));
                    }
                }
            } else {
                response.narration = content;
                response.voiceText = content.substring(0, Math.min(300, content.length()));
            }
        } catch (Exception e) {
            response.narration = content;
            response.voiceText = content.substring(0, Math.min(300, content.length()));
        }
        return response;
    }

    private Equipment parseEquipmentFromJson(JSONObject json) throws JSONException {
        Equipment eq = new Equipment();
        eq.setName(json.optString("name", "Objet inconnu"));
        eq.setDescription(json.optString("description", ""));
        eq.setType(json.optString("type", "Arme"));
        String rarityStr = json.optString("rarity", "COMMON");
        try {
            eq.setRarity(Rarity.valueOf(rarityStr.toUpperCase()));
        } catch (Exception e) {
            eq.setRarity(Rarity.COMMON);
        }
        eq.setDamage(json.optInt("damage", 0));
        eq.setDefense(json.optInt("defense", 0));
        eq.setLore(json.optString("lore", ""));
        eq.setImageUrl(generateItemImageUrl(eq.getName(), eq.getType(), eq.getRarity()));
        return eq;
    }

    private Planet parsePlanetFromJson(JSONObject json) throws JSONException {
        Planet planet = new Planet();
        planet.setName(json.optString("name", "Planète inconnue"));
        planet.setBiome(json.optString("biome", "Inconnu"));
        planet.setDescription(json.optString("description", ""));
        planet.setDangerLevel(json.optInt("dangerLevel", 1));
        planet.setResourceLevel(json.optInt("resourceLevel", 1));
        planet.setImageUrl(generatePlanetImageUrl(planet.getName(), planet.getBiome()));

        return planet;
    }

    public String generateImageUrl(String prompt) {
        String encodedPrompt = prompt.replace(" ", "%20").replace(",", "%2C");
        return POLLINATIONS_IMAGE_URL + encodedPrompt + "?width=512&height=512&nologo=true";
    }

    public String generateItemImageUrl(String name, String type, Rarity rarity) {
        String prompt = "Fantasy RPG item, " + type + " called " + name + ", " + rarity.toString().toLowerCase() + " rarity, detailed, dark background";
        return generateImageUrl(prompt);
    }

    public String generatePlanetImageUrl(String name, String biome) {
        String prompt = "Sci-fi alien planet, " + name + ", " + biome + " biome, space view, detailed, cinematic";
        return generateImageUrl(prompt);
    }

    public String generateMerchantImageUrl(String type, String merchantName) {
        String prompt = "Fantasy RPG merchant portrait, " + type + " called " + merchantName + ", detailed character art, dark background";
        return generateImageUrl(prompt);
    }

    public String generateSceneImageUrl(String description) {
        String prompt = "Fantasy RPG scene, " + description + ", cinematic, detailed, dark atmosphere";
        return generateImageUrl(prompt);
    }

    public String generateCharacterImageUrl(com.cosmicodyssey.rpg.models.Character character) {
        String prompt = "Fantasy RPG character portrait, " + character.getName() + ", " + character.getClassName() + ", " + character.getRace() + ", detailed character art";
        return generateImageUrl(prompt);
    }

    public interface AIResponseCallback {
        void onSuccess(StoryResponse response);
        void onError(String error);
    }

    public interface ModelCallback {
        void onModelSelected(String model);
    }

    public static class StoryResponse {
        public String narration = "";
        public String voiceText = "";
        public String sceneImage = "";
        public List<String> choices = new ArrayList<>();
        public List<Equipment> newItems = new ArrayList<>();
        public List<Planet> newPlanets = new ArrayList<>();
    }
}
