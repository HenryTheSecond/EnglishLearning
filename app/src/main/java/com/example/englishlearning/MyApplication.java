package com.example.englishlearning;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.englishlearning.Databases.UserDataHelper;

public class MyApplication extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        MyApplication.context = getApplicationContext();

        createTestRecordDatabase();
        createNotedDatabase();
        createPraticeRecordDatabase();

        createNotificationChannel();
    }

    private void createPraticeRecordDatabase(){
        UserDataHelper helper = new UserDataHelper(context);
        SQLiteDatabase database = helper.getWritableDatabase();

        database.execSQL("CREATE TABLE IF NOT EXISTS practice_listening(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "date_time TEXT," +
                "correct int," +
                "id_listenings TEXT," +
                "listening_answer TEXT)" );

        database.execSQL("CREATE TABLE IF NOT EXISTS practice_filling_blank(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "date_time TEXT," +
                "correct int," +
                "id_filling_blanks TEXT," +
                "filling_blank_answer TEXT)" );

        database.execSQL("CREATE TABLE IF NOT EXISTS practice_reading(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "date_time TEXT," +
                "correct int," +
                "id_readings TEXT," +
                "reading_answer TEXT)" );

        database.execSQL("CREATE TABLE IF NOT EXISTS practice_single_question(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "date_time TEXT," +
                "correct int," +
                "single_answer TEXT)" );

        database.execSQL("CREATE TABLE IF NOT EXISTS practice_writing(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "date_time TEXT," +
                "correct int," +
                "writing_answer TEXT)" );
    }

    private void createNotedDatabase() {
        UserDataHelper helper = new UserDataHelper(context);
        SQLiteDatabase database = helper.getWritableDatabase();

        database.execSQL("CREATE TABLE IF NOT EXISTS note_word(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "content TEXT," +
                "meaning TEXT," +
                "type TEXT)" );
    }

    private void createTestRecordDatabase(){
        UserDataHelper helper = new UserDataHelper(context);
        SQLiteDatabase database = helper.getWritableDatabase();
        database.execSQL("CREATE TABLE IF NOT EXISTS test_record(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "date_time TEXT," +
                "point REAL," +
                "id_listenings TEXT," +
                "listening_answer TEXT," +
                "id_filling_blank TEXT," +
                "filling_blank_answer TEXT," +
                "id_reading TEXT," +
                "reading_answer TEXT," +
                "single_question TEXT," +
                "writing TEXT )");
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createNotificationChannel(){
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        NotificationChannel channel = new NotificationChannel(getResources().getString(R.string.id_channel),
                getResources().getString(R.string.channel_name),
                NotificationManager.IMPORTANCE_DEFAULT);
        channel.setDescription(getResources().getString(R.string.channel_description));
        notificationManager.createNotificationChannel(channel);
    }


    public static Context getAppContext(){
        return MyApplication.context;
    }
}
