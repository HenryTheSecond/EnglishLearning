package com.example.englishlearning.Model;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.englishlearning.Databases.EnglishHelper;
import com.example.englishlearning.MyApplication;

import java.util.ArrayList;
import java.util.List;

public class SingleQuestion {
    private int id;
    private String question;
    private String rawIdMultipleChoice;
    private int idAnswer;
    private int level;

    private List<MultipleChoiceAnswer> listChoice;

    public SingleQuestion(int id, String question, String rawIdMultipleChoice, int idAnswer, int level) {
        this.id = id;
        this.question = question;
        this.rawIdMultipleChoice = rawIdMultipleChoice;
        this.idAnswer = idAnswer;
        this.level = level;

        listChoice = new ArrayList<>();
        initMultipleChoice();
    }

    private void initMultipleChoice() {
        String tmp = rawIdMultipleChoice.replace("[", "");
        String[] strIdChoice = tmp.split("]");

        for(String id: strIdChoice){
            listChoice.add(MultipleChoiceAnswer.getMultipleChoiceById( Integer.parseInt(id) ));
        }

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getRawIdMultipleChoice() {
        return rawIdMultipleChoice;
    }

    public void setRawIdMultipleChoice(String rawIdMultipleChoice) {
        this.rawIdMultipleChoice = rawIdMultipleChoice;
    }

    public int getIdAnswer() {
        return idAnswer;
    }

    public void setIdAnswer(int idAnswer) {
        this.idAnswer = idAnswer;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public List<MultipleChoiceAnswer> getListChoice() {
        return listChoice;
    }

    public void setListChoice(List<MultipleChoiceAnswer> listChoice) {
        this.listChoice = listChoice;
    }

    public static SingleQuestion getSingleQuestionById(int id){
        EnglishHelper helper = new EnglishHelper(MyApplication.getAppContext());
        SQLiteDatabase database = helper.getReadableDatabase();
        Cursor cursor = database.query("single_question", null, "id = ?", new String[]{String.valueOf(id)}, null, null, null);
        while(cursor.moveToNext()){
            return new SingleQuestion(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getInt(3), cursor.getInt(4));
        }
        return null;
    }
}
