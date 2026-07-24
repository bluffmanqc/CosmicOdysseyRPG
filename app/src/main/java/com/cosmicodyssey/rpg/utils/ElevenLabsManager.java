package com.cosmicodyssey.rpg.utils;

import android.content.Context;
import android.media.MediaPlayer;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.json.JSONObject;

public class ElevenLabsManager {
    private static final String TAG = "ElevenLabsManager";
    private static final String ELEVENLABS_URL = "https://api.elevenlabs.io/v1/text-to-speech/";
    private static final String DEFAULT_VOICE_ID = "21m00Tcm4TlvDq8ikWAM";
    private final Context context;
    private final OkHttpClient client;
    private final TTSManager fallbackTTS;
    private String apiKey = "";
    private MediaPlayer mediaPlayer;
    private boolean useElevenLabs = true;

    public interface TTSCallback {
        void onStart();
        void onComplete();
        void onError(String error);
    }

    public ElevenLabsManager(Context context) {
        this.context = context.getApplicationContext();
        this.client = new OkHttpClient.Builder().build();
        this.fallbackTTS = new TTSManager(context);
        loadApiKey();
    }

    private void loadApiKey() {
        android.content.SharedPreferences prefs = context.getSharedPreferences("CosmicOdysseyPrefs", Context.MODE_PRIVATE);
        apiKey = prefs.getString("elevenlabs_api_key", "");
        useElevenLabs = !apiKey.isEmpty();
    }

    public void setApiKey(String key) {
        this.apiKey = key;
        this.useElevenLabs = !key.isEmpty();
        android.content.SharedPreferences prefs = context.getSharedPreferences("CosmicOdysseyPrefs", Context.MODE_PRIVATE);
        prefs.edit().putString("elevenlabs_api_key", key).apply();
    }

    public boolean hasApiKey() {
        return useElevenLabs && !apiKey.isEmpty();
    }

    public void speak(String text, TTSCallback callback) {
        if (text == null || text.isEmpty()) {
            if (callback != null) callback.onError("Texte vide");
            return;
        }
        if (useElevenLabs && hasApiKey()) {
            speakElevenLabs(text, callback);
        } else {
            speakFallback(text, callback);
        }
    }

    private void speakElevenLabs(String text, TTSCallback callback) {
        if (callback != null) callback.onStart();
        try {
            JSONObject body = new JSONObject();
            body.put("text", text);
            body.put("model_id", "eleven_multilingual_v2");
            JSONObject voiceSettings = new JSONObject();
            voiceSettings.put("stability", 0.5);
            voiceSettings.put("similarity_boost", 0.75);
            body.put("voice_settings", voiceSettings);

            Request request = new Request.Builder()
                .url(ELEVENLABS_URL + DEFAULT_VOICE_ID)
                .addHeader("xi-api-key", apiKey)
                .addHeader("Content-Type", "application/json")
                .post(RequestBody.create(body.toString(), MediaType.parse("application/json")))
                .build();

            client.newCall(request).enqueue(new Callback() {
                @Override public void onFailure(Call call, IOException e) {
                    Log.e(TAG, "ElevenLabs fail: " + e.getMessage());
                    fallbackTTS.speak(text);
                    if (callback != null) callback.onComplete();
                }
                @Override public void onResponse(Call call, Response response) throws IOException {
                    if (!response.isSuccessful() || response.body() == null) {
                        fallbackTTS.speak(text);
                        if (callback != null) callback.onComplete();
                        return;
                    }
                    byte[] audioData = response.body().bytes();
                    playAudio(audioData, callback);
                }
            });
        } catch (Exception e) {
            fallbackTTS.speak(text);
            if (callback != null) callback.onComplete();
        }
    }

    private void playAudio(byte[] audioData, TTSCallback callback) {
        try {
            File tempFile = new File(context.getCacheDir(), "elevenlabs_temp.mp3");
            FileOutputStream fos = new FileOutputStream(tempFile);
            fos.write(audioData);
            fos.close();

            if (mediaPlayer != null) {
                mediaPlayer.release();
            }
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(tempFile.getAbsolutePath());
            mediaPlayer.prepare();
            mediaPlayer.setOnCompletionListener(mp -> {
                mp.release();
                mediaPlayer = null;
                if (callback != null) callback.onComplete();
            });
            mediaPlayer.start();
        } catch (Exception e) {
            Log.e(TAG, "Play audio error: " + e.getMessage());
            if (callback != null) callback.onComplete();
        }
    }

    private void speakFallback(String text, TTSCallback callback) {
        if (callback != null) callback.onStart();
        fallbackTTS.speak(text);
        if (callback != null) callback.onComplete();
    }

    public void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        fallbackTTS.stop();
    }

    public void shutdown() {
        stop();
        fallbackTTS.shutdown();
    }
}