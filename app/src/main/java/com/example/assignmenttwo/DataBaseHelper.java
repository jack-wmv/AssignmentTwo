package com.example.assignmenttwo;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

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

    //COMMENTS HERE
    public ArrayList<Model> getAllData(String orderBy){

        ArrayList<Model> arrayList = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + Constants.LOCATIONS_TABLE_NAME + " ORDER BY "+ orderBy;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

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
        return arrayList;
    }
}
