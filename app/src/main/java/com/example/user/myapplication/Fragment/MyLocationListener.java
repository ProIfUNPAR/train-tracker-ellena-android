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
    Marker mark;
    DirectionFragment dirFragment;
    ArrayList<Marker> markerList;
    GoogleMap mMap;

    public MyLocationListener(DirectionFragment d, GoogleMap mmap){
        this.dirFragment = d;
        this.jarak = dirFragment.getJarak();
        this.dur = dirFragment.getDuration();
        this.mMap = mmap;
    }

    public MyLocationListener(){
        this.dirFragment = new DirectionFragment();
        this.jarak = dirFragment.getJarak();
        this.dur = dirFragment.getDuration();

    }

    @Override
    public void onLocationChanged(Location location) {
        int i = 1;
        markerList = dirFragment.getMarkerList();
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        double jarakResult;
        LatLng latlong = new LatLng(latitude, longitude);
        //mark = mMap.addMarker(new MarkerOptions().position(latlong));
        //if (mark != null) {
        //    mark.remove();/
            //editText.setText(String.format("%.2f",speed));
        //}
        speed = location.getSpeed() * 3.6;
        Stasiun stasiunAkhir = dirFragment.getStasiunAkhir();

        double jarakKeStasiunTerdekat = 0;
        jarakResult = 0;

        if (stasiunAkhir != null) {
            jarakKeStasiunTerdekat = jarak.getDistance(latitude,longitude,markerList.get(i).getPosition().latitude,markerList.get(i).getPosition().longitude);

            if(jarakKeStasiunTerdekat < 2){
                i++;
            }

            jarakResult = jarakKeStasiunTerdekat;
            for (int j = i;j< markerList.size()-1;j++){
                jarakResult += jarak.getDistance(markerList.get(j).getPosition().latitude,markerList.get(j).getPosition().longitude,
                        markerList.get(j+1).getPosition().latitude,markerList.get(j+1).getPosition().longitude);
            }
            //jarakResult = jarak.getDistance(latitude, longitude, stasiunAkhir.getLatitude(), stasiunAkhir.getLongitude()) / 1000;
        }
        //distanceView = findViewById(R.id.textView12);

        //double time = (jarakResult/speed)*60;
        String time;
        String time2;
        if (speed != 0) {
            time = dur.calculateTime(speed, jarakResult);
            time2 = dur.calculateTime(speed, jarakKeStasiunTerdekat);
        } else {
            time = "Not moving";
            time2 = "Not moving";
        }
        //timeView = findViewById(R.id.textView5);

        //Buat output
        //distanceView.setText(String.format("%.2f",jarakResult));
        //timeView.setText(String.format("%.2f",time));


        // mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlong, 15.5f));
        Log.d("Distance2", String.format("%.2f", jarakResult));
        Log.d("Distance3", String.format("%.2f", jarakKeStasiunTerdekat));
        Log.d("Time", time);

        dirFragment.listener.setSpeedETA(jarakResult, time, jarakKeStasiunTerdekat, time2, speed);

        if(jarakResult>0 && jarakResult/1000<=2 && dirFragment.isAlarmSet) {
            DirectionFragment.jenisAlarm = 1;
            dirFragment.startAlarm();

        }
        if(jarakResult>0 && jarakResult/1000<=0.5 && dirFragment.isAlarmSet){
            DirectionFragment.jenisAlarm = 0;
            dirFragment.startAlarm();
            dirFragment.isAlarmSet = false;
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
        Log.d("debug_provider", "aa");
        if (intent.getAction().matches("android.location.PROVIDERS_CHANGED")) {
            Log.d("debug_provider", "sasasaddasd");
            LocationManager locman = (LocationManager) dirFragment.getContext().getSystemService(Context.LOCATION_SERVICE);
            if(!locman.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                dirFragment.noGPSAlert();
            }
        }
        if (intent.getAction().matches("android.net.conn.CONNECTIVITY_CHANGE")) {
            Log.d("debug_provider", "sasad");

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