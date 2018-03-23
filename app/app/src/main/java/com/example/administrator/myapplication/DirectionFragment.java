package com.example.administrator.myapplication;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Switch;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;


import com.example.administrator.myapplication.Database.DBKereta;
import com.example.administrator.myapplication.Database.DBStasiun;
import com.example.administrator.myapplication.Database.DatabaseHelper;
import com.example.administrator.myapplication.Database.Kereta;
import com.example.administrator.myapplication.Database.Stasiun;
import com.google.android.gms.maps.SupportMapFragment;

import java.util.ArrayList;

/**
 * Created by user on 3/23/2018.
 */

public class DirectionFragment extends Fragment implements View.OnClickListener,OnMapReadyCallback{
    protected Spinner keretaSpinner, asalSpinner, tujuanSpinner;
    protected Button searchBtn;
    protected Switch alarmSwitch;

    protected Kereta selectedKereta;

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

       this.searchBtn.setOnClickListener(this);

        this.asalList= new ArrayList<Stasiun>();
        this.tujuanList = new ArrayList<Stasiun>();
        this.kereta=new ArrayList<Kereta>();
        this.trackList=new ArrayList<String>();

        this.mDBHelper=new DatabaseHelper(this.getContext());
        this.dbKereta=new DBKereta(this.mDBHelper);
        this.kereta=this.dbKereta.getListTrains();

        this.dbStasiun=new DBStasiun(this.mDBHelper);
        this.stasiun=this.dbStasiun.getListStasiun();

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        ArrayList<String> namaKereta = new ArrayList<String>();


        for (int i = 0; i < kereta.size(); i++) {
            namaKereta.add(kereta.get(i).getNama());
        }

        ArrayAdapter<Kereta> keretaArrayAdapter = new ArrayAdapter<Kereta>(getContext(), android.R.layout.simple_spinner_item, kereta);
        keretaSpinner.setAdapter(keretaArrayAdapter);

        keretaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String nama = adapterView.getItemAtPosition(i).toString();
                deleteList(asalList);
                for (int x = 0; x < kereta.size(); x++) {
                    if (kereta.get(i).getNama().equals(nama)) {
                        selectedKereta = kereta.get(i);
                        trackList = selectedKereta.getTrack();
                        break;
                    }
                }
                for (int x = 0; x < trackList.size() - 1; x++) {
                    asalList.add(dbStasiun.getStasiunByName(trackList.get(x)));
                }
                ArrayAdapter<Stasiun> asalArrayAdapter = new ArrayAdapter<Stasiun>(getContext(), android.R.layout.simple_spinner_item, asalList);
                asalSpinner.setAdapter(asalArrayAdapter);
                asalSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        deleteList(tujuanList);

                        for (int x = 0; x < trackList.size(); x++) {
                            tujuanList.add(dbStasiun.getStasiunByName(trackList.get(x)));

                        }
                        int x = 0;
                        while (x <= asalSpinner.getSelectedItemPosition()) {
                            tujuanList.remove(0);
                            x++;
                        }

                        ArrayAdapter<Stasiun> tujuanArrayAdapter = new ArrayAdapter<Stasiun>(getContext(), android.R.layout.simple_spinner_item, tujuanList);
                        tujuanSpinner.setAdapter(tujuanArrayAdapter);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

       return view;
    }

    private void deleteList(ArrayList<Stasiun> list){
        while(!list.isEmpty()){
            list.remove(0);
        }
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==this.searchBtn.getId()){

        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

    }
}
