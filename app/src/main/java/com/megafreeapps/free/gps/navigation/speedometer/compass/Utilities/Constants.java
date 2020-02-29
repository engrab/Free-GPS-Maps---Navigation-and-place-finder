package com.megafreeapps.free.gps.navigation.speedometer.compass.Utilities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class Constants {

    public static void ShowInfo(Context context, String Title, String Message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(Title);
        builder.setMessage(Message);
        builder.setCancelable(true);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create();
        builder.show();
    }

    public static final int Service_SUCCESS_RESULT = 0;
    public static final int Service_FAILURE_RESULT = 1;
    private static final String APP_PACKAGE_NAME = "com.megaminds.gps.navigation.live.traffic.route.placefinder";
    public static final String RESULT_DATA_KEY = APP_PACKAGE_NAME + ".RESULT_DATA_KEY";
    public static final String LOCATION_DATA_EXTRA = APP_PACKAGE_NAME + ".LOCATION_DATA_EXTRA";
    public static final String RECEIVER = APP_PACKAGE_NAME + ".RECEIVER";
    public static final String PRODUCT_ID = "com.megaminds.gps.navigation.live.traffic.route.placefinder";
}
