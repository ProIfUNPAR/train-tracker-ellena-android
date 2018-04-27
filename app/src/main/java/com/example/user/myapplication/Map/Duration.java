package com.example.user.myapplication.Map;

import android.util.Log;

/**
 * Created by GeneralDevil X on 2/15/2018.
 */

public class Duration {

    public String calculateTime(double speed , double distance){
        int jam = 0, menit;
        String hasil = new String();

        double time =((distance/1000) / speed) * 60;
        menit = (int) time;

        if(menit >= 60) {
            jam = menit / 60;
            menit = menit % 60;
        }
        hasil = jam + " jam " +  menit + " menit";
        return hasil;
    }
}
