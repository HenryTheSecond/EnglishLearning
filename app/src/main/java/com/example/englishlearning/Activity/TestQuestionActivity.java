package com.example.englishlearning.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import android.os.CountDownTimer;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.example.englishlearning.Databases.UserDataHelper;
import com.example.englishlearning.Model.FillingBlank;
import com.example.englishlearning.Model.Listening;
import com.example.englishlearning.Model.Reading;
import com.example.englishlearning.Model.ReviewModel.RawTestRecord;
import com.example.englishlearning.Model.SingleQuestion;
import com.example.englishlearning.Model.Writing;
import com.example.englishlearning.QuestionFragment.FillingBlankParagraphFragment;
import com.example.englishlearning.QuestionFragment.ListeningFragment;
import com.example.englishlearning.QuestionFragment.ReadingParagraphFragment;
import com.example.englishlearning.QuestionFragment.SingleQuestionFragment;
import com.example.englishlearning.QuestionFragment.WritingFragment;
import com.example.englishlearning.R;
import com.example.englishlearning.Utils;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class TestQuestionActivity extends AppCompatActivity {
    private CountDownTimer countDownTimer;
    private TextView tvCountDownTimer;
    private FrameLayout questionContent;
    private Button btnQuestionNumber;
    private TableLayout tableQuestion;
    private Button btnSubmit;
    private Button btnPrevious;
    private Button btnNext;

    private List<Button> listBtnQuestion;
    private int currentQuestion;

    //Questions
    List<Listening> listenings;
    FillingBlank fillingBlank;
    Reading reading;
    List<SingleQuestion> singleQuestions;
    List<Writing> writings;

    long id = 0;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_question);

        Utils.clearAnswerStoreFile(this);

        //Binding
        tvCountDownTimer = findViewById(R.id.tv_countdown_timer);
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

        if(Utils.isLoggedIn(this)){
            reference = FirebaseDatabase.getInstance().getReference("test_record").child(Utils.getLogin(this));
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot data: snapshot.getChildren()){
                        if(Long.parseLong(data.getKey())>id)
                            id = Long.parseLong(data.getKey());
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }



        findAllQuestionButtons();

        int level = getIntent().getIntExtra(PickLevelActivity.LEVEL_KEY, 1);
        getQuestions(level);//Get random question by level

        //Initialize first question
        addFragment(new int[]{1,2,3}, new ListeningFragment(listenings.get(0), listBtnQuestion.get(0), listBtnQuestion.get(1),
                listBtnQuestion.get(2)));
        currentQuestion = 1;
        btnQuestionNumber.setText("1-3");

        startTimer();
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
        listenings = new ArrayList<>();
        singleQuestions = new ArrayList<>();
        writings = new ArrayList<>();

        Cursor cursor = Utils.getRandomQuestions("listening", 2, level);
        while(cursor.moveToNext()){
            listenings.add(new Listening(cursor.getInt(0), cursor.getString(1), cursor.getString(2),
                                        cursor.getString(3), cursor.getInt(4)));
        }

        cursor = Utils.getRandomQuestions("filling_blank", 1, level);
        while (cursor.moveToNext()){
            fillingBlank = new FillingBlank( cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getInt(3) );
        }

        cursor = Utils.getRandomQuestions("reading", 1, level);
        while (cursor.moveToNext()){
            reading = new Reading(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getInt(3));
        }

        cursor = Utils.getRandomQuestions("single_question", 4, level);
        while (cursor.moveToNext()){
            singleQuestions.add( new SingleQuestion( cursor.getInt(0), cursor.getString(1), cursor.getString(2),
                                                     cursor.getInt(3), cursor.getInt(4)) );
        }

        cursor = Utils.getRandomQuestions("writing", 3, level);
        while (cursor.moveToNext()){
            writings.add( new Writing(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getInt(3)) );
        }
    }


    private void btnSubmitClicked(){
        AlertDialog.Builder builder = new AlertDialog.Builder(TestQuestionActivity.this);
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
        int questionNumber = 1; // Iterator All Question

        double point = 0;
        String date = Utils.getCurrentTimeString();

        String idListenings =  "[" + String.valueOf(listenings.get(0).getId()) + "]" + "[" + String.valueOf(listenings.get(1).getId()) + "]";
        String listeningAnswer = "";
        for(Listening item: listenings){
            for( Listening.ListeningQuestion question: item.getListQuestions() ){
                String idAnswer = Utils.getIdAnswer(this, questionNumber, question.getListChoice());
                listeningAnswer += "[" + String.valueOf(question.getId())  +  ","  +  idAnswer   + "]";
                point += Utils.getPoint(question, Integer.parseInt(idAnswer));
                questionNumber++;
            }
            listeningAnswer += "/";
        }

        String idFillingBlank = String.valueOf(fillingBlank.getId());
        String fillingBlankAnswer = "";
        for(FillingBlank.FillingBlankQuestion question: fillingBlank.getListQuestions()){
            String idAnswer = Utils.getIdAnswer(this, questionNumber, question.getListChoice());
            fillingBlankAnswer += "[" + String.valueOf(question.getId())  +  ","  +  idAnswer   + "]";
            point += Utils.getPoint(question, Integer.parseInt(idAnswer));
            questionNumber++;
        }


        String idReading = String.valueOf(reading.getId());
        String readingAnswer = "";
        for(Reading.ReadingQuestion question: reading.getListQuestions()){
            String idAnswer = Utils.getIdAnswer(this, questionNumber, question.getListChoice());
            readingAnswer += "[" + String.valueOf(question.getId())  +  ","  +  idAnswer   + "]";
            point += Utils.getPoint(question, Integer.parseInt(idAnswer));
            questionNumber++;
        }

        String singleQuestionAnswer = "";
        for(SingleQuestion question: singleQuestions){
            String idAnswer = Utils.getIdAnswer(this, questionNumber, question.getListChoice());
            singleQuestionAnswer += "[" + String.valueOf(question.getId())  +  ","  +  idAnswer   + "]";
            point += Utils.getPoint(question, Integer.parseInt(idAnswer));
            questionNumber++;
        }

        String writingAnswer = "";
        for(Writing question: writings){
            String answer = Utils.getWritingAnswer(this, questionNumber);
            writingAnswer += "[" + String.valueOf(question.getId()) + "," + answer + "]";
            point += Utils.getPoint(question, answer);
            questionNumber++;
        }

        long idInsert;
        RawTestRecord record = null;
        Intent intent = new Intent(TestQuestionActivity.this, ReviewResult.class);

        if(Utils.isLoggedIn(this)){
            idInsert = saveToFirebase(date, point, idListenings, listeningAnswer, idFillingBlank, fillingBlankAnswer,
                    idReading, readingAnswer, singleQuestionAnswer, writingAnswer);
            record = new RawTestRecord((int)idInsert, date, point, idListenings, listeningAnswer, idFillingBlank, fillingBlankAnswer,
                                        idReading, readingAnswer, singleQuestionAnswer, writingAnswer);
            Gson gson = new Gson();
            intent.putExtra(ReviewResult.RECORD_KEY, gson.toJson(record));
        }
        else{
            idInsert = saveToSqlite(date, point, idListenings, listeningAnswer, idFillingBlank, fillingBlankAnswer,
                    idReading, readingAnswer, singleQuestionAnswer, writingAnswer);
            intent.putExtra(ReviewResult.ID_TEST_RECORD_KEY, idInsert);
        }

        startActivity(intent);

        finish();

    }

    private long saveToFirebase(String date, double point, String idListenings, String listeningAnswer, String idFillingBlank, String fillingBlankAnswer, String idReading, String readingAnswer, String singleQuestionAnswer, String writingAnswer) {
        id++;
        DatabaseReference node = reference.child( String.valueOf( id ) );
        node.child("date_time").setValue(date);
        node.child("point").setValue(point);
        node.child("id_listenings").setValue(idListenings);
        node.child("listening_answer").setValue(listeningAnswer);
        node.child("id_filling_blank").setValue(idFillingBlank);
        node.child("filling_blank_answer").setValue(fillingBlankAnswer);
        node.child("id_reading").setValue(idReading);
        node.child("reading_answer").setValue(readingAnswer);
        node.child("reading_answer").setValue(readingAnswer);
        node.child("single_question").setValue(singleQuestionAnswer);
        node.child("writing").setValue(writingAnswer);

        return id;
    }


    private long saveToSqlite(String date, double point, String idListenings, String listeningAnswer,String idFillingBlank, String fillingBlankAnswer,
                              String idReading, String readingAnswer, String singleQuestionAnswer, String writingAnswer){
        UserDataHelper helper = new UserDataHelper(this);
        SQLiteDatabase database = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("date_time", date);
        contentValues.put("point", point);
        contentValues.put("id_listenings", idListenings);
        contentValues.put("listening_answer", listeningAnswer);
        contentValues.put("id_filling_blank", idFillingBlank);
        contentValues.put("filling_blank_answer", fillingBlankAnswer);
        contentValues.put("id_reading", idReading);
        contentValues.put("reading_answer", readingAnswer);
        contentValues.put("reading_answer", readingAnswer);
        contentValues.put("single_question", singleQuestionAnswer);
        contentValues.put("writing", writingAnswer);
        return database.insert("test_record", null, contentValues);
    }


    private void startTimer(){
        countDownTimer = new CountDownTimer(1000*60*30, 1000) {
            @Override
            public void onTick(long l) {
                String second = String.format("%02d", (l/1000)%60);
                tvCountDownTimer.setText(String.valueOf(l/1000/60) + ":" + second );
            }

            @Override
            public void onFinish() {
                finishTest();
            }
        };
        countDownTimer.start();
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
                addFragment(new int[]{4,5,6}, new ListeningFragment(listenings.get(1), listBtnQuestion.get(3), listBtnQuestion.get(4),
                        listBtnQuestion.get(5)));
                btnQuestionNumber.setText("4-6");
                currentQuestion = 4;
                break;
            }
            case 4:
            case 5:
            case 6:{
                addFragment(new int[]{7,8,9,10}, new FillingBlankParagraphFragment(fillingBlank, listBtnQuestion.get(6),
                        listBtnQuestion.get(7), listBtnQuestion.get(8), listBtnQuestion.get(9)));
                btnQuestionNumber.setText("7-10");
                currentQuestion = 7;
                break;
            }

            //Filling Blank
            case 7:
            case 8:
            case 9:
            case 10:{
                addFragment(new int[]{11,12,13}, new ReadingParagraphFragment(reading, listBtnQuestion.get(10), listBtnQuestion.get(11),
                        listBtnQuestion.get(12)));
                btnQuestionNumber.setText("11-13");
                currentQuestion = 11;
                break;
            }

            //Reading
            case 11:
            case 12:
            case 13:{
                addFragment(new int[]{14}, new SingleQuestionFragment(singleQuestions.get(0), listBtnQuestion.get(13)));
                btnQuestionNumber.setText("14");
                currentQuestion = 14;
                break;
            }

            //Single Questions
            case 14:{
                addFragment(new int[]{15}, new SingleQuestionFragment(singleQuestions.get(1), listBtnQuestion.get(14)));
                btnQuestionNumber.setText("15");
                currentQuestion = 15;
                break;
            }
            case 15:{
                addFragment(new int[]{16}, new SingleQuestionFragment(singleQuestions.get(2), listBtnQuestion.get(15)));
                btnQuestionNumber.setText("16");
                currentQuestion = 16;
                break;}
            case 16:{
                addFragment(new int[]{17}, new SingleQuestionFragment(singleQuestions.get(3), listBtnQuestion.get(16)));
                btnQuestionNumber.setText("17");
                currentQuestion = 17;
                break;
            }
            case 17:{
                addFragment(new int[]{18}, new WritingFragment(writings.get(0), listBtnQuestion.get(17)));
                btnQuestionNumber.setText("18");
                currentQuestion = 18;
                break;
            }

            //Writing
            case 18:{
                addFragment(new int[]{19}, new WritingFragment(writings.get(1), listBtnQuestion.get(18)));
                btnQuestionNumber.setText("19");
                currentQuestion = 19;
                break;
            }
            case 19:{
                addFragment(new int[]{20}, new WritingFragment(writings.get(2), listBtnQuestion.get(19)));
                btnQuestionNumber.setText("20");
                currentQuestion = 20;
                break;
            }
        }
    }

    private void previousQuestion(){
        switch (currentQuestion){
            case 4:
            case 5:
            case 6:{
                addFragment(new int[]{1,2,3}, new ListeningFragment(listenings.get(0), listBtnQuestion.get(0), listBtnQuestion.get(1),
                        listBtnQuestion.get(2)));
                btnQuestionNumber.setText("1-3");
                currentQuestion = 1;
                break;
            }

            //Filling Blank
            case 7:
            case 8:
            case 9:
            case 10:{
                addFragment(new int[]{4,5,6}, new ListeningFragment(listenings.get(1), listBtnQuestion.get(3), listBtnQuestion.get(4),
                        listBtnQuestion.get(5)));
                btnQuestionNumber.setText("4-6");
                currentQuestion = 4;
                break;
            }

            //Reading
            case 11:
            case 12:
            case 13:{
                addFragment(new int[]{7,8,9,10}, new FillingBlankParagraphFragment(fillingBlank, listBtnQuestion.get(6),
                        listBtnQuestion.get(7), listBtnQuestion.get(8), listBtnQuestion.get(9)));
                btnQuestionNumber.setText("7-10");
                currentQuestion = 7;
                break;
            }

            //Single Questions
            case 14:{
                addFragment(new int[]{11,12,13}, new ReadingParagraphFragment(reading, listBtnQuestion.get(10), listBtnQuestion.get(11),
                        listBtnQuestion.get(12)));
                btnQuestionNumber.setText("11-13");
                currentQuestion = 11;
                break;
            }
            case 15:{
                addFragment(new int[]{14}, new SingleQuestionFragment(singleQuestions.get(0), listBtnQuestion.get(13)));
                btnQuestionNumber.setText("14");
                currentQuestion = 14;
                break;}
            case 16:{
                addFragment(new int[]{15}, new SingleQuestionFragment(singleQuestions.get(1), listBtnQuestion.get(14)));
                btnQuestionNumber.setText("15");
                currentQuestion = 15;
                break;
            }
            case 17:{
                addFragment(new int[]{16}, new SingleQuestionFragment(singleQuestions.get(2), listBtnQuestion.get(15)));
                btnQuestionNumber.setText("16");
                currentQuestion = 16;
                break;
            }

            //Writing
            case 18:{
                addFragment(new int[]{17}, new SingleQuestionFragment(singleQuestions.get(3), listBtnQuestion.get(16)));
                btnQuestionNumber.setText("17");
                currentQuestion = 17;
                break;
            }
            case 19:{
                addFragment(new int[]{18}, new WritingFragment(writings.get(0), listBtnQuestion.get(17)));
                btnQuestionNumber.setText("18");
                currentQuestion = 18;
                break;
            }
            case 20:{
                addFragment(new int[]{19}, new WritingFragment(writings.get(1), listBtnQuestion.get(18)));
                btnQuestionNumber.setText("19");
                currentQuestion = 19;
                break;
            }
        }
    }


    public void btnQuestionClick(View view) {
        Button btn = (Button)view;
        int number = Integer.parseInt( btn.getText().toString() );
        switch(number){
            //Listening
            case 1:
            case 2:
            case 3:{
                addFragment(new int[]{1,2,3}, new ListeningFragment(listenings.get(0), listBtnQuestion.get(0), listBtnQuestion.get(1),
                                                                    listBtnQuestion.get(2)));
                btnQuestionNumber.setText("1-3");
                break;
            }
            case 4:
            case 5:
            case 6:{
                addFragment(new int[]{4,5,6}, new ListeningFragment(listenings.get(1), listBtnQuestion.get(3), listBtnQuestion.get(4),
                        listBtnQuestion.get(5)));
                btnQuestionNumber.setText("4-6");
                break;
            }

            //Filling Blank
            case 7:
            case 8:
            case 9:
            case 10:{
                addFragment(new int[]{7,8,9,10}, new FillingBlankParagraphFragment(fillingBlank, listBtnQuestion.get(6),
                        listBtnQuestion.get(7), listBtnQuestion.get(8), listBtnQuestion.get(9)));
                btnQuestionNumber.setText("7-10");
                break;
            }

            //Reading
            case 11:
            case 12:
            case 13:{
                addFragment(new int[]{11,12,13}, new ReadingParagraphFragment(reading, listBtnQuestion.get(10), listBtnQuestion.get(11),
                        listBtnQuestion.get(12)));
                btnQuestionNumber.setText("11-13");
                break;
            }

            //Single Questions
            case 14:{
                addFragment(new int[]{14}, new SingleQuestionFragment(singleQuestions.get(0), listBtnQuestion.get(13)));
                btnQuestionNumber.setText("14");
                break;
            }
            case 15:{
                addFragment(new int[]{15}, new SingleQuestionFragment(singleQuestions.get(1), listBtnQuestion.get(14)));
                btnQuestionNumber.setText("15");
                break;}
            case 16:{
                addFragment(new int[]{16}, new SingleQuestionFragment(singleQuestions.get(2), listBtnQuestion.get(15)));
                btnQuestionNumber.setText("16");
                break;
            }
            case 17:{
                addFragment(new int[]{17}, new SingleQuestionFragment(singleQuestions.get(3), listBtnQuestion.get(16)));
                btnQuestionNumber.setText("17");
                break;
            }

            //Writing
            case 18:{
                addFragment(new int[]{18}, new WritingFragment(writings.get(0), listBtnQuestion.get(17)));
                btnQuestionNumber.setText("18");
                break;
            }
            case 19:{
                addFragment(new int[]{19}, new WritingFragment(writings.get(1), listBtnQuestion.get(18)));
                btnQuestionNumber.setText("19");
                break;
            }
            case 20:{
                addFragment(new int[]{20}, new WritingFragment(writings.get(2), listBtnQuestion.get(19)));
                btnQuestionNumber.setText("20");
                break;
            }
        }
        currentQuestion = number;
    }



}