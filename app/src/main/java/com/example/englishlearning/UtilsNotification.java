package com.example.englishlearning;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class UtilsNotification {
    public static int day = Constant.initDayNoti;
    public static final int requestCode = 100;
    public static int countAfterFirstNoti = -1;

    public static void createAlarm(){
        Log.e("AAA", "create alarm");
        Context context = MyApplication.getAppContext();
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
        long triggerTime = System.currentTimeMillis() + (day*1000);
        Intent intent = new Intent(context, NotificationReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.set(AlarmManager.RTC, triggerTime, pi);
    }

    public static void deleteAlarm(){
        Log.e("AAA", "delete alarm");
        Context context = MyApplication.getAppContext();
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
        Intent intent = new Intent(context, NotificationReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(pi);
    }
}
