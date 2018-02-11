package com.tracker.train.traintracker;

import java.util.ArrayList;

/**
 * Created by user on 2/11/2018.
 */

public class Kereta {
    private String nama;
    private ArrayList<String> trackDanJadwal;

    public Kereta(String nama, ArrayList<String> trackDanJadwal) {
        this.nama = nama;
        this.trackDanJadwal = trackDanJadwal;
    }

    public ArrayList<String> getTrackDanJadwal() {
        return trackDanJadwal;
    }

    public String getNama() {
        return nama;
    }

}
