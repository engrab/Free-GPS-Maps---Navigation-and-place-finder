package com.megafreeapps.free.gps.navigation.speedometer.compass.Fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.megafreeapps.free.gps.navigation.speedometer.compass.Models.NearByModel;
import com.megafreeapps.free.gps.navigation.speedometer.compass.R;
import com.megafreeapps.free.gps.navigation.speedometer.compass.Utilities.AppPurchasePref;
import com.megafreeapps.free.gps.navigation.speedometer.compass.Utilities.LocaleHelper;
import com.megafreeapps.free.gps.navigation.speedometer.compass.Utilities.Utils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.LOCATION_SERVICE;

public class Near_By_Places_Fragment extends Fragment {
    List<NearByModel> list;
    RecyclerView recyclerView;
    int position = 0;
    private Activity context;
    private InterstitialAd mInterstitialAd;
    //    AdView mAdView;
    boolean isFromText = false;
    boolean isFromText1 = false;
    boolean isFromText2 = false;

    public Near_By_Places_Fragment() {
        // Required empty public constructor
    }

    private void Init(View view) {
        context = getActivity();
        AppPurchasePref appPurchasePref = new AppPurchasePref(context);
        if (appPurchasePref.getItemDetail().equals("") && appPurchasePref.getProductId().equals("")) {

//            mAdView = view.findViewById(R.id.adView);
//            mAdView.loadAd(new AdRequest.Builder().build());
//            mAdView.setAdListener(new AdListener() {
//                @Override
//                public void onAdLoaded() {
//                    super.onAdLoaded();
//                    mAdView.setVisibility(View.VISIBLE);
//                }
//            });

            mInterstitialAd = new InterstitialAd(context);
            mInterstitialAd.setAdUnitId(getString(R.string.inter_ad_unit_id));
            mInterstitialAd.loadAd(new AdRequest.Builder().build());
            mInterstitialAd.setAdListener(new AdListener() {
                @Override
                public void onAdClosed() {
                    mInterstitialAd.loadAd(new AdRequest.Builder().build());

                    if (isFromText && !isFromText1 && !isFromText2) {
                        onClickListener();
                    } else if (!isFromText && isFromText1 && !isFromText2) {
                        onClickListener1();
                    } else if (!isFromText && !isFromText1 && isFromText2) {
                        onClickListener2();
                    }
                }
            });
        }

        listOptions();
        recyclerView = view.findViewById(R.id.rvItems);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new Near_By_Places_Adapter(context, list));

    }

    private void listOptions() {
        try {
            list = new ArrayList<>();
            list.add(new NearByModel(R.drawable.accountant, getString(R.string.txt_accounting), R.drawable.airport, getString(R.string.txt_airport), R.drawable.amusementpark, getString(R.string.txt_amusement_park)));
            list.add(new NearByModel(R.drawable.aquarium, getString(R.string.txt_aquarium), R.drawable.artgallery, getString(R.string.txt_art_gallery), R.drawable.atm, getString(R.string.txt_atm)));
            list.add(new NearByModel(123, getString(R.string.txt_admob), 123, getString(R.string.txt_admob), 123, getString(R.string.txt_admob)));
            list.add(new NearByModel(R.drawable.bakery, getString(R.string.txt_bakery), R.drawable.bank, getString(R.string.txt_bank), R.drawable.bar, getString(R.string.txt_bar)));
            list.add(new NearByModel(R.drawable.beauty_salon, getString(R.string.txt_beauty_salon), R.drawable.bicycle, getString(R.string.txt_bicycle_store), R.drawable.bookstore, getString(R.string.txt_book_store)));
            list.add(new NearByModel(123, getString(R.string.txt_admob), 123, getString(R.string.txt_admob), 123, getString(R.string.txt_admob)));
            list.add(new NearByModel(R.drawable.bowlingalley, getString(R.string.txt_bowling_alley), R.drawable.bus_station, getString(R.string.txt_bus_stop), R.drawable.cafe, getString(R.string.txt_cafe)));
            list.add(new NearByModel(R.drawable.car_dealer, getString(R.string.txt_car_dealer), R.drawable.car_rental, getString(R.string.txt_car_rental), R.drawable.car_repair, getString(R.string.txt_car_repair)));
            list.add(new NearByModel(123, getString(R.string.txt_admob), 123, getString(R.string.txt_admob), 123, getString(R.string.txt_admob)));
            list.add(new NearByModel(R.drawable.car_wash, getString(R.string.txt_car_wash), R.drawable.casino, getString(R.string.txt_casino), R.drawable.cemetery, getString(R.string.txt_cemetery)));
            list.add(new NearByModel(R.drawable.church, getString(R.string.txt_church), R.drawable.city_hall, getString(R.string.txt_city_hall), R.drawable.clothing_store, getString(R.string.txt_clothing_store)));
            list.add(new NearByModel(123, getString(R.string.txt_admob), 123, getString(R.string.txt_admob), 123, getString(R.string.txt_admob)));
            list.add(new NearByModel(R.drawable.convenience_store, getString(R.string.txt_convenience_store), R.drawable.courthouse, getString(R.string.txt_court), R.drawable.dentist, getString(R.string.txt_dentist)));
            list.add(new NearByModel(R.drawable.department_store, getString(R.string.txt_department_store), R.drawable.doctor, getString(R.string.txt_doctor), R.drawable.electronics_store, getString(R.string.txt_electrician)));
            list.add(new NearByModel(123, getString(R.string.txt_admob), 123, getString(R.string.txt_admob), 123, getString(R.string.txt_admob)));
            list.add(new NearByModel(R.drawable.electronics_store, getString(R.string.txt_electronics_store), R.drawable.embassy, getString(R.string.txt_embassy), R.drawable.real_estate_agency, getString(R.string.txt_establishment)));
            list.add(new NearByModel(R.drawable.finance, getString(R.string.txt_finance), R.drawable.fire_station, getString(R.string.txt_fire_station), R.drawable.florist, getString(R.string.txt_florist)));
            list.add(new NearByModel(123, getString(R.string.txt_admob), 123, getString(R.string.txt_admob), 123, getString(R.string.txt_admob)));
            list.add(new NearByModel(R.drawable.food, getString(R.string.txt_food), R.drawable.funeral_home, getString(R.string.txt_funeral_home), R.drawable.furniture_store, getString(R.string.txt_furniture_store)));
            list.add(new NearByModel(R.drawable.gas_station, getString(R.string.txt_gas_station), R.drawable.general_contractor, getString(R.string.txt_general_contractor), R.drawable.gym, getString(R.string.txt_gym)));
            list.add(new NearByModel(123, getString(R.string.txt_admob), 123, getString(R.string.txt_admob), 123, getString(R.string.txt_admob)));
            list.add(new NearByModel(R.drawable.hair_care, getString(R.string.txt_hair_care), R.drawable.hardware_store, getString(R.string.txt_hardware_store), R.drawable.health, getString(R.string.txt_health)));
            list.add(new NearByModel(R.drawable.hindu_temple, getString(R.string.txt_temple), R.drawable.pet_store, getString(R.string.txt_home_goods_store), R.drawable.hospital, getString(R.string.txt_hospital)));
            list.add(new NearByModel(123, getString(R.string.txt_admob), 123, getString(R.string.txt_admob), 123, getString(R.string.txt_admob)));
            list.add(new NearByModel(R.drawable.insurance_agency, getString(R.string.txt_insurance_agency), R.drawable.jewelry_store, getString(R.string.txt_jewelry_store), R.drawable.laundry, getString(R.string.txt_laundry)));
            list.add(new NearByModel(R.drawable.lawyer, getString(R.string.txt_lawyer), R.drawable.library, getString(R.string.txt_library), R.drawable.liquor_store, getString(R.string.txt_liquor_store)));
            list.add(new NearByModel(123, getString(R.string.txt_admob), 123, getString(R.string.txt_admob), 123, getString(R.string.txt_admob)));
            list.add(new NearByModel(R.drawable.local_government_office, getString(R.string.txt_local_government_office), R.drawable.locksmith, getString(R.string.txt_locksmith), R.drawable.lodging, getString(R.string.txt_lodging)));
            list.add(new NearByModel(R.drawable.meal_delivery, getString(R.string.txt_meal_delivery), R.drawable.meal_takeaway, getString(R.string.txt_meal_takeaway), R.drawable.mosque, getString(R.string.txt_mosque)));
            list.add(new NearByModel(123, getString(R.string.txt_admob), 123, getString(R.string.txt_admob), 123, getString(R.string.txt_admob)));
            list.add(new NearByModel(R.drawable.movie_rental, getString(R.string.txt_movie_rental), R.drawable.movie_theater, getString(R.string.txt_movie_theater), R.drawable.moving_company, getString(R.string.txt_moving_company)));
            list.add(new NearByModel(R.drawable.museum, getString(R.string.txt_museum), R.drawable.night_club, getString(R.string.txt_night_club), R.drawable.painter, getString(R.string.txt_painter)));
            list.add(new NearByModel(123, getString(R.string.txt_admob), 123, getString(R.string.txt_admob), 123, getString(R.string.txt_admob)));
            list.add(new NearByModel(R.drawable.park, getString(R.string.txt_park), R.drawable.parking, getString(R.string.txt_parking), R.drawable.pet_store, getString(R.string.txt_pet_store)));
            list.add(new NearByModel(R.drawable.pharmacy, getString(R.string.txt_pharmacy), R.drawable.physiotherapist, getString(R.string.txt_physiotherapist), R.drawable.place_of_worship, getString(R.string.txt_place_worship)));
            list.add(new NearByModel(123, getString(R.string.txt_admob), 123, getString(R.string.txt_admob), 123, getString(R.string.txt_admob)));
            list.add(new NearByModel(R.drawable.plumber, getString(R.string.txt_plumber), R.drawable.police, getString(R.string.txt_police), R.drawable.post_office, getString(R.string.txt_post_office)));
            list.add(new NearByModel(R.drawable.real_estate_agency, getString(R.string.txt_real_estate_agency), R.drawable.restaurant, getString(R.string.txt_restaurant), R.drawable.roofing_contractor, getString(R.string.txt_roofing_contractor)));
            list.add(new NearByModel(123, getString(R.string.txt_admob), 123, getString(R.string.txt_admob), 123, getString(R.string.txt_admob)));
            list.add(new NearByModel(R.drawable.rv_park, getString(R.string.txt_rv_park), R.drawable.school, getString(R.string.txt_school), R.drawable.shoe_store, getString(R.string.txt_shoe_store)));
            list.add(new NearByModel(R.drawable.shopping_mall, getString(R.string.txt_shopping_mall), R.drawable.spa, getString(R.string.txt_spa), R.drawable.stadium, getString(R.string.txt_stadium)));
            list.add(new NearByModel(123, getString(R.string.txt_admob), 123, getString(R.string.txt_admob), 123, getString(R.string.txt_admob)));
            list.add(new NearByModel(R.drawable.storage, getString(R.string.txt_storage), R.drawable.store, getString(R.string.txt_store), R.drawable.subway_station, getString(R.string.txt_subway_station)));
            list.add(new NearByModel(R.drawable.grocery, getString(R.string.txt_super_market), R.drawable.synagogue, getString(R.string.txt_synagogue), R.drawable.taxi_stand, getString(R.string.txt_taxi_stand)));
            list.add(new NearByModel(123, getString(R.string.txt_admob), 123, getString(R.string.txt_admob), 123, getString(R.string.txt_admob)));
            list.add(new NearByModel(R.drawable.bus_station, getString(R.string.txt_train_station), R.drawable.transit_station, getString(R.string.txt_transit_station), R.drawable.travel_agency, getString(R.string.txt_travel_agency)));
            list.add(new NearByModel(R.drawable.university, getString(R.string.txt_university), R.drawable.veterinary_care, getString(R.string.txt_veterinary_care), R.drawable.zoo, getString(R.string.txt_zoo)));
            list.add(new NearByModel(123, getString(R.string.txt_admob), 123, getString(R.string.txt_admob), 123, getString(R.string.txt_admob)));

        } catch (Exception ignored) {
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_near_by_places, container, false);
        Init(view);
        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
    }

    private void onClickListener() {
        try {
            LocationManager locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
            if (locationManager != null) {
                String provider = locationManager.getBestProvider(new Criteria(), true);
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission
                        .ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                Location location = locationManager.getLastKnownLocation(provider);
                if (location != null) {
                    Uri gmmIntentUri = Uri.parse("geo:" + location.getLatitude() + "," + location.getLongitude() +
                            "?q=" + list.get(position).text);
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    if (mapIntent.resolveActivity(context.getPackageManager()) != null) {
                        startActivity(mapIntent);
                    }
                } else {
                    Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + list.get(position).text);
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    if (mapIntent.resolveActivity(context.getPackageManager()) != null) {
                        startActivity(mapIntent);
                    }
                }
            } else {
                Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + list.get(position).text);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                if (mapIntent.resolveActivity(context.getPackageManager()) != null) {
                    startActivity(mapIntent);
                }
            }
        } catch (Exception ignored) {
        }
    }

    private void onClickListener1() {
        try {
            LocationManager locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
            if (locationManager != null) {
                String provider = locationManager.getBestProvider(new Criteria(), true);
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission
                        .ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                Location location = locationManager.getLastKnownLocation(provider);
                if (location != null) {
                    Uri gmmIntentUri = Uri.parse("geo:" + location.getLatitude() + "," + location.getLongitude() +
                            "?q=" + list.get(position).text1);
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    if (mapIntent.resolveActivity(context.getPackageManager()) != null) {
                        startActivity(mapIntent);
                    }
                } else {
                    Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + list.get(position).text1);
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    if (mapIntent.resolveActivity(context.getPackageManager()) != null) {
                        startActivity(mapIntent);
                    }
                }
            } else {
                Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + list.get(position).text1);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                if (mapIntent.resolveActivity(context.getPackageManager()) != null) {
                    startActivity(mapIntent);
                }
            }
        } catch (Exception ignored) {
        }
    }

    private void onClickListener2() {
        try {
            LocationManager locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
            if (locationManager != null) {
                String provider = locationManager.getBestProvider(new Criteria(), true);
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission
                        .ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                Location location = locationManager.getLastKnownLocation(provider);
                if (location != null) {
                    Uri gmmIntentUri = Uri.parse("geo:" + location.getLatitude() + "," + location.getLongitude() +
                            "?q=" + list.get(position).text2);
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    if (mapIntent.resolveActivity(context.getPackageManager()) != null) {
                        startActivity(mapIntent);
                    }
                } else {
                    Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + list.get(position).text2);
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    if (mapIntent.resolveActivity(context.getPackageManager()) != null) {
                        startActivity(mapIntent);
                    }
                }
            } else {
                Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + list.get(position).text2);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                if (mapIntent.resolveActivity(context.getPackageManager()) != null) {
                    startActivity(mapIntent);
                }
            }
        } catch (Exception ignored) {
        }
    }


    public class Near_By_Places_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        Activity context;
        private List<NearByModel> placesInfo;

        Near_By_Places_Adapter(Activity c, List<NearByModel> list) {
            this.context=c;
            this.placesInfo = list;
        }


        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            if (viewType == 1) {
                LinearLayout linearLayout = new LinearLayout(getActivity());
                linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                LinearLayout.LayoutParams attributLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                attributLayoutParams.gravity = Gravity.CENTER;
                linearLayout.setLayoutParams(attributLayoutParams);
                linearLayout.setGravity(Gravity.CENTER);
                AdView adView = new AdView(getActivity());
                adView.setAdUnitId(getString(R.string.banner_ad_unit_id));
                adView.setAdSize(AdSize.BANNER);
                adView.loadAd(new AdRequest.Builder().build());
                linearLayout.addView(adView);
                return new AdViewHolder(linearLayout);
            } else {

                return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.grid_single_row, parent, false));

            }
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            NearByModel nearByModel = placesInfo.get(position);

            if (holder.getItemViewType() == 1)
                return;

            if (holder instanceof ViewHolder) {
                ViewHolder Holder = (ViewHolder) holder;
                Picasso.get().load(nearByModel.id).fit().into(Holder.IvImg);
                Holder.TvText.setText(nearByModel.text);
                Picasso.get().load(nearByModel.id1).fit().into(Holder.IvImg1);
                Holder.TvText1.setText(nearByModel.text1);
                Picasso.get().load(nearByModel.id2).fit().into(Holder.IvImg2);
                Holder.TvText2.setText(nearByModel.text2);

            }

        }

        @Override
        public int getItemViewType(int position) {
            if (placesInfo.get(position).text.equals("admob") &&
                    placesInfo.get(position).text1.equals("admob") &&
                    placesInfo.get(position).text2.equals("admob")) {
                return 1;

            } else
                return 2;

        }

        @Override
        public int getItemCount() {
            return placesInfo.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            ImageView IvImg, IvImg1, IvImg2;
            TextView TvText, TvText1, TvText2;


            ViewHolder(View itemView) {
                super(itemView);


                IvImg = itemView.findViewById(R.id.tpyeicon_new);
                IvImg1 = itemView.findViewById(R.id.tpyeicon_new1);
                IvImg2 = itemView.findViewById(R.id.tpyeicon_new2);

                TvText = itemView.findViewById(R.id.typetxt_new);
                TvText1 = itemView.findViewById(R.id.typetxt_new1);
                TvText2 = itemView.findViewById(R.id.typetxt_new2);

                IvImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        isFromText = true;
                        isFromText1 = false;
                        isFromText2 = false;
                        try {
                            if (!Utils.isNetworkAvailable(context)) {
                                Toast.makeText(context, "No Internet Connection", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            position = getAdapterPosition();
                            if (mInterstitialAd != null && mInterstitialAd.isLoaded()) {
                                mInterstitialAd.show();
                            } else {
                                onClickListener();
                            }
                        } catch (Exception ignored) {
                        }
                    }
                });
                IvImg1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        isFromText = false;
                        isFromText1 = true;
                        isFromText2 = false;
                        try {
                            if (!Utils.isNetworkAvailable(context)) {
                                Toast.makeText(context, "No Internet Connection", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            position = getAdapterPosition();
                            if (mInterstitialAd != null && mInterstitialAd.isLoaded()) {
                                mInterstitialAd.show();
                            } else {
                                onClickListener1();
                            }
                        } catch (Exception ignored) {
                        }
                    }
                });
                IvImg2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        isFromText = false;
                        isFromText1 = false;
                        isFromText2 = true;
                        try {
                            if (!Utils.isNetworkAvailable(context)) {
                                Toast.makeText(context, "No Internet Connection", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            position = getAdapterPosition();
                            if (mInterstitialAd != null && mInterstitialAd.isLoaded()) {
                                mInterstitialAd.show();
                            } else {
                                onClickListener2();
                            }
                        } catch (Exception ignored) {
                        }
                    }
                });
            }

        }

        class AdViewHolder extends RecyclerView.ViewHolder {


            AdViewHolder(View itemView) {
                super(itemView);


            }

        }

    }

}
