package com.example.englishlearning;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.example.englishlearning.Activity.DashBoard;
import com.example.englishlearning.Activity.LoginActivity;

public class NotificationReceiver extends BroadcastReceiver {
    public static int requestCode = 0;

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Notification", Toast.LENGTH_SHORT).show();
        Intent contentIntent;
        if(Utils.isLoggedIn(context))
            contentIntent = new Intent(context, DashBoard.class);
        else
            contentIntent = new Intent(context, LoginActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, requestCode, contentIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, context.getResources().getString(R.string.id_channel))
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(context.getApplicationInfo().name)
                .setContentText(context.getResources().getString(R.string.notification_content) + String.valueOf(UtilsNotification.countAfterFirstNoti))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent);

        Notification notification = mBuilder.build();
        NotificationManager nm = context.getSystemService(NotificationManager.class);
        nm.notify(requestCode, notification);

        UtilsNotification.countAfterFirstNoti++;

        if(UtilsNotification.countAfterFirstNoti < 3  ){
            UtilsNotification.day = Constant.afterFirstInitDay;
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
            long triggerTime = System.currentTimeMillis() + (UtilsNotification.day * 1000);
//        Intent intent1 = new Intent(context, Receiver.class);
            PendingIntent pi = PendingIntent.getBroadcast(context, UtilsNotification.requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            alarmManager.set(AlarmManager.RTC_WAKEUP, triggerTime, pi);
        }
    }
}
