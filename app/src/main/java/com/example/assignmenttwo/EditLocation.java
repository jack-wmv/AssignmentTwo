package com.example.assignmenttwo;

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

import com.example.assignmenttwo.activities.AddLocation;
import com.example.assignmenttwo.activities.MainActivity;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class EditLocation extends AppCompatActivity {

    private ImageView imageView;
    private EditText longitude, latitude;
    Button addLocation;
    ActionBar actionBar;

    private String id, longText, latText, address;
    private boolean editMode = false;
    private DataBaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_location);

        actionBar = getSupportActionBar();
        actionBar.setTitle("Add New Location");

        imageView = findViewById(R.id.location_image);
        longitude = findViewById(R.id.longitude);
        latitude = findViewById(R.id.latitude);

        addLocation = findViewById(R.id.addButton);

        Intent intent = getIntent();
        editMode = intent.getBooleanExtra("editMode", editMode);
        id = intent.getStringExtra("ID");
        latText = intent.getStringExtra("latitude");
        longText = intent.getStringExtra("longitude");
        address = intent.getStringExtra("address");

        if(editMode) {
            actionBar.setTitle("Update Information");

            editMode = intent.getBooleanExtra("editMode", editMode);
            id = intent.getStringExtra("ID");
            latText = intent.getStringExtra("latitude");
            longText = intent.getStringExtra("longitude");
            address = intent.getStringExtra("address");

            latitude.setText(latText);
            longitude.setText(longText);


        }
        else {
            actionBar.setTitle("Add Information");

        }

        dbHelper = new DataBaseHelper(this);
        addLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    getData();
                    startActivity(new Intent(EditLocation.this, MainActivity.class));
                    Toast.makeText(EditLocation.this, "Updated", Toast.LENGTH_SHORT).show();
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

        dbHelper.updateLocation(id, address, latText, longText);

    }
}
