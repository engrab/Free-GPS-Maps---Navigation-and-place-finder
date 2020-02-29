package com.megafreeapps.free.gps.navigation.speedometer.compass.Fragments;


import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.text.DecimalFormat;

import com.megafreeapps.free.gps.navigation.speedometer.compass.R;
import com.megafreeapps.free.gps.navigation.speedometer.compass.Utilities.AppPurchasePref;
import com.megafreeapps.free.gps.navigation.speedometer.compass.Utilities.LocaleHelper;

import static android.content.Context.SENSOR_SERVICE;

public class Compass_Fragment extends Fragment implements SensorEventListener
{
    AdView mAdView;
    Activity context;
    // define the display assembly compass picture
    private ImageView image;
    private TextView tvDegree;
    DecimalFormat decimalFormat;
    

    // record the compass picture angle turned
    private float currentDegree = 0f;


    // device sensor manager
    private SensorManager mSensorManager;

    public Compass_Fragment()
    {
        // Required empty public constructor
    }

    private void Init(View view)
    {
        context = getActivity();

        AppPurchasePref appPurchasePref = new AppPurchasePref(context);
        if (appPurchasePref.getItemDetail().equals("") && appPurchasePref.getProductId().equals("")) {
            mAdView = view.findViewById(R.id.adView);
            mAdView.loadAd(new AdRequest.Builder().build());
            mAdView.setAdListener(new AdListener()
            {
                @Override
                public void onAdLoaded()
                {
                    super.onAdLoaded();
                    mAdView.setVisibility(View.VISIBLE);
                }
            });
        }

        decimalFormat = new DecimalFormat("###");
        image = view.findViewById(R.id.imageViewCompass);
        tvDegree = view.findViewById(R.id.tvDegree);

        // initialize your android device sensor capabilities
        mSensorManager = (SensorManager) context.getSystemService(SENSOR_SERVICE);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_compass, container, false);
        Init(view);
        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser)
    {
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent)
    {
        // get the angle around the z-axis rotated
        float degree = Math.round(sensorEvent.values[0]);
        tvDegree.setText(String.valueOf(decimalFormat.format(degree) + (char) 0x00B0));

        // create a rotation animation (reverse turn degree degrees)
        RotateAnimation ra = new RotateAnimation(
                currentDegree,
                -degree,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f);

        // how long the animation will take speedometer
        ra.setDuration(210);

        // set the animation after the end of the reservation status
        ra.setFillAfter(true);

        // Start the animation
        image.startAnimation(ra);
        currentDegree = -degree;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i)
    {

    }

    @Override
    public void onResume()
    {

        super.onResume();

        if (mAdView != null){
            mAdView.resume();
        }
        if (mSensorManager != null)
        {
            mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                    SensorManager.SENSOR_DELAY_GAME);
        }
    }

    @Override
    public void onPause()
    {
        if (mAdView != null){
            mAdView.pause();
        }
        super.onPause();
        if (mSensorManager != null)
        {
            mSensorManager.unregisterListener(this);
        }
    }



    @Override
    public void onDestroy() {
        if (mAdView != null){
            mAdView.destroy();
        }

        super.onDestroy();
    }
}
