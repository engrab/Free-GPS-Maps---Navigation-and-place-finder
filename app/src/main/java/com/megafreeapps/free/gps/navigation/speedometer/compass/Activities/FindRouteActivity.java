package com.megafreeapps.free.gps.navigation.speedometer.compass.Activities;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.megafreeapps.free.gps.navigation.speedometer.compass.Database.SavedRouteDatabase;
import com.megafreeapps.free.gps.navigation.speedometer.compass.R;
import com.megafreeapps.free.gps.navigation.speedometer.compass.Services.AddressService;
import com.megafreeapps.free.gps.navigation.speedometer.compass.Utilities.AppPurchasePref;
import com.megafreeapps.free.gps.navigation.speedometer.compass.Utilities.Constants;
import com.megafreeapps.free.gps.navigation.speedometer.compass.Utilities.LocaleHelper;

import java.util.Arrays;
import java.util.List;

public class FindRouteActivity extends AppCompatActivity implements View.OnClickListener {
    private final int START_PLACE_REQUEST = 1;
    private final int END_PLACE_REQUEST = 2;
    private boolean IsAddressRequested = false;
    private AddressResultReceiver ResultReceiver;
    private EndAddressResultReceiver endResultReceiver;
    private LatLng Start_Latlan;
    private LatLng End_Latlan;
    private ImageView set_my_location;
    private SavedRouteDatabase database;
    private FusedLocationProviderClient FusedLocation;

    private TextView start_latitude, start_longitude, end_latitude, end_longitude, start_address, end_address;
    private AdView route_adView;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_finder);

//        Toolbar toolbar = findViewById(R.id.find_route_toolbar);
//        toolbar.setTitle("Find Route");
//        toolbar.setNavigationIcon(R.drawable.bakery);
//        setSupportActionBar(toolbar);
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });

        database = new SavedRouteDatabase(this);
        database.open();

        FusedLocation = LocationServices.getFusedLocationProviderClient(this);
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), getString(R.string.google_maps_key));
        }

        ResultReceiver = new AddressResultReceiver(new Handler());
        endResultReceiver = new EndAddressResultReceiver(new Handler());


        start_latitude = findViewById(R.id.start_latitude);
        start_longitude = findViewById(R.id.start_longitude);
        end_latitude = findViewById(R.id.end_latitude);
        end_longitude = findViewById(R.id.end_longitude);
        start_address = findViewById(R.id.start_address);
        end_address = findViewById(R.id.end_address);
        set_my_location = findViewById(R.id.set_my_location);
        set_my_location.setOnClickListener(this);


        findViewById(R.id.lay_start_point).setOnClickListener(this);
        findViewById(R.id.lay_end_point).setOnClickListener(this);
        findViewById(R.id.btn_start_navigation).setOnClickListener(this);
        findViewById(R.id.btn_save_route).setOnClickListener(this);



        AppPurchasePref appPurchasePref = new AppPurchasePref(this);
        if (appPurchasePref.getItemDetail().equals("") && appPurchasePref.getProductId().equals("")) {

            route_adView = findViewById(R.id.route_adView);
            AdRequest adRequest = new AdRequest.Builder().build();
            route_adView.loadAd(adRequest);
            route_adView.setAdListener(new AdListener() {
                @Override
                public void onAdLoaded() {
                    super.onAdLoaded();
                    route_adView.setVisibility(View.VISIBLE);
                }
            });
        }


    }


    @Override
    protected void onPause() {
        super.onPause();
        if (route_adView != null)
            route_adView.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (route_adView != null)
            route_adView.resume();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.lay_start_point:
                // ye wahe ap wali activity h


                List<Place.Field> fields = Arrays.asList(com.google.android.libraries.places.api.model.Place.Field.ID, com.google.android.libraries.places.api.model.Place.Field.NAME, com.google.android.libraries.places.api.model.Place.Field.LAT_LNG);
                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields).build(this);
                startActivityForResult(intent, START_PLACE_REQUEST);

//                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
//                try {
//                    startActivityForResult(builder.build(FindRouteActivity.this), START_PLACE_REQUEST);
//                } catch (GooglePlayServicesRepairableException e) {
//                    Toast.makeText(FindRouteActivity.this, "exception", Toast.LENGTH_SHORT).show();
//                    e.printStackTrace();
//                } catch (GooglePlayServicesNotAvailableException e) {
//                    Toast.makeText(FindRouteActivity.this, "exception 2", Toast.LENGTH_SHORT).show();
//                    e.printStackTrace();
//                }
                break;
            case R.id.lay_end_point:


                List<Place.Field> fields2 = Arrays.asList(com.google.android.libraries.places.api.model.Place.Field.ID, com.google.android.libraries.places.api.model.Place.Field.NAME, com.google.android.libraries.places.api.model.Place.Field.LAT_LNG);
                Intent intent2 = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields2).build(this);
                startActivityForResult(intent2, END_PLACE_REQUEST);

//                PlacePicker.IntentBuilder builder2 = new PlacePicker.IntentBuilder();
//                try {
//                    startActivityForResult(builder2.build(FindRouteActivity.this), END_PLACE_REQUEST);
//
//                } catch (GooglePlayServicesRepairableException e) {
//                    e.printStackTrace();
//                } catch (GooglePlayServicesNotAvailableException e) {
//                    e.printStackTrace();
//                }

                break;
            case R.id.btn_start_navigation:

                if (Start_Latlan == null) {
                    Constants.ShowInfo(FindRouteActivity.this, "Start Point", "Please Select start location first!!");
                } else if (End_Latlan == null) {
                    Constants.ShowInfo(FindRouteActivity.this, "End Point", "Please Select End location first !!");

                } else if (Start_Latlan != null && End_Latlan != null) {
                    Uri geo_uri = Uri.parse("http://maps.google.com/maps?f= d&saddr=" + Start_Latlan.latitude + "," + Start_Latlan.longitude + "&daddr=" + End_Latlan.latitude + "," + End_Latlan.longitude + "&travelmode=driving");
                    Intent intent_ = new Intent(Intent.ACTION_VIEW, geo_uri);
                    intent_.setPackage("com.google.android.apps.maps");
                    if (intent_.resolveActivity(getPackageManager()) != null) {
                        startActivity(intent_);
                    }
                }

                break;

            case R.id.set_my_location:
                GetLocation();
                break;
            case R.id.btn_save_route:

                if (Start_Latlan == null) {
                    Constants.ShowInfo(FindRouteActivity.this, "Start Point", "Please Select start location first!!");
                } else if (End_Latlan == null) {
                    Constants.ShowInfo(FindRouteActivity.this, "End Point", "Please Select End location first !!");

                } else if (Start_Latlan != null && End_Latlan != null &&
                        !start_address.getText().toString().equals("") &&
                        !end_address.getText().toString().equals("")) {

                    if (database != null && database.isOpen()) {

                        long result = database.insertResult(Start_Latlan.latitude, Start_Latlan.longitude,
                                End_Latlan.latitude, End_Latlan.longitude,
                                start_address.getText().toString(),
                                end_address.getText().toString());

                        if (result != -1)
                            Toast.makeText(this, "Rout saved Successfully", Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(this, "Rout not saved ", Toast.LENGTH_SHORT).show();

                    }


                    //savedPlaces.save();
                }

                break;
        }
    }


    private void GetLocation() {
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION};
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                ActivityCompat.requestPermissions(FindRouteActivity.this, permissions, 1);
            }
            return;
        }
        FusedLocation.getLastLocation()
                .addOnSuccessListener(FindRouteActivity.this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            Start_Latlan = new LatLng(location.getLatitude(), location.getLongitude());

                            GetAddress(location, true);
                            start_latitude.setText(String.format("Latitude = %s", String.valueOf(Start_Latlan.latitude)));
                            start_longitude.setText(String.valueOf(String.format("Longitude = %s", Start_Latlan.longitude)));


                        } else {
                            Constants.ShowInfo(FindRouteActivity.this, "Location", " Failed to load your location !!");
                        }
                    }
                });
    }

    private void GetAddress(Location location, boolean IsStart) {
        if (!Geocoder.isPresent()) {
            Toast.makeText(this, R.string.geocoder_not_available, Toast.LENGTH_LONG).show();
            return;
        }
        if (!IsAddressRequested) {
            IsAddressRequested = true;
            AddressServiceIntent(location, IsStart);
        }
    }

    protected void AddressServiceIntent(Location location, boolean IsStart) {
        Intent intent = new Intent(this, AddressService.class);
        if (IsStart) {
            intent.putExtra(Constants.RECEIVER, ResultReceiver);
        } else {
            intent.putExtra(Constants.RECEIVER, endResultReceiver);
        }

        intent.putExtra(Constants.LOCATION_DATA_EXTRA, location);
        startService(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case START_PLACE_REQUEST:
                if (resultCode == RESULT_OK && data != null) {

                    Place place = Autocomplete.getPlaceFromIntent(data);

                    Start_Latlan = place.getLatLng();

                    Location loc = new Location("Start Location");
                    loc.setLatitude(Start_Latlan.latitude);
                    loc.setLongitude(Start_Latlan.longitude);
                    GetAddress(loc, true);

                    start_latitude.setVisibility(View.VISIBLE);
                    start_longitude.setVisibility(View.VISIBLE);
                    findViewById(R.id.tvSourceMessage).setVisibility(View.GONE);
                    start_latitude.setText(String.format("Latitude = %s", String.valueOf(Start_Latlan.latitude)));
                    start_longitude.setText(String.valueOf(String.format("Longitude = %s", Start_Latlan.longitude)));
                    start_address.setText(start_address.getText().toString());


                } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                    // TODO: Handle the error.
                    //  Status status = Autocomplete.getStatusFromIntent(data);
                } else if (resultCode == RESULT_CANCELED) {
                    // The user canceled the operation.
                }

//                    Place place = PlacePicker.getPlace(this, data);
//                    Start_Latlan = place.getLatLng();
//                    start_latitude.setText(String.valueOf(Start_Latlan.latitude));
//                    start_longitude.setText(String.valueOf(Start_Latlan.longitude));
//                    start_address.setText(place.getAddress());


                break;
            case END_PLACE_REQUEST:
                if (resultCode == RESULT_OK && data != null) {

                    Place place = Autocomplete.getPlaceFromIntent(data);
                    End_Latlan = place.getLatLng();

                    Location loc = new Location("End Location");
                    loc.setLatitude(End_Latlan.latitude);
                    loc.setLongitude(End_Latlan.longitude);
                    GetAddress(loc, false);


                    end_latitude.setVisibility(View.VISIBLE);
                    end_longitude.setVisibility(View.VISIBLE);
                    findViewById(R.id.tvDestinationMessage).setVisibility(View.GONE);
                    end_latitude.setText(String.format("Latitude = %s", String.valueOf(End_Latlan.latitude)));
                    end_longitude.setText(String.valueOf(String.format("Longitude = %s", End_Latlan.longitude)));
                    end_address.setText(end_address.getText().toString());



                } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                    // TODO: Handle the error.
                    //  Status status = Autocomplete.getStatusFromIntent(data);
                } else if (resultCode == RESULT_CANCELED) {
                    // The user canceled the operation.
                }


//                    Place place = PlacePicker.getPlace(this, data);
//                    End_Latlan = place.getLatLng();
//                    end_latitude.setText(String.valueOf(End_Latlan.latitude));
//                    end_longitude.setText(String.valueOf(End_Latlan.longitude));
//                    end_address.setText(place.getAddress());


                break;
        }


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (database != null)
            database.close();


        if (route_adView != null)
            route_adView.destroy();
    }

    //    to find start address
    class AddressResultReceiver extends android.os.ResultReceiver {
        AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            try {
                IsAddressRequested = false;
                if (resultData == null) {
                    return;
                }
                String mAddressOutput = resultData.getString(Constants.RESULT_DATA_KEY);
                if (mAddressOutput == null) {
                    mAddressOutput = "";
                }
                if (resultCode == Constants.Service_SUCCESS_RESULT) {
                    start_address.setText(mAddressOutput);
                }
            } catch (Exception ignored) {
            }

        }
    }

    //    to find end eddress
    class EndAddressResultReceiver extends ResultReceiver {
        EndAddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            try {
                IsAddressRequested = false;
                if (resultData == null) {
                    return;
                }
                String mAddressOutput = resultData.getString(Constants.RESULT_DATA_KEY);
                if (mAddressOutput == null) {
                    mAddressOutput = "";
                }
                if (resultCode == Constants.Service_SUCCESS_RESULT) {
                    end_address.setText(mAddressOutput);
                }
            } catch (Exception ignored) {
            }

        }
    }

}
