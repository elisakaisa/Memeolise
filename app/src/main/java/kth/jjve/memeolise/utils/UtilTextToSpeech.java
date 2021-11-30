package kth.jjve.memeolise.utils;

import android.content.Context;
import android.speech.tts.TextToSpeech;

import java.util.Locale;

public class UtilTextToSpeech {

    private static TextToSpeech textToSpeech;
    private static final int utteranceId = 42;

    public static void initialize(Context appContext) {
        if (textToSpeech == null) {
            textToSpeech = new TextToSpeech(appContext,
                    status -> {
                        if (status == TextToSpeech.SUCCESS) {
                            textToSpeech.setLanguage(Locale.UK);
                        }
                    });
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
        }
    }
}
