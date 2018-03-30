package com.example.user.myapplication.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.user.myapplication.Database.DBKereta;
import com.example.user.myapplication.Database.DBStasiun;
import com.example.user.myapplication.Database.DatabaseHelper;
import com.example.user.myapplication.Database.Kereta;
import com.example.user.myapplication.Database.Stasiun;
import com.example.user.myapplication.R;

import java.util.ArrayList;

/**
 * Created by Asus on 11/03/2018.
 */

public class ScheduleFragment extends Fragment implements Runnable {
    private EditText etSearch;
    private ImageView ivButton;
    private ListView listResult;
    protected DatabaseHelper mDBHelper;
    protected DBKereta dbKereta;
    protected DBStasiun dbStasiun;
    protected ArrayAdapter<Kereta> keretaAdapter;

    protected ArrayList<Stasiun> asalList;
    protected ArrayList<Stasiun> tujuanList;
    protected ArrayList<Kereta> kereta;
    protected ArrayList<Stasiun> stasiun;
    protected ArrayList<String> trackList;

    public ScheduleFragment(){

    }


    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_schedule, container, false);
        this.etSearch = view.findViewById(R.id.et_train_search);
        this.ivButton = view.findViewById(R.id.iv_get_schedule_btn);
        this.listResult = view.findViewById(R.id.listview_route);
        this.mDBHelper=new DatabaseHelper(this.getContext());
        this.dbKereta=new DBKereta(this.mDBHelper);
        this.kereta = this.dbKereta.getListTrains();
        this.keretaAdapter = new ArrayAdapter<>(getActivity(), R.layout.string_list_kereta, kereta);
        this.listResult.setAdapter(keretaAdapter);
        return view;
    }

    public static ScheduleFragment newInstance(){
        ScheduleFragment scheduleFragment = new ScheduleFragment();
        return scheduleFragment;
    }

    @Override
    public void run() {

    }
}
