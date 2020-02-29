package com.megafreeapps.free.gps.navigation.speedometer.compass.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;


import com.megafreeapps.free.gps.navigation.speedometer.compass.Models.SavedPlacesModel;

import java.util.ArrayList;

public class SavedRouteDatabase {
    private static final String DATABASE_NAME = "saved_routes.db";
    private static final String DATABASE_TABLE = "result";
    private static final String ID = "`id`";
    private static final String START_LAT = "`start_lat`";
    private static final String START_LAN = "`start_lan`";
    private static final String END_LAT = "`end_lat`";
    private static final String END_LAN = "`end_lan`";
    private static final String START_ADDRESS = "`start_address`";
    private static final String END_ADDRESS = "`end_address`";

    private static final int VERSION = 1;
    private static SQLiteDatabase db;
    private DBOpenHelper helper;

    public SavedRouteDatabase(Context context) {
        this.helper = new DBOpenHelper(context, DATABASE_NAME, null, VERSION);
    }


    public void close() {
        if (db.isOpen()) {
            db.close();
        }
    }

    public void open() throws SQLiteException {
        try {
            db = this.helper.getWritableDatabase();
        } catch (SQLiteException e) {
            db = this.helper.getReadableDatabase();
        }
    }

    public void DeleteAllRow() {
        db.delete(DATABASE_TABLE, null, null);
    }

    public boolean isOpen() {
        return db.isOpen();
    }

    void clear() {
        db.execSQL("delete from result;");
    }

    public long insertResult(Double start_lat, Double Start_lan, Double end_lat, Double end_lan, String start_address, String end_address) {
        ContentValues newRow = new ContentValues();
        newRow.put(START_LAT, start_lat);
        newRow.put(START_LAN, Start_lan);
        newRow.put(END_LAT, end_lat);
        newRow.put(END_LAN, end_lan);
        newRow.put(START_ADDRESS, start_address);
        newRow.put(END_ADDRESS, end_address);


        long ss = db.insert(DATABASE_TABLE, null, newRow);
        return ss;
    }

    public void DeleteSavedRoute(int id) {
        db.execSQL("delete from result where id=" + id);
    }

    public ArrayList<SavedPlacesModel> GetSavedPlaces() {//String customId, PurchasePreference appPurchasePref
        ArrayList<SavedPlacesModel> saved_route_list = null;
        // boolean FirstAdmob = true;
        Cursor cursor = null;
        try {
            cursor = db.query(true, DATABASE_TABLE, new String[]{ID, START_LAT, START_LAN, END_LAT, END_LAN, START_ADDRESS, END_ADDRESS},
                    null, null, null, null, null, null);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                //  int ads_count = 1;
                saved_route_list = new ArrayList<>();
                while (!cursor.isAfterLast()) {
                    saved_route_list.add(new SavedPlacesModel(
                            cursor.getInt(cursor.getColumnIndex("id")),
                            cursor.getDouble(cursor.getColumnIndex("start_lat")),
                            cursor.getDouble(cursor.getColumnIndex("start_lan")),
                            cursor.getDouble(cursor.getColumnIndex("end_lat")),
                            cursor.getDouble(cursor.getColumnIndex("end_lan")),
                            cursor.getString(cursor.getColumnIndex("start_address")),
                            cursor.getString(cursor.getColumnIndex("end_address"))));
//
                    cursor.moveToNext();
//                    if (appPurchasePref.getItemDetail().equals("") && appPurchasePref.getProductId().equals("")) {
//                        ads_count++;
//                        if (ads_count % 5 == 0) {
//                            if (FirstAdmob) {
//                                FirstAdmob = false;
//                                translationList.add(new ModelTranslation(1,
//                                        "admob", "admob", "", "", "", "", "", "", 1, 1, "", true, false));
//                            } else {
//                                FirstAdmob = true;
//                                translationList.add(new ModelTranslation(1, "facebook", "facebook", "", "", "", "", "", "", 1, 1, "", true, false));
//                            }
//                        }
//                    }
                }
            }
        } catch (Exception ignored) {
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return saved_route_list;
    }

    private static class DBOpenHelper extends SQLiteOpenHelper {
        private static final String DATABASE_CREATE = "create table result (`id` INTEGER PRIMARY KEY AUTOINCREMENT, `start_lat` DOUBLE, `start_lan` " +
                "DOUBLE,`end_lat` DOUBLE, `end_lan` DOUBLE, `start_address` TEXT, `end_address` TEXT);";

        DBOpenHelper(Context context, String name, CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DATABASE_CREATE);
        }

        public void onUpgrade(SQLiteDatabase db, int _oldVersion, int _newVersion) {
            db.execSQL("DROP TABLE IF EXISTS result");
            onCreate(db);
        }
    }
}
