package com.tracker.train.traintracker;

import android.database.Cursor;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by user on 2/11/2018.
 */

public class DBStasiun {
    private DatabaseHelper mDBHelper;

    public DBStasiun(DatabaseHelper mDBHelper) {
        this.mDBHelper = mDBHelper;
    }

    public ArrayList<String> getAllStasiun(){

       mDBHelper.openDataBase();

        String query="SELECT * FROM stasiun";
        Cursor cursor = mDBHelper.getmDataBase().rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                //belum beres, blm bikin kelas stasiun
                System.out.print(" "+cursor.getString(1)); //nama stasiun
                System.out.print(" "+cursor.getString(2)); //id kota, harus di join buat dapetin nama kota
                System.out.print(" "+cursor.getString(3));// latitude
                System.out.println(" "+cursor.getString(4)); //longitude
            } while (cursor.moveToNext());
        }

        cursor.close();
        mDBHelper.close();
        return null;
    }
}
