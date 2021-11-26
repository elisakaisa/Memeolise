package kth.jjve.memeolise;
/*
This activity is the activity in which the game is played
 */

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.speech.tts.TextToSpeech;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.Timer;
import java.util.TimerTask;

import kth.jjve.memeolise.game.ResultsDialog;


public class GameActivity extends AppCompatActivity implements ResultsDialog.ResultsDialogListener {
    /*--------------------------- LOG -----------------------*/
    private static final String LOG_TAG = GameActivity.class.getSimpleName();

    /*------------------------- PREFS ---------------------*/
    private Preferences cPreferences;
    private int maxEventNo;
    private long eventInterval;

    /*------------------------ TIMER ------------------------*/
    private Timer eventTimer = null;
    private Handler handler;
    private int eventNo;
    private int countdownNo;

    /*---------------------------- UI -----------------------*/
    private Button buttonVisual;
    private Button buttonAudio;
    private TextView eventNoView;
    private ImageView countDown1, countDown2, countDown3;

    /*------------------------- COUNTERS --------------------*/
    public int audioMatchCounter = 0;
    public int visualMatchCounter = 0;

    /*--------------------- TEXT TO SPEECH ------------------*/
    private TextToSpeech textToSpeech;
    private static final int utteranceId = 42;
    //TODO might be where we set the voice? to be investigated

    /*------------------------ RESULTS ----------------------*/
    private String resultName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        /*---------------------- Hooks ----------------------*/
        buttonVisual = findViewById(R.id.buttonVisualMatch);
        buttonAudio = findViewById(R.id.buttonAudioMatch);
        eventNoView = (TextView) findViewById(R.id.textView_game_eventNo);
        countDown1 = (ImageView) findViewById(R.id.IV_game_countdown1);
        countDown2 = (ImageView) findViewById(R.id.IV_game_countdown2);
        countDown3 = (ImageView) findViewById(R.id.IV_game_countdown3);
      
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

        /*---------------- START GAME ----------------------*/
        handler = new Handler();

        boolean started = startTimer();
        if (!started) {
            Toast.makeText(this, "Task already running", Toast.LENGTH_SHORT).show();
        }
    }
  
  
    @Override
    protected void onPause() {
    // cancels the text to speech and the timer to save resources
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onPause();
        cancelTimer();
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
        // Method to get the needed preferences
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

        if (cPreferences != null){
            maxEventNo = cPreferences.getNumberofEvents();
            eventInterval = (long) cPreferences.getEventInterval();
        }
    }

    @Override
    public void applyName(String name) {
        resultName = name;
    }

    private class EventTimerTask extends TimerTask{
        // Class to run the game, based on the timer
        @Override
        public void run() {
            if (countdownNo<=3) {
                Log.i("EventTask", "In countdown");
                publishCountdown(countdownNo);
                countdownNo++;
            } else {
                eventNo++;
                Log.i("EventTask", "Event number " + eventNo);
                handler.post(() -> eventNoView.setText(String.valueOf(eventNo)));
                if (eventNo >= maxEventNo) {
                    cancelTimer();
                    handler.post(GameActivity.this::openResultsDialog);
                }
            }
        }
    }

    private void publishCountdown(int cd) {
        switch(cd){
            case 0:
                handler.post(() -> {
                    countDown3.setVisibility(View.VISIBLE);
                    countDown2.setVisibility(View.INVISIBLE);
                    countDown1.setVisibility(View.INVISIBLE);
                });
                break;
            case 1:
                handler.post(() -> {
                    countDown3.setVisibility(View.INVISIBLE);
                    countDown2.setVisibility(View.VISIBLE);
                    countDown1.setVisibility(View.INVISIBLE);
                });
                break;
            case 2:
                handler.post(() -> {
                    countDown3.setVisibility(View.INVISIBLE);
                    countDown2.setVisibility(View.INVISIBLE);
                    countDown1.setVisibility(View.VISIBLE);
                });
                break;
            case 3:
                handler.post(() -> {
                    countDown3.setVisibility(View.INVISIBLE);
                    countDown2.setVisibility(View.INVISIBLE);
                    countDown1.setVisibility(View.INVISIBLE);
                });
                break;
        }
    }

    private void openResultsDialog() {
        ResultsDialog resultsDialog = new ResultsDialog();
        resultsDialog.show(getSupportFragmentManager(), "results dialog");
    }

    private boolean startTimer(){
        // Method to start the timer
        if (eventTimer == null){
            countdownNo = 0;
            eventNo = 0;
            eventTimer = new Timer();
            EventTimerTask eTT = new EventTimerTask();
            eventTimer.schedule(eTT, 0, eventInterval);
            return true;
        }
        return false;
    }

    private void cancelTimer(){
        //Method to cancel the timer
        if (eventTimer != null){
            eventTimer.cancel();
            eventTimer = null;
            Log.i("eventTask", "timer canceled");
            handler.post(() -> Toast.makeText(getApplicationContext(), "Timer stopped", Toast.LENGTH_SHORT).show());
            //Todo: replace toast above by a Dialog that allows the input of some text (Jitse can do that :) )
        }
    }

    //Todo: make a serialiser that saves the result
}