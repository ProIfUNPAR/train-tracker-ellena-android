package com.tracker.train.traintracker;

/**
 * Created by user on 2/11/2018.
 */

public class Stasiun {
    private String kota,nama;
    private double latitude,longitude;

    public Stasiun(String nama, String kota, double latitude, double longitude) {
        this.kota = kota;
        this.nama = nama;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getKota() {
        return kota;
    }

    public String getNama() {
        return nama;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    @Override
    public String toString(){
        return this.getNama();
    }
}
