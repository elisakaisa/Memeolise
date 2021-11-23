package kth.jjve.memeolise;

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

public class GameActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    //UI
    private Button buttonPosition;
    private Button buttonAudio;

    // variables for counting how many times button is pressed
    public int positionMatchCounter = 0;
    public int audioMatchCounter = 0;

    // tts variables
    private TextToSpeech textToSpeach;
    private static final int utteranceId = 42; //might be where we set the voice? to be investigated


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        /*---------------------- Hooks ----------------------*/
        buttonPosition = findViewById(R.id.buttonPositionMatch);
        buttonAudio = findViewById(R.id.buttonAudioMatch);

        /*-------------- On Click Listener ------------------*/
        buttonPosition.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                positionMatchCounter++;
                sayIt("position"); //get "speak failed: not bound to TTS engine", check if works with phone
                Log.d(LOG_TAG, "position clicked");
            }
        });

        buttonAudio.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                audioMatchCounter++;
                sayIt("audio");
                Log.d(LOG_TAG, "audio clicked");
            }
        });

    }

    // for text to speech
    private void sayIt(String utterance) {
        textToSpeach.speak(utterance, TextToSpeech.QUEUE_FLUSH,
                null, new String("" + utteranceId));
    }

    // NB! Cancel the current and queued utterances, then shut down the service to
    // de-allocate resources
    @Override
    protected void onPause() {
        if (textToSpeach != null) {
            textToSpeach.stop();
            textToSpeach.shutdown();
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Initialize the text-to-speech service - we do this initialization
        // in onResume because we shutdown the service in onPause
        Log.d(LOG_TAG, "onResume called");
        textToSpeach = new TextToSpeech(getApplicationContext(),
                new TextToSpeech.OnInitListener() {
                    @Override
                    public void onInit(int status) {
                        if (status == TextToSpeech.SUCCESS) {
                            textToSpeach.setLanguage(Locale.UK);
                        }
                    }
                });
    }



    /*--------Alert dialogs (ie game finished)-----*/
    // TODO check if we can input sth in alert dialog (popup for players name)
    private AlertDialog createMsgDialog(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("Ok", new AlertDialog.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        return builder.create();
    }

}