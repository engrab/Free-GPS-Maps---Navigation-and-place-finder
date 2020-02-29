package com.megafreeapps.free.gps.navigation.speedometer.compass.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import cn.refactor.lib.colordialog.PromptDialog;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.megafreeapps.free.gps.navigation.speedometer.compass.R;
import com.megafreeapps.free.gps.navigation.speedometer.compass.Utilities.AppPurchasePref;

import permission.auron.com.marshmallowpermissionhelper.ActivityManagePermission;
import permission.auron.com.marshmallowpermissionhelper.PermissionResult;
import permission.auron.com.marshmallowpermissionhelper.PermissionUtils;

public class Splash_Activity extends ActivityManagePermission {
    final int SPLASH_TIME_OUT = 2000;
    private boolean denay = false;
    InterstitialAd mInterstitialAd;
    public static final String PREFS_NAME = "MyPrefsFile";
    public static final String KEY_FIRST_TIME = "firstTime";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.gc();

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash);

        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        boolean firstTime = prefs.getBoolean(KEY_FIRST_TIME, false);

        if (!firstTime) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean(KEY_FIRST_TIME, true);
            editor.apply();
        }

        MobileAds.initialize(getApplicationContext(), getString(R.string.app_id));

        AppPurchasePref appPurchasePref = new AppPurchasePref(getApplicationContext());
        if (appPurchasePref.getItemDetail().equals("") && appPurchasePref.getProductId().equals("")) {

            mInterstitialAd = new InterstitialAd(this);
            mInterstitialAd.setAdUnitId(getString(R.string.inter_ad_unit_id));
            mInterstitialAd.loadAd(new AdRequest.Builder().build());
            mInterstitialAd.setAdListener(new AdListener() {
                @Override
                public void onAdClosed() {
                    super.onAdClosed();
                    if (!firstTime) {
                        startActivity(new Intent (Splash_Activity.this, Select_Language_Activity.class));
                        finish();

                    } else {
                        startActivity(new Intent (Splash_Activity.this, MainActivity.class));
                        finish();
                    }
                }
            });
        }

        askCompactPermissions(new String[]{
                        PermissionUtils.Manifest_ACCESS_FINE_LOCATION,
                        PermissionUtils.Manifest_ACCESS_COARSE_LOCATION},
                new PermissionResult() {
                    @Override
                    public void permissionGranted() {
                        //permission granted
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (mInterstitialAd != null && mInterstitialAd.isLoaded()) {
                                    mInterstitialAd.show();
                                } else {
                                    if (!firstTime) {
                                        startActivity(new Intent (Splash_Activity.this, Select_Language_Activity.class));
                                        finish();

                                    } else {
                                        startActivity(new Intent (Splash_Activity.this, MainActivity.class));
                                        finish();
                                    }
                                }
                                // close this activity
                            }
                        }, SPLASH_TIME_OUT);
                    }

                    @Override
                    public void permissionDenied() {
                        denay = true;
                        permissionWarningDialog();
                    }

                    @Override
                    public void permissionForeverDenied() {
                        denay = true;
                        permissionWarningDialog();
                    }

                });

    }

    @Override
    public void onBackPressed() {
        if (denay) {
            super.onBackPressed();
        }
    }

    public void permissionWarningDialog() {

        new PromptDialog(Splash_Activity.this)
                .setDialogType(PromptDialog.DIALOG_TYPE_WARNING)
                .setAnimationEnable(true)
                .setTitleText(getString(R.string.pdialog_title))
                .setContentText(getString(R.string.pdialog_text))
                .setPositiveListener(getString(R.string.pdialog_setting_btn), new PromptDialog.OnPositiveListener() {
                    @Override
                    public void onClick(PromptDialog dialog) {
                        openSettingsApp(Splash_Activity.this);
                        dialog.dismiss();
                    }
                }).show();
    }
}
