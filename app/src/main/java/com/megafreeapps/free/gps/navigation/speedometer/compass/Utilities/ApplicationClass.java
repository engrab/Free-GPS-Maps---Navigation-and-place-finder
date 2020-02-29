package com.megafreeapps.free.gps.navigation.speedometer.compass.Utilities;

import android.content.Context;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.google.android.gms.ads.MobileAds;
import com.megafreeapps.free.gps.navigation.speedometer.compass.R;

import java.util.Locale;


public class ApplicationClass extends MultiDexApplication {
    private static ApplicationClass applicationClass;


    public ApplicationClass() {
        applicationClass = this;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        MobileAds.initialize(this, getString(R.string.app_id));
    }
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base, Locale.getDefault().getLanguage()));
        MultiDex.install(this);
    }

}
