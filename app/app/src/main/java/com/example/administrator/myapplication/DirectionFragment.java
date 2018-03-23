package com.example.administrator.myapplication;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Switch;

import com.example.administrator.myapplication.Database.DBKereta;
import com.example.administrator.myapplication.Database.DBStasiun;
import com.example.administrator.myapplication.Database.DatabaseHelper;
import com.example.administrator.myapplication.Database.Kereta;
import com.example.administrator.myapplication.Database.Stasiun;

import java.util.ArrayList;

/**
 * Created by user on 3/23/2018.
 */

public class DirectionFragment extends Fragment {
    protected Spinner keretaSpinner, asalSpinner, tujuanSpinner;
    protected Button searchBtn;
    protected Switch alarmSwitch;

    protected ArrayList<Stasiun> asalList;
    protected ArrayList<Stasiun> tujuanList;
    protected ArrayList<Kereta> kereta;
    protected ArrayList<Stasiun> stasiun;
    protected ArrayList<String> trackList;

    protected DatabaseHelper mDBHelper;
    protected DBKereta dbKereta;
    protected DBStasiun dbStasiun;

    public DirectionFragment() {
    }


    public static DirectionFragment newInstance(){
        DirectionFragment directionFragment=new DirectionFragment();
        return directionFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
       View view=inflater.inflate(R.layout.activity_maps,container,false);
       this.searchBtn=view.findViewById(R.id.btn_search);
       this.alarmSwitch=view.findViewById(R.id.sw_alarm);
       this.keretaSpinner=view.findViewById(R.id.kereta_list);
       this.asalSpinner=view.findViewById(R.id.asal_list);
       this.tujuanSpinner=view.findViewById(R.id.tujuan_list);


        this.asalList= new ArrayList<Stasiun>();
        this.tujuanList = new ArrayList<Stasiun>();
        this.kereta=new ArrayList<Kereta>();
        this.trackList=new ArrayList<String>();

        this.mDBHelper=new DatabaseHelper(this.getContext());
        this.dbKereta=new DBKereta(this.mDBHelper);
        this.kereta=this.dbKereta.getListTrains();

        this.dbStasiun=new DBStasiun(this.mDBHelper);
        this.stasiun=this.dbStasiun.getListStasiun();

       return view;
    }


}
