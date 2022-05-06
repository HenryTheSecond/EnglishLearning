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

import com.example.englishlearning.Model.ReviewModel.FillingBlankReview;
import com.example.englishlearning.Model.ReviewModel.ListeningReview;
import com.example.englishlearning.Model.ReviewModel.RawTestRecord;
import com.example.englishlearning.Model.ReviewModel.ReadingReview;
import com.example.englishlearning.Model.ReviewModel.SingleQuestionReview;
import com.example.englishlearning.Model.ReviewModel.TestRecord;
import com.example.englishlearning.Model.ReviewModel.WritingReview;
import com.example.englishlearning.QuestionFragment.FillingBlankParagraphFragment;
import com.example.englishlearning.QuestionFragment.ListeningFragment;
import com.example.englishlearning.QuestionFragment.ReadingParagraphFragment;
import com.example.englishlearning.QuestionFragment.SingleQuestionFragment;
import com.example.englishlearning.QuestionFragment.WritingFragment;
import com.example.englishlearning.R;
import com.example.englishlearning.ReviewFragment.FillingBlankReviewFragment;
import com.example.englishlearning.ReviewFragment.ListeningReviewFragment;
import com.example.englishlearning.ReviewFragment.ReadingReviewFragment;
import com.example.englishlearning.ReviewFragment.SingleQuestionReviewFragment;
import com.example.englishlearning.ReviewFragment.WritingReviewFragment;

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
        addFragment(new int[]{1,2,3}, new ListeningReviewFragment(record.getListeningReviews().get(0), listBtnQuestion.get(0), listBtnQuestion.get(1),
                listBtnQuestion.get(2)));
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

        switch(number){
            //Listening
            case 1:
            case 2:
            case 3:{
                addFragment(new int[]{1,2,3}, new ListeningReviewFragment(record.getListeningReviews().get(0), listBtnQuestion.get(0), listBtnQuestion.get(1),
                        listBtnQuestion.get(2)));
                btnQuestionNumber.setText("1-3");
                break;
            }
            case 4:
            case 5:
            case 6:{
                addFragment(new int[]{4,5,6}, new ListeningReviewFragment(record.getListeningReviews().get(1), listBtnQuestion.get(3), listBtnQuestion.get(4),
                        listBtnQuestion.get(5)));
                btnQuestionNumber.setText("4-6");
                break;
            }

            //Filling Blank
            case 7:
            case 8:
            case 9:
            case 10:{
                addFragment(new int[]{7,8,9,10}, new FillingBlankReviewFragment(record.getFillingBlankReview(), listBtnQuestion.get(6),
                        listBtnQuestion.get(7), listBtnQuestion.get(8), listBtnQuestion.get(9)));
                btnQuestionNumber.setText("7-10");
                break;
            }

            //Reading
            case 11:
            case 12:
            case 13:{
                addFragment(new int[]{11,12,13}, new ReadingReviewFragment(record.getReadingReview(), listBtnQuestion.get(10), listBtnQuestion.get(11),
                        listBtnQuestion.get(12)));
                btnQuestionNumber.setText("11-13");
                break;
            }

            //Single Questions
            case 14:{
                addFragment(new int[]{14}, new SingleQuestionReviewFragment(record.getSingleQuestionReviews().get(0), listBtnQuestion.get(13)));
                break;
            }
            case 15:{
                addFragment(new int[]{15}, new SingleQuestionReviewFragment(record.getSingleQuestionReviews().get(1), listBtnQuestion.get(14)));
                btnQuestionNumber.setText("15");
                break;}
            case 16:{
                addFragment(new int[]{16}, new SingleQuestionReviewFragment(record.getSingleQuestionReviews().get(2), listBtnQuestion.get(15)));
                btnQuestionNumber.setText("16");
                break;
            }
            case 17:{
                addFragment(new int[]{17}, new SingleQuestionReviewFragment(record.getSingleQuestionReviews().get(3), listBtnQuestion.get(16)));
                btnQuestionNumber.setText("17");
                break;
            }

            //Writing
            case 18:{
                addFragment(new int[]{18}, new WritingReviewFragment(record.getWritingReviews().get(0), listBtnQuestion.get(17)));
                btnQuestionNumber.setText("18");
                break;
            }
            case 19:{
                addFragment(new int[]{19}, new WritingReviewFragment(record.getWritingReviews().get(1), listBtnQuestion.get(18)));
                btnQuestionNumber.setText("19");
                break;
            }
            case 20:{
                addFragment(new int[]{20}, new WritingReviewFragment(record.getWritingReviews().get(2), listBtnQuestion.get(19)));
                btnQuestionNumber.setText("20");
                break;
            }
        }
        currentQuestion = number;
    }
}