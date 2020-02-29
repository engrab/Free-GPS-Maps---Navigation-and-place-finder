package com.megafreeapps.free.gps.navigation.speedometer.compass.Database;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import com.megafreeapps.free.gps.navigation.speedometer.compass.Models.Favourite_Places_Model;
import com.megafreeapps.free.gps.navigation.speedometer.compass.R;

public class DbHelper
{
    private Context context;
    private SQLiteDatabase db;
    private Resources resources;

    public DbHelper(Context context)
    {
        try
        {
            this.context = context;
            resources = context.getResources();
            db = context.openOrCreateDatabase(resources.getString(R.string.TxtDataBaseName), Context.MODE_PRIVATE, null);
            String VARCHAR, CreateTable;
            CreateTable = resources.getString(R.string.TxtCreateTable);
            VARCHAR = resources.getString(R.string.TxtVARCHAR);

            //********************************* Tbl Countries **************************************
            db.execSQL(CreateTable + resources.getString(R.string.TxtTblPlaces) +
                    "(" + resources.getString(R.string.TxtDbAddress) + VARCHAR + "," +
                    resources.getString(R.string.TxtDbName) + VARCHAR + "," +
                    resources.getString(R.string.TxtDbLatitude) + VARCHAR + "," +
                    resources.getString(R.string.TxtDbLongitude) + VARCHAR + "," +
                    resources.getString(R.string.TxtDbDate) + VARCHAR + "," +
                    resources.getString(R.string.TxtDbTime) + VARCHAR + ")");

        }
        catch (Exception ignored)
        {
        }
    }

    //************************************************ Insert Data *****************************************
    public boolean InsertData(ContentValues values, String Table)
    {
        boolean Status = false;
        try
        {
            long Result = db.insert(Table, null, values);
            if (Result > 0)
            {
                Status = true;
            }
        }
        catch (Exception ignored)
        {
        }

        return Status;
    }

    //************************************************* Delete Table Data **********************************
    public void DeleteTableData(String Table)
    {
        try
        {
            db.delete(Table, null, null);
        }
        catch (Exception ignored)
        {
        }
    }

    public List<Favourite_Places_Model> GetFavouritePlacesList()
    {
        List<Favourite_Places_Model> list = null;
        Cursor cursor = null;
        try
        {
            cursor = db.rawQuery("select * from " + resources.getString(R.string.TxtTblPlaces), null);
            if (cursor != null && cursor.getCount() > 0)
            {
                list = new ArrayList<>();
                cursor.moveToFirst();
                int AddressIndex = cursor.getColumnIndex(resources.getString(R.string.TxtDbAddress));
                int NameIndex = cursor.getColumnIndex(resources.getString(R.string.TxtDbName));
                int DateIndex = cursor.getColumnIndex(resources.getString(R.string.TxtDbDate));
                int LatitudeIndex = cursor.getColumnIndex(resources.getString(R.string.TxtDbLatitude));
                int LongitudeIndex = cursor.getColumnIndex(resources.getString(R.string.TxtDbLongitude));
                int TimeIndex = cursor.getColumnIndex(resources.getString(R.string.TxtDbTime));
                while (!cursor.isAfterLast())
                {
                    list.add(new Favourite_Places_Model(cursor.getString(AddressIndex), cursor.getString(NameIndex),
                            cursor.getString(DateIndex), cursor.getString(TimeIndex), cursor.getString(LatitudeIndex),
                            cursor.getString(LongitudeIndex)));
                    cursor.moveToNext();
                }
            }
        }
        catch (Exception ignored)
        {
        }
        finally
        {
            if (cursor != null)
            {
                cursor.close();
            }
        }
        return list;
    }

    public void CloseDatabase()
    {
        try
        {
            if (db != null && db.isOpen())
            {
                db.close();
            }
        }
        catch (Exception ignored)
        {
        }
    }
}
