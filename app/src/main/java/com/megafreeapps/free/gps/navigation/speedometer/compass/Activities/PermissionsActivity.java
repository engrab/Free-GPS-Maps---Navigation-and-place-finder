package com.megafreeapps.free.gps.navigation.speedometer.compass.Activities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;

import com.megafreeapps.free.gps.navigation.speedometer.compass.Utilities.LocaleHelper;

/**
 * Created by Hafiz Muhammad Imran on 8/2/2016.
 */
public class PermissionsActivity extends AppCompatActivity
{
    Activity context;
    protected void attachBaseContext(Context base)
    {
        super.attachBaseContext(LocaleHelper.onAttach(base));
    }

    String[] Permissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.ACCESS_WIFI_STATE, Manifest.permission.INTERNET, Manifest.permission.READ_PHONE_STATE};

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        context = PermissionsActivity.this;

        ActivityCompat.requestPermissions(context, Permissions, 1122);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        finish();
    }
}