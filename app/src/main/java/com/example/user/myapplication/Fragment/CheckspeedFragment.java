package com.example.user.myapplication.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.user.myapplication.R;
import com.example.user.myapplication.ResultSearchActivity;

public class CheckspeedFragment extends Fragment {




    public Button but2;


    public CheckspeedFragment() {

    }


    public static CheckspeedFragment newInstance() {
        CheckspeedFragment fragment = new CheckspeedFragment();
        return fragment;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_checkspeed2,container,false);
        Button directionsButton = (Button) view.findViewById(R.id.checkspeed_button);
        directionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(),"Check Speed Button",Toast.LENGTH_SHORT).show();

            }
        });
        but2 = (Button) view.findViewById(R.id.checkspeed_button);
        but2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent toy = new Intent(CheckspeedFragment.super.getContext(),ResultSearchActivity.class);
                startActivity(toy);

            }

        });


        return  view;
    }
}
