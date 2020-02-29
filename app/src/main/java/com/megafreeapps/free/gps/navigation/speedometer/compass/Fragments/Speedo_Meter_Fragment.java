package com.megafreeapps.free.gps.navigation.speedometer.compass.Fragments;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.github.anastr.speedviewlib.SpeedView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.megafreeapps.free.gps.navigation.speedometer.compass.Activities.PermissionsActivity;
import com.megafreeapps.free.gps.navigation.speedometer.compass.R;
import com.megafreeapps.free.gps.navigation.speedometer.compass.Utilities.AppPurchasePref;
import com.megafreeapps.free.gps.navigation.speedometer.compass.Utilities.LocaleHelper;

import java.text.DecimalFormat;

/**
 * A simple {@link Fragment} subclass.
 */
public class Speedo_Meter_Fragment extends Fragment implements LocationListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener
{
    protected GoogleApiClient mGoogleApiClient;
    protected LocationRequest mLocationRequest;
    protected Location startingLocation;
    DecimalFormat decimalFormat;
    private Activity context;
    private SpeedView speedView;
    private TextView tvDistance;
    AdView mAdView;
    private boolean isStart = false;
    private Button btnStart;
    BillingProcessor bp;

    public Speedo_Meter_Fragment()
    {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_speedo_meter, container, false);
        Init(view);
        return view;
    }

    private void Init(View view)
    {
        context = getActivity();
        AppPurchasePref appPurchasePref = null;
        if (context != null) {
            appPurchasePref = new AppPurchasePref(context);
        }
        if (appPurchasePref != null && appPurchasePref.getItemDetail().equals("") && appPurchasePref.getProductId().equals("")) {

            mAdView = view.findViewById(R.id.adView);
            mAdView.loadAd(new AdRequest.Builder().build());
            mAdView.setAdListener(new AdListener() {
                @Override
                public void onAdLoaded() {
                    super.onAdLoaded();
                    mAdView.setVisibility(View.VISIBLE);
                }
            });
        }
        decimalFormat = new DecimalFormat("###.#");
        speedView = view.findViewById(R.id.speedView);
        tvDistance = view.findViewById(R.id.tvDistance);
        btnStart = view.findViewById(R.id.btnStart);
        btnStart.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (isStart)
                {
                    isStart = false;
                    btnStart.setText("Start");
                    speedView.stop();
                }
                else
                {
                    tvDistance.setText("0.0KM");
                    isStart = true;
                    startingLocation = null;
                    btnStart.setText("Stop");
                }
            }
        });
        buildGoogleApiClient();
    }

    @Override
    public void onLocationChanged(Location location)
    {
        if (startingLocation != null && isStart)
        {
            float speed = 0;
            float distance = location.distanceTo(startingLocation) / 1000;
            tvDistance.setText(String.valueOf(decimalFormat.format(distance) + "KM"));
            // time taken (in seconds)
            float timeTaken = ((location.getTime() - startingLocation.getTime()) / 1000);
            // calculate speed
            if (timeTaken > 0)
            {
                speed = getAverageSpeed(distance, timeTaken);
            }

            if (speed >= 0)
            {
                speedView.speedTo(speed);
            }
        }
        else
        {
            speedView.speedTo(0);
            startingLocation = location;
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();
    }

    private float getAverageSpeed(float distance, float timeTaken)
    {
        float speed = 0;
        if (distance > 0)
        {
            float distancePerSecond = timeTaken > 0 ? distance / timeTaken : 0;
            float distancePerHour = distancePerSecond * 60 * 60;
            speed = distancePerHour > 0 ? (distancePerHour / 1000) : 0;
        }

        return speed;
    }

    protected synchronized void buildGoogleApiClient()
    {
        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        createLocationRequest();
    }

    protected void createLocationRequest()
    {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(500);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    public void onConnected(Bundle connectionHint)
    {
        if (startingLocation == null)
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            {
                if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        || ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                {
                    startActivity(new Intent(context, PermissionsActivity.class));
                }
                else
                {
                    startingLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                }
            }
            else
            {
                startingLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            }
        }
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int cause)
    {
        if (mGoogleApiClient != null)
        {
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult result)
    {
        if (result.hasResolution())
        {
            try
            {
                result.startResolutionForResult(context, 2);
            }
            catch (IntentSender.SendIntentException e)
            {
                e.printStackTrace();
                GoogleApiAvailability.getInstance().getErrorDialog(context, result.getErrorCode(), 0).show();
            }
        }
        else
        {
            Log.d("error", "cannot resolve connection issue");
        }
    }

    @Override
    public void onStart()
    {
        super.onStart();
        if (mGoogleApiClient != null && !mGoogleApiClient.isConnected())
        {
            mGoogleApiClient.connect();
        }
        else
        {
            startLocationUpdates();
        }
    }

    @Override
    public void onPause()
    {
        super.onPause();
    }

    @Override
    public void onStop()
    {
        if (!isStart && mGoogleApiClient.isConnected())
        {
            stopLocationUpdates();
        }
        super.onStop();
    }

    protected void startLocationUpdates()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(context,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            {
                startActivity(new Intent(context, PermissionsActivity.class));
            }
            else
            {
                if (mGoogleApiClient != null && mGoogleApiClient.isConnected())
                {
                    LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
                }
                else
                {
                    buildGoogleApiClient();
                }
            }
        }
        else
        {
            if (mGoogleApiClient != null && mGoogleApiClient.isConnected())
            {
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
            }
            else
            {
                buildGoogleApiClient();
            }
        }
    }

    protected void stopLocationUpdates()
    {
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected())
        {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2)
        {
            if (resultCode != Activity.RESULT_OK)
            {
                mGoogleApiClient.connect();
            }
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
