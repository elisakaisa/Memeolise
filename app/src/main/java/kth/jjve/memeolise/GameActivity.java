package kth.jjve.memeolise;
/*
This activity is the activity in which the game is played
 */

import static kth.jjve.memeolise.game.GameView.SIZE;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.xml.transform.Result;

import kth.jjve.memeolise.game.GameLogic;
import kth.jjve.memeolise.game.ResultList;
import kth.jjve.memeolise.game.ResultStorage;
import kth.jjve.memeolise.game.Results;
import kth.jjve.memeolise.utils.UtilTextToSpeech;
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
    private int n;                  // chosen n of the user

    /*------------------------ TIMER ------------------------*/
    private Timer eventTimer = null;
    private Handler handler;
    private int eventNo;
    private int countdownNo;

    /*---------------------------- UI -----------------------*/
    private Button buttonVisual;
    private Button buttonAudio;

    private TextView eventNoView, scoreView;
    private ImageView[] imageViews;
    private ImageView countDown1, countDown2, countDown3;
    private ImageView red_dot, orange_dot, green_dot;

    /*------------------------- COUNTERS --------------------*/
    private boolean visualClick;
    private boolean audioClick;
    private int score;

    /*----------------------- GAME --------------------------*/
    private GameLogic gameLogic;
    int scoreChecker;

    /*---------------------- DRAWABLE -----------------------*/
    private Drawable squareDrawable;

    /*----------------- TEXT TO SPEECH ----------------------*/
    private UtilTextToSpeech utilTextToSpeech;

    /*------------------------ RESULTS ----------------------*/
    private Results results;
    private ResultStorage resultStorage;
    private List<Results> resultList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        /*---------------------- Hooks ----------------------*/
        buttonVisual = findViewById(R.id.buttonVisualMatch);
        buttonAudio = findViewById(R.id.buttonAudioMatch);
        imageViews = loadReferencesToImageViews();
        eventNoView = findViewById(R.id.textView_game_eventNo);
        scoreView = findViewById(R.id.textView_game_score);
        countDown1 = findViewById(R.id.IV_game_countdown1);
        countDown2 = findViewById(R.id.IV_game_countdown2);
        countDown3 = findViewById(R.id.IV_game_countdown3);
        red_dot = findViewById(R.id.reddot);
        orange_dot = findViewById(R.id.orangedot);
        green_dot = findViewById(R.id.greendot);
      
        /*----------------- Preferences/Results--------------*/
        getPreferences();

        /*------------------ Drawable -----------------------*/
        Resources resources = getResources();
        squareDrawable = ResourcesCompat.getDrawable(resources, R.drawable.square, null);
        initializeSquares();
        setInvisibleSquares();
        setInvisiblePointDots();

        /*----------------- TEXT TO SPEECH ----------------------*/
        utilTextToSpeech = new UtilTextToSpeech();

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
    // NB! Cancel the current and queued utterances, then shut down the service to
    // de-allocate resources
        UtilTextToSpeech.shutdown();
        super.onPause();
        cancelTimer();
    }

    @Override
    protected void onResume() {
        // re-initialise the text-to-speech service (was shutdown in onPause)
        super.onResume();
        UtilTextToSpeech.initialize(getApplicationContext());
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


    private void cancelTimer() {
        //Method to cancel the timer
        if (eventTimer != null) {
            eventTimer.cancel();
            eventTimer = null;
            Log.i("eventTask", "timer canceled");
            handler.post(() -> Toast.makeText(getApplicationContext(), "Timer stopped", Toast.LENGTH_SHORT).show());
        }
    }

      
    private ImageView[] loadReferencesToImageViews() {
        // sets images views in the grid
        ImageView[] imgViews = new ImageView[SIZE * SIZE];
        imgViews[0] = findViewById(R.id.imageView0);
        imgViews[1] = findViewById(R.id.imageView1);
        imgViews[2] = findViewById(R.id.imageView2);
        imgViews[3] = findViewById(R.id.imageView3);
        imgViews[4] = findViewById(R.id.imageView4);
        imgViews[5] = findViewById(R.id.imageView5);
        imgViews[6] = findViewById(R.id.imageView6);
        imgViews[7] = findViewById(R.id.imageView7);
        imgViews[8] = findViewById(R.id.imageView8);

        return imgViews;
    }
    

    public void initializeSquares(){
        for (int i = 0; i<9; i++) {
            imageViews[i].setImageDrawable(squareDrawable);
        }
    }

    public void setVisibleSquare(int index) {
        //method to make the red square visible
        imageViews[index].setVisibility(View.VISIBLE);
    }

    public void setInvisibleSquares() {
        // method to make the red square invisible
        for (int i = 0; i<9; i++) {
            imageViews[i].setVisibility(View.INVISIBLE);
        }
    }

    public void setInvisiblePointDots() {
        green_dot.setVisibility(View.INVISIBLE);
        orange_dot.setVisibility(View.INVISIBLE);
        red_dot.setVisibility(View.INVISIBLE);
    }

    public void setVisiblePoint(ImageView dot) {
        dot.setVisibility(View.VISIBLE);
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
                // clearing of previous red squares needs to be done on main thread
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setInvisibleSquares();
                        setInvisiblePointDots();
                    }
                });

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
        // Method that checks if the user has scored points
        // And outputs the score to the ui
        if (audioOn && !visualOn){
            scoreChecker = gameLogic.checkAudioScored(n, eventNo, audioClick);
        } else if (visualOn && !audioOn){
            scoreChecker = gameLogic.checkVisualScored(n, eventNo, visualClick);
        } else{
            scoreChecker = gameLogic.checkCombinedScored(n, eventNo, audioClick, visualClick);
        }

        if (scoreChecker == -1){
            // both are incorrect
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    setVisiblePoint(red_dot);
                }
            });

        } else if (scoreChecker == 0){
            // only one is incorrect
            score++;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    setVisiblePoint(orange_dot);
                }
            });

        } else {
            // both are correct
            score = score +2;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    setVisiblePoint(green_dot);
                }
            });
        }
        scoreView.setText(String.valueOf(score));
    }


    private void eventRunner(int eventNo){
        // Method that runs the events on the UI
        handler.post(() -> eventNoView.setText(String.valueOf(eventNo)));
        if (audioOn){
            String letter = gameLogic.returnRandomLetter();
            UtilTextToSpeech.sayIt(letter);
            Log.i("EventHappen", "Letter is " + letter);
        }
        if (visualOn){
            int index = gameLogic.returnRandomPosition();
            // changes in UI need to be on main thread
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    setVisibleSquare(index);
                }
            });

            Log.i("EventHappen", "Position is " + index);
        }
    }


    private void resetClicks(){
        // Method to reset button clicks
        visualClick = false;
        audioClick = false;
    }


    private void publishCountdown(int cd) {
        // Method that publishes the countdown before the game
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
        // Method that opens the dialog for name input
        ResultsDialog resultsDialog = new ResultsDialog();
        resultsDialog.show(getSupportFragmentManager(), "results dialog");
    }

    @Override
    public void applyName(String name) {
        // Method to get the name from the dialog into the activity
        // Save the results to the result class
        results = new Results();
        results.setResultName(name);
        results.setMaxscore(maxEventNo - n);
        results.setScore(score);

        saveResults(results);
        finish();
    }

    public void getResults(){
        try{
            FileInputStream fin = openFileInput("results.ser");

            // Wrapping our stream
            ObjectInputStream oin = new ObjectInputStream(fin);

            // Reading in our object
            resultStorage = (ResultStorage) oin.readObject();

            // Closing our object stream which also closes the wrapped stream
            oin.close();

        } catch (Exception e) {
            Log.i(LOG_TAG, "Error is " + e);
            e.printStackTrace();
        }

        if (resultStorage != null){
            resultList = resultStorage.getResultList();
        }
    }

    public void saveResults(Results results){
        getResults();

        if(resultList == null){
            resultList = new ArrayList<>();
            resultList.add(results);
        } else{
            resultList.add(results);
        }
        ResultStorage resultStorage = new ResultStorage(resultList);
        try{
            // Open a file stream
            FileOutputStream fos = openFileOutput("results.ser", Context.MODE_PRIVATE);

            // Wrapping our file stream
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            // Writing the serializable object to the file
            oos.writeObject(resultStorage);

            // Closing our object stream which also closes the wrapped stream.
            oos.close();

            Toast.makeText(getApplicationContext(), "Result saved", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.i(LOG_TAG, "Exception is " + e);
            e.printStackTrace();
        }

    }

    //Todo: add a safety for when both audio and visual have been turned off
}