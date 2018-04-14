package com.example.user.myapplication.Fragment;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.LocationListener;
import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.example.user.myapplication.Alarm.AlarmNotificationReceiver;
import com.example.user.myapplication.Map.Distance;
import com.example.user.myapplication.Map.Duration;
import com.example.user.myapplication.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;


import com.example.user.myapplication.Database.DBKereta;
import com.example.user.myapplication.Database.DBStasiun;
import com.example.user.myapplication.Database.DatabaseHelper;
import com.example.user.myapplication.Database.Kereta;
import com.example.user.myapplication.Database.Stasiun;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Calendar;

import static android.content.Context.LOCATION_SERVICE;

/**
 * Created by user on 3/23/2018.
 */

public class DirectionFragment extends Fragment implements View.OnClickListener,OnMapReadyCallback,CompoundButton.OnCheckedChangeListener, LocationListener{
    protected Spinner keretaSpinner, asalSpinner, tujuanSpinner;
    protected Button searchBtn;
    protected Switch alarmSwitch;

    protected Kereta selectedKereta;

    protected ArrayList<Stasiun> asalList;
    protected ArrayList<Stasiun> tujuanList;
    protected ArrayList<Kereta> kereta;
    protected ArrayList<Stasiun> stasiun;
    protected ArrayList<String> trackList;
    protected ArrayList<Stasiun> stasiunListAll;
    protected ArrayList<Marker> markerList;

    protected DatabaseHelper mDBHelper;
    protected DBKereta dbKereta;
    protected DBStasiun dbStasiun;


    protected GoogleMap mMap;
    protected LocationManager locationManager;
    protected Marker mark;
    protected Marker awal, tujuan;
    protected Distance jarak;
    protected Duration dur;
    protected Polyline poly;
    protected boolean alarmFlag;
    double latitude, longitude, speed;

    AlarmManager manager;
    Intent alarmIntent;
    PendingIntent pendingIntent;
    double jarakResult;

    protected Stasiun stasiunAwal, stasiunAkhir;
    protected boolean isAlarmSet;

    FragmentListener listener;

    public DirectionFragment() {
    }


    public static DirectionFragment newInstance(){
        DirectionFragment directionFragment=new DirectionFragment();
        return directionFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
       View view=inflater.inflate(R.layout.maps_fragment,container,false);
       this.searchBtn=view.findViewById(R.id.btn_search);
       this.alarmSwitch=view.findViewById(R.id.sw_alarm);
       this.keretaSpinner=view.findViewById(R.id.kereta_list);
       this.asalSpinner=view.findViewById(R.id.asal_list);
       this.tujuanSpinner=view.findViewById(R.id.tujuan_list);

       this.searchBtn.setOnClickListener(this);
        this.alarmFlag=false;
        this.asalList= new ArrayList<Stasiun>();
        this.tujuanList = new ArrayList<Stasiun>();
        this.kereta=new ArrayList<Kereta>();
        this.trackList=new ArrayList<String>();
        this.markerList = new ArrayList<>();

        this.mDBHelper=new DatabaseHelper(this.getContext());
        this.dbKereta=new DBKereta(this.mDBHelper);
        this.kereta=this.dbKereta.getListTrains();

        this.dbStasiun=new DBStasiun(this.mDBHelper);
        this.stasiun=this.dbStasiun.getListStasiun();

        this.isAlarmSet = false;
        this.manager = (AlarmManager)getActivity().getSystemService(Context.ALARM_SERVICE);

        this.alarmSwitch.setOnCheckedChangeListener(this);


        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        this.jarak = new Distance();
        this.dur = new Duration();

        stasiunListAll = new ArrayList<Stasiun>();

        ArrayAdapter<Kereta> keretaArrayAdapter = new ArrayAdapter<Kereta>(getContext(), android.R.layout.simple_spinner_item, kereta);
        keretaSpinner.setAdapter(keretaArrayAdapter);

        keretaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String nama = adapterView.getItemAtPosition(i).toString();
                deleteList(asalList);
                if(!stasiunListAll.isEmpty()){
                    stasiunListAll.clear();
                }
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
                        tujuanSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                if(!stasiunListAll.isEmpty()){
                                    stasiunListAll.clear();
                                }
                                //stasiunListAll.addAll(asalSpinner);
                                Stasiun tujuanTemp = dbStasiun.getStasiunByName(tujuanSpinner.getSelectedItem().toString());
                                Log.d("stasiunname", tujuanTemp.getNama());
                                for(int j = asalSpinner.getSelectedItemPosition(); j < asalSpinner.getAdapter().getCount(); j++){
                                    Stasiun stasiunTemp = dbStasiun.getStasiunByName(asalSpinner.getItemAtPosition(j).toString());
                                    if(stasiunTemp.equals(tujuanTemp)){
                                        break;
                                    }
                                    stasiunListAll.add(stasiunTemp);
                                }
                                stasiunListAll.add(tujuanTemp);
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
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

        }

        if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            Log.d("debuggps", "Susa");
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
        }

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Log.d("debuggps", "Asus");
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        }
       return view;
    }

    private void deleteList(ArrayList<Stasiun> list){
        while(!list.isEmpty()){
            list.remove(0);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof FragmentListener){
            this.listener = (FragmentListener) context;
        }
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==this.searchBtn.getId()){
            double distance = 0;
            /** for(int i = 0; i < asalList.size() ; i++){
             stasiunAwal = asalList.get(i);
             if(stasiunAwal.getNama().equals(asalSpinner.getSelectedItem().toString())){
             break;
             }
             }
             for(int i = 0; i < tujuanList.size(); i++){
             stasiunAkhir = tujuanList.get(i);
             if(stasiunAkhir.getNama().equals(tujuanSpinner.getSelectedItem().toString())){
             break;
             }
             } */

            stasiunAwal=(Stasiun)asalSpinner.getSelectedItem();
            stasiunAkhir=(Stasiun)tujuanSpinner.getSelectedItem();

            Log.d("Stasiun", stasiunAwal.getLatitude() + " " + stasiunAwal.getLongitude());
            Log.d("Stasiun", stasiunAkhir.getLatitude() + " " + stasiunAkhir.getLongitude());

            //if(awal != null || tujuan != null){
                //awal.remove();
            mMap.clear();
            if(!markerList.isEmpty()){
                //poly.remove();
                Log.d("markerlog", "polylog");
            }
            //markerList.clear();
            while(!markerList.isEmpty()){
                //
                // markerList.get(0).remove();
                markerList.remove(0);
                Log.d("markerlog", "markerlog");
            }
            //tujuan.remove();
            //}

            LatLng koorAwal = new LatLng(stasiunAwal.getLatitude(),stasiunAwal.getLongitude());
            LatLng koorAkhir = new LatLng(stasiunAkhir.getLatitude(),stasiunAkhir.getLongitude());
            //awal = mMap.addMarker(new MarkerOptions().position(koorAwal));
            //tujuan = mMap.addMarker(new MarkerOptions().position(koorAkhir));

            for (int i = 0; i< stasiunListAll.size()-1;i++){
                LatLng first = new LatLng(stasiunListAll.get(i).getLatitude(),stasiunListAll.get(i).getLongitude());
                LatLng last = new LatLng(stasiunListAll.get(i+1).getLatitude(),stasiunListAll.get(i+1).getLongitude());
                Marker stasiun1;
                if(i == 0){
                    stasiun1 = mMap.addMarker(new MarkerOptions().position(first).title("Start"));
                    stasiun1.showInfoWindow();
                }
                else{
                    stasiun1 = mMap.addMarker(new MarkerOptions().position(first));
                }
                Marker stasiun2;
                if(i == stasiunListAll.size() - 2){
                    stasiun2 = mMap.addMarker(new MarkerOptions().position(last).title("Finish"));
                }
                else {
                    stasiun2 = mMap.addMarker(new MarkerOptions().position(last));
                }
                if(!markerList.isEmpty()){
                    markerList.get(i).remove();
                    markerList.remove(i);
                }
                markerList.add(stasiun1);
                markerList.add(stasiun2);

                poly = mMap.addPolyline(new PolylineOptions()
                        .add(first, last)
                        .width(5)
                        .color(Color.BLUE));
                poly.setVisible(true);
            }

            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            builder.include(koorAwal);
            builder.include(koorAkhir);
            LatLngBounds bounds = builder.build();

            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 50);
            mMap.animateCamera(cu);
            /** for(int i = 0; !temp2.getNama().equals(stasiunAkhir.getNama()); i++){
             distance += jarak.getDistance(temp1.getLatitude(), temp1.getLongitude(), temp2.getLatitude(), temp2.getLongitude());
             temp1 = temp2;
             temp2 = tujuanList.get(i);
             }
             */

            /*for(int i = 0; i <= tujuanList.indexOf(stasiunAkhir) - 1; i++){
                distance += jarak.getDistance(asalList.get(i).getLatitude(), asalList.get(i).getLongitude(), asalList.get(i + 1).getLatitude(), asalList.get(i + 1).getLongitude());
            }*/

            distance=jarak.getDistance(stasiunAwal.getLatitude(),stasiunAwal.getLongitude(),stasiunAkhir.getLatitude(),stasiunAkhir.getLongitude());
            Log.d("Distance", String.format("%.2f", (distance / 1000)));
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera

        LatLng myPosition;
        //  mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));


        System.out.println("sebelum permission");

        int MY_PERMISSION_LOCATION = 10;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && getActivity().checkSelfPermission(
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(
                    new String[]{
                            android.Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSION_LOCATION);
        } else {
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }

        }
        //if(!isAlarmSet){
        //    cancelAlarm();
        //}
        mMap.setMyLocationEnabled(true);
        try{
            LocationManager locationManager = (LocationManager) (getActivity().getSystemService(LOCATION_SERVICE));
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            if (location != null) {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                LatLng latLng = new LatLng(latitude, longitude);
                myPosition = new LatLng(latitude, longitude);

                System.out.println("masuk");
                LatLng coordinate = new LatLng(latitude, longitude);
                CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(coordinate, 15.5f);
                mMap.animateCamera(yourLocation);}
        }
        catch(Exception e){
            Log.d("ErrorLog", "Error location");
        }
    }

    public void startAlarm() {
        Calendar cal = Calendar.getInstance();
        this.alarmIntent = new Intent(getActivity(),AlarmNotificationReceiver.class);
        this.pendingIntent = PendingIntent.getBroadcast(getContext(),0,alarmIntent,0);
        this.alarmFlag = true;
        this.alarmSwitch.setChecked(false);
        // 1 minutes = 60.000 millis
        manager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
    }

    public void cancelAlarm(){
        if(manager!=null){
            manager.cancel(pendingIntent);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if(!b){
            if(isAlarmSet){
                cancelAlarm();
            }
            isAlarmSet = false;
            System.out.println("1");
        }
        else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) && !isAlarmSet){
            Toast.makeText(getActivity().getApplicationContext(),"Alarm is already set",Toast.LENGTH_LONG).show();
            isAlarmSet = true;
            System.out.println("2");
        }
        else {
            Toast.makeText(getActivity().getApplicationContext(), "Cannot set alarm.", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        int i = asalSpinner.getSelectedItemPosition() + 1;
        Log.d("debuggps", "ADSADSAD");
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        LatLng latlong = new LatLng(latitude, longitude);

        if (mark != null) {
            mark.remove();
            //editText = findViewById(R.id.textView2);
            speed = location.getSpeed() * 3.6;
            //editText.setText(String.format("%.2f",speed));
        }

        double jarakKeStasiunTerdekat = 0;
        jarakResult = 0;

        if (stasiunAkhir != null) {
            jarakKeStasiunTerdekat = jarak.getDistance(latitude,longitude,stasiunListAll.get(i).getLatitude(),stasiunListAll.get(i).getLongitude());

            if(jarakKeStasiunTerdekat < 2){
                i++;
            }

            jarakResult = jarakKeStasiunTerdekat;
            for (int j = i;j< stasiunListAll.size()-1;j++){
                jarakResult += jarak.getDistance(stasiunListAll.get(j).getLatitude(),stasiunListAll.get(j).getLongitude(),
                        stasiunListAll.get(j+1).getLatitude(),stasiunListAll.get(j+1).getLongitude());
            }
            //jarakResult = jarak.getDistance(latitude, longitude, stasiunAkhir.getLatitude(), stasiunAkhir.getLongitude()) / 1000;
        }
        //distanceView = findViewById(R.id.textView12);

        //double time = (jarakResult/speed)*60;
        String time;
        if (speed != 0) {
            time = dur.calculateTime(speed, jarakKeStasiunTerdekat);
        } else {
            time = "Not moving";
        }
        //timeView = findViewById(R.id.textView5);

        //Buat output
        //distanceView.setText(String.format("%.2f",jarakResult));
        //timeView.setText(String.format("%.2f",time));

        // mark = mMap.addMarker(new MarkerOptions().position(latlong));
        // mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlong, 15.5f));
        Log.d("Distance2", String.format("%.2f", jarakResult));
        Log.d("Distance3", String.format("%.2f", jarakKeStasiunTerdekat));
        Log.d("Time", time);

        listener.setSpeedETA(jarakResult, time);

        if(jarakResult>0 && jarakResult/1000<=150 && isAlarmSet) {
            startAlarm();
            isAlarmSet = false;
            Log.d("alarmdebug", String.valueOf(isAlarmSet));
            //btnSetAlarm.setText("set alarm");
        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
