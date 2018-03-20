package com.example.administrator.myapplication;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

/**
 * Created by Asus on 11/03/2018.
 */

public class ScheduleFragment extends Fragment {
    private EditText etSearch;
    private ImageView ivButton;
    private ListView listResult;

    public ScheduleFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_schedule, container, false);
        this.etSearch = view.findViewById(R.id.et_train_search);
        this.ivButton = view.findViewById(R.id.iv_get_schedule_btn);
        this.listResult = view.findViewById(R.id.listview_route);
    }
}
