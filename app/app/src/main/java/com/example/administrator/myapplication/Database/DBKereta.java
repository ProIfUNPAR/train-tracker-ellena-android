package com.example.administrator.myapplication.Database;

import android.database.Cursor;
import android.util.Log;

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
        ArrayList<Kereta> trains= new ArrayList<Kereta>();

        String query="SELECT * FROM track";
        Cursor cursor = mDBHelper.getmDataBase().rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {

                /*System.out.print(" "+cursor.getString(1)); //nama stasiun
                System.out.println(" "+cursor.getString(2)); // jadwal dan track, stasiun msh dlm bentuk id*/

                ArrayList<String> routes=new ArrayList<String>();
                ArrayList<String> waktuBerangkat=new  ArrayList<String>();
                ArrayList<String> waktuTiba=new  ArrayList<String>();

                String trackRaw = cursor.getString(2);
                String s = "";

                String schedule = "";
                boolean flag = true;
              //  System.out.println("Raw : " + trackRaw);
              //  System.out.print("Route : ");
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
                                //route += stasiunCur.getString(1);
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
                           // schedule += ",";
                            waktuTiba.add(schedule);
                            schedule ="";
                           // route += " -> ";
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
                //System.out.println(route);
                //System.out.println(schedule);



                //System.out.println(" "+cursor.getString(2)); // jadwal dan track, stasiun msh dlm bentuk id

            } while (cursor.moveToNext());

        }

        cursor.close();
        mDBHelper.close();

    }


    public Kereta getKeretaByName(String name){
        Kereta result=null;
        for (int i=0;i<this.listTrains.size();i++){
            if(this.listTrains.get(i).getNama().equals(name)){
                result=this.listTrains.get(i);
                break;
            }
        }
        return result;
    }


    /*public ArrayList<String> getNamaKereta(){
        mDBHelper.openDataBase();
        ArrayList<String> nama = new ArrayList<String>();
        String query = "SELECT nama FROM track";

    }*/
}
