package com.example.user.myapplication.Fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;

import com.example.user.myapplication.Database.Stasiun;
import com.example.user.myapplication.Map.Distance;
import com.example.user.myapplication.Map.Duration;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

/**
 * Created by Asus on 19/04/2018.
 */

public class MyLocationListener extends BroadcastReceiver implements LocationListener {

    double latitude, longitude, speed;
    Distance jarak;
    Duration dur;
    DirectionFragment dirFragment;
    ArrayList<Marker> markerList;
    GoogleMap mMap;
    static int i = 0;
    static ArrayList<Stasiun> awalList;
    static ArrayList<Stasiun> akhirList;
    static double jarakKeStasiunTerdekat;

    public MyLocationListener(DirectionFragment d, GoogleMap mmap){
        this.dirFragment = d;
        this.jarak = dirFragment.getJarak();
        this.dur = dirFragment.getDuration();
        this.mMap = mmap;
    }

    @Override
    public void onLocationChanged(Location location) {
        markerList = dirFragment.getMarkerList();
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        double jarakResult;

        speed = location.getSpeed() * 3.6;
        Stasiun stasiunAwal = dirFragment.getStasiunAwal();
        Stasiun stasiunAkhir = dirFragment.getStasiunAkhir();

        jarakResult = 0;

        if (stasiunAkhir != null) {

            if(jarakKeStasiunTerdekat/1000 < 2 && (i - 1 < akhirList.size() || (i - 1 < akhirList.size() && !(akhirList.get(i - 1).equals(stasiunAkhir))))){
                i++;
            }
            if(i < markerList.size()) {
                jarakKeStasiunTerdekat = jarak.getDistance(latitude, longitude, markerList.get(i).getPosition().latitude, markerList.get(i).getPosition().longitude);
            }
            jarakResult = jarakKeStasiunTerdekat;
            for (int j = i;j< markerList.size()-1;j++){
                jarakResult += jarak.getDistance(markerList.get(j).getPosition().latitude,markerList.get(j).getPosition().longitude,
                        markerList.get(j+1).getPosition().latitude,markerList.get(j+1).getPosition().longitude);
            }
        }
        Stasiun stasiunSelanjutnya;
        if(markerList.size() > 0 && i < akhirList.size()) {
            stasiunSelanjutnya = akhirList.get(i - 1);
        }
        else if(markerList.size() <= 0){
            stasiunSelanjutnya = new Stasiun("kos" +
                    "" +
                    "" +
                    "" +
                    "" +
                    "ong", "koosng", 0, 0);
        }
        else{
            stasiunSelanjutnya = dirFragment.getStasiunSelanjutnya(akhirList.size()-1);
        }

        String time;
        String time2;
        if (speed != 0) {
            time = dur.calculateTime(speed, jarakResult);
            time2 = dur.calculateTime(speed, jarakKeStasiunTerdekat);
        } else {
            time = "Tidak bergerak";
            time2 = "Tidak bergerak";
        }
        Log.d("Distance2", String.format("%.2f", jarakResult));
        Log.d("Distance3", String.format("%.2f", jarakKeStasiunTerdekat));
        Log.d("Time", time);

        dirFragment.listener.setSpeedETA(jarakResult, time, jarakKeStasiunTerdekat, time2, speed, stasiunAwal, stasiunAkhir, stasiunSelanjutnya);

        if(jarakResult>0 && jarakResult/1000<=5 && dirFragment.isAlarmSet) {
            DirectionFragment.jenisAlarm = 1;
            dirFragment.isAlarm2Set = true;
            dirFragment.startAlarm();
            dirFragment.isAlarmSet = false;
        }
        if(jarakResult>0 && jarakResult/1000<=1 && dirFragment.isAlarm2Set){
            DirectionFragment.jenisAlarm = 0;
            dirFragment.isAlarm2Set = false;
            dirFragment.startAlarm();
        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().matches("android.location.PROVIDERS_CHANGED")) {
            LocationManager locman = (LocationManager) dirFragment.getContext().getSystemService(Context.LOCATION_SERVICE);
            if(!locman.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                dirFragment.noGPSAlert();
            }
        }
        if (intent.getAction().matches("android.net.conn.CONNECTIVITY_CHANGE")) {

            if(!this.isNetworkOn(dirFragment.getContext())){
                dirFragment.noInternetAlert();
            }
        }
    }
    public boolean isNetworkOn(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return (netInfo != null && netInfo.isConnected());
    }
}