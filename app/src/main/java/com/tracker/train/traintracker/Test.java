package com.tracker.train.traintracker;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class Test extends AppCompatActivity {
    private DatabaseHelper mDBHelper;
    //private SQLiteDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        //membuat class yang menghandle data base
        mDBHelper = new DatabaseHelper(this);


        /**
        //mengambil record dari tabel provinsi
        String query="SELECT * FROM stasiun";
        Cursor cursor = mDBHelper.getmDataBase().rawQuery(query, null);
        //melakukan iterasi untuk setiap record
        if (cursor.moveToFirst()) {
            do {
                System.out.print(cursor.getString(0));
                System.out.print(" "+cursor.getString(1));
                System.out.print(" "+cursor.getString(2));
                System.out.print(" "+cursor.getString(3));
                System.out.println(" "+cursor.getString(4));
            } while (cursor.moveToNext());
        }

        cursor.close();
        mDBHelper.close();
         **/


       DBKereta dbKereta=new DBKereta(mDBHelper);
       ArrayList<Kereta> kereta=dbKereta.getAllKereta();
        for (int i=0;i<kereta.size();i++){
            System.out.print(kereta.get(i).getNama()+" ");
            ArrayList<String> temp=kereta.get(i).getWaktuTiba();
            for (int j=0;j<temp.size();j++){
                System.out.print(temp.get(j)+" ");
            }
            System.out.println();
        }


        DBStasiun dbStasiun=new DBStasiun(mDBHelper);
        ArrayList<Stasiun> stasiun=dbStasiun.getAllStasiun();
        for (int i=0;i<stasiun.size();i++){
            System.out.print(stasiun.get(i).getNama()+" ");
            System.out.print(stasiun.get(i).getKota()+" ");
            System.out.print(stasiun.get(i).getLatitude()+" ");
            System.out.println(stasiun.get(i).getLongitude());
        }

    }
}
