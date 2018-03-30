package com.example.administrator.myapplication;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

public class Main3Activity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    protected DrawerLayout drawer;
    protected FragmentManager fragmentManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(Main3Activity.this,android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.StationName));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        this.fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft=this.fragmentManager.beginTransaction();

        ft.replace(R.id.fragment_container,new DirectionFragment()).commit();

        Toolbar toolbar = this.findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


       


        this.drawer = this.findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        this.drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = this.findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

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
            ft.replace(R.id.fragment_container,new Alarm2Fragment()).commit();
            getSupportActionBar().setTitle("Alarm");
        }  else if (id == R.id.navigation_directions) {
            ft.replace(R.id.fragment_container,new DirectionFragment()).commit();
            getSupportActionBar().setTitle("Directions");
        } else if (id == R.id.navigation_schedule) {
            ft.replace(R.id.fragment_container,new ScheduleFragment()).commit();
            getSupportActionBar().setTitle("Schedule");
        }

        this.drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
