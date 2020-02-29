package com.megafreeapps.free.gps.navigation.speedometer.compass.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.megafreeapps.free.gps.navigation.speedometer.compass.Fragments.Compass_Fragment;
import com.megafreeapps.free.gps.navigation.speedometer.compass.Fragments.Navigation_Fragment;
import com.megafreeapps.free.gps.navigation.speedometer.compass.Fragments.Near_By_Places_Fragment;
import com.megafreeapps.free.gps.navigation.speedometer.compass.Fragments.Speedo_Meter_Fragment;
import com.megafreeapps.free.gps.navigation.speedometer.compass.Fragments.Voice_Navigation_Fragment;
import com.megafreeapps.free.gps.navigation.speedometer.compass.Interfaces.LocationCommunicator;
import com.megafreeapps.free.gps.navigation.speedometer.compass.R;
import com.megafreeapps.free.gps.navigation.speedometer.compass.Utilities.AppPurchasePref;
import com.megafreeapps.free.gps.navigation.speedometer.compass.Utilities.LocaleHelper;
import com.megafreeapps.free.gps.navigation.speedometer.compass.Utilities.Utils;

import static com.megafreeapps.free.gps.navigation.speedometer.compass.Utilities.Utils.animation;

public class MainActivity extends AppCompatActivity implements BillingProcessor.IBillingHandler, NavigationView.OnNavigationItemSelectedListener {
    Activity context;
    LocationCommunicator locationCommunicator;
    int PLACE_PICKER_REQUEST = 122;
    private Dialog mDialog;
    BillingProcessor bp;
    ImageView ivRemoveAd;
    InterstitialAd mInterstitialAd;
    AdView mDialogeAdView;
    AlertDialog alertDialog = null;

    private AppPurchasePref appPurchasePref;


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base));
    }

    private void Init() {

        if (appPurchasePref.getItemDetail().equals("") && appPurchasePref.getProductId().equals("")) {

            mInterstitialAd = new InterstitialAd(this);
            mInterstitialAd.setAdUnitId(getString(R.string.inter_ad_unit_id));
            mInterstitialAd.loadAd(new AdRequest.Builder().build());
            mInterstitialAd.setAdListener(new AdListener() {
                @Override
                public void onAdClosed() {
                    super.onAdClosed();
                }
            });
        }
        TabLayout tabs = findViewById(R.id.tabs);
        ViewPager container = findViewById(R.id.container);
        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        tabs.setSelectedTabIndicatorHeight(8);
        container.setAdapter(mSectionsPagerAdapter);
        tabs.setupWithViewPager(container);

        container.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void ExitDialog() {

        try {
            View view = View.inflate(this, R.layout.dialog_exit_layout, null);
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setView(view);
            AppPurchasePref appPurchasePref = new AppPurchasePref(this);
            if (appPurchasePref.getItemDetail().equals("") && appPurchasePref.getProductId().equals("")) {

                mDialogeAdView = view.findViewById(R.id.dialoge_adView);
                mDialogeAdView.loadAd(new AdRequest.Builder().build());
                mDialogeAdView.setAdListener(new AdListener() {
                    @Override
                    public void onAdLoaded() {
                        super.onAdLoaded();
                        mDialogeAdView.setVisibility(View.VISIBLE);
                    }
                });
            }
//            ((TextView) view.findViewById(R.id.dialog_heading)).setText("Want to Exit?");
//            ((TextView) view.findViewById(R.id.exit_app)).setText("Yes");
//            ((TextView) view.findViewById(R.id.cancel_app)).setText("No");
            view.findViewById(R.id.exit_app).setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    alertDialog.dismiss();
                    finish();
                }
            });
            view.findViewById(R.id.cancel_app).setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    alertDialog.dismiss();
                }
            });
            alertDialog = alertDialogBuilder.create();
            alertDialog.setCancelable(false);
            alertDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
        } else {
            ExitDialog();
        }

    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            DrawerLayout drawer = findViewById(R.id.drawer_layout);

            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            }
            // Handle the camera action
        } else if (id == R.id.nav_language) {
            startActivity(new Intent(MainActivity.this, Select_Language_Activity.class));

        } else if (id == R.id.nav_share) {
            Intent shareAppIntent = new Intent(android.content.Intent.ACTION_SEND);
            shareAppIntent.setType("text/plain");
            shareAppIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getString(R.string.app_name));
            shareAppIntent.putExtra(android.content.Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=" + context.getPackageName() + "&hl=en");
            if (shareAppIntent.resolveActivity(context.getPackageManager()) != null) {
                startActivity(Intent.createChooser(shareAppIntent, "Share via"));
            }

        } else if (id == R.id.nav_rate) {
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=" + context.getPackageName() + "&hl=en"));
                if (intent.resolveActivity(context.getPackageManager()) != null) {
                    startActivity(intent);
                }
            } catch (android.content.ActivityNotFoundException anfe) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://play.google.com/store/apps/developer?id=Mega+Free+Apps+Developers"));
                if (intent.resolveActivity(context.getPackageManager()) != null) {
                    startActivity(intent);
                }
            }

        } else if (id == R.id.nav_more) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://play.google.com/store/apps/developer?id=Mega+Free+Apps+Developers"));
            if (intent.resolveActivity(context.getPackageManager()) != null) {
                startActivity(intent);
            }

        } else if (id == R.id.nav_privacy) {
            startActivity(new Intent(MainActivity.this, PrivacyPolicyActivity.class));

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        FirebaseApp.initializeApp(this);
//        FirebaseMessaging.getInstance().subscribeToTopic(getPackageName());
        context = MainActivity.this;
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        appPurchasePref = new AppPurchasePref(getApplicationContext());

        bp = new BillingProcessor(getApplicationContext(), "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAh49sluoXot+wmG0eCenQ7E9f4k9Ti3qUC+XKfa94ycfefEF0utJAz9YNWDvO81Ge1W08M3D/4Rgr4R9WoSI1z1ibcB3Sf6rvAoZixq4HFmECDC7pE57oBG+ryFrON3Ks3oIx3ca6hfzlSGkFOzz2fyiX9l3Cd333Xshua0WGkMa1LRDBH9dPPdXgEKrxjk3HCJDnkz6utGSWy7CsbiLr0rDlfrvdJZCXw5s4MSd/9pD0uliz6HmpsHP4v87NGvMaaVwbTvY+OoZngOAbZG0NTASdCWrODNSS+tJzZbA5BvDlq8fnMK4k2p2+1azJ4/bnOqW38G1imMUjJJzTxEl9XQIDAQAB", this);

        ivRemoveAd = findViewById(R.id.ivAdRemove);
        ivRemoveAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    bp.purchase(context, Utils.INAPPproductId);
                } catch (Exception ignored) {
                }
            }
        });
        animation(ivRemoveAd);
        Init();
    }


//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == PLACE_PICKER_REQUEST) {
//            if (resultCode == RESULT_OK) {
//                try {
//                    Place place = PlacePicker.getPlace(context, data);
//                    locationCommunicator.PlaceLocation(place.getLatLng());
//                } catch (Exception ignored) {
//                }
//            }
//        }
//        if (!bp.handleActivityResult(requestCode, resultCode, data)) {
//            super.onActivityResult(requestCode, resultCode, data);
//        }
//    }


    @Override
    public void onProductPurchased(@NonNull String productId, @Nullable TransactionDetails details) {
        ViewUpdate();
        appPurchasePref.setProductId(productId);
        if (details != null) {
            appPurchasePref.setItemDetails(details.toString());
        }
    }

    @Override
    public void onPurchaseHistoryRestored() {
        ViewUpdate();
    }

    @Override
    public void onBillingError(int errorCode, @Nullable Throwable error) {

    }

    @Override
    public void onBillingInitialized() {
        ViewUpdate();
    }


    private void ViewUpdate() {
        if (bp != null && bp.isPurchased(Utils.INAPPproductId)) {
            appPurchasePref.setProductId(getPackageName());
            appPurchasePref.setItemDetails(getPackageName());
            findViewById(R.id.ivAdRemove).setVisibility(View.GONE);

        }
    }

    private class SectionsPagerAdapter extends FragmentPagerAdapter {

        SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            if (position == 0) {
                return new Navigation_Fragment();
            } else if (position == 1) {
                return new Near_By_Places_Fragment();
            } else if (position == 2) {
                return new Voice_Navigation_Fragment();
            } else if (position == 3) {
                return new Speedo_Meter_Fragment();
            } else {
                return new Compass_Fragment();
            }
        }

        @Override
        public int getCount() {
            return 5;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.navigation);
                case 1:
                    return getString(R.string.nearby_places);
                case 2:
                    return getString(R.string.voice_navigation);
                case 3:
                    return getString(R.string.speedometer);
                case 4:
                    return getString(R.string.compass);
            }
            return null;
        }
    }

    @Override
    protected void onDestroy() {
        if (bp != null) {
            bp.release();
        }
        super.onDestroy();
    }
}
