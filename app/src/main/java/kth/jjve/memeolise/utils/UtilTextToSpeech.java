package kth.jjve.memeolise.utils;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import java.util.Locale;

import kth.jjve.memeolise.GameActivity;

public class UtilTextToSpeech {

    private static TextToSpeech textToSpeech;
    private static final int utteranceId = 42;

    private static final String LOG_TAG = UtilTextToSpeech.class.getSimpleName();

    public static void initialize(Context appContext) {
        if (textToSpeech == null) {
            textToSpeech = new TextToSpeech(appContext,
                    status -> {
                        if (status == TextToSpeech.SUCCESS) {
                            textToSpeech.setLanguage(Locale.UK);
                        }
                    });
            Log.i(LOG_TAG, "tts initialized");
        }
    }

    public static void sayIt(String utterance) {
        if (textToSpeech != null) {
            textToSpeech.speak(utterance, TextToSpeech.QUEUE_FLUSH,
                    null, "" + utteranceId);
        }
    }

    public static void shutdown() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
            Log.i(LOG_TAG, "tts shut down");
        }
    }
}
