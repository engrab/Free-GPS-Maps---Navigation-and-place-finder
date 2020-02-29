package com.megafreeapps.free.gps.navigation.speedometer.compass.Fragments;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.megafreeapps.free.gps.navigation.speedometer.compass.Activities.Favourite_Places_Activity;
import com.megafreeapps.free.gps.navigation.speedometer.compass.Activities.FindRouteActivity;
import com.megafreeapps.free.gps.navigation.speedometer.compass.Activities.PermissionsActivity;
import com.megafreeapps.free.gps.navigation.speedometer.compass.R;
import com.megafreeapps.free.gps.navigation.speedometer.compass.Utilities.AppPurchasePref;
import com.megafreeapps.free.gps.navigation.speedometer.compass.Utilities.LocaleHelper;
import com.megafreeapps.free.gps.navigation.speedometer.compass.Utilities.Utils;

import java.util.Arrays;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class Navigation_Fragment extends Fragment implements View.OnClickListener, LocationListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener
{
    protected GoogleApiClient mGoogleApiClient;
    protected LocationRequest mLocationRequest;
    protected Location mCurrentLocation;
    Activity context;
    int PLACE_PICKER_REQUEST = 11;
    int viewId = 0;
    AdView mAdView;
    double Latitude, Longitude;
    BillingProcessor bp;
    private InterstitialAd mInterstitialAd;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(LocaleHelper.onAttach(context));
    }


    public Navigation_Fragment()
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

            mInterstitialAd = new InterstitialAd(context);
            mInterstitialAd.setAdUnitId(getString(R.string.inter_ad_unit_id));
            mInterstitialAd.loadAd(new AdRequest.Builder().build());
            mInterstitialAd.setAdListener(new AdListener() {
                @Override
                public void onAdClosed() {
                    mInterstitialAd.loadAd(new AdRequest.Builder().build());
                    clickListener();
                }
            });
        }
        if (!Places.isInitialized()) {
            Places.initialize(context, getString(R.string.google_maps_key));
        }
        view.findViewById(R.id.ivDrivingRoute).setOnClickListener(this);
        view.findViewById(R.id.ivRouteFinder).setOnClickListener(this);
        view.findViewById(R.id.ivMyLocation).setOnClickListener(this);
        view.findViewById(R.id.ivFavouritePlace).setOnClickListener(this);
        view.findViewById(R.id.ivShareLocation).setOnClickListener(this);
        view.findViewById(R.id.ivShareApp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareAppIntent = new Intent(android.content.Intent.ACTION_SEND);
                shareAppIntent.setType("text/plain");
                shareAppIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getString(R.string.app_name));
                shareAppIntent.putExtra(android.content.Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=" +
                        context.getPackageName() + "&hl=en");
                if (shareAppIntent.resolveActivity(context.getPackageManager()) != null)
                {
                    startActivity(Intent.createChooser(shareAppIntent, "Share via"));
                }
            }
        });
        view.findViewById(R.id.ivRateUs).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try
                {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=" + context.getPackageName() + "&hl=en"));
                    if (intent.resolveActivity(context.getPackageManager()) != null)
                    {
                        startActivity(intent);
                    }
                }
                catch (android.content.ActivityNotFoundException anfe)
                {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("https://play.google.com/store/apps/developer?id=Mega+Free+Apps+Developers"));
                    if (intent.resolveActivity(context.getPackageManager()) != null)
                    {
                        startActivity(intent);
                    }
                }
            }
        });

        buildGoogleApiClient();
    }

    private void animation(View iv)
    {
        ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(iv, "scaleX", 0.5f);
        scaleXAnimator.setRepeatMode(ValueAnimator.REVERSE);
        scaleXAnimator.setRepeatCount(ValueAnimator.INFINITE);
        scaleXAnimator.setDuration(1000);

        ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(iv, "scaleY", 0.5f);
        scaleYAnimator.setRepeatMode(ValueAnimator.REVERSE);
        scaleYAnimator.setRepeatCount(ValueAnimator.INFINITE);
        scaleYAnimator.setDuration(1000);

        AnimatorSet set = new AnimatorSet();
        set.playTogether(scaleXAnimator, scaleYAnimator);
        set.start();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_navigation, container, false);
        Init(view);
        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser)
    {
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    public void onClick(View view)
    {
        viewId = view.getId();
        if (mInterstitialAd != null && mInterstitialAd.isLoaded())
        {
            mInterstitialAd.show();
        }
        else
        {
            clickListener();
        }
    }

    private void clickListener()
    {
        switch (viewId)
        {
            case R.id.ivDrivingRoute:
                try
                {
                    List<Place.Field> fields = Arrays.asList(com.google.android.libraries.places.api.model.Place.Field.ID, com.google.android.libraries.places.api.model.Place.Field.NAME, com.google.android.libraries.places.api.model.Place.Field.LAT_LNG);
                    Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields).build(context);
                    startActivityForResult(intent, PLACE_PICKER_REQUEST);

                }
                catch (Exception ignore){

                }
//                try {
//                    PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
//                    startActivityForResult(builder.build(context), PLACE_PICKER_REQUEST);
//                }catch (Exception ignore){
//                    // to do
//                }
                break;
            case R.id.ivRouteFinder:
                startActivity(new Intent(context, FindRouteActivity.class));
                break;
            case R.id.ivMyLocation:
                Uri gmmIntentUri = Uri.parse("geo:0,0?q=Current Location");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                if (mapIntent.resolveActivity(context.getPackageManager()) != null)
                {
                    startActivity(mapIntent);
                }
                break;
            case R.id.ivFavouritePlace:
                startActivity(new Intent(context, Favourite_Places_Activity.class));
                break;
//            case R.id.ivAd:
//                startActivity(new Intent(this, ChineseActivity.class));
//                break;
            case R.id.ivShareLocation:
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "https://gps.google.com/gps?q=loc:" + Latitude
                        + "," + Longitude);
                if (sharingIntent.resolveActivity(context.getPackageManager()) != null)
                {
                    startActivity(Intent.createChooser(sharingIntent, "Share Location via"));
                }
                break;
        }
    }

    @Override
    public void onDestroy()
    {
        if (mAdView != null)
        {
            mAdView.destroy();
        }

        super.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK && data != null) {
                try {
                    Place place = Autocomplete.getPlaceFromIntent(data);
                    Uri gmmIntentUri = Uri.parse("google.navigation:q=" + place.getName());
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    if (mapIntent.resolveActivity(context.getPackageManager()) != null) {
                        startActivity(mapIntent);
                    }
                } catch (Exception ignored) {
                }
            }
        }
        else if (requestCode == 1)
        {
            if (resultCode != Activity.RESULT_OK)
            {
                mGoogleApiClient.connect();
            }
        }
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
        mLocationRequest.setInterval(Utils.UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(Utils.FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    public void onConnected(Bundle connectionHint)
    {
        if (mCurrentLocation == null)
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
                    mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                    if (mCurrentLocation != null)
                    {
                        Latitude = mCurrentLocation.getLatitude();
                        Longitude = mCurrentLocation.getLongitude();
                    }
                    else
                    {
                        Latitude = 0;
                        Longitude = 0;
                    }
                }
            }
            else
            {
                mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                if (mCurrentLocation != null)
                {
                    Latitude = mCurrentLocation.getLatitude();
                    Longitude = mCurrentLocation.getLongitude();
                }
                else
                {
                    Latitude = 0;
                    Longitude = 0;
                }
            }
        }
        startLocationUpdates();
    }

    @Override
    public void onLocationChanged(Location location)
    {
        mCurrentLocation = location;
        if (mCurrentLocation != null)
        {
            Latitude = mCurrentLocation.getLatitude();
            Longitude = mCurrentLocation.getLongitude();
        }
        else
        {
            Latitude = 0;
            Longitude = 0;
        }
    }

    @Override
    public void onConnectionSuspended(int cause)
    {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult result)
    {
        if (result.hasResolution())
        {
            try
            {
                result.startResolutionForResult(context, 1);
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
        if (!mGoogleApiClient.isConnected())
        {
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();
        if (mAdView != null)
        {
            mAdView.resume();
        }
        if (mGoogleApiClient.isConnected())
        {
            startLocationUpdates();
        }
    }

    @Override
    public void onPause()
    {
        super.onPause();
        if (mAdView != null)
        {
            mAdView.pause();
        }
        if (mGoogleApiClient.isConnected())
        {
            stopLocationUpdates();
        }
    }

    @Override
    public void onStop()
    {
        if (mGoogleApiClient.isConnected())
        {
            mGoogleApiClient.disconnect();
        }

        super.onStop();
    }

    protected void startLocationUpdates()
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
                if (mGoogleApiClient.isConnected())
                {
                    LocationServices.FusedLocationApi.requestLocationUpdates(
                            mGoogleApiClient, mLocationRequest, this);
                }
                else
                {
                    buildGoogleApiClient();
                }
            }
        }
        else
        {
            if (mGoogleApiClient.isConnected())
            {
                LocationServices.FusedLocationApi.requestLocationUpdates(
                        mGoogleApiClient, mLocationRequest, this);
            }
            else
            {
                buildGoogleApiClient();
            }
        }
    }

    protected void stopLocationUpdates()
    {
        if (mGoogleApiClient.isConnected())
        {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }
}
