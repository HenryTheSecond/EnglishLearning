package com.example.englishlearning;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.englishlearning.Databases.UserDataHelper;

public class MyApplication extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        MyApplication.context = getApplicationContext();

        createTestRecordDatabase();
        createNotedDatabase();
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

    public static Context getAppContext(){
        return MyApplication.context;
    }
}
