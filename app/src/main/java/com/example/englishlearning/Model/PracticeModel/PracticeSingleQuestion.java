package com.example.englishlearning.Model.PracticeModel;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.englishlearning.Databases.UserDataHelper;
import com.example.englishlearning.Model.ReviewModel.SingleQuestionReview;
import com.example.englishlearning.MyApplication;

import java.util.ArrayList;
import java.util.List;

public class PracticeSingleQuestion extends GeneralPractice {

    private int correct;
    private String singleAnswer;

    public PracticeSingleQuestion(long id, String dateTime, int correct, String singleAnswer) {
        this.id = id;
        this.dateTime = dateTime;
        this.correct = correct;
        this.singleAnswer = singleAnswer;
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

    public String getSingleAnswer() {
        return singleAnswer;
    }

    public void setSingleAnswer(String singleAnswer) {
        this.singleAnswer = singleAnswer;
    }

    public static PracticeSingleQuestion getPracticeSingleQuestionById(int id){
        UserDataHelper helper = new UserDataHelper(MyApplication.getAppContext());
        SQLiteDatabase database = helper.getReadableDatabase();
        Cursor cursor = database.query("practice_single_question", null, "id = ?", new String[]{String.valueOf(id)}, null, null, null);
        while(cursor.moveToNext()){
            return new PracticeSingleQuestion(cursor.getInt(0), cursor.getString(1), cursor.getInt(2),
                    cursor.getString(3));
        }
        return null;
    }

    public List<SingleQuestionReview> getReviews(){
        List<SingleQuestionReview> list = new ArrayList<>();
        String[] tmpAnswer = singleAnswer.replace("[", "").split("]");
        for(String item: tmpAnswer){
            String[] tmp = item.split(",");
            int questionId = Integer.parseInt(tmp[0]);
            int answerId = Integer.parseInt(tmp[1]);
            list.add(new SingleQuestionReview(questionId, answerId));
        }
        return list;
    }
}
