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

public class ResultsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout3;
    private NavigationView navigationView3;
    private Toolbar toolbar3;

    /*--------------------------- LOG -----------------------*/
    private static final String LOG_TAG = PrefsActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        /*---------------------- Hooks ----------------------*/
        drawerLayout3 = findViewById(R.id.drawer_layout_results);
        navigationView3 = findViewById(R.id.nav_view_results);
        toolbar3 = findViewById(R.id.results_toolbar);

        /*--------------------- Tool bar --------------------*/
        setSupportActionBar(toolbar3);

        /*---------------Navigation drawer menu -------------*/
        navigationView3.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout3,toolbar3,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout3.addDrawerListener(toggle);
        toggle.syncState();
        navigationView3.setNavigationItemSelectedListener(this);
        navigationView3.setCheckedItem(R.id.nav_results);
    }

    @Override
    protected void onResume(){
        super.onResume();
        navigationView3.setCheckedItem(R.id.nav_results);
        Log.i(LOG_TAG, "onResume happens");
    }

    @Override
    public void onBackPressed() {

        if(drawerLayout3.isDrawerOpen(GravityCompat.START)){
            drawerLayout3.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        int id = menuItem.getItemId();
        if (id == R.id.nav_home){
            Intent intent = new Intent(ResultsActivity.this, MainActivity.class);
            startActivity(intent);
        }else if (id == R.id.nav_preferences){
            Intent intent = new Intent(ResultsActivity.this, PrefsActivity.class);
            startActivity(intent);
        }
        drawerLayout3.closeDrawer(GravityCompat.START);
        return true;
    }
}