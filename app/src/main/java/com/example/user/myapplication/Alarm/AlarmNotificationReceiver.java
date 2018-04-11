package com.example.user.myapplication.Alarm;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.provider.Settings;
import android.support.v7.app.NotificationCompat;

import com.example.user.myapplication.ListViewSecondActivity;
import com.example.user.myapplication.R;

public class AlarmNotificationReceiver  extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock w1 = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP
                | PowerManager.ON_AFTER_RELEASE, "Turn On Screen");
        w1.acquire();

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

        builder.setAutoCancel(true)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setVisibility(Notification.VISIBILITY_PUBLIC)
                .setContentTitle("Train Tracker")
                .setContentText("Sebentar lagi Anda sampai ke tujuan!")
                .setContentInfo("Alarm");

        if (ListViewSecondActivity.alarm_mode==0 || ListViewSecondActivity.alarm_mode==2){
            builder.setVibrate(new long[]{1000, 1000});
        }
        if (ListViewSecondActivity.alarm_mode==1 || ListViewSecondActivity.alarm_mode==2){
            builder.setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND);
            if (ListViewSecondActivity.ringtone_mode==0) {
                builder.setSound(Settings.System.DEFAULT_NOTIFICATION_URI);
            }
            else if (ListViewSecondActivity.ringtone_mode==1){
                //builder.setSound(R.drawable.ring1);
            }
            else if (ListViewSecondActivity.ringtone_mode==2){

            }
        }

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, builder.build());

        try {
            Thread.sleep(3000);
            if (w1.isHeld()){
                w1.release();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}