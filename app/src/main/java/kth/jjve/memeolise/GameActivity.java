package kth.jjve.memeolise;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import java.io.FileInputStream;
import java.io.ObjectInputStream;

public class GameActivity extends AppCompatActivity {
    /*--------------------------- LOG -----------------------*/
    private static final String LOG_TAG = PrefsActivity.class.getSimpleName();

    /*------------------------- CLASSES ---------------------*/
    private Preferences cPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        getPreferences();
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
}