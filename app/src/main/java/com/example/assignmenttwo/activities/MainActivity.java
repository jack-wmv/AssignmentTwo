/*
Programmer: Jackson Landry 100302201
Date: 17-11-2021
Purpose: This is the main activity, which is the first screen the user sees and displays all the items contained in the database
 */

package com.example.assignmenttwo.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

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
        int size = db.getAllData(Constants.LOCATIONS_COLUMN_ID + " DESC").size();

        try {
            fillDb(size);
        } catch (IOException e) {
            e.printStackTrace();
        }

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

    //method to fill the database with random addresses which are dtermined by randomly generated longitudes and latitudes
    private void fillDb(int size) throws IOException {
        double latMin = -90.0;
        double latMax = 90.0;
        double lonMin = -180;
        double lonMax = 180;
        String longitude, latitude;
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
        Random r = new Random();

        for(int i = size; i < 50; i++){
            double latVal = latMin + (latMax - latMin) * r.nextDouble();
            double lonVal = lonMin + (lonMax - lonMin) * r.nextDouble();
            addresses = geocoder.getFromLocation(latVal, lonVal, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            if(addresses != null && addresses.size()>0) {
                String address = addresses.get(0).getAddressLine(0);
                latitude = latVal + "";
                longitude = lonVal + "";
                db.insertLocation(address, latitude, longitude);
            }

        }
    }
}