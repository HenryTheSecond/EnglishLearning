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
import com.example.englishlearning.Model.FillingBlank;
import com.example.englishlearning.Model.SingleQuestion;
import com.example.englishlearning.QuestionFragment.FillingBlankParagraphFragment;
import com.example.englishlearning.QuestionFragment.SingleQuestionFragment;
import com.example.englishlearning.R;
import com.example.englishlearning.Utils;

import java.util.ArrayList;
import java.util.List;

public class PracticeSingleQuestionActivity extends AppCompatActivity {

    private FrameLayout questionContent;
    private Button btnQuestionNumber;
    private TableLayout tableQuestion;
    private Button btnSubmit;
    private Button btnPrevious;
    private Button btnNext;

    private List<Button> listBtnQuestion;
    private int currentQuestion;

    private List<SingleQuestion> singleQuestions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice_single_question);

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
        getQuestions(level);

        addFragment(new int[]{1}, new SingleQuestionFragment(singleQuestions.get(0), listBtnQuestion.get(0)));
        currentQuestion = 1;
        btnQuestionNumber.setText("1");
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
        singleQuestions = new ArrayList<>();

        Cursor cursor = Utils.getRandomQuestions("single_question", 5, level);
        while(cursor.moveToNext()){
            singleQuestions.add(new SingleQuestion(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getInt(3), cursor.getInt(4)));
        }
    }

    private void btnSubmitClicked(){
        AlertDialog.Builder builder = new AlertDialog.Builder(PracticeSingleQuestionActivity.this);
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

        String singleAnswer = "";
        for(SingleQuestion question: singleQuestions){
            String idAnswer = Utils.getIdAnswer(this, questionNumber, question.getListChoice());
            singleAnswer += "[" + String.valueOf(question.getId())  +  ","  +  idAnswer   + "]";

            if(Integer.parseInt(idAnswer) == question.getIdAnswer())
                correct++;
            questionNumber++;
        }

        UserDataHelper helper = new UserDataHelper(this);
        SQLiteDatabase database = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("date_time", date);
        contentValues.put("correct", correct);
        contentValues.put("single_answer", singleAnswer);
        long idInsert = database.insert("practice_single_question", null, contentValues);

        Intent intent = new Intent(PracticeSingleQuestionActivity.this, PracticeSingleQuestionReview.class);
        intent.putExtra(PracticeSingleQuestionReview.ID_TEST_RECORD_KEY, idInsert);
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
        if(currentQuestion == 5)
            return;

        addFragment(new int[]{currentQuestion+1}, new SingleQuestionFragment(singleQuestions.get(currentQuestion+1-1), listBtnQuestion.get(currentQuestion+1-1)));
        currentQuestion++;
        btnQuestionNumber.setText( String.valueOf(currentQuestion) );
    }

    private void previousQuestion(){
        if(currentQuestion == 1)
            return;

        addFragment(new int[]{currentQuestion-1}, new SingleQuestionFragment(singleQuestions.get(currentQuestion-1-1), listBtnQuestion.get(currentQuestion-1-1)));
        currentQuestion--;
        btnQuestionNumber.setText( String.valueOf(currentQuestion) );
    }

    public void btnQuestionClick(View view){
        Button btn = (Button)view;
        int number = Integer.parseInt( btn.getText().toString() );

        addFragment(new int[]{number}, new SingleQuestionFragment(singleQuestions.get(number-1), listBtnQuestion.get(number-1)));
        btnQuestionNumber.setText( String.valueOf(number) );

        currentQuestion = number;
    }
}