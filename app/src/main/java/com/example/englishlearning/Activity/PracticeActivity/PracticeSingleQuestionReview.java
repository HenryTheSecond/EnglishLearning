package com.example.englishlearning.Activity.PracticeActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.example.englishlearning.Model.PracticeModel.PracticeFillingBlank;
import com.example.englishlearning.Model.PracticeModel.PracticeSingleQuestion;
import com.example.englishlearning.Model.ReviewModel.FillingBlankReview;
import com.example.englishlearning.Model.ReviewModel.SingleQuestionReview;
import com.example.englishlearning.QuestionFragment.SingleQuestionFragment;
import com.example.englishlearning.R;
import com.example.englishlearning.ReviewFragment.FillingBlankReviewFragment;
import com.example.englishlearning.ReviewFragment.SingleQuestionReviewFragment;

import java.util.ArrayList;
import java.util.List;

public class PracticeSingleQuestionReview extends AppCompatActivity {
    public static final String ID_TEST_RECORD_KEY = "id_test_record_key";

    PracticeSingleQuestion rawReview;
    List<SingleQuestionReview> singleQuestionReviews;

    private TextView tvPoint;
    private Button btnQuestionNumber;
    private TableLayout tableQuestion;
    private FrameLayout questionContent;
    private Button btnPrevious;
    private Button btnNext;

    private List<Button> listBtnQuestion;

    private int currentQuestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice_single_question_review);

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

        long id = this.getIntent().getExtras().getLong(ID_TEST_RECORD_KEY);
        rawReview = PracticeSingleQuestion.getPracticeSingleQuestionById((int)id);
        singleQuestionReviews = rawReview.getReviews();
        changeColorBtnQuestion();

        addFragment(new int[]{1}, new SingleQuestionReviewFragment(singleQuestionReviews.get(0), listBtnQuestion.get(0)));
        currentQuestion = 1;
        btnQuestionNumber.setText("1");

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
        for(SingleQuestionReview singleQuestionReview: singleQuestionReviews){
            if(singleQuestionReview.isCorrect())
                listBtnQuestion.get(index).setBackgroundTintList(this.getResources().getColorStateList(R.color.correct_answer));
            else
                listBtnQuestion.get(index).setBackgroundTintList(this.getResources().getColorStateList(R.color.incorrect_answer));
            index++;
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
        if(currentQuestion == 5)
            return;

        addFragment(new int[]{currentQuestion+1}, new SingleQuestionReviewFragment(singleQuestionReviews.get(currentQuestion+1-1), listBtnQuestion.get(currentQuestion+1-1)));
        currentQuestion++;
        btnQuestionNumber.setText( String.valueOf(currentQuestion) );
    }

    private void previousQuestion(){
        if(currentQuestion == 1)
            return;

        addFragment(new int[]{currentQuestion-1}, new SingleQuestionReviewFragment(singleQuestionReviews.get(currentQuestion-1-1), listBtnQuestion.get(currentQuestion-1-1)));
        currentQuestion--;
        btnQuestionNumber.setText( String.valueOf(currentQuestion) );
    }

    public void btnQuestionClick(View view){
        Button btn = (Button)view;
        int number = Integer.parseInt( btn.getText().toString() );

        addFragment(new int[]{number}, new SingleQuestionReviewFragment(singleQuestionReviews.get(number-1), listBtnQuestion.get(number-1)));
        btnQuestionNumber.setText( String.valueOf(number) );

        currentQuestion = number;
    }
}