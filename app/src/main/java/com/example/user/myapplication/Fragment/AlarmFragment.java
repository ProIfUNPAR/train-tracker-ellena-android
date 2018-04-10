package com.example.user.myapplication.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.user.myapplication.ListViewSecondActivity;
import com.example.user.myapplication.R;


public class AlarmFragment extends Fragment {

    Toolbar toolbar;
    ListView listViewAlarmMenu;

    public AlarmFragment() {
    }


    public static AlarmFragment newInstance() {
        AlarmFragment fragment = new AlarmFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_alarm2,container,false);
        listViewAlarmMenu = (ListView) view.findViewById(R.id.listview3);
        ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(AlarmFragment.super.getContext(),android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.OptionAlarm));


        listViewAlarmMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(AlarmFragment.super.getContext(), ListViewSecondActivity.class);
                intent.putExtra("OptionAlarm", listViewAlarmMenu.getItemAtPosition(i).toString());
                startActivity(intent);
            }
        });
        listViewAlarmMenu.setAdapter(mAdapter);
        return  view;
    }
}
