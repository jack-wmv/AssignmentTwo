/*
Programmer: Jackson Landry 100302201
Date: 17-11-2021
Purpose: This is the add location activity, in which the user can add locations by entering latitude and longitude. This will be processed and turned into an address using geocoder.
 */

package com.example.assignmenttwo.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.assignmenttwo.DataBaseHelper;
import com.example.assignmenttwo.R;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class AddLocation extends AppCompatActivity {

    private ImageView imageView;
    private EditText longitude, latitude;
    Button addLocation;
    ActionBar actionBar;

    private String longText, latText;
    private DataBaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_location);

        actionBar = getSupportActionBar();
        actionBar.setTitle("Add New Location");

        imageView = findViewById(R.id.location_image);
        longitude = findViewById(R.id.longitude);
        latitude = findViewById(R.id.latitude);

        addLocation = findViewById(R.id.addButton);

        dbHelper = new DataBaseHelper(this);
        addLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    getData();
                    startActivity(new Intent(AddLocation.this, MainActivity.class));
                    Toast.makeText(AddLocation.this, "Add Successful", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void getData() throws IOException {
        latText = ""+latitude.getText().toString().trim();
        longText = ""+longitude.getText().toString().trim();
        double lat = Double.parseDouble(latText);
        double lon = Double.parseDouble(longText);
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());

        addresses = geocoder.getFromLocation(lat, lon, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        String address = addresses.get(0).getAddressLine(0);
        dbHelper.insertLocation(address, latText, longText);
    }
}