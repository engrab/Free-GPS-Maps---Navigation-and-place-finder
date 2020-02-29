package com.megafreeapps.free.gps.navigation.speedometer.compass.Models;


public class NearByModel {
    public String text;
    public int id;
    public String text1;
    public int id1;
    public String text2;
    public int id2;

    public NearByModel(int id, String text, int id1, String text1, int id2, String text2)
    {
        this.text = text;
        this.id = id;
        this.text1 = text1;
        this.id1 = id1;
        this.text2 = text2;
        this.id2 = id2;
    }
}
