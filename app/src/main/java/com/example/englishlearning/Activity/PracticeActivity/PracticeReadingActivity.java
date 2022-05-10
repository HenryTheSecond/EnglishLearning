package com.example.englishlearning.Activity.PracticeActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TableLayout;

import com.example.englishlearning.Activity.PickLevelActivity;
import com.example.englishlearning.Databases.UserDataHelper;
import com.example.englishlearning.Model.Listening;
import com.example.englishlearning.Model.PracticeModel.PracticeReading;
import com.example.englishlearning.Model.Reading;
import com.example.englishlearning.QuestionFragment.ListeningFragment;
import com.example.englishlearning.QuestionFragment.ReadingParagraphFragment;
import com.example.englishlearning.R;
import com.example.englishlearning.Utils;

import java.util.ArrayList;
import java.util.List;

public class PracticeReadingActivity extends AppCompatActivity {

    private FrameLayout questionContent;
    private Button btnQuestionNumber;
    private TableLayout tableQuestion;
    private Button btnSubmit;
    private Button btnPrevious;
    private Button btnNext;

    private List<Button> listBtnQuestion;
    private int currentQuestion;

    private List<Reading> readings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice_reading);

        Utils.clearAnswerStoreFile(this);

        questionContent = findViewById(R.id.question_content);
        btnQuestionNumber = findViewById(R.id.btn_question_number);
        tableQuestion = findViewById(R.id.table_question);
        btnSubmit = findViewById(R.id.btn_submit);
        btnPrevious = findViewById(R.id.btn_previous);
        btnNext = findViewById(R.id.btn_next);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextQuestion();
            }
        });

        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                previousQuestion();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnSubmitClicked();
            }
        });

        listBtnQuestion = new ArrayList<>();
        btnQuestionNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tableQuestion.getVisibility() == View.GONE)
                    tableQuestion.setVisibility(View.VISIBLE);
                else
                    tableQuestion.setVisibility(View.GONE);
            }
        });

        findAllQuestionButtons();

        int level = getIntent().getIntExtra(PickLevelActivity.LEVEL_KEY, 1);
        getQuestions(level);//Get random question by level

        addFragment(new int[]{1,2,3}, new ReadingParagraphFragment(readings.get(0), listBtnQuestion.get(0), listBtnQuestion.get(1),
                listBtnQuestion.get(2)));
        currentQuestion = 1;
        btnQuestionNumber.setText("1-3");
    }

    private void findAllQuestionButtons() {
        ViewGroup view = findViewById(R.id.table_question);
        for(int i=0; i<view.getChildCount(); i++){
            ViewGroup childView = (ViewGroup) view.getChildAt(i);
            for(int j=0; j<childView.getChildCount(); j++){
                listBtnQuestion.add( (Button)childView.getChildAt(j) );
            }
        }
    }

    private void getQuestions(int level){
        readings = new ArrayList<>();

        Cursor cursor = Utils.getRandomQuestions("reading", 3, level);
        while(cursor.moveToNext()){
            readings.add(new Reading(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getInt(3)));
        }
    }

    private void btnSubmitClicked(){
        AlertDialog.Builder builder = new AlertDialog.Builder(PracticeReadingActivity.this);
        builder.setTitle("Submit")
                .setMessage("Are you sure to submit?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finishTest();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void finishTest(){
        int questionNumber = 1;
        int correct = 0;
        String date = Utils.getCurrentTimeString();

        String idReadings =  "[" + String.valueOf(readings.get(0).getId()) + "]" + "[" + String.valueOf(readings.get(1).getId()) + "]" + "[" + String.valueOf(readings.get(2).getId()) +"]";
        String readingAnswer = "";
        for(Reading item: readings){
            for( Reading.ReadingQuestion question: item.getListQuestions() ){
                String idAnswer = Utils.getIdAnswer(this, questionNumber, question.getListChoice());
                readingAnswer += "[" + String.valueOf(question.getId())  +  ","  +  idAnswer   + "]";

                if(Integer.parseInt(idAnswer) == question.getIdAnswer())
                    correct++;
                questionNumber++;
            }
            readingAnswer += "/";
        }

        UserDataHelper helper = new UserDataHelper(this);
        SQLiteDatabase database = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("date_time", date);
        contentValues.put("correct", correct);
        contentValues.put("id_readings", idReadings);
        contentValues.put("reading_answer", readingAnswer);
        long idInsert = database.insert("practice_reading", null, contentValues);

        Intent intent = new Intent(PracticeReadingActivity.this, PracticeReadingReview.class);
        intent.putExtra(PracticeReadingReview.ID_TEST_RECORD_KEY, idInsert);
        startActivity(intent);

        finish();
    }

    private void addFragment(int[] group,   Fragment fragment){
        //If current question is one of the member of group question, we don't add fragment
        for(int item: group){
            if(currentQuestion == item)
                return;
        }

        if (fragment != null) {
            FragmentManager fmgr = getSupportFragmentManager();
            FragmentTransaction ft = fmgr.beginTransaction();
            ft.replace(R.id.question_content, fragment);
            //ft.addToBackStack(fragment.getClass().getSimpleName());
            ft.commit();
        }
    }


    private void nextQuestion(){
        switch(currentQuestion){
            //Listening
            case 1:
            case 2:
            case 3:{
                addFragment(new int[]{4,5,6}, new ReadingParagraphFragment(readings.get(1), listBtnQuestion.get(3), listBtnQuestion.get(4),
                        listBtnQuestion.get(5)));
                btnQuestionNumber.setText("4-6");
                currentQuestion = 4;
                break;
            }
            case 4:
            case 5:
            case 6:{
                addFragment(new int[]{7,8,9}, new ReadingParagraphFragment(readings.get(2), listBtnQuestion.get(6), listBtnQuestion.get(7),
                        listBtnQuestion.get(8)));
                btnQuestionNumber.setText("7-9");
                currentQuestion = 7;
                break;
            }

        }
    }

    private void previousQuestion(){
        switch (currentQuestion){
            case 4:
            case 5:
            case 6:{
                addFragment(new int[]{1,2,3}, new ReadingParagraphFragment(readings.get(0), listBtnQuestion.get(0), listBtnQuestion.get(1),
                        listBtnQuestion.get(2)));
                btnQuestionNumber.setText("1-3");
                currentQuestion = 3;
                break;
            }

            //Filling Blank
            case 7:
            case 8:
            case 9:{
                addFragment(new int[]{4,5,6}, new ReadingParagraphFragment(readings.get(1), listBtnQuestion.get(3), listBtnQuestion.get(4),
                        listBtnQuestion.get(5)));
                btnQuestionNumber.setText("4-6");
                currentQuestion = 6;
                break;
            }

        }
    }

    public void btnQuestionClick(View view){
        Button btn = (Button)view;
        int number = Integer.parseInt( btn.getText().toString() );
        switch(number){
            //Listening
            case 1:
            case 2:
            case 3:{
                addFragment(new int[]{1,2,3}, new ReadingParagraphFragment(readings.get(0), listBtnQuestion.get(0), listBtnQuestion.get(1),
                        listBtnQuestion.get(2)));
                btnQuestionNumber.setText("1-3");
                break;
            }
            case 4:
            case 5:
            case 6:{
                addFragment(new int[]{4,5,6}, new ReadingParagraphFragment(readings.get(1), listBtnQuestion.get(3), listBtnQuestion.get(4),
                        listBtnQuestion.get(5)));
                btnQuestionNumber.setText("4-6");
                break;
            }

            //Filling Blank
            case 7:
            case 8:
            case 9:{
                addFragment(new int[]{7,8,9}, new ReadingParagraphFragment(readings.get(2), listBtnQuestion.get(6), listBtnQuestion.get(7),
                        listBtnQuestion.get(8)));
                btnQuestionNumber.setText("7-9");
                break;
            }

        }
        currentQuestion = number;
    }
}