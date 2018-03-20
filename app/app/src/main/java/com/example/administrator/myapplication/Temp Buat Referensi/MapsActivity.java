package com.tracker.mapdatabase;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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

import java.util.ArrayList;
import java.util.Calendar;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, View.OnClickListener {

    private GoogleMap mMap;
    private DatabaseHelper mDBHelper;
    //private SQLiteDatabase mDb;
    private Spinner keretaSpinner, asalSpinner, tujuanSpinner;
    private ArrayList<Kereta> kereta;
    private ArrayList<Stasiun> stasiun;
    private ArrayList<String> trackList;
    private Kereta selectedKereta;
    private Button searchBtn;
    ArrayList<Stasiun> asalList = new ArrayList<Stasiun>();
    ArrayList<Stasiun> tujuanList = new ArrayList<Stasiun>();
    LocationManager locationManager;
    Marker mark;
    Marker awal, tujuan;
    Distance jarak;
    Duration dur;
    Polyline poly;
    private boolean alarmFlag;

    Button btnSetAlarm;
    AlarmManager manager;
    Intent alarmIntent;
    PendingIntent pendingIntent;
    double jarakResult;

    private Stasiun stasiunAwal, stasiunAkhir;
    boolean isAlarmSet;
    //LatLng lat = new LatLng(-6.874905, 107.605060);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        isAlarmSet = false;
        manager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarmIntent = new Intent(MapsActivity.this,AlarmNotificationReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(this,0,alarmIntent,0);

        btnSetAlarm = (Button)findViewById(R.id.btn_alarm);
        btnSetAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(btnSetAlarm.getText().toString().equals("Cancel Alarm")){
                    cancelAlarm();
                    btnSetAlarm.setText("set alarm");
                    isAlarmSet = false;
                }
                else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) && !isAlarmSet){
                    Toast.makeText(getApplicationContext(),"Alarm is already set",Toast.LENGTH_LONG).show();
                    isAlarmSet = true;
                    btnSetAlarm.setText("Cancel Alarm");
                    if(isAlarmSet){
                        btnSetAlarm.setText("Cancel Alarm");
                    }
                    else{
                        cancelAlarm();
                        btnSetAlarm.setText("set alarm");
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(), "Cannot set alarm.", Toast.LENGTH_LONG).show();
                }
            }
        });

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        this.jarak = new Distance();
        this.dur = new Duration();
        this.alarmFlag = false;
        //membuat class yang menghandle data base
        mDBHelper = new DatabaseHelper(this);

        this.searchBtn = (Button) findViewById(R.id.btn_search);
        this.searchBtn.setOnClickListener(this);
        //inisialize spinner
        this.keretaSpinner = (Spinner) findViewById(R.id.kereta_list);
        this.asalSpinner = (Spinner) findViewById(R.id.asal_list);
        this.tujuanSpinner = (Spinner) findViewById(R.id.tujuan_list);

        DBKereta keretaDB = new DBKereta(mDBHelper);
        kereta = keretaDB.getListTrains();

        final DBStasiun dbStasiun = new DBStasiun(mDBHelper);


        ArrayList<String> namaKereta = new ArrayList<String>();


        for (int i = 0; i < kereta.size(); i++) {
            namaKereta.add(kereta.get(i).getNama());
        }

        ArrayAdapter<Kereta> keretaArrayAdapter = new ArrayAdapter<Kereta>(this, android.R.layout.simple_spinner_item, kereta);
        //ArrayAdapter<String> keretaArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, namaKereta);
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
                    //Print latitude longitude
                    //System.out.println(asalList.get(x).getLatitude()+" "+asalList.get(x).getLongitude());
                    //stasiunAwal = asalList.get(x);
                    System.out.println("end1");
                }
                ArrayAdapter<Stasiun> asalArrayAdapter = new ArrayAdapter<Stasiun>(MapsActivity.this, android.R.layout.simple_spinner_item, asalList);
                asalSpinner.setAdapter(asalArrayAdapter);
                asalSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        deleteList(tujuanList);

                        for (int x = 0; x < trackList.size(); x++) {
                            tujuanList.add(dbStasiun.getStasiunByName(trackList.get(x)));
                            //Print latitude longitude
                            //System.out.println(tujuanList.get(x).getLatitude()+" "+tujuanList.get(x).getLongitude());
                            //stasiunAkhir = tujuanList.get(x);
                            System.out.println("end2");
                        }
                        int x = 0;
                        while (x <= asalSpinner.getSelectedItemPosition()) {
                            tujuanList.remove(0);
                            x++;
                        }
                        ArrayAdapter<Stasiun> tujuanArrayAdapter = new ArrayAdapter<Stasiun>(MapsActivity.this, android.R.layout.simple_spinner_item, tujuanList);
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

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new LocationListener() {
                double latitude, longitude, speed;
                /*TextView editText;
                TextView distanceView;
                TextView timeView;*/

                @Override
                public void onLocationChanged(Location location) {
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                    LatLng latlong = new LatLng(latitude, longitude);

                    if (mark != null) {
                        mark.remove();
                        //editText = findViewById(R.id.textView2);
                        speed = location.getSpeed() * 3.6;
                        //editText.setText(String.format("%.2f",speed));
                    }
                    jarakResult = 0;
                    if (stasiunAkhir != null) {
                        jarakResult = jarak.getDistance(latitude, longitude, stasiunAkhir.getLatitude(), stasiunAkhir.getLongitude()) / 1000;
                    }
                    //distanceView = findViewById(R.id.textView12);

                    //double time = (jarakResult/speed)*60;
                    String time;
                    if (speed != 0) {
                        time = dur.calculateTime(speed, jarakResult);
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
                    Log.d("Time", time);

                    if(jarakResult<=15 && isAlarmSet) {
                        startAlarm();
                        isAlarmSet = false;
                        btnSetAlarm.setText("set alarm");
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
            });
        }
        // BUAT DEBUG
        if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, new LocationListener() {
                double latitude, longitude, speed;
                /*TextView editText;
                TextView distanceView;
                TextView timeView;*/

                @Override
                public void onLocationChanged(Location location) {
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                    LatLng latlong = new LatLng(latitude, longitude);

                    if (mark != null) {
                        mark.remove();
                        //editText = findViewById(R.id.textView2);
                        speed = location.getSpeed() * 3.6;
                        //editText.setText(String.format("%.2f",speed));
                    }
                    double jarakResult = 0;
                    if (stasiunAkhir != null) {
                        jarakResult = jarak.getDistance(latitude, longitude, stasiunAkhir.getLatitude(), stasiunAkhir.getLongitude()) / 1000;
                    }
                    //distanceView = findViewById(R.id.textView12);

                    //double time = (jarakResult/speed)*60;
                    String time;
                    if (speed != 0) {
                        time = dur.calculateTime(speed, jarakResult);
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
                    Log.d("Time", time);

                    if(jarakResult<=15 && isAlarmSet) {
                        startAlarm();
                        isAlarmSet = false;
                        btnSetAlarm.setText("set alarm");
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
            });
        }
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera

        LatLng myPosition;
      //  mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));


        System.out.println("sebelum permission");

        int MY_PERMISSION_LOCATION = 10;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && checkSelfPermission(
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(
                    new String[]{
                            android.Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSION_LOCATION);
        } else {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

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

    @Override
    public void onClick(View view) {
        if(view.getId() == this.searchBtn.getId()){
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

            if(awal != null || tujuan != null){
                awal.remove();
                tujuan.remove();
                poly.remove();
            }

            LatLng koorAwal = new LatLng(stasiunAwal.getLatitude(),stasiunAwal.getLongitude());
            LatLng koorAkhir = new LatLng(stasiunAkhir.getLatitude(),stasiunAkhir.getLongitude());
            awal = mMap.addMarker(new MarkerOptions().position(koorAwal));
            tujuan = mMap.addMarker(new MarkerOptions().position(koorAkhir));
            Stasiun temp1 = asalList.get(0);
            Stasiun temp2 = tujuanList.get(0);


            poly = mMap.addPolyline(new PolylineOptions()
                    .add(new LatLng(stasiunAwal.getLatitude(),stasiunAwal.getLongitude()), new LatLng(stasiunAkhir.getLatitude(),stasiunAkhir.getLongitude()))
                    .width(5)
                    .color(Color.BLUE));
            poly.setVisible(true);
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

    private void deleteList(ArrayList<Stasiun> list){
        while(!list.isEmpty()){
            list.remove(0);
        }
    }

    public void startAlarm() {
        Calendar cal = Calendar.getInstance();
        this.alarmFlag = true;
        // 1 minutes = 60.000 millis
        manager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis()+3000, pendingIntent);
    }

    public void cancelAlarm(){
        if(manager!=null){
            manager.cancel(pendingIntent);
        }
    }
}