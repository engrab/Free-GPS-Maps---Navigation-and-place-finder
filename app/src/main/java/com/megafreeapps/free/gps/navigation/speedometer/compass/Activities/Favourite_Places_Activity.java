package com.megafreeapps.free.gps.navigation.speedometer.compass.Activities;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.megafreeapps.free.gps.navigation.speedometer.compass.Adapters.Favourite_Places_Adapter;
import com.megafreeapps.free.gps.navigation.speedometer.compass.Database.DbHelper;
import com.megafreeapps.free.gps.navigation.speedometer.compass.Models.Favourite_Places_Model;
import com.megafreeapps.free.gps.navigation.speedometer.compass.R;
import com.megafreeapps.free.gps.navigation.speedometer.compass.Utilities.AppPurchasePref;
import com.megafreeapps.free.gps.navigation.speedometer.compass.Utilities.LocaleHelper;
import com.megafreeapps.free.gps.navigation.speedometer.compass.Utilities.Utils;

public class Favourite_Places_Activity extends AppCompatActivity
{
    private Activity context;
    private int PLACE_REQUEST = 11;
    private Resources resources;
    private RecyclerView rvItems;
    private DbHelper dbHelper;
    private List<Favourite_Places_Model> list;
    private Favourite_Places_Adapter placesAdapter;
    private InterstitialAd mInterstitialAd;


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite_places);
        AppPurchasePref appPurchasePref = new AppPurchasePref(getApplicationContext());
        if (appPurchasePref.getItemDetail().equals("") && appPurchasePref.getProductId().equals("")) {
            mInterstitialAd = new InterstitialAd(this);
            mInterstitialAd.setAdUnitId(getString(R.string.inter_ad_unit_id));
            mInterstitialAd.loadAd(new AdRequest.Builder().build());
            mInterstitialAd.setAdListener(new AdListener()
            {
                @Override
                public void onAdClosed()
                {
                    super.onAdClosed();
                    finish();
                }
            });

        }
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), getString(R.string.google_maps_key));
        }
        context = Favourite_Places_Activity.this;
        dbHelper = new DbHelper(context);
        resources = getResources();
        rvItems = findViewById(R.id.rvItems);
        rvItems.setLayoutManager(new LinearLayoutManager(context));
        list = new ArrayList<>();
        list = dbHelper.GetFavouritePlacesList();
        if (list != null && list.size() > 0)
        {
            placesAdapter = new Favourite_Places_Adapter(context, list);
            rvItems.setAdapter(placesAdapter);
        }
        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                try
                {
                    List<Place.Field> fields = Arrays.asList(com.google.android.libraries.places.api.model.Place.Field.ID, com.google.android.libraries.places.api.model.Place.Field.NAME, com.google.android.libraries.places.api.model.Place.Field.LAT_LNG);
                    Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields).build(Favourite_Places_Activity.this);
                    startActivityForResult(intent, PLACE_REQUEST);

                }
                catch (Exception ignore){

                }
            }
        });
        SetupToolbar();
    }

    private void SetupToolbar()
    {
        try
        {
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
            toolbar.setNavigationOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if (mInterstitialAd != null && mInterstitialAd.isLoaded())
                    {
                        mInterstitialAd.show();
                    }
                    else
                    {
                        finish();
                    }
                }
            });
        }
        catch (Exception ignored)
        {
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PLACE_REQUEST)
        {
            if (resultCode == RESULT_OK)
            {
                try
                {
                    Place place = Autocomplete.getPlaceFromIntent(data);
                    LatLng latLng = place.getLatLng();
                    ContentValues values = new ContentValues();
                    values.put(resources.getString(R.string.TxtDbAddress), place.getAddress() + "");
                    values.put(resources.getString(R.string.TxtDbDate), Utils.GetCustomDate("dd/MM/yyyy"));
                    values.put(resources.getString(R.string.TxtDbTime), Utils.GetCustomDate("hh:mm a"));
                    values.put(resources.getString(R.string.TxtDbName), place.getName() + "");
                    values.put(resources.getString(R.string.TxtDbLatitude), latLng.latitude + "");
                    values.put(resources.getString(R.string.TxtDbLongitude), latLng.longitude + "");

                    if (dbHelper.InsertData(values, resources.getString(R.string.TxtTblPlaces)))
                    {
                        if (list != null && list.size() > 0)
                        {
                            list.add(new Favourite_Places_Model(place.getAddress() + "", place.getName() + "",
                                    Utils.GetCustomDate("dd/MM/yyyy"), Utils.GetCustomDate("hh:mm a"), latLng.latitude + "",
                                    latLng.longitude + ""));
                            placesAdapter.notifyDataSetChanged();
                        }
                        else
                        {
                            list = new ArrayList<>();
                            list.add(new Favourite_Places_Model(place.getAddress() + "", place.getName() + "",
                                    Utils.GetCustomDate("dd/MM/yyyy"), Utils.GetCustomDate("hh:mm a"), latLng.latitude + "",
                                    latLng.longitude + ""));
                            placesAdapter = new Favourite_Places_Adapter(context, list);
                            rvItems.setAdapter(placesAdapter);
                        }
                    }
                }
                catch (Exception ignored)
                {
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.favourite_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            // action with ID action_refresh was selected
            case R.id.action_clear:
                dbHelper.DeleteTableData(resources.getString(R.string.TxtTblPlaces));
                list.clear();
                rvItems.setAdapter(null);
                break;
        }

        return true;
    }

    @Override
    public void onBackPressed()
    {
        if (mInterstitialAd != null && mInterstitialAd.isLoaded())
        {
            mInterstitialAd.show();
        }
        else
        {
            super.onBackPressed();
        }
    }
}
