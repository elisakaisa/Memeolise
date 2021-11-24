package kth.jjve.memeolise;
/*
This activity is the activity in which the game is played
 */

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.speech.tts.TextToSpeech;
import android.widget.Button;

import java.util.Locale;
import java.io.FileInputStream;
import java.io.ObjectInputStream;


public class GameActivity extends AppCompatActivity {
    /*--------------------------- LOG -----------------------*/
    private static final String LOG_TAG = GameActivity.class.getSimpleName();

    /*------------------------- CLASSES ---------------------*/
    private Preferences cPreferences;

    /*---------------------------- UI -----------------------*/
    private Button buttonVisual;
    private Button buttonAudio;

    /*------------------------- COUNTERS --------------------*/
    public int audioMatchCounter = 0;
    public int visualMatchCounter = 0;

    /*--------------------- TEXT TO SPEECH ------------------*/
    private TextToSpeech textToSpeech;
    private static final int utteranceId = 42;
    //TODO might be where we set the voice? to be investigated


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        /*---------------------- Hooks ----------------------*/
        buttonVisual = findViewById(R.id.buttonVisualMatch);
        buttonAudio = findViewById(R.id.buttonAudioMatch);
      
        /*----------------- Preferences ---------------------*/
        getPreferences();

        /*-------------- On Click Listener ------------------*/
        buttonVisual.setOnClickListener(v -> {
            visualMatchCounter++;
            sayIt("position"); //get "speak failed: not bound to TTS engine", check if works with phone
            Log.d(LOG_TAG, "position clicked");
        });

        buttonAudio.setOnClickListener(v -> {
            audioMatchCounter++;
            sayIt("audio");
            Log.d(LOG_TAG, "audio clicked");
        });

    }
  
  
    @Override
    protected void onPause() {
    // NB! Cancel the current and queued utterances, then shut down the service to
    // de-allocate resources
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onPause();
    }

  
    @Override
    protected void onResume() {
        super.onResume();
        // Initialize the text-to-speech service - we do this initialization
        // in onResume because we shutdown the service in onPause
        textToSpeech = new TextToSpeech(getApplicationContext(),
                status -> {
                    if (status == TextToSpeech.SUCCESS) {
                        textToSpeech.setLanguage(Locale.UK);
                    }
                });
    }


    private void sayIt(String utterance) {
        // for text to speech
        textToSpeech.speak(utterance, TextToSpeech.QUEUE_FLUSH,
                null, "" + utteranceId);
    }

  
    private void getPreferences(){
        try{
            FileInputStream fin = openFileInput("preferences.ser");

            // Wrapping our stream
            ObjectInputStream oin = new ObjectInputStream(fin);

            // Reading in our object
            cPreferences = (Preferences) oin.readObject();

            // Closing our object stream which also closes the wrapped stream
            oin.close();

        } catch (Exception e) {
            Log.i(LOG_TAG, "Error is " + e);
            e.printStackTrace();
        }

        //todo: expand so that needed preferences are automatically taken if cPreferences is not null
    }
  

    /*--------Alert dialogs (ie game finished)-----*/
    // TODO check if we can input sth in alert dialog (popup for players name)
    private AlertDialog createMsgDialog(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("Ok", (dialog, id) -> {
        });
        return builder.create();
    }

}