package com.megafreeapps.free.gps.navigation.speedometer.compass.Models;


public class SavedPlacesModel {
    public double StartLat;
    public double StartLng;
    public double EndLat;
    public double EndLng;
    public String StartAddress;
    public String EndAddress;
    public int Id;


    public SavedPlacesModel(int id, double startLat, double startlan, double endLat, double endLan, String startAddress, String endAddress) {
        Id = id;
        StartLat = startLat;
        StartLng = startlan;
        EndLat = endLat;
        EndLng = endLan;
        StartAddress = startAddress;
        EndAddress = endAddress;
    }

}
