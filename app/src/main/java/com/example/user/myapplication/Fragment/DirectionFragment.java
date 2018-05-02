package com.example.user.myapplication.Fragment;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentFilter;
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
import android.widget.Switch;
import android.widget.Toast;

import com.example.user.myapplication.Alarm.AlarmNotificationReceiver;
import com.example.user.myapplication.Map.Distance;
import com.example.user.myapplication.Map.Duration;
import com.example.user.myapplication.NoDefaultSpinner;
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

import java.util.ArrayList;
import java.util.Calendar;

import static android.content.Context.LOCATION_SERVICE;

/**
 * Created by user on 3/23/2018.
 */

public class DirectionFragment extends Fragment implements View.OnClickListener,OnMapReadyCallback,CompoundButton.OnCheckedChangeListener{
    protected NoDefaultSpinner keretaSpinner;
    protected NoDefaultSpinner asalSpinner, tujuanSpinner;
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
    private MyLocationListener loclistenerNetwork;
    private MyLocationListener loclistenerGPS;

    protected DatabaseHelper mDBHelper;
    protected DBKereta dbKereta;
    protected DBStasiun dbStasiun;

    protected GoogleMap mMap;
    protected LocationManager locationManager;
    protected Distance jarak;
    protected Duration dur;
    protected Polyline poly;
    protected boolean alarmFlag;

    AlarmManager manager;
    Intent alarmIntent;
    PendingIntent pendingIntent;

    protected Stasiun stasiunAwal, stasiunAkhir, stasiunSelanjutnya;
    protected boolean isAlarmSet,isAlarm2Set;


    FragmentListener listener;

    // 0 = sudah sampai 1 = sebentar lagi
    public static int jenisAlarm;

    public DirectionFragment(){
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
        ArrayList<Stasiun> dummyList = new ArrayList<>();
        dummyList.add(new Stasiun("Pilih kereta terlebih dahulu", "Pilih kereta terlebih dahulu", 0, 0));

        ArrayAdapter<Stasiun> defaultAdapter = new ArrayAdapter<Stasiun>(getContext(), android.R.layout.simple_spinner_item, dummyList);
        asalSpinner.setAdapter(defaultAdapter);
        tujuanSpinner.setAdapter(defaultAdapter);
        asalSpinner.setSelection(-1);
        tujuanSpinner.setSelection(-1);
        asalSpinner.setEnabled(false);
        tujuanSpinner.setEnabled(false);

        ArrayAdapter<Kereta> keretaArrayAdapter = new ArrayAdapter<Kereta>(getContext(), R.layout.spinner_list_items, kereta);
        keretaSpinner.setAdapter(keretaArrayAdapter);
        keretaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i > -1) {
                    asalSpinner.setEnabled(true);
                    tujuanSpinner.setEnabled(true);
                    asalSpinner.setClickable(true);
                    tujuanSpinner.setClickable(true);
                    String nama = adapterView.getItemAtPosition(i).toString();
                    deleteList(asalList);
                    if (!stasiunListAll.isEmpty()) {
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
                    ArrayAdapter<Stasiun> asalArrayAdapter = new ArrayAdapter<Stasiun>(getContext(), R.layout.spinner_list_items, asalList);
                    asalSpinner.setAdapter(asalArrayAdapter);
                    asalSpinner.setSelection(0);
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

                            ArrayAdapter<Stasiun> tujuanArrayAdapter = new ArrayAdapter<Stasiun>(getContext(), R.layout.spinner_list_items, tujuanList);
                            tujuanSpinner.setAdapter(tujuanArrayAdapter);
                            tujuanSpinner.setSelection(tujuanList.size() - 1);
                            tujuanSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                    if (!stasiunListAll.isEmpty()) {
                                        stasiunListAll.clear();
                                    }
                                    Stasiun tujuanTemp = dbStasiun.getStasiunByName(tujuanSpinner.getSelectedItem().toString());
                                    for (int j = asalSpinner.getSelectedItemPosition(); j < asalSpinner.getAdapter().getCount(); j++) {
                                        Stasiun stasiunTemp = dbStasiun.getStasiunByName(asalSpinner.getItemAtPosition(j).toString());
                                        if (stasiunTemp.equals(tujuanTemp)) {
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
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        makeLocationService();

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
        if(view.getId()==this.searchBtn.getId() && (asalSpinner.getSelectedItemPosition() > -1 && tujuanSpinner.getSelectedItemPosition() > -1)){
            stasiunAwal=(Stasiun)asalSpinner.getSelectedItem();
            stasiunAkhir=(Stasiun)tujuanSpinner.getSelectedItem();
            stasiunSelanjutnya = (Stasiun) tujuanSpinner.getItemAtPosition(0);

            mMap.clear();
            if(!markerList.isEmpty()){

            }
            while(!markerList.isEmpty()){
                markerList.remove(0);
            }

            LatLng koorAwal = new LatLng(stasiunAwal.getLatitude(),stasiunAwal.getLongitude());
            LatLng koorAkhir = new LatLng(stasiunAkhir.getLatitude(),stasiunAkhir.getLongitude());

            for (int i = 0; i< stasiunListAll.size()-1;i++){
                LatLng first = new LatLng(stasiunListAll.get(i).getLatitude(),stasiunListAll.get(i).getLongitude());
                LatLng last = new LatLng(stasiunListAll.get(i+1).getLatitude(),stasiunListAll.get(i+1).getLongitude());
                Marker stasiun1;
                if(i == 0){
                    stasiun1 = mMap.addMarker(new MarkerOptions().position(first).title("Mulai"));
                    stasiun1.showInfoWindow();
                }
                else{
                    stasiun1 = mMap.addMarker(new MarkerOptions().position(first));
                }
                Marker stasiun2;
                if(i == stasiunListAll.size() - 2){
                    stasiun2 = mMap.addMarker(new MarkerOptions().position(last).title("Berhenti"));
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
            MyLocationListener.i = 0;
            MyLocationListener.jarakKeStasiunTerdekat = 0;
            MyLocationListener.awalList = asalList;
            MyLocationListener.akhirList = tujuanList;
        }
        else{
            Toast.makeText(getContext(), "Kereta belum dipilih", Toast.LENGTH_LONG).show();
        }
    }

    public Stasiun getStasiunAwal(){
        return stasiunAwal;
    }

    public Stasiun getStasiunAkhir(){
        return stasiunAkhir;
    }

    public ArrayList<Stasiun> getAwalList(){
        return asalList;
    }

    public ArrayList<Stasiun> getAkhirList(){
        return tujuanList;
    }

    public Stasiun getStasiunSelanjutnya(int i){
        return tujuanList.get(i);
    }

    public Distance getJarak(){
        return jarak;
    }

    public Duration getDuration(){
        return dur;
    }

    public ArrayList<Marker> getMarkerList(){
        return markerList;
    }

    public LocationManager getLocationManager(){
        return this.locationManager;
    }

    public MyLocationListener getListener(){
        return new MyLocationListener(this, mMap);
    }

    public void killLocationService(){
        this.getContext().unregisterReceiver(loclistenerGPS);
        this.getContext().unregisterReceiver(loclistenerNetwork);
        Log.d("debugprovidersize", String.valueOf(locationManager.getAllProviders().size()));
        locationManager.removeUpdates(loclistenerGPS);
        locationManager.removeUpdates(loclistenerNetwork);
        Log.d("debugprovidersize", String.valueOf(locationManager.getAllProviders().size()));
        for(int i = 0; i < locationManager.getAllProviders().size(); i++){
            Log.d("debugprovidersize", locationManager.getAllProviders().get(i));
        }
        locationManager = null;
        loclistenerGPS = null;
        loclistenerNetwork = null;
    }

    public void makeLocationService(){
        IntentFilter filterGPS = new IntentFilter();
        filterGPS.addAction("android.location.PROVIDERS_CHANGED");
        filterGPS.addCategory("android.intent.category.DEFAULT");

        IntentFilter filterNet = new IntentFilter();
        filterNet.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        filterNet.addCategory("android.intent.category.DEFAULT");

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
        this.loclistenerNetwork = new MyLocationListener(this, mMap);
        this.loclistenerGPS = new MyLocationListener(this, mMap);
        this.getContext().registerReceiver(loclistenerNetwork, filterNet);
        this.getContext().registerReceiver(loclistenerGPS, filterGPS);
        if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, loclistenerNetwork);
        }

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, loclistenerGPS);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
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
        mMap.setMyLocationEnabled(true);
        try{
            final LocationManager locationManager = (LocationManager) (getActivity().getSystemService(LOCATION_SERVICE));
            MyLocationListener loclistener = new MyLocationListener(this, mMap);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, loclistener);
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            if (location != null) {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                final LatLng coordinate = new LatLng(latitude, longitude);
                CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(coordinate, 15.5f);
                mMap.animateCamera(yourLocation);

                mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
                    @Override
                    public boolean onMyLocationButtonClick() {
                        if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                            noGPSAlert();
                        }
                        else{
                            CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(coordinate, 15.5f);
                            mMap.animateCamera(yourLocation);
                        }
                        return true;
                    }
                });
            }
            locationManager.removeUpdates(loclistener);
        }
        catch(Exception e){

        }
    }

    public void startAlarm() {
        Calendar cal = Calendar.getInstance();
        this.alarmIntent = new Intent(getActivity(),AlarmNotificationReceiver.class);
        this.pendingIntent = PendingIntent.getBroadcast(getContext(),0,alarmIntent,0);
        this.alarmFlag = true;
        if(!isAlarm2Set) {
            this.alarmSwitch.setChecked(false);
        }
        // 1 minutes = 60.000 millis
        manager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
    }

    public void cancelAlarm(){
        if(manager!=null){
            if(pendingIntent != null) {
                manager.cancel(pendingIntent);
            }
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
            Toast.makeText(getActivity().getApplicationContext(),"Alarm sudah dinyalakan",Toast.LENGTH_LONG).show();
            isAlarmSet = true;
            System.out.println("2");
        }
        else {
            Toast.makeText(getActivity().getApplicationContext(), "Alarm tidak dapat dinyalakan.", Toast.LENGTH_LONG).show();
        }
    }

    public void noGPSAlert() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("GPS dalam kondisi mati. Mohon nyalakan GPS").setCancelable(true)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    public void noInternetAlert() {
        if(getContext() != null) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setMessage("Tidak dapat terhubung ke internet. Mohon nyalakan layanan Wi-Fi atau data seluler").setCancelable(true)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            startActivity(new Intent(Settings.ACTION_DATA_ROAMING_SETTINGS));
                        }
                    });
            final AlertDialog alert = builder.create();
            alert.show();
        }
    }


}
