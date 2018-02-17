package com.tracker.train.traintracker;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class Test extends AppCompatActivity{
    private DatabaseHelper mDBHelper;
    //private SQLiteDatabase mDb;
    private Spinner keretaSpinner, asalSpinner, tujuanSpinner;
    private ArrayList<Kereta> kereta;
    private ArrayList<Stasiun> stasiun;
    private ArrayList<String> trackList;
    private Kereta selectedKereta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        //membuat class yang menghandle data base
        mDBHelper = new DatabaseHelper(this);

        //inisialize spinner
        this.keretaSpinner = (Spinner) findViewById(R.id.kereta_list);
        this.asalSpinner = (Spinner) findViewById(R.id.asal_list);
        this.tujuanSpinner = (Spinner) findViewById(R.id.tujuan_list);

        DBKereta keretaDB = new DBKereta(mDBHelper);
        kereta = keretaDB.getListTrains();

        final DBStasiun dbStasiun=new DBStasiun(mDBHelper);


        ArrayList<String> namaKereta = new ArrayList<String>();

        for(int i = 0; i < kereta.size(); i++){
            namaKereta.add(kereta.get(i).getNama());
        }

        ArrayAdapter<Kereta> keretaArrayAdapter = new ArrayAdapter<Kereta>(this, android.R.layout.simple_spinner_item, kereta);
        //ArrayAdapter<String> keretaArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, namaKereta);
        keretaSpinner.setAdapter(keretaArrayAdapter);
        keretaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String nama = adapterView.getItemAtPosition(i).toString();
                ArrayList<Stasiun> asalList = new ArrayList<Stasiun>();
                for(int x = 0; x < kereta.size(); x++){
                    if(kereta.get(i).getNama().equals(nama)){
                        selectedKereta = kereta.get(i);
                        trackList = selectedKereta.getTrack();
                        break;
                    }
                }
                for(int x = 0; x < trackList.size() - 1; x++){
                    asalList.add(dbStasiun.getStasiunByName(trackList.get(x)));
                    //Print latitude longitude
                    System.out.println(asalList.get(x).getLatitude()+" "+asalList.get(x).getLongitude());
                    System.out.println("end1");
                }
                ArrayAdapter<Stasiun> asalArrayAdapter = new ArrayAdapter<Stasiun>(Test.this, android.R.layout.simple_spinner_item, asalList);
                asalSpinner.setAdapter(asalArrayAdapter);
                asalSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        ArrayList<Stasiun> tujuanList = new ArrayList<Stasiun>();
                        for(int x = 0; x < trackList.size(); x++){
                            tujuanList.add(dbStasiun.getStasiunByName(trackList.get(x)));
                            //Print latitude longitude
                            System.out.println(tujuanList.get(x).getLatitude()+" "+tujuanList.get(x).getLongitude());
                            System.out.println("end2");
                        }
                        int x = 0;
                        while(x <= asalSpinner.getSelectedItemPosition()){
                            tujuanList.remove(0);
                            x++;
                        }
                        ArrayAdapter<Stasiun> tujuanArrayAdapter = new ArrayAdapter<Stasiun>(Test.this, android.R.layout.simple_spinner_item, tujuanList);
                        tujuanSpinner.setAdapter(tujuanArrayAdapter);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

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


       /*DBKereta dbKereta=new DBKereta(mDBHelper);
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
        }*/

    }

    /*@Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch(adapterView.getId()){
            case R.id.kereta_list:

            case R.id.asal_list:
                ArrayAdapter<String> asalAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, trackList);
                asalSpinner.setAdapter(asalAdapter);

            case R.id.tujuan_list:
                ArrayList<String> tujuanList = trackList;
                int x = 0;
                while(!tujuanList.get(x).equals(selectedKereta.getNama())){
                    tujuanList.remove(x);
                    x++;
                }
                ArrayAdapter<String> tujuanAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, tujuanList);
                tujuanSpinner.setAdapter(tujuanAdapter);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }*/
}
