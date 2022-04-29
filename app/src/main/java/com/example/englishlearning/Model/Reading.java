package com.example.englishlearning.Model;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.englishlearning.Databases.EnglishHelper;
import com.example.englishlearning.MyApplication;

import java.util.ArrayList;
import java.util.List;

public class Reading {
    private int id;
    private String paragraph;
    private String rawIdQuestions;
    private int level;

    private List<ReadingQuestion> listQuestions;

    public Reading(int id, String paragraph, String rawIdQuestions, int level) {
        this.id = id;
        this.paragraph = paragraph;
        this.rawIdQuestions = rawIdQuestions;
        this.level = level;

        listQuestions = new ArrayList<>();

        initListQuestions();
    }

    private void initListQuestions() {
        String tmp = rawIdQuestions.replace("[", "");
        String[] strIdQuestions = tmp.split("]");
        for(String id: strIdQuestions){
            listQuestions.add(ReadingQuestion.getReadingQuestionById(Integer.parseInt(id)));
        }
    }

    public static Reading getReadingById(int id){
        EnglishHelper helper = new EnglishHelper(MyApplication.getAppContext());
        SQLiteDatabase database = helper.getReadableDatabase();
        Cursor cursor = database.query("reading", null, "id = ?", new String[]{String.valueOf(id)}, null, null, null);
        while(cursor.moveToNext()){
            return new Reading(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getInt(3));
        }
        return null;
    }

    //----------------------------------------------
    public static class ReadingQuestion{
        private int id;
        private String question;
        private String rawIdMultipleChoice;
        private int idAnswer;

        private List<MultipleChoiceAnswer> listChoice;

        public ReadingQuestion(int id, String question, String rawIdMultipleChoice, int idAnswer) {
            this.id = id;
            this.question = question;
            this.rawIdMultipleChoice = rawIdMultipleChoice;
            this.idAnswer = idAnswer;

            listChoice = new ArrayList<>();
            initMultipleChoice();
        }

        private void initMultipleChoice(){
            String tmp = rawIdMultipleChoice.replace("[", "");
            String[] strIdChoice = tmp.split("]");

            for(String id: strIdChoice){
                listChoice.add(MultipleChoiceAnswer.getMultipleChoiceById( Integer.parseInt(id) ));
            }
        }

        public static ReadingQuestion getReadingQuestionById(int id){
            EnglishHelper helper = new EnglishHelper(MyApplication.getAppContext());
            SQLiteDatabase database = helper.getReadableDatabase();
            Cursor cursor = database.query("reading_question", null, "id = ?", new String[]{String.valueOf(id)}, null, null, null);
            while(cursor.moveToNext()){
                return new ReadingQuestion(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getInt(3));
            }
            return null;
        }
    }
}
