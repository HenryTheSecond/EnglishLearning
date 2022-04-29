package com.example.englishlearning.Model;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.englishlearning.Databases.EnglishHelper;
import com.example.englishlearning.MultipleChoice;
import com.example.englishlearning.MyApplication;

public class MultipleChoiceAnswer {
    private int id;
    private String content;

    public MultipleChoiceAnswer(int id, String content) {
        this.id = id;
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public static MultipleChoiceAnswer getMultipleChoiceById(int id){
        EnglishHelper helper = new EnglishHelper(MyApplication.getAppContext());
        SQLiteDatabase database = helper.getReadableDatabase();
        Cursor cursor = database.query("multiple_choice_answer", null, "id = ?", new String[]{String.valueOf(id)}, null, null, null);
        while(cursor.moveToNext()){
            return new MultipleChoiceAnswer(cursor.getInt(0), cursor.getString(1));
        }
        return null;
    }
}
