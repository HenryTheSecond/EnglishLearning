package com.example.englishlearning.Databases;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class UserDataHelper extends SQLiteOpenHelper {

    public static final String DBNAME = "user_data.db";
    public static final int DBVERSION = 1;

    public UserDataHelper(Context context){
        super(context, DBNAME, null, DBVERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
