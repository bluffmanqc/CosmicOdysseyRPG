package com.cosmicodyssey.rpg.utils;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import java.util.HashMap;
import java.util.Locale;

public class TTSManager {
    private TextToSpeech tts;
    private boolean isReady = false;
    private float speechRate = 0.9f;
    private float pitch = 1.0f;

    public TTSManager(Context context) {
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
        HashMap<String, String> params = new HashMap<>();
        params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "cosmic");
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, params);
    }

    public void stop() {
        if (tts != null) tts.stop();
    }

    public void shutdown() {
        if (tts != null) tts.shutdown();
    }

    public boolean isReady() { return isReady; }
}
