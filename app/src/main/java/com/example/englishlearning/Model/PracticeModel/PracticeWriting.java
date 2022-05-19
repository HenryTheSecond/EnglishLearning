package com.example.englishlearning.Model.PracticeModel;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.englishlearning.Databases.UserDataHelper;
import com.example.englishlearning.Model.ReviewModel.WritingReview;
import com.example.englishlearning.MyApplication;

import java.util.ArrayList;
import java.util.List;

public class PracticeWriting extends GeneralPractice {

    private int correct;
    private String writingAnswer;

    public PracticeWriting(long id, String dateTime, int correct, String writingAnswer) {
        this.id = id;
        this.dateTime = dateTime;
        this.correct = correct;
        this.writingAnswer = writingAnswer;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    @Override
    public String getResult() {
        return String.valueOf(correct) + "/5";
    }

    public int getCorrect() {
        return correct;
    }

    public void setCorrect(int correct) {
        this.correct = correct;
    }

    public String getWritingAnswer() {
        return writingAnswer;
    }

    public void setWritingAnswer(String writingAnswer) {
        this.writingAnswer = writingAnswer;
    }

    public static PracticeWriting getPracticeWritingById(int id){
        UserDataHelper helper = new UserDataHelper(MyApplication.getAppContext());
        SQLiteDatabase database = helper.getReadableDatabase();
        Cursor cursor = database.query("practice_writing", null, "id = ?", new String[]{String.valueOf(id)}, null, null, null);
        while(cursor.moveToNext()){
            return new PracticeWriting(cursor.getInt(0), cursor.getString(1), cursor.getInt(2),
                    cursor.getString(3));
        }
        return null;
    }

    public List<WritingReview> getReviews(){
        List<WritingReview> list = new ArrayList<>();
        String[] tmpAnswer = writingAnswer.replace("[", "").split("]");
        for(String item: tmpAnswer){
            String[] tmp = item.split(",");
            int questionId = Integer.parseInt(tmp[0]);
            String answer = "";
            if( tmp.length ==2 )
                answer = tmp[1];
            list.add(new WritingReview(questionId, answer));
        }
        return list;
    }
}
