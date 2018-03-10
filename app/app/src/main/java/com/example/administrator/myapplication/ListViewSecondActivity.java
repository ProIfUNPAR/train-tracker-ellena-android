package com.example.administrator.myapplication;

import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toolbar;

public class ListViewSecondActivity extends AppCompatActivity {

    android.support.v7.widget.Toolbar mToolbar;
    Fragment RingtoneFragment;
    Checkspeed2Fragment CheckSpeed = new Checkspeed2Fragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view_second);

        mToolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar1);
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            mToolbar.setTitle(bundle.getString("OptionAlarm"));
            if(mToolbar.getTitle().toString().equalsIgnoreCase("Ringtone")){

            }
        }



    }
}
