package com.example.englishlearning.Model.PracticeModel;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.englishlearning.Databases.UserDataHelper;
import com.example.englishlearning.Model.ReviewModel.ListeningReview;
import com.example.englishlearning.MyApplication;

import java.util.ArrayList;
import java.util.List;

public class PracticeListening extends GeneralPractice {

    private int correct;
    private String idListenings;
    private String listeningAnswer;

    public PracticeListening(long id, String dateTime, int correct, String idListenings, String listeningAnswer) {
        this.id = id;
        this.dateTime = dateTime;
        this.correct = correct;
        this.idListenings = idListenings;
        this.listeningAnswer = listeningAnswer;
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
        return String.valueOf(correct) + "/9";
    }

    public int getCorrect() {
        return correct;
    }

    public void setCorrect(int correct) {
        this.correct = correct;
    }

    public String getIdListenings() {
        return idListenings;
    }

    public void setIdListenings(String idListenings) {
        this.idListenings = idListenings;
    }

    public String getListeningAnswer() {
        return listeningAnswer;
    }

    public void setListeningAnswer(String listeningAnswer) {
        this.listeningAnswer = listeningAnswer;
    }

    public static PracticeListening getPracticeListeningById(int id){
        UserDataHelper helper = new UserDataHelper(MyApplication.getAppContext());
        SQLiteDatabase database = helper.getReadableDatabase();
        Cursor cursor = database.query("practice_listening", null, "id = ?", new String[]{String.valueOf(id)}, null, null, null);
        while(cursor.moveToNext()){
            return new PracticeListening(cursor.getInt(0), cursor.getString(1), cursor.getInt(2),
                    cursor.getString(3), cursor.getString(4));
        }
        return null;
    }

    public List<ListeningReview> getReviews(){
        List<ListeningReview> list = new ArrayList<>();
        String[] tmpIdListenings = idListenings.replace("[", "").split("]");
        String[] tmpAnswer = listeningAnswer.split("/");
        for(int i=0; i<tmpIdListenings.length; i++){
            int listeningId = Integer.parseInt(tmpIdListenings[i]);
            list.add( new ListeningReview(listeningId, tmpAnswer[i]) );
        }
        return list;
    }
}
