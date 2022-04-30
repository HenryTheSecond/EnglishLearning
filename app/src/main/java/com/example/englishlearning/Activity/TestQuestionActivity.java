package com.example.englishlearning.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.opengl.Visibility;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.example.englishlearning.AnswerStore.AnswerStore;
import com.example.englishlearning.AnswerStore.WritingStore;
import com.example.englishlearning.Model.FillingBlank;
import com.example.englishlearning.Model.Listening;
import com.example.englishlearning.Model.Reading;
import com.example.englishlearning.Model.SingleQuestion;
import com.example.englishlearning.Model.Writing;
import com.example.englishlearning.QuestionFragment.FillingBlankParagraphFragment;
import com.example.englishlearning.QuestionFragment.ListeningFragment;
import com.example.englishlearning.QuestionFragment.ReadingParagraphFragment;
import com.example.englishlearning.QuestionFragment.SingleQuestionFragment;
import com.example.englishlearning.QuestionFragment.WritingFragment;
import com.example.englishlearning.R;
import com.example.englishlearning.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class TestQuestionActivity extends AppCompatActivity {
    private CountDownTimer countDownTimer;
    private TextView tvCountDownTimer;
    private FrameLayout questionContent;
    private Button btnQuestionNumber;
    private TableLayout tableQuestion;

    private List<Button> listBtnQuestion;
    private int currentQuestion;

    //Questions
    List<Listening> listenings;
    FillingBlank fillingBlank;
    Reading reading;
    List<SingleQuestion> singleQuestions;
    List<Writing> writings;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_question);

        Utils.clearAnswerStoreFile(this);
        currentQuestion = 0;

        //Binding
        tvCountDownTimer = findViewById(R.id.tv_countdown_timer);
        questionContent = findViewById(R.id.question_content);
        btnQuestionNumber = findViewById(R.id.btn_question_number);
        tableQuestion = findViewById(R.id.table_question);

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

        getQuestions(1);

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

        Cursor cursor = Utils.getRandomQuestions("listening", 2);
        while(cursor.moveToNext()){
            listenings.add(new Listening(cursor.getInt(0), cursor.getString(1), cursor.getString(2),
                                        cursor.getString(3), cursor.getInt(4)));
        }

        cursor = Utils.getRandomQuestions("filling_blank", 1);
        while (cursor.moveToNext()){
            fillingBlank = new FillingBlank( cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getInt(3) );
        }

        cursor = Utils.getRandomQuestions("reading", 1);
        while (cursor.moveToNext()){
            reading = new Reading(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getInt(3));
        }

        cursor = Utils.getRandomQuestions("single_question", 4);
        while (cursor.moveToNext()){
            singleQuestions.add( new SingleQuestion( cursor.getInt(0), cursor.getString(1), cursor.getString(2),
                                                     cursor.getInt(3), cursor.getInt(4)) );
        }

        cursor = Utils.getRandomQuestions("writing", 3);
        while (cursor.moveToNext()){
            writings.add( new Writing(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getInt(3)) );
        }
    }

    private void startTimer(){
        countDownTimer = new CountDownTimer(1000*60*35, 1000) {
            @Override
            public void onTick(long l) {
                String second = String.format("%02d", (l/1000)%60);
                tvCountDownTimer.setText(String.valueOf(l/1000/60) + ":" + second );
            }

            @Override
            public void onFinish() {
                tvCountDownTimer.setText("00:00");
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
                break;
            }
            case 4:
            case 5:
            case 6:{
                addFragment(new int[]{4,5,6}, new ListeningFragment(listenings.get(1), listBtnQuestion.get(3), listBtnQuestion.get(4),
                        listBtnQuestion.get(5)));
                break;
            }

            //Filling Blank
            case 7:
            case 8:
            case 9:
            case 10:{
                addFragment(new int[]{7,8,9,10}, new FillingBlankParagraphFragment(fillingBlank, listBtnQuestion.get(6),
                        listBtnQuestion.get(7), listBtnQuestion.get(8), listBtnQuestion.get(9)));
                break;
            }

            //Reading
            case 11:
            case 12:
            case 13:{
                addFragment(new int[]{11,12,13}, new ReadingParagraphFragment(reading, listBtnQuestion.get(10), listBtnQuestion.get(11),
                        listBtnQuestion.get(12)));
                break;
            }

            //Single Questions
            case 14:{
                addFragment(new int[]{14}, new SingleQuestionFragment(singleQuestions.get(0), listBtnQuestion.get(13)));
                break;
            }
            case 15:{
                addFragment(new int[]{15}, new SingleQuestionFragment(singleQuestions.get(1), listBtnQuestion.get(14)));
                break;}
            case 16:{
                addFragment(new int[]{16}, new SingleQuestionFragment(singleQuestions.get(2), listBtnQuestion.get(15)));
                break;
            }
            case 17:{
                addFragment(new int[]{17}, new SingleQuestionFragment(singleQuestions.get(3), listBtnQuestion.get(16)));
                break;
            }

            //Writing
            case 18:{
                addFragment(new int[]{18}, new WritingFragment(writings.get(0), listBtnQuestion.get(17)));
                break;
            }
            case 19:{
                addFragment(new int[]{19}, new WritingFragment(writings.get(1), listBtnQuestion.get(18)));
                break;
            }
            case 20:{
                addFragment(new int[]{20}, new WritingFragment(writings.get(2), listBtnQuestion.get(19)));
                break;
            }
        }
        currentQuestion = number;
    }
}