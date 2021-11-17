/*
Programmer: Jackson Landry 100302201
Date: 17-11-2021
Purpose: This is the database helper, this is used when a change needs to be made to the database, including creating the table, and inserting, updating, deleting, and getting data.
 */

package com.example.assignmenttwo;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Address;
import android.location.Geocoder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.assignmenttwo.activities.MainActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class DataBaseHelper extends SQLiteOpenHelper{
    public DataBaseHelper(@Nullable Context context) {
        super(context, Constants.DATABASE_NAME, null, Constants.DB_VERSION);
    }
    //Creates table
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Constants.CREATE_TABLE);
    }

    //onUpgrade method
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS "+Constants.LOCATIONS_TABLE_NAME);
        onCreate(db);
    }

    //method to insert a location, takes the address, latitude and longitude and inserts it into the database
    public boolean insertLocation(String address, String latitude, String longitude){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("address", address);
        contentValues.put("latitude", latitude);
        contentValues.put("longitude", longitude);
        db.insert("locations", null, contentValues);
        return true;
    }

    //update location, runs when user chooses to change the location of an already existing location in the database
    public void updateLocation(String id, String address, String latitude, String longitude){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("address", address);
        contentValues.put("latitude", latitude);
        contentValues.put("longitude", longitude);

        db.update(Constants.LOCATIONS_TABLE_NAME, contentValues,   Constants.LOCATIONS_COLUMN_ID + " = ?", new String[]{id});
        db.close();

    }

    //deletes a location from the database
    public void deleteLocation(String id){

        SQLiteDatabase db = getWritableDatabase();
        db.delete(Constants.LOCATIONS_TABLE_NAME, Constants.LOCATIONS_COLUMN_ID + " = ? ", new String[]{id});
        db.close();
    }

    //method to get all the data from the database, gets passed how to order
    public ArrayList<Model> getAllData(String orderBy){

        ArrayList<Model> arrayList = new ArrayList<>();

        //query to select all items from the table and order by whatever was specified
        String selectQuery = "SELECT * FROM " + Constants.LOCATIONS_TABLE_NAME + " ORDER BY "+ orderBy;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        //if there is another entry in the database, add to the arraylist
        if(cursor.moveToNext()) {
            do {
                @SuppressLint("Range") Model model = new Model(
                        ""+cursor.getString(cursor.getColumnIndex(Constants.LOCATIONS_COLUMN_ID)),
                        ""+cursor.getString(cursor.getColumnIndex(Constants.LOCATIONS_COLUMN_ADDRESS)),
                        ""+cursor.getDouble(cursor.getColumnIndex(Constants.LOCATIONS_COLUMN_LATITUDE)),
                        ""+cursor.getDouble(cursor.getColumnIndex(Constants.LOCATIONS_COLUMN_LONGITUDE))
                );
                arrayList.add(model);
            } while(cursor.moveToNext());
        }
        db.close();
        //return a completed arraylist of all entries
        return arrayList;
    }
}
