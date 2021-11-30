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

import kth.jjve.memeolise.game.GameLogic;
import kth.jjve.memeolise.game.ResultsDialog;


public class GameActivity extends AppCompatActivity implements ResultsDialog.ResultsDialogListener {
    /*--------------------------- LOG -----------------------*/
    private static final String LOG_TAG = GameActivity.class.getSimpleName();

    /*------------------------- PREFS ---------------------*/
    private Preferences cPreferences;
    private int maxEventNo;
    private long eventInterval;
    private boolean audioOn;
    private boolean visualOn;
    private int n;

    /*------------------------ TIMER ------------------------*/
    private Timer eventTimer = null;
    private Handler handler;
    private int eventNo;
    private int countdownNo;

    /*---------------------------- UI -----------------------*/
    private Button buttonVisual;
    private Button buttonAudio;
    private TextView eventNoView, scoreView;
    private ImageView countDown1, countDown2, countDown3;

    /*------------------------- COUNTERS --------------------*/
    private boolean visualClick;
    private boolean audioClick;
    private int score;

    /*----------------------- GAME --------------------------*/
    private GameLogic gameLogic;
    int scoreChecker;

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
        eventNoView = findViewById(R.id.textView_game_eventNo);
        scoreView = findViewById(R.id.textView_game_score);
        countDown1 = findViewById(R.id.IV_game_countdown1);
        countDown2 = findViewById(R.id.IV_game_countdown2);
        countDown3 = findViewById(R.id.IV_game_countdown3);
      
        /*----------------- Preferences ---------------------*/
        getPreferences();

        /*-------------- On Click Listener ------------------*/
        buttonVisual.setOnClickListener(v -> {
            if (eventNo > n && eventNo <= maxEventNo){
                visualClick = true;
            } else {
                Toast.makeText(this, "Not clickable", Toast.LENGTH_SHORT).show();
            }
            Log.d(LOG_TAG, "visual clicked");
        });

        buttonAudio.setOnClickListener(v -> {
            if (eventNo > n && eventNo <= maxEventNo){
                audioClick = true;
            } else {
                Toast.makeText(this, "Not clickable", Toast.LENGTH_SHORT).show();
            }
            Log.d(LOG_TAG, "audio clicked");
        });

        /*---------------- START GAME ----------------------*/
        handler = new Handler();
        gameLogic = GameLogic.getInstance(); //singleton

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
        // re-initialise the text-to-speech service (was shutdown in onPause)
        super.onResume();
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
            audioOn = cPreferences.getAudio();
            visualOn = cPreferences.getVisual();
            n = cPreferences.getValueofN();
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
                eventNo++;  //increase the eventNo (starts at 1)
                Log.i("EventTask", "Event number " + eventNo);
                if (eventNo <= n+1){
                    eventRunner(eventNo);
                } else if (eventNo> n+1 && eventNo<=maxEventNo){
                    checkIfScored(n, eventNo-1);
                    resetClicks();
                    eventRunner(eventNo);
                } else {
                    checkIfScored(n, eventNo-1);
                    resetClicks();
                    cancelTimer();
                    handler.post(GameActivity.this::openResultsDialog);
                }
            }
        }
    }

    private void checkIfScored(int n, int eventNo) {
        if (audioOn && !visualOn){
            scoreChecker = gameLogic.checkAudioScored(n, eventNo, audioClick);
        } else if (visualOn && !audioOn){
            scoreChecker = gameLogic.checkVisualScored(n, eventNo, visualClick);
        } else{
            scoreChecker = gameLogic.checkCombinedScored(n, eventNo, audioClick, visualClick);
        }

        if (scoreChecker == -1){ //Todo: move to a method somewhere else
            // both are incorrect
            // Todo: display something to show that something has not been noticed

        } else if (scoreChecker == 0){
            // only one is incorrect
            score++;

        } else {
            // both are correct
            score = score +2;
        }
        scoreView.setText(String.valueOf(score));
    }

    private void eventRunner(int eventNo){
        handler.post(() -> eventNoView.setText(String.valueOf(eventNo)));
        if (audioOn){
            String letter = gameLogic.returnRandomLetter();
            sayIt(letter);
            Log.i("EventHappen", "Letter is " + letter);
        }
        if (visualOn){
            //Todo: run the visual part
        }
    }

    private void resetClicks(){
        visualClick = false;
        audioClick = false;
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
        }
    }

    //Todo: make a maximum score checker at the end of the game
    //Todo: make a serialiser that saves the result
    //Todo: go back to homescreen after saving the result
}