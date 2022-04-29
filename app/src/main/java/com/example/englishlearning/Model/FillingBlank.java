package com.example.englishlearning.Model;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.englishlearning.Databases.EnglishHelper;
import com.example.englishlearning.MyApplication;

import java.util.ArrayList;
import java.util.List;

public class FillingBlank {
    private int id;
    private String paragraph;
    private String rawIdQuestions;
    private int level;

    private List<FillingBlankQuestion> listQuestions;

    public FillingBlank(int id, String paragraph, String rawIdQuestions, int level) {
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
            listQuestions.add(FillingBlankQuestion.getFillingBlankQuestionById(Integer.parseInt(id)));
        }
    }

    public static FillingBlank getFillingBlankById(int id){
        EnglishHelper helper = new EnglishHelper(MyApplication.getAppContext());
        SQLiteDatabase database = helper.getReadableDatabase();
        Cursor cursor = database.query("filling_blank", null, "id = ?", new String[]{String.valueOf(id)}, null, null, null);
        while(cursor.moveToNext()){
            return new FillingBlank(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getInt(3));
        }
        return null;
    }


    //-------------------------------------------------------------
    public static class FillingBlankQuestion{
        private int id;
        private String rawIdMultipleChoice;
        private int idAnswer;

        private List<MultipleChoiceAnswer> listChoice;

        public FillingBlankQuestion(int id, String rawIdMultipleChoice, int idAnswer) {
            this.id = id;
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


        public static FillingBlankQuestion getFillingBlankQuestionById(int id){
            EnglishHelper helper = new EnglishHelper(MyApplication.getAppContext());
            SQLiteDatabase database = helper.getReadableDatabase();
            Cursor cursor = database.query("filling_blank_question", null, "id = ?", new String[]{String.valueOf(id)}, null, null, null);
            while(cursor.moveToNext()){
                return new FillingBlankQuestion(cursor.getInt(0), cursor.getString(1), cursor.getInt(2));
            }
            return null;
        }
    }
}
