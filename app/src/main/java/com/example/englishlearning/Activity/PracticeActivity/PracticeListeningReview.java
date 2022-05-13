package com.example.englishlearning.Activity.PracticeActivity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.englishlearning.Model.PracticeModel.PracticeFillingBlank;
import com.example.englishlearning.Model.PracticeModel.PracticeListening;
import com.example.englishlearning.Model.ReviewModel.FillingBlankReview;
import com.example.englishlearning.Model.ReviewModel.ListeningReview;
import com.example.englishlearning.Model.ReviewModel.RawTestRecord;
import com.example.englishlearning.Model.ReviewModel.ReadingReview;
import com.example.englishlearning.Model.ReviewModel.SingleQuestionReview;
import com.example.englishlearning.Model.ReviewModel.WritingReview;
import com.example.englishlearning.QuestionFragment.ListeningFragment;
import com.example.englishlearning.R;
import com.example.englishlearning.ReviewFragment.ListeningReviewFragment;
import com.example.englishlearning.Utils;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class PracticeListeningReview extends AppCompatActivity {
    public static final String ID_TEST_RECORD_KEY = "id_test_record_key";

    PracticeListening rawReview;
    List<ListeningReview> listeningReviews;

    private TextView tvPoint;
    private Button btnQuestionNumber;
    private TableLayout tableQuestion;
    private FrameLayout questionContent;
    private Button btnPrevious;
    private Button btnNext;

    private List<Button> listBtnQuestion;

    private int currentQuestion;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice_listening_review);

        questionContent = findViewById(R.id.question_content);
        btnQuestionNumber = findViewById(R.id.btn_question_number);
        tableQuestion = findViewById(R.id.table_question);
        tvPoint = findViewById(R.id.tv_point);
        btnPrevious = findViewById(R.id.btn_previous);
        btnNext = findViewById(R.id.btn_next);
        listBtnQuestion = new ArrayList<>();

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

        if(Utils.isLoggedIn(this)){
            Gson gson = new Gson();
            rawReview = gson.fromJson( getIntent().getStringExtra("record"), PracticeListening.class );
        }else{
            long id = this.getIntent().getExtras().getLong(ID_TEST_RECORD_KEY);
            rawReview = PracticeListening.getPracticeListeningById((int)id);
        }


        listeningReviews = rawReview.getReviews();
        changeColorBtnQuestion();



        addFragment(new int[]{1,2,3}, new ListeningReviewFragment(listeningReviews.get(0), listBtnQuestion.get(0), listBtnQuestion.get(1),
                listBtnQuestion.get(2)));
        currentQuestion = 1;
        btnQuestionNumber.setText("1-3");

        tvPoint.setText(String.valueOf(rawReview.getCorrect()));
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

    private void changeColorBtnQuestion(){
        int index = 0;
        for(ListeningReview listeningReview: listeningReviews){
            for(int i=0; i< listeningReview.getListening().getListQuestions().size(); i++){
                if( listeningReview.isCorrect(i) )
                    listBtnQuestion.get(index).setBackgroundTintList(this.getResources().getColorStateList(R.color.correct_answer));
                else
                    listBtnQuestion.get(index).setBackgroundTintList(this.getResources().getColorStateList(R.color.incorrect_answer));
                index++;
            }
        }
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
                addFragment(new int[]{4,5,6}, new ListeningReviewFragment(listeningReviews.get(1), listBtnQuestion.get(3), listBtnQuestion.get(4),
                        listBtnQuestion.get(5)));
                btnQuestionNumber.setText("4-6");
                currentQuestion = 4;
                break;
            }
            case 4:
            case 5:
            case 6:{
                addFragment(new int[]{7,8,9}, new ListeningReviewFragment(listeningReviews.get(2), listBtnQuestion.get(6), listBtnQuestion.get(7),
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
                addFragment(new int[]{1,2,3}, new ListeningReviewFragment(listeningReviews.get(0), listBtnQuestion.get(0), listBtnQuestion.get(1),
                        listBtnQuestion.get(2)));
                btnQuestionNumber.setText("1-3");
                currentQuestion = 3;
                break;
            }

            //Filling Blank
            case 7:
            case 8:
            case 9:{
                addFragment(new int[]{4,5,6}, new ListeningReviewFragment(listeningReviews.get(1), listBtnQuestion.get(3), listBtnQuestion.get(4),
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
                addFragment(new int[]{1,2,3}, new ListeningReviewFragment(listeningReviews.get(0), listBtnQuestion.get(0), listBtnQuestion.get(1),
                        listBtnQuestion.get(2)));
                btnQuestionNumber.setText("1-3");
                break;
            }
            case 4:
            case 5:
            case 6:{
                addFragment(new int[]{4,5,6}, new ListeningReviewFragment(listeningReviews.get(1), listBtnQuestion.get(3), listBtnQuestion.get(4),
                        listBtnQuestion.get(5)));
                btnQuestionNumber.setText("4-6");
                break;
            }

            //Filling Blank
            case 7:
            case 8:
            case 9:{
                addFragment(new int[]{7,8,9}, new ListeningReviewFragment(listeningReviews.get(2), listBtnQuestion.get(6), listBtnQuestion.get(7),
                        listBtnQuestion.get(8)));
                btnQuestionNumber.setText("7-9");
                break;
            }

        }
        currentQuestion = number;
    }
}
