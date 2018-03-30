package com.example.user.myapplication.Map;

/**
 * Created by GeneralDevil X on 2/15/2018.
 */

public class Duration {

    public String calculateTime(double speed , double distance){
        int jam = 0, menit;
        String hasil = new String();

        double time =(distance / speed) * 60;
        menit = (int) time;

        /*while(menit >60){
            jam += 1;
            menit -= 60;
            hasil = jam + "jam ";
        }*/
        jam = menit % 60;
        menit = menit - (60 * jam);

        hasil = jam + " jam " +  menit + " menit";
        return hasil;
    }
}
