package com.example.user.myapplication.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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

public class ScheduleFragment extends Fragment implements Runnable, View.OnClickListener {
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
    ArrayList<Kereta> temp;

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
        this.temp = new ArrayList<>();
        etSearch.addTextChangedListener(new CustomTextWatcher());
        listResult.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Kereta selectedKereta = keretaAdapter.getItem(i);
                assert selectedKereta != null;
                etSearch.setText(selectedKereta.getNama());
            }
        });
        this.ivButton.setOnClickListener(this);
        return view;
    }

    public static ScheduleFragment newInstance(){
        ScheduleFragment scheduleFragment = new ScheduleFragment();
        return scheduleFragment;
    }

    @Override
    public void run() {

    }

    public void searchItem(String textToSearch){
        initList();
        for(int i = 0; i < temp.size(); i++){
            if(!temp.get(i).getNama().toLowerCase().contains(textToSearch)){
                temp.remove(i);
                i--;
            }
        }
        keretaAdapter.notifyDataSetChanged();
    }

    public void initList(){
        this.temp.clear();
        this.temp.addAll(kereta);
        this.keretaAdapter = new ArrayAdapter<>(getActivity(), R.layout.string_list_kereta, temp);
        this.listResult.setAdapter(keretaAdapter);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == ivButton.getId()){
            Log.d("clicktrigger", "clicked");
        }
    }

    private class CustomTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if(charSequence.toString().equals("")){
                //reset listview
                initList();
            } else{
                //perform search
                searchItem(charSequence.toString());
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    }
}
