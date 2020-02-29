package com.megafreeapps.free.gps.navigation.speedometer.compass.Services;

import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.text.TextUtils;


import androidx.annotation.Nullable;

import com.megafreeapps.free.gps.navigation.speedometer.compass.R;
import com.megafreeapps.free.gps.navigation.speedometer.compass.Utilities.Constants;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AddressService extends IntentService {

    protected ResultReceiver resultReceiver;

    public AddressService() {
        super("AddressService");
    }

    private void SendBackResultToReceiver(int resultCode, String message) {
        try {
            Bundle bundle = new Bundle();
            bundle.putString(Constants.RESULT_DATA_KEY, message);
            resultReceiver.send(resultCode, bundle);
        } catch (Exception ignored) {
        }
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        try {
            if (intent == null) {
                return;
            }
            resultReceiver = intent.getParcelableExtra(Constants.RECEIVER);

            if (resultReceiver == null) {
                return;
            }
            Location location = intent.getParcelableExtra(Constants.LOCATION_DATA_EXTRA);
            if (location == null) {
                return;
            }
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            String errorMessage = "";
            List<Address> addresses = null;
            try {
                addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            } catch (IOException ioException) {
                errorMessage = getString(R.string.no_service_available);
            } catch (IllegalArgumentException illegalArgumentException) {
                errorMessage = getString(R.string._lat_long_not_corrent);
            }

            if (addresses == null || addresses.size() == 0) {
                if (errorMessage.isEmpty()) {
                    errorMessage = getString(R.string.address_not_found);
                }
                SendBackResultToReceiver(Constants.Service_FAILURE_RESULT, errorMessage);
            } else {
                Address address = addresses.get(0);
                ArrayList<String> addressFragments = new ArrayList<>();
                for (int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                    addressFragments.add(address.getAddressLine(i));
                }
                SendBackResultToReceiver(Constants.Service_SUCCESS_RESULT, TextUtils.join(System.getProperty("line.separator"), addressFragments));
            }
        } catch (Exception ignored) {
        }
    }


}
