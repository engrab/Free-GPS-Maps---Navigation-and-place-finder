package com.megafreeapps.free.gps.navigation.speedometer.compass.Models;

/**
 * Created by Khan on 12/30/2017.
 */

public class Street_View_Model
{
    public String Name;
    public double Latitude, Longitude;

    public Street_View_Model(String name, double latitude, double longitude)
    {
        Name = name;
        Latitude = latitude;
        Longitude = longitude;
    }
}
