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

    public ArrayList<Stasiun> getAllStasiun(){

        ArrayList<Stasiun> listStasiun=new ArrayList<Stasiun>();
        mDBHelper.openDataBase();

        //String query="SELECT * FROM stasiun";

        String query="SELECT stasiun.nama,kota.namaKota,stasiun.latitude,stasiun.longitude FROM stasiun INNER JOIN kota ON kota.idKota=stasiun.idKota";
        Cursor cursor = mDBHelper.getmDataBase().rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {

                String namaStasiun=""+cursor.getString(0);
                String kota=""+cursor.getString(1);
                double latitude=Double.parseDouble(cursor.getString(2)+"");
                double longitude=Double.parseDouble(cursor.getString(3)+"");

                Stasiun stasiun=new Stasiun(namaStasiun,kota,latitude,longitude);
                listStasiun.add(stasiun);

               /** System.out.print(" "+cursor.getString(0)); //nama stasiun
                System.out.print(" "+cursor.getString(1)); //id kota, harus di join buat dapetin nama kota
                System.out.print(" "+cursor.getString(2));// latitude
                System.out.println(" "+cursor.getString(3)); //longitude
                */
            } while (cursor.moveToNext());
        }

        cursor.close();
        mDBHelper.close();
        return listStasiun;
    }
}
