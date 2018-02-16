package com.tracker.train.traintracker;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Asus on 16/02/2018.
 */

public class StasiunAdapter extends ArrayAdapter<Stasiun> implements SpinnerAdapter{

    ArrayList<Stasiun> stasiunList;
    Test activity;
    boolean isAwal;


    public StasiunAdapter(@NonNull Test context, @NonNull ArrayList<Stasiun> objects, boolean isAwal) {
        super(context, 0, objects);
        this.activity = context;
        this.stasiunList = objects;
        this.isAwal = isAwal;
    }

    @NonNull
    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null){
            if(isAwal) {
                convertView = LayoutInflater.from(this.activity).inflate(R.layout.awal_spinner, parent, false);
            }
            else{
                convertView = LayoutInflater.from(this.activity).inflate(R.layout.akhir_spinner, parent, false);
            }
        }
        Stasiun stasiun = this.stasiunList.get(position);
        //Print latitude longitude
        System.out.println("Lat = " + stasiun.getLatitude());
        System.out.println("Long = " + stasiun.getLongitude());
        if(isAwal) {
            TextView awalText = convertView.findViewById(R.id.stasiun_awal_text);
            awalText.setText(stasiun.getNama());
        }
        else{
            TextView akhirText = convertView.findViewById(R.id.stasiun_akhir_text);
            akhirText.setText(stasiun.getNama());
        }
        return convertView;
    }
}
