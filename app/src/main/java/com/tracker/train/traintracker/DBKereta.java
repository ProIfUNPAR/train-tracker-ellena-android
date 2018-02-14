package com.tracker.train.traintracker;

import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by user on 2/11/2018.
 */

public class DBKereta {
    private DatabaseHelper mDBHelper;

    public DBKereta(DatabaseHelper mDBHelper) {
        this.mDBHelper = mDBHelper;
    }

    public ArrayList<String> getAllKereta(){
        mDBHelper.openDataBase();

        String query="SELECT * FROM track";
        Cursor cursor = mDBHelper.getmDataBase().rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                //belum beres, blm bikin kelas kereta
                System.out.print(" "+cursor.getString(1)); //nama stasiun
                System.out.println(" "+cursor.getString(2)); // jadwal dan track, stasiun msh dlm bentuk id

            } while (cursor.moveToNext());
        }

        cursor.close();
        mDBHelper.close();

        return null;
    }
}
