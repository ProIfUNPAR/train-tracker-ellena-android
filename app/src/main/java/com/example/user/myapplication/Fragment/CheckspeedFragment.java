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
    protected TextView tvStasiun;
    protected TextView tvJarakNext;
    protected TextView tvWaktuNext;
    protected TextView tvKecepatan;
    protected TextView tvStasiunNext;


    private String jarak;
    private String waktu;
    private String stasiun;
    private String jarakNext;
    private String waktuNext;
    private String kecepatan;
    private String stasiunNext;


    public CheckspeedFragment() {

    }


    public static CheckspeedFragment newInstance() {
        CheckspeedFragment fragment = new CheckspeedFragment();
        Bundle args = new Bundle();
        args.putString("jarak", "Belum ada data");
        args.putString("waktu", "Belum ada data");
        args.putString("stasiun","Belum ada data");
        args.putString("jarakNext", "Belum ada data");
        args.putString("waktuNext", "Belum ada data");
        args.putString("kecepatan", "Belum ada data");
        args.putString("stasiun","Belum ada data");
        args.putString("stasiunNext","Belum ada data");

        fragment.setArguments(args);
        return fragment;
    }

    public static CheckspeedFragment newInstance(String jarak, String waktu,String stasiun, String jarakNext, String waktuNext,String stasiunNext, String kecepatan) {
        CheckspeedFragment fragment = new CheckspeedFragment();
        Bundle args = new Bundle();
        args.putString("jarak", jarak);
        args.putString("waktu", waktu);
        args.putString("jarakNext", jarakNext);
        args.putString("waktuNext", waktuNext);
        args.putString("kecepatan", kecepatan);
        args.putString("stasiun", stasiun);
        args.putString("stasiunNext", stasiunNext);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_checkspeed2,container,false);
        this.tvJarak = view.findViewById(R.id.tv_jarak_result);
        this.tvWaktu = view.findViewById(R.id.tv_time_result);
        this.tvJarakNext = view.findViewById(R.id.tv_jarak_result2);
        this.tvWaktuNext = view.findViewById(R.id.tv_time_result2);
        this.tvKecepatan = view.findViewById(R.id.tv_kecepatan_result);
        this.tvStasiun = view.findViewById(R.id.tv_namaStasiun_result);
        this.tvStasiunNext = view.findViewById(R.id.tv_namaStasiunNext_result);

        //jarak = this.getArguments().getString("jarak", "");
        //waktu = this.getArguments().getString("waktu", "");
        if(jarak == null){
            jarak = "Belum ada data";
        }
        if(waktu == null){
            waktu = "Belum ada data";
        }
        if(jarakNext== null){
            jarakNext = "Belum ada data";
        }
        if(waktuNext == null){
            waktuNext = "Belum ada data";
        }
        if(kecepatan == null){
            kecepatan = "Belum ada data";
        }
        if(stasiun == null){
            stasiun = "Belum ada data";
        }
        if(stasiunNext == null){
            stasiunNext = "Belum ada data";
        }



        tvJarak.setText(jarak);
        tvWaktu.setText(waktu);
        tvJarakNext.setText(jarakNext);
        tvWaktuNext.setText(waktuNext);
        tvKecepatan.setText(kecepatan);
        tvStasiun.setText(stasiun);
        tvStasiunNext.setText(stasiunNext);

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
        jarak = bundle.getString("jarak", "");
        waktu = bundle.getString("waktu", "");
        jarakNext = bundle.getString("jarakNext", "");
        waktuNext = bundle.getString("waktuNext", "");
        kecepatan = bundle.getString("kecepatan", "");
        stasiun = bundle.getString("stasiun","");
        stasiunNext = bundle.getString("stasiunNext","");
        if(isVisible()) {
            tvJarak.setText(jarak);
            tvWaktu.setText(waktu);
            tvJarakNext.setText(jarakNext);
            tvWaktuNext.setText(waktuNext);
            tvKecepatan.setText(kecepatan);
            tvStasiun.setText(stasiun);
            tvStasiunNext.setText(stasiunNext);
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
