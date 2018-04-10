package com.example.user.myapplication.Fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.myapplication.R;
import com.example.user.myapplication.ResultSearchActivity;

public class CheckspeedFragment extends Fragment {



    FragmentListener listener;
    //public Button but2;

    protected TextView tvJarak;
    protected TextView tvWaktu;

    private String jarak;
    private String waktu;


    public CheckspeedFragment() {

    }


    public static CheckspeedFragment newInstance() {
        CheckspeedFragment fragment = new CheckspeedFragment();
        Bundle args = new Bundle();
        args.putString("jarak", "");
        args.putString("waktu", "");
        fragment.setArguments(args);
        return fragment;
    }

    public static CheckspeedFragment newInstance(String jarak, String waktu) {
        CheckspeedFragment fragment = new CheckspeedFragment();
        Bundle args = new Bundle();
        args.putString("jarak", jarak);
        args.putString("waktu", waktu);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_checkspeed2,container,false);
        this.tvJarak = view.findViewById(R.id.tv_jarak_result);
        this.tvWaktu = view.findViewById(R.id.tv_time_result);
        //jarak = this.getArguments().getString("jarak", "");
        //waktu = this.getArguments().getString("waktu", "");
        if(jarak == null){
            jarak = "";
        }
        if(waktu == null){
            waktu = "";
        }
        tvJarak.setText(jarak);
        tvWaktu.setText(waktu);
        /*Button directionsButton = (Button) view.findViewById(R.id.checkspeed_button);
        directionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(),"Check Speed Button",Toast.LENGTH_SHORT).show();

            }
        });*/
        /*but2 = (Button) view.findViewById(R.id.checkspeed_button);
        but2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent toy = new Intent(CheckspeedFragment.super.getContext(),ResultSearchActivity.class);
                startActivity(toy);

            }

        });*/
        return  view;
    }
    public void setEverything(){
        Bundle bundle = getArguments();
        Log.d("debugCheckSPeed", "abc");
        jarak = bundle.getString("jarak", "");
        waktu = bundle.getString("waktu", "");
        if(isVisible()) {
            tvJarak.setText(jarak);
            tvWaktu.setText(waktu);
        }
        /*tvJarak.setText(jarak);
        tvWaktu.setText(waktu);*/
    }

    public void setTvJarak(String s){
        this.tvJarak.setText(s);
    }
    public void setTvWaktu(String s){
        this.tvWaktu.setText(s);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof FragmentListener){
            this.listener = (FragmentListener) context;
        }
    }
}
