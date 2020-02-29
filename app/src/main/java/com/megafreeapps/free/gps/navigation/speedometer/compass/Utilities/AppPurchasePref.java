package com.megafreeapps.free.gps.navigation.speedometer.compass.Utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.megafreeapps.free.gps.navigation.speedometer.compass.R;

/**
 * Created by Jointech on 9/11/2017.
 */

public class AppPurchasePref
{
    private final String productId = "productIdOfItemoffline";
    private final String TransactionDetails = "TransactionDetailsOffile";
    private SharedPreferences pref;
    private Editor editor;


    public AppPurchasePref(Context context)
    {
        pref = context.getSharedPreferences(context.getResources().getString(R.string.app_name), 0);
        editor = pref.edit();
    }

    public String getProductId()
    {
        return pref.getString(productId, "");
    }

    public void setProductId(String id)
    {
        editor.putString(productId, id).commit();
    }

    public void setItemDetails(String details)
    {
        editor.putString(TransactionDetails, details).commit();
    }

    public String getItemDetail()
    {
        return pref.getString(TransactionDetails, "");
    }


}
