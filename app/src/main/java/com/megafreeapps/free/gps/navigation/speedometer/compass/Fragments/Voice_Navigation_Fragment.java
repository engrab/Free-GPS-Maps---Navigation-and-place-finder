package com.megafreeapps.free.gps.navigation.speedometer.compass.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import java.util.Arrays;
import java.util.List;

import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.megafreeapps.free.gps.navigation.speedometer.compass.Adapters.Voice_Navigation_Adapter;
import com.megafreeapps.free.gps.navigation.speedometer.compass.R;
import com.megafreeapps.free.gps.navigation.speedometer.compass.Utilities.AppPurchasePref;

import static android.app.Activity.RESULT_OK;

public class Voice_Navigation_Fragment extends Fragment implements View.OnClickListener
{
    int PLACE_PICKER_REQUEST = 11;
    int VOICE_REQUEST = 12;
    private AdView mAdView;
    Activity context;
    RecyclerView recyclerView;
    private InterstitialAd mInterstitialAd;
    BillingProcessor bp;


    public Voice_Navigation_Fragment()
    {
        // Required empty public constructor
    }

    private void Init(View view)
    {
        context = getActivity();
        AppPurchasePref appPurchasePref = new AppPurchasePref(getActivity());
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

        }
        if (!Places.isInitialized()) {
            Places.initialize(context, getString(R.string.google_maps_key));
        }
        recyclerView = view.findViewById(R.id.rvItems);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        view.findViewById(R.id.tvVoice).setOnClickListener(this);
        view.findViewById(R.id.ivRecorder).setOnClickListener(this);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_voice_navigation, container, false);
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
        switch (view.getId())
        {
            case R.id.tvVoice:
                List<Place.Field> fields = Arrays.asList(com.google.android.libraries.places.api.model.Place.Field.ID, com.google.android.libraries.places.api.model.Place.Field.NAME, com.google.android.libraries.places.api.model.Place.Field.LAT_LNG);
                Intent intents = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields).build(context);
                startActivityForResult(intents, PLACE_PICKER_REQUEST);

//                try
//                {
//                    PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
//                    startActivityForResult(builder.build(context), PLACE_PICKER_REQUEST);
//                }
//                catch (GooglePlayServicesRepairableException e)
//                {
//                    // TODO: Handle the error.
//                }
//                catch (GooglePlayServicesNotAvailableException e)
//                {
//                    // TODO: Handle the error.
//                }
                break;
            case R.id.ivRecorder:
                try
                {
                    if (mInterstitialAd != null && mInterstitialAd.isLoaded())
                    {
                        mInterstitialAd.show();
                    }
                    if (context.getPackageManager().queryIntentActivities(new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0)
                            .size() != 0)
                    {
                        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Where you want to go! Speak Now!");
                        startActivityForResult(intent, VOICE_REQUEST);
                    }
                    else
                    {
                        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                        alertDialog.setTitle("Warning!");
                        alertDialog.setMessage("Voice Recognition Engine on Your Device is Not Active");
                        alertDialog.setButton("OK", new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int which)
                            {
                                dialog.dismiss();
                            }
                        });
                        alertDialog.show();
                    }
                }
                catch (Exception ignored)
                {
                }
                break;
        }
    }


    @Override
    public void onPause()
    {
        if (mAdView != null)
        {
            mAdView.pause();
        }
        super.onPause();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        if (mAdView != null)
        {
            mAdView.resume();
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
        if (requestCode == PLACE_PICKER_REQUEST)
        {
            if (resultCode == RESULT_OK)
            {
                try
                {
                    Place place = Autocomplete.getPlaceFromIntent(data);
                    Uri gmmIntentUri = Uri.parse("google.navigation:q=" + place.getName());
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    if (mapIntent.resolveActivity(context.getPackageManager()) != null)
                    {
                        startActivity(mapIntent);
                    }
                }
                catch (Exception ignored)
                {
                }
            }
        }
        else if (requestCode == VOICE_REQUEST)
        {
            if (resultCode == RESULT_OK)
            {
                try
                {
                    List<String> list = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    if (list != null && list.size() > 0)
                    {
                        recyclerView.setAdapter(new Voice_Navigation_Adapter(context, list));
                    }
                    else
                    {
                        recyclerView.setAdapter(null);
                    }
                }
                catch (Exception ignored)
                {
                }
            }
        }
    }
}
