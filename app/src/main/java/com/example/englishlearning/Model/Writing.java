package com.example.englishlearning.Model;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.englishlearning.Databases.EnglishHelper;
import com.example.englishlearning.MyApplication;

public class Writing {
    private int id;
    private String question;
    private String answer;
    private int level;

    public Writing(int id, String question, String answer, int level) {
        this.id = id;
        this.question = question;
        this.answer = answer;
        this.level = level;
    }

    public static Writing getWritingById(int id){
        EnglishHelper helper = new EnglishHelper(MyApplication.getAppContext());
        SQLiteDatabase database = helper.getReadableDatabase();
        Cursor cursor = database.query("writing", null, "id = ?", new String[]{String.valueOf(id)}, null, null, null);
        while(cursor.moveToNext()){
            return new Writing(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getInt(3));
        }
        return null;
    }
}
