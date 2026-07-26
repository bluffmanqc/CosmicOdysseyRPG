package com.cosmicodyssey.rpg.utils;

import android.content.Context;
import android.content.SharedPreferences;
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

public class TTSManager {
    private static final String TAG = "TTSManager";
    private static final String PREFS_NAME = "TTSConfig";
    private static final String KEY_ELEVENLABS_API_KEY = "elevenlabs_api_key";
    private static final String KEY_ELEVENLABS_VOICE_ID = "elevenlabs_voice_id";
    private static final String DEFAULT_VOICE_ID = "21m00Tcm4TlvDq8ikWAM";
    
    private TextToSpeech tts;
    private OkHttpClient httpClient;
    private MediaPlayer mediaPlayer;
    private Context context;
    private boolean isReady = false;
    private boolean useElevenLabs = false;
    private String apiKey;
    private String voiceId;
    private float speechRate = 0.9f;
    private float pitch = 1.0f;

    public TTSManager(Context context) {
        this.context = context.getApplicationContext();
        loadElevenLabsConfig();
        tts = new TextToSpeech(context, status -> {
            if (status == TextToSpeech.SUCCESS) {
                tts.setLanguage(Locale.FRENCH);
                tts.setSpeechRate(speechRate);
                tts.setPitch(pitch);
                isReady = true;
            }
        });
    }

    public void speak(String text) {
        if (!isReady || text == null || text.isEmpty()) return;
        if (useElevenLabs && apiKey != null && !apiKey.isEmpty()) {
            speakWithElevenLabs(text);
        } else {
            speakWithNativeTTS(text);
        }
    }

    private void speakWithNativeTTS(String text) {
        HashMap<String, String> params = new HashMap<>();
        params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "cosmic");
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, params);
    }

    private void speakWithElevenLabs(String text) {
        if (httpClient == null) httpClient = new OkHttpClient();
        String url = "https://api.elevenlabs.io/v1/text-to-speech/" + voiceId;
        JSONObject json = new JSONObject();
        try {
            json.put("text", text);
            json.put("model_id", "eleven_multilingual_v2");
            json.put("voice_settings", new JSONObject().put("stability", 0.5).put("similarity_boost", 0.75));
        } catch (Exception e) {
            speakWithNativeTTS(text);
            return;
        }
        RequestBody body = RequestBody.create(json.toString(), MediaType.parse("application/json"));
        Request request = new Request.Builder()
                .url(url)
                .addHeader("xi-api-key", apiKey)
                .addHeader("Content-Type", "application/json")
                .post(body)
                .build();
        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "ElevenLabs failed", e);
                speakWithNativeTTS(text);
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful() || response.body() == null) {
                    speakWithNativeTTS(text);
                    return;
                }
                File tempFile = new File(context.getCacheDir(), "elevenlabs_temp.mp3");
                try (FileOutputStream fos = new FileOutputStream(tempFile)) {
                    fos.write(response.body().bytes());
                }
                playAudioFile(tempFile);
            }
        });
    }

    private void playAudioFile(File audioFile) {
        try {
            if (mediaPlayer != null) {
                mediaPlayer.release();
            }
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(audioFile.getAbsolutePath());
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (Exception e) {
            Log.e(TAG, "Play audio failed", e);
        }
    }

    public void stop() {
        if (tts != null) tts.stop();
        if (mediaPlayer != null) mediaPlayer.stop();
    }

    public void shutdown() {
        if (tts != null) tts.shutdown();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    public void setElevenLabsConfig(String apiKey, String voiceId) {
        this.apiKey = apiKey;
        this.voiceId = (voiceId != null && !voiceId.isEmpty()) ? voiceId : DEFAULT_VOICE_ID;
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit();
        if (apiKey != null && !apiKey.isEmpty()) {
            editor.putString(KEY_ELEVENLABS_API_KEY, apiKey);
            editor.putString(KEY_ELEVENLABS_VOICE_ID, this.voiceId);
            useElevenLabs = true;
            if (httpClient == null) httpClient = new OkHttpClient();
        } else {
            editor.remove(KEY_ELEVENLABS_API_KEY);
            editor.remove(KEY_ELEVENLABS_VOICE_ID);
            useElevenLabs = false;
        }
        editor.apply();
    }

    private void loadElevenLabsConfig() {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        apiKey = prefs.getString(KEY_ELEVENLABS_API_KEY, null);
        voiceId = prefs.getString(KEY_ELEVENLABS_VOICE_ID, DEFAULT_VOICE_ID);
        useElevenLabs = (apiKey != null && !apiKey.isEmpty());
        if (useElevenLabs && httpClient == null) httpClient = new OkHttpClient();
    }

    public boolean isElevenLabsEnabled() {
        return useElevenLabs;
    }
}
