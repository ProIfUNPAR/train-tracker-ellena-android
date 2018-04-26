package com.example.user.myapplication.Fragment;

import com.example.user.myapplication.Database.Stasiun;

/**
 * Created by Asus on 09/04/2018.
 */

public interface FragmentListener {
    void setSpeedETA(double jarak, String time, double jarak2, String time2, double speed, Stasiun stasiunAwal,
                     Stasiun stasiunAkhir, Stasiun stasiunSelanjutnya);
}
