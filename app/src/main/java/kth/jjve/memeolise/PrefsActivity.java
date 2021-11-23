package kth.jjve.memeolise;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

public class PrefsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout2;
    private NavigationView navigationView2;
    private Toolbar toolbar2;

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
    }

    @Override
    protected void onResume(){
        super.onResume();
        navigationView2.setCheckedItem(R.id.nav_preferences);
        Log.i(LOG_TAG, "onResume happens");
    }

    @Override
    public void onBackPressed() {

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
    //Todo: Add onClickListeners to the onCreate --> see FancyWeatherApp for example
    //Todo: Add onSwitchListener to the onCreate --> find online if/how that works
    //Todo: Add serialiser and deserialiser for preference data
}