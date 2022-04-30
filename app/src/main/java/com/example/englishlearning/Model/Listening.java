package com.example.englishlearning.Model;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.englishlearning.Databases.EnglishHelper;
import com.example.englishlearning.MultipleChoice;
import com.example.englishlearning.MyApplication;

import java.util.ArrayList;
import java.util.List;

public class Listening {

    private int id;
    private String fileName;
    private String script;
    private String rawIdQuestions;
    private List<ListeningQuestion> listQuestions;
    private int level;

    public Listening(int id, String fileName, String script, String rawIdQuestions, int level) {
        this.id = id;
        this.fileName = fileName;
        this.script = script;
        this.rawIdQuestions = rawIdQuestions;
        this.level = level;
        listQuestions = new ArrayList<>();

        initListQuestions();
    }

    private void initListQuestions(){
        String tmp = rawIdQuestions.replace("[", "");
        String[] strIdQuestions = tmp.split("]");
        for(String id: strIdQuestions){
            listQuestions.add(ListeningQuestion.getListeningQuestionById(Integer.parseInt(id)));
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script = script;
    }

    public String getRawIdQuestions() {
        return rawIdQuestions;
    }

    public void setRawIdQuestions(String rawIdQuestions) {
        this.rawIdQuestions = rawIdQuestions;
    }

    public List<ListeningQuestion> getListQuestions() {
        return listQuestions;
    }

    public void setListQuestions(List<ListeningQuestion> listQuestions) {
        this.listQuestions = listQuestions;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public static Listening getListeningById(int id){
        EnglishHelper helper = new EnglishHelper(MyApplication.getAppContext());
        SQLiteDatabase database = helper.getReadableDatabase();
        Cursor cursor = database.query("listening", null, "id = ?", new String[]{String.valueOf(id)}, null, null, null);
        while(cursor.moveToNext()){
            return new Listening(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getInt(4));
        }
        return null;
    }

    //----------------------------------------------------------------------
    public static class ListeningQuestion{

        private int id;
        private String question;
        private String rawIdMultipleChoice;
        private List<MultipleChoiceAnswer> listChoice;

        private int idAnswer;

        public ListeningQuestion(int id, String question, String rawIdMultipleChoice, int idAnswer) {

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

        public List<MultipleChoiceAnswer> getListChoice() {
            return listChoice;
        }

        public void setListChoice(List<MultipleChoiceAnswer> listChoice) {
            this.listChoice = listChoice;
        }

        public int getIdAnswer() {
            return idAnswer;
        }

        public void setIdAnswer(int idAnswer) {
            this.idAnswer = idAnswer;
        }

        public static ListeningQuestion getListeningQuestionById(int id){
            EnglishHelper helper = new EnglishHelper(MyApplication.getAppContext());
            SQLiteDatabase database = helper.getReadableDatabase();
            Cursor cursor = database.query("listening_question", null, "id = ?", new String[]{String.valueOf(id)}, null, null, null);
            while(cursor.moveToNext()){
                return new ListeningQuestion(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getInt(3));
            }
            return null;
        }
    }
}
