package com.example.user.myapplication.Database;

import android.database.Cursor;

import java.util.ArrayList;

/**
 * Created by user on 2/11/2018.
 */

public class DBKereta {
    private DatabaseHelper mDBHelper;
    private ArrayList<Kereta> listTrains;


    public DBKereta(DatabaseHelper mDBHelper) {
        this.mDBHelper = mDBHelper;
        this.listTrains= new ArrayList<Kereta>();
        this.getTrainsFromDB();
    }

    public ArrayList<Kereta> getListTrains() {
        return this.listTrains;
    }

    private void getTrainsFromDB(){
        mDBHelper.openDataBase();

        String query="SELECT * FROM track";
        Cursor cursor = mDBHelper.getmDataBase().rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                ArrayList<String> routes=new ArrayList<String>();
                ArrayList<String> waktuBerangkat=new  ArrayList<String>();
                ArrayList<String> waktuTiba=new  ArrayList<String>();

                String trackRaw = cursor.getString(2);
                String s = "";

                String schedule = "";
                boolean flag = true;

                for(int i = 0; i < trackRaw.length(); i++){
                    if(flag) {
                        if(trackRaw.charAt(i) != ';'){
                            s += trackRaw.charAt(i);
                        }
                        else{

                            int id = Integer.parseInt(s);
                            String stasiunQuery = "SELECT * FROM stasiun WHERE id = " + id;
                            Cursor stasiunCur = mDBHelper.getmDataBase().rawQuery(stasiunQuery, null);
                            if(stasiunCur.moveToFirst()) {
                                routes.add(stasiunCur.getString(1));
                                flag = false;
                                stasiunCur.close();
                            }
                            else{
                                System.out.println("error" + id);
                            }
                        }
                    }
                    else{
                        if(trackRaw.charAt(i) == ','){
                            flag = true;
                            s = "";
                            waktuTiba.add(schedule);
                            schedule ="";
                        }
                        else{
                            if(trackRaw.charAt(i) != ';'){
                                schedule += trackRaw.charAt(i);

                            }
                            else{
                                waktuBerangkat.add(schedule);
                                schedule = "";
                            }
                        }
                    }

                    if(i==trackRaw.length()-1){
                        waktuTiba.add("-");
                    }
                }
                String namaKereta=""+cursor.getString(1);
                Kereta kereta=new Kereta(namaKereta,routes,waktuBerangkat,waktuTiba);
                this.listTrains.add(kereta);

            } while (cursor.moveToNext());

        }

        cursor.close();
        mDBHelper.close();

    }
}
