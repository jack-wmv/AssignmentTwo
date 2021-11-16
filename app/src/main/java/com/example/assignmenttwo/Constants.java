/*
Programmer: Jackson Landry 100302201
Date: 16-11-2021
Purpose: This class sets the constant values for the database, ie. the columns, and contains the string to create the table which is done by the database helper.
 */

package com.example.assignmenttwo;

public class Constants {

    public static final String DATABASE_NAME = "database";
    public static final int DB_VERSION = 1;
    public static final String LOCATIONS_TABLE_NAME = "locations";
    public static final String LOCATIONS_COLUMN_ID = "ID";
    public static final String LOCATIONS_COLUMN_ADDRESS = "address";
    public static final String LOCATIONS_COLUMN_LATITUDE = "latitude";
    public static final String LOCATIONS_COLUMN_LONGITUDE = "longitude";

    public static final String CREATE_TABLE = "CREATE TABLE " + LOCATIONS_TABLE_NAME + " ("
            + LOCATIONS_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + LOCATIONS_COLUMN_ADDRESS + " TEXT,"
            + LOCATIONS_COLUMN_LATITUDE + " TEXT,"
            + LOCATIONS_COLUMN_LONGITUDE + " TEXT"
            + ");";
}
