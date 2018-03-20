package com.tracker.mapdatabase;

import java.util.ArrayList;

/**
 * Created by user on 2/11/2018.
 */

public class Kereta {
    private String nama;
    private ArrayList<String> track,waktuBerangkat,waktuTiba;

    public Kereta(String nama, ArrayList<String> track,ArrayList<String> waktuBerangkat,ArrayList<String> waktuTiba) {
        this.nama = nama;
        this.track = track;
        this.waktuBerangkat=waktuBerangkat;
        this.waktuTiba=waktuTiba;
    }

    public ArrayList<String> getTrack() {
        return track;
    }



    public ArrayList<String> getWaktuBerangkat() {
        return waktuBerangkat;
    }

    public ArrayList<String> getWaktuTiba() {
        return waktuTiba;
    }

    public String getNama() {
        return nama;
    }

    @Override
    public String toString(){
        return this.getNama();
    }

}
