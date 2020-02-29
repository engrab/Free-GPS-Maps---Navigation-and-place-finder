package com.megafreeapps.free.gps.navigation.speedometer.compass.Models;

/**
 * Created by Khan on 12/30/2017.
 */

public class Favourite_Places_Model
{
    public String Address, Name, Date, Time, Latitude, Longitude;

    public Favourite_Places_Model(String Address, String Name, String Date, String Time, String Latitude, String Longitude)
    {
        this.Address = Address;
        this.Name = Name;
        this.Date = Date;
        this.Time = Time;
        this.Latitude = Latitude;
        this.Longitude = Longitude;
    }
}
