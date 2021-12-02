package kth.jjve.memeolise;
/*
This activity allows the user to input
preferences which will be saved to a locally stored
preferences file
 */

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.widget.SwitchCompat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


public class PrefsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    /*-------------------------- VIEWS ----------------------*/
    private DrawerLayout drawerLayout2;
    private NavigationView navigationView2;
    private Toolbar toolbar2;
    private Button button1, button2, button3, buttonSave;
    private SwitchCompat switch1, switch2, switch3;
    private EditText et1, et2, et3;

    /*-------------------------- PREFS ----------------------*/
    private Preferences cPreferences;   //Object of the class Preferences
    private int cTheme;                 //Todo: add the theme here
    private int cVoice = 42;            //Integer that decides which voice is being used
    private boolean cAudio = true;      //Boolean that decides if audio is on or off
    private boolean cVisual = true;     //Boolean that decides if visuals are on or off
    private double cEventInterval = 50;    //Interval between events in game
    private int cNumberofEvents = 10;   //Number of events the user plays
    private int cValueofN = 1;          //User can decide n

    /*--------------------------- LOG -----------------------*/
    private static final String LOG_TAG = PrefsActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prefs);

        /*---------------------- Hooks ----------------------*/
        drawerLayout2 = findViewById(R.id.drawer_layout_prefs);
        navigationView2 = findViewById(R.id.nav_view_prefs);
        toolbar2 = findViewById(R.id.prefs_toolbar);
        button1 = findViewById(R.id.button_prefs_theme1);
        button2 = findViewById(R.id.button_prefs_theme2);
        button3 = findViewById(R.id.button_prefs_theme3);
        buttonSave = findViewById(R.id.button_prefs_Save);
        switch1 = findViewById(R.id.switchVoice);
        switch2 = findViewById(R.id.switchAudio);
        switch3 = findViewById(R.id.switchVisual);
        et1 = findViewById(R.id.editTextNr_prefs_EventInterval);
        et2 = findViewById(R.id.editTextNr_prefs_NumberofEvents);
        et3 = findViewById(R.id.editTextNr_prefs_ValueN);

        /*--------------------- Tool bar --------------------*/
        setSupportActionBar(toolbar2);

        /*---------------Navigation drawer menu -------------*/
        navigationView2.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout2,toolbar2,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout2.addDrawerListener(toggle);
        toggle.syncState();
        navigationView2.setNavigationItemSelectedListener(this);
        navigationView2.setCheckedItem(R.id.nav_preferences);

        /*-------------------- Get preferences ------------------*/
        getPreferences();

        /*-------------------- onClickListeners ------------------*/
        button1.setOnClickListener(v -> cTheme = 1);

        button2.setOnClickListener(v -> cTheme = 2);

        button3.setOnClickListener(v -> cTheme = 3);

        buttonSave.setOnClickListener(v -> {
            setPreferences();
            Toast toast = Toast.makeText(getApplicationContext(), "Preferences saved",
                    Toast.LENGTH_SHORT);
            toast.show();
        });

        switch1.setOnCheckedChangeListener((buttonView, isChecked) -> {
            //TODO: take Elisa's political crap away before handing in :D
            if (isChecked){
                cVoice = 42;
                Toast toast = Toast.makeText(getApplicationContext(), "Gender is a social construct", Toast.LENGTH_SHORT);
                toast.show();
            }else{
                cVoice = 32; //Todo: find how to change the voice
                Toast toast = Toast.makeText(getApplicationContext(), "Gender is a social construct", Toast.LENGTH_SHORT);
                toast.show();
            }
        });

        switch2.setOnCheckedChangeListener((buttonView, isChecked) -> cAudio = isChecked);

        switch3.setOnCheckedChangeListener((buttonView, isChecked) -> cVisual = isChecked);
    }

    @Override
    protected void onResume(){
        super.onResume();
        navigationView2.setCheckedItem(R.id.nav_preferences);
    }

    @Override
    protected void onPause() {
        // NB! Cancel the current and queued utterances, then shut down the service to
        // de-allocate resources
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        Log.i(LOG_TAG, "onBackPressed happens");
        if(drawerLayout2.isDrawerOpen(GravityCompat.START)){
            drawerLayout2.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        if (id == R.id.nav_home){
            Intent intent = new Intent(PrefsActivity.this, MainActivity.class);
            startActivity(intent);
        }else if (id == R.id.nav_results){
            Intent intent = new Intent(PrefsActivity.this, ResultsActivity.class);
            startActivity(intent);
        }
        drawerLayout2.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setPreferences(){
        // Method to set the preferences and serialise them to the
        // locally stored preferences file
        cEventInterval = Double.parseDouble(et1.getText().toString());
        cNumberofEvents = Integer.parseInt(et2.getText().toString());
        cValueofN = Integer.parseInt(et3.getText().toString());

        serialisePreferences(cTheme, cVoice, cAudio, cVisual,
                             cEventInterval, cNumberofEvents, cValueofN);
    }

    private void getPreferences() {
        // Method that gets the current preferences
        // and updates the ui accordingly
        deserialisePreferences();

        if (cPreferences!=null){
            cTheme = cPreferences.getTheme();
            cVoice = cPreferences.getVoice();
            cAudio = cPreferences.getAudio();
            cVisual = cPreferences.getVisual();
            cEventInterval = (double) cPreferences.getEventInterval() / 1000;
            cNumberofEvents = cPreferences.getNumberofEvents();
            cValueofN = cPreferences.getValueofN();

            if (cVoice == 42) {
                switch1.setChecked(true);
            }else{
                switch2.setChecked(false);
            }

            switch2.setChecked(cAudio);
            switch3.setChecked(cVisual);

            et1.setText(String.valueOf(cEventInterval));
            et2.setText(String.valueOf(cNumberofEvents));
            et3.setText(String.valueOf(cValueofN));
        }
    }

    private void serialisePreferences(int theme, int voice, boolean audio, boolean visual,
                                      double eventInterval, int numberofEvents, int valueofN){
        // Method that serialises the preferences
        Preferences prefs = new Preferences(theme, voice, audio, visual, eventInterval, numberofEvents, valueofN);
        try{
            FileOutputStream fos = openFileOutput("preferences.ser", Context.MODE_PRIVATE);

            // Wrapping our file stream
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            // Writing the serializable object to the file
            oos.writeObject(prefs);

            // Closing our object stream which also closes the wrapped stream.
            oos.close();
        } catch (Exception e) {
            Log.i(LOG_TAG, "Exception is " + e);
            e.printStackTrace();
        }
    }

    private void deserialisePreferences(){
        // Method to deserialise preferences
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
    }

}