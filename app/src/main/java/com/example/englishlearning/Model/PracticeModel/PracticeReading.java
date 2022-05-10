package com.example.englishlearning.Model.PracticeModel;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.englishlearning.Databases.UserDataHelper;
import com.example.englishlearning.Model.ReviewModel.ListeningReview;
import com.example.englishlearning.Model.ReviewModel.ReadingReview;
import com.example.englishlearning.MyApplication;

import java.util.ArrayList;
import java.util.List;

public class PracticeReading {
    private int id;
    private String dateTime;
    private int correct;
    private String idReadings;
    private String readingAnswer;

    public PracticeReading(int id, String dateTime, int correct, String idReadings, String readingAnswer) {
        this.id = id;
        this.dateTime = dateTime;
        this.correct = correct;
        this.idReadings = idReadings;
        this.readingAnswer = readingAnswer;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public int getCorrect() {
        return correct;
    }

    public void setCorrect(int correct) {
        this.correct = correct;
    }

    public String getIdReadings() {
        return idReadings;
    }

    public void setIdReadings(String idReadings) {
        this.idReadings = idReadings;
    }

    public String getReadingAnswer() {
        return readingAnswer;
    }

    public void setReadingAnswer(String readingAnswer) {
        this.readingAnswer = readingAnswer;
    }

    public static PracticeReading getPracticeReadingById(int id){
        UserDataHelper helper = new UserDataHelper(MyApplication.getAppContext());
        SQLiteDatabase database = helper.getReadableDatabase();
        Cursor cursor = database.query("practice_reading", null, "id = ?", new String[]{String.valueOf(id)}, null, null, null);
        while(cursor.moveToNext()){
            return new PracticeReading(cursor.getInt(0), cursor.getString(1), cursor.getInt(2),
                    cursor.getString(3), cursor.getString(4));
        }
        return null;
    }

    public List<ReadingReview> getReviews(){
        List<ReadingReview> list = new ArrayList<>();
        String[] tmpIdReadings = idReadings.replace("[", "").split("]");
        String[] tmpAnswer = readingAnswer.split("/");
        for(int i=0; i<tmpIdReadings.length; i++){
            int readingId = Integer.parseInt(tmpIdReadings[i]);
            list.add( new ReadingReview(readingId, tmpAnswer[i]) );
        }
        return list;
    }
}
