package com.example.user.myapplication;

import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.user.myapplication.Alarm.AlarmNotificationReceiver;
import com.example.user.myapplication.Fragment.CheckspeedFragment;

public class ListViewSecondActivity extends AppCompatActivity {
    public static int alarm_mode = 2;
    android.support.v7.widget.Toolbar mToolbar;
    Fragment RingtoneFragment;
    CheckspeedFragment CheckSpeed = new CheckspeedFragment();
    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view_second);

        mToolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar1);
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            mToolbar.setTitle(bundle.getString("OptionAlarm"));
            if(mToolbar.getTitle().toString().equalsIgnoreCase("Choose Mode")){
                lv = findViewById(R.id.modeAlarmListView);
                lv.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,
                        getResources().getStringArray(R.array.OptionAlarmMode)));
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        String s = lv.getItemAtPosition(i).toString();
                        if(s.equalsIgnoreCase("Just Vibrate")){
                            alarm_mode = 0;
                        }
                        else if(s.equalsIgnoreCase("Just Alarm")){
                            alarm_mode = 1;
                        }
                        else if(s.equalsIgnoreCase("Alarm and Vibrate")){
                            alarm_mode = 2;
                        }
                        Toast.makeText(ListViewSecondActivity.this, "Mode has changed to " + s, Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    }
}
