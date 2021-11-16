package com.example.assignmenttwo.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;

import com.example.assignmenttwo.Adapter;
import com.example.assignmenttwo.Constants;
import com.example.assignmenttwo.DataBaseHelper;
import com.example.assignmenttwo.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    FloatingActionButton fab;
    ActionBar actionBar;
    RecyclerView mRecyclerView;
    DataBaseHelper db;
    Adapter adapter;

    //on create method
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //setting top name to Locations
        actionBar = getSupportActionBar();
        actionBar.setTitle("Locations");

        mRecyclerView = findViewById(R.id.recyclerView);
        db = new DataBaseHelper(this);

        //showing all data
        showRecord();

        fab = findViewById(R.id.addFabButton);

        //onclick for floating action button to add new location
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddLocation.class);
                intent.putExtra("editMode", false);
                startActivity(intent);

            }
        });
    }

    //called to display all locations
    private void showRecord() {
        //gets all locations and orders them by column id
        adapter = new Adapter(MainActivity.this, db.getAllData(Constants.LOCATIONS_COLUMN_ID + " DESC"));
        mRecyclerView.setAdapter(adapter);

    }

    //when activity resumes, all of the database is shown
    @Override
    public void onResume(){
        super.onResume();
        showRecord();
    }

    //oncreate for options menu which allows to search the database
    public boolean onCreateOptionsMenu(Menu menu){

        getMenuInflater().inflate(R.menu.searchbar_menu,menu);

        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(this);
        return true;
    }


    //runs when the search is submitted and filters based on what the user has entered
    @Override
    public boolean onQueryTextSubmit(String query) {
        adapter.getFilter().filter(query);

        return false;
    }

    //dynamic processing of user input to show relevant database entries
    @Override
    public boolean onQueryTextChange(String newText) {
        adapter.getFilter().filter(newText);
        return false;
    }
}