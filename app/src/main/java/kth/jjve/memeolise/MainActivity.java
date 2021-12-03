package kth.jjve.memeolise;
/*
This activity is the home screen of the app
The homescreen contains a start button, a help button for game explanation
It also contains a visual to show if audio/visual is on/off and a navigation bar

Names: Jitse van Esch & Elisa Perini
Date: 02.12.21
 */

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.io.FileInputStream;
import java.io.ObjectInputStream;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    /*--------------------------- VIEW ----------------------*/
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ImageView visualOn, visualOff, audioOn, audioOff;
    private TextView eventNo, nValue;

    /*--------------------------- LOG -----------------------*/
    private static final String LOG_TAG = PrefsActivity.class.getSimpleName();

    /*------------------------- PREFS ---------------------*/
    private Preferences cPreferences;
    private boolean cAudioOnOff;
    private boolean cVisualOnOff;
    private int cEventNr;
    private int cNvalue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*---------------------- Hooks ----------------------*/
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        Toolbar toolbar = findViewById(R.id.main_toolbar);
        Button buttonStart = findViewById(R.id.buttonStart);
        ImageView buttonHelp = findViewById(R.id.buttonHelp);
        visualOn = findViewById(R.id.IV_home_visualOn);
        visualOff = findViewById(R.id.IV_home_visualOff);
        audioOn = findViewById(R.id.IV_home_audioOn);
        audioOff = findViewById(R.id.IV_home_audioOff);
        eventNo = findViewById(R.id.tv_home_eventnr);
        nValue = findViewById(R.id.tv_home_nback);

        /*---------------------- Prefs ----------------------*/
        getPreferences();               // Get the preferences from the local storage
        if(cPreferences != null) {
            setViews();                 // set the views accordingly
        }

        /*--------------------- Tool bar --------------------*/
        setSupportActionBar(toolbar);

        /*---------------Navigation drawer menu -------------*/
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);

        /*-------------- On Click Listener ------------------*/
        buttonStart.setOnClickListener(v -> {
            // This button starts the game in the gameactivity
            if(cPreferences == null){
                Toast.makeText(getApplicationContext(), "There are no preferences yet", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, PrefsActivity.class);
                startActivity(intent);
            } else if (!cAudioOnOff && !cVisualOnOff){
                Toast.makeText(getApplicationContext(), "No method selected", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(MainActivity.this, GameActivity.class);
                startActivity(intent);
            }
        });

        buttonHelp.setOnClickListener(v -> {
            // This button shows the game explanation in a dialog
            openDialog();
        });
    }

    @Override
    protected void onResume(){
        super.onResume();
        navigationView.setCheckedItem(R.id.nav_home);
        Log.i(LOG_TAG, "onResume happens");
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        if (id == R.id.nav_preferences){
            Intent intent = new Intent(MainActivity.this, PrefsActivity.class);
            startActivity(intent);
        }else if (id == R.id.nav_results){
            Intent intent = new Intent(MainActivity.this, ResultsActivity.class);
            startActivity(intent);
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void getPreferences(){
        // Function to deserialise the preferences file
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
            cAudioOnOff = cPreferences.getAudio();
            cVisualOnOff = cPreferences.getVisual();
            cEventNr = cPreferences.getNumberofEvents();
            cNvalue = cPreferences.getValueofN();
        }
    }

    @SuppressLint("SetTextI18n")
    private void setViews(){
        // Method to set the views on the homescreen
        // based on the preferences
        if (cAudioOnOff){
            audioOn.setVisibility(View.VISIBLE);
            audioOff.setVisibility(View.INVISIBLE);
        }else{
            audioOff.setVisibility(View.VISIBLE);
            audioOn.setVisibility(View.INVISIBLE);
        }       // Get the audio preference
        if (cVisualOnOff){
            visualOn.setVisibility(View.VISIBLE);
            visualOff.setVisibility(View.INVISIBLE);
        }else{
            visualOff.setVisibility(View.VISIBLE);
            visualOn.setVisibility(View.INVISIBLE);
        }

        nValue.setText(Integer.toString(cNvalue));
        eventNo.setText(Integer.toString(cEventNr));

    }

    private void openDialog(){
        // Function that opens a dialog
        HelpDialog helpDialog = new HelpDialog();
        helpDialog.show(getSupportFragmentManager(), "help dialog");
    }
}