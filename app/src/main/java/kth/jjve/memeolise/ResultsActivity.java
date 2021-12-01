package kth.jjve.memeolise;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

import kth.jjve.memeolise.game.ResultStorage;
import kth.jjve.memeolise.game.Results;
import kth.jjve.memeolise.view.ResultItem;
import kth.jjve.memeolise.view.ResultItemAdapter;

public class ResultsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout3;
    private NavigationView navigationView3;
    private Toolbar toolbar3;

    /*--------------------------- LOG -----------------------*/
    private static final String LOG_TAG = PrefsActivity.class.getSimpleName();

    /*------------------------- CLASSES ---------------------*/
    private Preferences cPreferences;

    /*------------------------- RESULTS ---------------------*/
    private ResultStorage resultStorage;
    private List<Results> resultList;

    /*-------------------------VIEWING ----------------------*/
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        /*---------------------- Hooks ----------------------*/
        drawerLayout3 = findViewById(R.id.drawer_layout_results);
        navigationView3 = findViewById(R.id.nav_view_results);
        toolbar3 = findViewById(R.id.results_toolbar);
        recyclerView = findViewById(R.id.results_recyclerview);

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

        /*--------------- Results ---------------------------*/
        getResults();
        if (resultList != null) {
            fillRecyclerView(resultList);
        }
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


    private void getResults(){
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

    public void fillRecyclerView(List<Results> RL){
        ArrayList<ResultItem> itemList = new ArrayList<>();
        for (Results resultInstance : RL){
            String name =  resultInstance.getResultName();
            int score = resultInstance.getScore();
            int maxscore = resultInstance.getMaxscore();

            itemList.add(new ResultItem(name, score, maxscore));
        }
        RecyclerView.Adapter<ResultItemAdapter.ResultItemViewHolder> adapter =
                new ResultItemAdapter(itemList);
        RecyclerView.LayoutManager lm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(lm);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}