package kth.jjve.memeolise;
/*
This activity is the activity in which the game is played
 */

import static kth.jjve.memeolise.game.GameView.SIZE;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.speech.tts.TextToSpeech;
import android.widget.Button;
import android.widget.ImageView;

import java.util.Locale;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

import kth.jjve.memeolise.game.GameView;
import kth.jjve.memeolise.utils.UtilTextToSpeech;


public class GameActivity extends AppCompatActivity {
    /*--------------------------- LOG -----------------------*/
    private static final String LOG_TAG = GameActivity.class.getSimpleName();

    /*------------------------- CLASSES ---------------------*/
    private Preferences cPreferences;

    /*---------------------------- UI -----------------------*/
    private Button buttonVisual;
    private Button buttonAudio;
    private ImageView[] imageViews;

    /*------------------------- COUNTERS --------------------*/
    public int audioMatchCounter = 0;
    public int visualMatchCounter = 0;

    /*--------------------- TEXT TO SPEECH ------------------*/
    private TextToSpeech textToSpeech;
    private static final int utteranceId = 42;
    //TODO might be where we set the voice? to be investigated

    /*---------------------- DRAWABLE -----------------------*/
    private Drawable squareDrawable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        /*---------------------- Hooks ----------------------*/
        buttonVisual = findViewById(R.id.buttonVisualMatch);
        buttonAudio = findViewById(R.id.buttonAudioMatch);
        imageViews = loadReferencesToImageViews();
      
        /*----------------- Preferences ---------------------*/
        getPreferences();

        /*------------------ Drawable -----------------------*/
        Resources resources = getResources();
        squareDrawable = ResourcesCompat.getDrawable(resources, R.drawable.square, null);
        setVisibleSquare(2); //sets the visible square (index 0 to 8, from left to right, top to bottom, image tags called)

        /*-------------- On Click Listener ------------------*/
        buttonVisual.setOnClickListener(v -> {
            visualMatchCounter++;
            UtilTextToSpeech.sayIt("position");
            Log.d(LOG_TAG, "position clicked");
        });

        buttonAudio.setOnClickListener(v -> {
            audioMatchCounter++;
            UtilTextToSpeech.sayIt("audio");
            Log.d(LOG_TAG, "audio clicked");
        });

    }
  
  
    @Override
    protected void onPause() {
    // NB! Cancel the current and queued utterances, then shut down the service to
    // de-allocate resources
        UtilTextToSpeech.shutdown();
        super.onPause();
    }

  
    @Override
    protected void onResume() {
        super.onResume();
        // Initialize the text-to-speech service - we do this initialization
        // in onResume because we shutdown the service in onPause
        UtilTextToSpeech.initialize(getApplicationContext());

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

    public void setVisibleSquare(int index) {
        //method to make the red square visible
        imageViews[index].setImageDrawable(squareDrawable);
    }

}