package com.example.englishlearning.Model.PracticeModel;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.englishlearning.Databases.UserDataHelper;
import com.example.englishlearning.Model.ReviewModel.FillingBlankReview;
import com.example.englishlearning.MyApplication;

import java.util.ArrayList;
import java.util.List;

public class PracticeFillingBlank extends GeneralPractice {

    private int correct;
    private String idFillingBlanks;
    private String fillingBlankAnswer;

    public PracticeFillingBlank(int id, String dateTime, int correct, String idFillingBlanks, String fillingBlankAnswer) {
        this.id = id;
        this.dateTime = dateTime;
        this.correct = correct;
        this.idFillingBlanks = idFillingBlanks;
        this.fillingBlankAnswer = fillingBlankAnswer;
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

    @Override
    public String getResult() {
        return String.valueOf(correct) + "/12";
    }

    public int getCorrect() {
        return correct;
    }

    public void setCorrect(int correct) {
        this.correct = correct;
    }

    public String getIdFillingBlanks() {
        return idFillingBlanks;
    }

    public void setIdFillingBlanks(String idFillingBlanks) {
        this.idFillingBlanks = idFillingBlanks;
    }

    public String getFillingBlankAnswer() {
        return fillingBlankAnswer;
    }

    public void setFillingBlankAnswer(String fillingBlankAnswer) {
        this.fillingBlankAnswer = fillingBlankAnswer;
    }

    public static PracticeFillingBlank getPracticeFillingBlankById(int id){
        UserDataHelper helper = new UserDataHelper(MyApplication.getAppContext());
        SQLiteDatabase database = helper.getReadableDatabase();
        Cursor cursor = database.query("practice_filling_blank", null, "id = ?", new String[]{String.valueOf(id)}, null, null, null);
        while(cursor.moveToNext()){
            return new PracticeFillingBlank(cursor.getInt(0), cursor.getString(1), cursor.getInt(2),
                    cursor.getString(3), cursor.getString(4));
        }
        return null;
    }

    public List<FillingBlankReview> getReviews(){
        List<FillingBlankReview> list = new ArrayList<>();
        String[] tmpIdListenings = idFillingBlanks.replace("[", "").split("]");
        String[] tmpAnswer = fillingBlankAnswer.split("/");
        for(int i=0; i<tmpIdListenings.length; i++){
            int fillingBlankId = Integer.parseInt(tmpIdListenings[i]);
            list.add( new FillingBlankReview(fillingBlankId, tmpAnswer[i]) );
        }
        return list;
    }
}
