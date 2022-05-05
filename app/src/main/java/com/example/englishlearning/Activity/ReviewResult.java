package com.example.englishlearning.Activity;

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

import com.example.englishlearning.Model.FillingBlank;
import com.example.englishlearning.Model.ReviewModel.FillingBlankReview;
import com.example.englishlearning.Model.ReviewModel.ListeningReview;
import com.example.englishlearning.Model.ReviewModel.RawTestRecord;
import com.example.englishlearning.Model.ReviewModel.ReadingReview;
import com.example.englishlearning.Model.ReviewModel.SingleQuestionReview;
import com.example.englishlearning.Model.ReviewModel.TestRecord;
import com.example.englishlearning.Model.ReviewModel.WritingReview;
import com.example.englishlearning.R;
import com.example.englishlearning.ReviewFragment.FillingBlankFragment;

import java.util.ArrayList;
import java.util.List;

public class ReviewResult extends AppCompatActivity {
    public static final String ID_TEST_RECORD_KEY = "id_test_record_key";
    private TestRecord record;
    private TextView tvPoint;
    private Button btnQuestionNumber;
    private TableLayout tableQuestion;
    private FrameLayout questionContent;

    private List<Button> listBtnQuestion;

    private int currentQuestion;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_result);

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

        long id = this.getIntent().getExtras().getLong(ID_TEST_RECORD_KEY);
        record = RawTestRecord.getRawTestRecordById( (int)id ).parseToTestRecord();
        changeColorBtnQuestion();

        currentQuestion = 0;
        addFragment(new int[]{1,2,3,4}, new FillingBlankFragment(record.getFillingBlankReview(), listBtnQuestion.get(0),
                listBtnQuestion.get(1),listBtnQuestion.get(2),listBtnQuestion.get(3)));
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
        for(ListeningReview listeningReview: record.getListeningReviews()){
            for(int i=0; i< listeningReview.getListening().getListQuestions().size(); i++){
                if( listeningReview.isCorrect(i) )
                    listBtnQuestion.get(index).setBackgroundTintList(this.getResources().getColorStateList(R.color.correct_answer));
                else
                    listBtnQuestion.get(index).setBackgroundTintList(this.getResources().getColorStateList(R.color.incorrect_answer));
                index++;
            }
        }

        FillingBlankReview fillingBlankReview = record.getFillingBlankReview();
        for(int i=0; i<fillingBlankReview.getFillingBlank().getListQuestions().size(); i++){
            if(fillingBlankReview.isCorrect(i))
                listBtnQuestion.get(index).setBackgroundTintList(this.getResources().getColorStateList(R.color.correct_answer));
            else
                listBtnQuestion.get(index).setBackgroundTintList(this.getResources().getColorStateList(R.color.incorrect_answer));
            index++;
        }

        ReadingReview readingReview = record.getReadingReview();
        for(int i=0; i<readingReview.getReading().getListQuestions().size(); i++){
            if(readingReview.isCorrect(i))
                listBtnQuestion.get(index).setBackgroundTintList(this.getResources().getColorStateList(R.color.correct_answer));
            else
                listBtnQuestion.get(index).setBackgroundTintList(this.getResources().getColorStateList(R.color.incorrect_answer));
            index++;
        }

        for(SingleQuestionReview singleQuestionReview: record.getSingleQuestionReviews()){
            if(singleQuestionReview.isCorrect())
                listBtnQuestion.get(index).setBackgroundTintList(this.getResources().getColorStateList(R.color.correct_answer));
            else
                listBtnQuestion.get(index).setBackgroundTintList(this.getResources().getColorStateList(R.color.incorrect_answer));
            index++;
        }

        for(WritingReview writingReview: record.getWritingReviews()){
            if(writingReview.isCorrect())
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

    public void btnQuestionClick(View view){
        Button btn = (Button)view;
        int number = Integer.parseInt( btn.getText().toString() );

    }
}