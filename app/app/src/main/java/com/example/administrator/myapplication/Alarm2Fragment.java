package com.example.administrator.myapplication;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.Toolbar;


public class Alarm2Fragment extends Fragment {

    Toolbar toolbar;
    ListView listViewAlarmMenu;

    public Alarm2Fragment() {
    }


    public static Alarm2Fragment newInstance() {
        Alarm2Fragment fragment = new Alarm2Fragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_alarm2,container,false);
        /*
        Button directionsButton = (Button) view.findViewById(R.id.alarm_button);
        directionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(),"Alarm Button",Toast.LENGTH_SHORT).show();

            }
        });
        */

        //toolbar =(Toolbar) view.findViewById(R.id.toolbar);
        listViewAlarmMenu = (ListView) view.findViewById(R.id.listview3);
        ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(Alarm2Fragment.super.getContext(),android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.OptionAlarm));


        listViewAlarmMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(Alarm2Fragment.super.getContext(), ListViewSecondActivity.class);
                intent.putExtra("OptionAlarm", listViewAlarmMenu.getItemAtPosition(i).toString());
                startActivity(intent);
            }
        });
        listViewAlarmMenu.setAdapter(mAdapter);

        return  view;
    }
}
