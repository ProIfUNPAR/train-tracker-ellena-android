package com.example.user.myapplication;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

import com.example.user.myapplication.Database.Stasiun;
import com.example.user.myapplication.Fragment.AlarmFragment;
import com.example.user.myapplication.Fragment.CheckspeedFragment;
import com.example.user.myapplication.Fragment.DirectionFragment;
import com.example.user.myapplication.Fragment.FragmentListener;
import com.example.user.myapplication.Fragment.MyLocationListener;
import com.example.user.myapplication.Fragment.ScheduleFragment;
import com.google.android.gms.maps.GoogleMap;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, FragmentListener, ExitDialogFragment.ExitDialogListener {

    protected DrawerLayout drawer;
    protected FragmentManager fragmentManager;
    protected AlarmFragment alarmFragment;
    protected DirectionFragment directionFragment;
    protected ScheduleFragment scheduleFragment;
    protected CheckspeedFragment checkSpeedFragment;
    private MyLocationListener tempListener;
    private LocationManager locmanager;
    private boolean isStopped;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.StationName));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.isStopped = false;

        this.fragmentManager = getSupportFragmentManager();

        Toolbar toolbar = this.findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.drawer = this.findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        this.drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = this.findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        this.alarmFragment = AlarmFragment.newInstance();
        this.directionFragment = DirectionFragment.newInstance();
        this.scheduleFragment = ScheduleFragment.newInstance();
        this.checkSpeedFragment = CheckspeedFragment.newInstance();

        FragmentTransaction ft=this.fragmentManager.beginTransaction();
        ft.add(R.id.fragment_container,this.scheduleFragment);
        ft.add(R.id.fragment_container,this.directionFragment);
        ft.add(R.id.fragment_container,this.checkSpeedFragment);
        ft.add(R.id.fragment_container,this.alarmFragment);
        ft.hide(this.scheduleFragment);
        ft.hide(this.checkSpeedFragment);
        ft.hide(this.alarmFragment);
        ft.commit();

        navigationView.setCheckedItem(R.id.navigation_directions);
        getSupportActionBar().setTitle("Peta");
    }


    public void showNoticeDialog() {

        DialogFragment dialog = new ExitDialogFragment();
        dialog.show(getSupportFragmentManager(), "NoticeDialogFragment");
    }


    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        System.out.println("yes");
        directionFragment.killLocationService();
        this.isStopped = true;
        super.onBackPressed();
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        System.out.println("no");
    }

    @Override
    public void onBackPressed() {

        if (this.drawer.isDrawerOpen(GravityCompat.START)) {
            this.drawer.closeDrawer(GravityCompat.START);
        } else {
            showNoticeDialog();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        final FragmentTransaction ft=this.fragmentManager.beginTransaction();

        if (id == R.id.navigation_alarm) {
            if(this.alarmFragment.isAdded()){
                ft.show(this.alarmFragment);
            }

            if(this.scheduleFragment.isAdded()){
                ft.hide(this.scheduleFragment);
            }
            if(this.directionFragment.isAdded()){
                ft.hide(this.directionFragment);
            }
            if(this.checkSpeedFragment.isAdded()){
                ft.hide(this.checkSpeedFragment);
            }
            getSupportActionBar().setTitle("Alarm");
            checkProvider();
        }
        else if (id == R.id.navigation_directions) {
            if(this.directionFragment.isAdded()){
                ft.show(this.directionFragment);
            }

            if(this.scheduleFragment.isAdded()){
                ft.hide(this.scheduleFragment);
            }
            if(this.alarmFragment.isAdded()){
                ft.hide(this.alarmFragment);
            }
            if(this.checkSpeedFragment.isAdded()){
                ft.hide(this.checkSpeedFragment);
            }
            checkProvider();
            getSupportActionBar().setTitle("Peta");
        }
        else if (id == R.id.navigation_schedule) {
            if(this.scheduleFragment.isAdded()){
                ft.show(this.scheduleFragment);
            }


            if(this.directionFragment.isAdded()){
                ft.hide(this.directionFragment);
            }
            if(this.alarmFragment.isAdded()){
                ft.hide(this.alarmFragment);
            }
            if(this.checkSpeedFragment.isAdded()){
                ft.hide(this.checkSpeedFragment);
            }
            getSupportActionBar().setTitle("Jadwal");
            checkProvider();
        }
        else if (id == R.id.navigation_speed) {
            if(this.checkSpeedFragment.isAdded()){
                ft.show(this.checkSpeedFragment);
            }


            if(this.directionFragment.isAdded()){
                ft.hide(this.directionFragment);
            }
            if(this.alarmFragment.isAdded()){
                ft.hide(this.alarmFragment);
            }
            if(this.scheduleFragment.isAdded()){
                ft.hide(this.scheduleFragment);
            }
            getSupportActionBar().setTitle("Cek Kecepatan");
            checkProvider();
        }

        ft.commit();
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void checkProvider(){
        if(!isGpsOn()){
            noGPSAlert();
        }
        if(!isNetworkOn()){
            noInternetAlert();
        }
    }

    public boolean isGpsOn(){
        LocationManager locman = (LocationManager) getSystemService(LOCATION_SERVICE);
        return locman.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }
    public boolean isNetworkOn() {
        if (this != null) {
            ConnectivityManager connectivityManager
                    = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = null;
            if (connectivityManager != null) {
                activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            }
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }
        else{
            return false;
        }
    }

    public void noGPSAlert() {
        if(this != null) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("GPS dalam kondisi mati. Mohon nyalakan GPS").setCancelable(false)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    });
            final AlertDialog alert = builder.create();
            alert.show();
        }
    }

    public void noInternetAlert() {
        if(this != null) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Tidak dapat terhubung ke internet. Mohon nyalakan layanan Wi-Fi atau data seluler").setCancelable(false)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            startActivity(new Intent(Settings.ACTION_DATA_ROAMING_SETTINGS));
                            //Intent intent = getActivity().getIntent();
                            //getActivity().finish();
                            //startActivity(intent);
                        }
                    });
            final AlertDialog alert = builder.create();
            alert.show();
        }
    }

    @Override
    public void setSpeedETA(double jarak, String time, double jarakNext, String waktuNext, double kecepatan,
                            Stasiun stasiunAwal, Stasiun stasiunAkhir, Stasiun stasiunSelanjutnya) {
        double tempJarak = jarak / 1000;
        double tempJarak2 = jarakNext / 1000;
        String jarakS = String.format("%.1f km", tempJarak);
        String jarakS2 = String.format("%.1f km", tempJarak2);
        int kecepatanInt = (int) Math.round(kecepatan);
        String kecepatanString = kecepatanInt + " km/jam";
        Bundle bundle = new Bundle();
        bundle.putString("jarak", jarakS);
        bundle.putString("waktu", time);
        bundle.putString("jarakNext", jarakS2);
        bundle.putString("waktuNext", waktuNext);
        bundle.putString("kecepatan", kecepatanString);
        if(stasiunAwal != null) {
            bundle.putString("stasiunAwal", stasiunAwal.getNama());
        }
        else{
            bundle.putString("stasiunAwal", "Stasiun Awal");
        }
        if(stasiunAkhir != null) {
            bundle.putString("stasiunAkhir", stasiunAkhir.getNama());
        }
        else{
            bundle.putString("stasiunAkhir", "Stasiun Akhir");
        }
        if(stasiunSelanjutnya != null) {
            bundle.putString("stasiunNext", stasiunSelanjutnya.getNama());
        }
        else{
            bundle.putString("stasiunNext", "Stasiun Berikutnya");
        }
        this.checkSpeedFragment.getArguments().putAll(bundle);
        this.checkSpeedFragment.setEverything();
    }
}
