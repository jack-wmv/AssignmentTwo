/*
Programmer: Jackson Landry 100302201
Date: 17-11-2021
Purpose: This is the Model, which constructs an object Model which is used to get and set values in entries in the database.
 */

package com.example.assignmenttwo;

public class Model {
    String id, address, latitude, longitude;

    //model constructor
    public Model(String id, String address, String latitude, String longitude) {
        this.id = id;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    //initiating getters and setters to get relevant information about database entries
    public String getId(){return id;}
    public void setId(String id){this.id=id;}
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
