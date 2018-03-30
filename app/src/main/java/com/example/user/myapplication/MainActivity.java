package com.example.user.myapplication;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

import com.example.user.myapplication.Fragment.AlarmFragment;
import com.example.user.myapplication.Fragment.DirectionFragment;
import com.example.user.myapplication.Fragment.ScheduleFragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    protected DrawerLayout drawer;
    protected FragmentManager fragmentManager;
    protected AlarmFragment alarmFragment;
    protected DirectionFragment directionFragment;
    protected ScheduleFragment scheduleFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.StationName));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


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



        this.alarmFragment=AlarmFragment.newInstance();
        this.directionFragment=DirectionFragment.newInstance();
        this.scheduleFragment=ScheduleFragment.newInstance();

        FragmentTransaction ft=this.fragmentManager.beginTransaction();

        ft.add(R.id.fragment_container,this.directionFragment).commit();

        navigationView.setCheckedItem(R.id.navigation_directions);
        getSupportActionBar().setTitle("Directions");
    }



    @Override
    public void onBackPressed() {

        if (this.drawer.isDrawerOpen(GravityCompat.START)) {
            this.drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        FragmentTransaction ft=this.fragmentManager.beginTransaction();

        if (id == R.id.navigation_alarm) {
            if(this.alarmFragment.isAdded()){
                ft.show(this.alarmFragment);
            }
            else{
                ft.add(R.id.fragment_container,this.alarmFragment);
            }

            if(this.scheduleFragment.isAdded()){
                ft.hide(this.scheduleFragment);
            }
            if(this.directionFragment.isAdded()){
                ft.hide(this.directionFragment);
            }
            getSupportActionBar().setTitle("Alarm");
        }  else if (id == R.id.navigation_directions) {
            if(this.directionFragment.isAdded()){
                ft.show(this.directionFragment);
            }
            else{
                ft.add(R.id.fragment_container,this.directionFragment);
            }

            if(this.scheduleFragment.isAdded()){
                ft.hide(this.scheduleFragment);
            }
            if(this.alarmFragment.isAdded()){
                ft.hide(this.alarmFragment);
            }
            getSupportActionBar().setTitle("Directions");
        } else if (id == R.id.navigation_schedule) {
            if(this.scheduleFragment.isAdded()){
                ft.show(this.scheduleFragment);
            }
            else{
                ft.add(R.id.fragment_container,this.scheduleFragment);
            }

            if(this.directionFragment.isAdded()){
                ft.hide(this.directionFragment);
            }
            if(this.alarmFragment.isAdded()){
                ft.hide(this.alarmFragment);
            }
            getSupportActionBar().setTitle("Schedule");
        }
        ft.commit();
        this.drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
