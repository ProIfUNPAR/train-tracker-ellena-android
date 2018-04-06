package com.example.user.myapplication;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.user.myapplication.Database.Kereta;
import com.example.user.myapplication.Fragment.ScheduleFragment;
import com.example.user.myapplication.R;

import java.util.List;

/**
 * Created by Asus on 05/04/2018.
 */

public class ScheduleListAdapter extends BaseAdapter {

    private ScheduleFragment scheduleFragment;
    private List<String> stasiunList, arrivalList, departureList;

    public ScheduleListAdapter(ScheduleFragment scheduleFragment, Kereta kereta){
        this.stasiunList = kereta.getTrack();
        this.arrivalList = kereta.getWaktuTiba();
        this.departureList = kereta.getWaktuBerangkat();
        this.scheduleFragment = scheduleFragment;
        String arrDebug = "";
        for(int i = 0; i < arrivalList.size(); i++){
            arrDebug = arrDebug + " " + arrivalList.get(i);
        }
        String depDebug = "";
        for(int i = 0; i < departureList.size(); i++){
            depDebug = depDebug + " " + departureList.get(i);
        }
        Log.d("arrdepdebug", "arrival : " + arrDebug);
        Log.d("arrdepdebug", "departure : " + depDebug);
        //Log.d("debugStation", String.valueOf(stasiunList.size()));
        //Log.d("debugStation", String.valueOf(arrivalList.size()));
        //Log.d("debugStation", String.valueOf(departureList.size()));
    }

    @Override
    public int getCount() {
        return stasiunList.size();
    }

    @Override
    public Object getItem(int i) {
        String[] item = new String[3];
        item[0] = stasiunList.get(i);
        item[1] = arrivalList.get(i);
        item[2] = departureList.get(i);
        return item;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final ViewHolder viewHolder;
        if(view == null){
            view = LayoutInflater.from(this.scheduleFragment.getActivity()).inflate(R.layout.string_list_jadwal, viewGroup, false);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder) view.getTag();
        }
        final String[] stringArr = (String[]) this.getItem(i);
        viewHolder.updateView(stringArr[0], stringArr[1], stringArr[2]);
        return view;
    }

    private class ViewHolder{
        protected TextView tvStation;
        protected TextView tvArrival;
        protected TextView tvDeparture;

        public ViewHolder(View view){
            this.tvStation = view.findViewById(R.id.tv_schedule_station);
            this.tvArrival = view.findViewById(R.id.tv_schedule_arrival);
            this.tvDeparture = view.findViewById(R.id.tv_schedule_departure);
        }

        public void updateView(String station, String arrival, String departure){
            this.tvStation.setText(station);
            this.tvArrival.setText(arrival);
            this.tvDeparture.setText(departure);
        }
    }
}
