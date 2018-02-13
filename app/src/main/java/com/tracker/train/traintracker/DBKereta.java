package com.tracker.train.traintracker;

import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by user on 2/11/2018.
 */

public class DBKereta {
    private DatabaseHelper mDBHelper;

    public DBKereta(DatabaseHelper mDBHelper) {
        this.mDBHelper = mDBHelper;
    }

    public ArrayList<String> getAllKereta(){
        mDBHelper.openDataBase();

        String query="SELECT * FROM track";
        Cursor cursor = mDBHelper.getmDataBase().rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                //belum beres, blm bikin kelas kereta
                /*System.out.print(" "+cursor.getString(1)); //nama stasiun
                System.out.println(" "+cursor.getString(2)); // jadwal dan track, stasiun msh dlm bentuk id*/
                String trackRaw = cursor.getString(2);
                String s = "";
                String route = "Route : ";
                String schedule = "Jadwal: ";
                boolean flag = true;
                System.out.println("Raw : " + trackRaw);
                System.out.print("Route : ");
                for(int i = 0; i < trackRaw.length(); i++){
                    if(flag) {
                        if(trackRaw.charAt(i) != ';'){
                            s += trackRaw.charAt(i);
                        }
                        else{
                            //System.out.print("The ID is: " + s + ".");
                            int id = Integer.parseInt(s);
                            String stasiunQuery = "SELECT * FROM stasiun WHERE id = " + id;
                            Cursor stasiunCur = mDBHelper.getmDataBase().rawQuery(stasiunQuery, null);
                            if(stasiunCur.moveToFirst()) {
                                route += stasiunCur.getString(1);
                                flag = false;
                                stasiunCur.close();
                            }
                            else{
                                System.out.println("wtf error " + id);
                            }
                        }
                    }
                    else{
                        if(trackRaw.charAt(i) == ','){
                            flag = true;
                            s = "";
                            schedule += ",";
                            route += " -> ";
                        }
                        else{
                            if(trackRaw.charAt(i) != ';'){
                                schedule += trackRaw.charAt(i);
                            }
                            else{
                                schedule += "_";
                            }
                        }
                    }
                }
                System.out.println(route);
                System.out.println(schedule);
            } while (cursor.moveToNext());
        }

        cursor.close();
        mDBHelper.close();

        return null;
    }
}
