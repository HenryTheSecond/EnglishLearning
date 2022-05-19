package com.example.englishlearning.Activity.PracticeActivity;

import androidx.annotation.NonNull;
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
import com.example.englishlearning.Model.PracticeModel.PracticeReading;
import com.example.englishlearning.Model.PracticeModel.PracticeWriting;
import com.example.englishlearning.Model.SingleQuestion;
import com.example.englishlearning.Model.Writing;
import com.example.englishlearning.QuestionFragment.SingleQuestionFragment;
import com.example.englishlearning.QuestionFragment.WritingFragment;
import com.example.englishlearning.R;
import com.example.englishlearning.Utils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PracticeWritingActivity extends AppCompatActivity {
    private FrameLayout questionContent;
    private Button btnQuestionNumber;
    private TableLayout tableQuestion;
    private Button btnSubmit;
    private Button btnPrevious;
    private Button btnNext;

    private List<Button> listBtnQuestion;
    private int currentQuestion;

    private List<Writing> writings;

    private long id = 0;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice_writing);

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

        if(Utils.isLoggedIn(this)){
            reference = FirebaseDatabase.getInstance().getReference("practice_writing").child(Utils.getLogin(this));
//            reference.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    for(DataSnapshot data: snapshot.getChildren()){
//                        if(Long.parseLong(data.getKey())>id)
//                            id = Long.parseLong(data.getKey());
//                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) { }
//            });
        }

        findAllQuestionButtons();

        int level = getIntent().getIntExtra(PickLevelActivity.LEVEL_KEY, 1);
        getQuestions(level);

        addFragment(new int[]{1}, new WritingFragment(writings.get(0), listBtnQuestion.get(0)));
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
        writings = new ArrayList<>();

        Cursor cursor = Utils.getRandomQuestions("writing", 5, level);
        while(cursor.moveToNext()){
            writings.add(new Writing( cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getInt(3)) );
        }
    }

    private void btnSubmitClicked(){
        AlertDialog.Builder builder = new AlertDialog.Builder(PracticeWritingActivity.this);
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

        String writingAnswer = "";
        for(Writing question: writings){
            String answer = Utils.getWritingAnswer(this, questionNumber);
            writingAnswer += "[" + String.valueOf(question.getId())  +  ","  +  answer   + "]";

            if(answer.trim().toLowerCase().equals( question.getAnswer().toLowerCase().trim() ))
                correct++;
            questionNumber++;
        }


        Intent intent = new Intent(PracticeWritingActivity.this, PracticeWritingReview.class);

        if(Utils.isLoggedIn(this)){
            saveToFirebase(date, correct, writingAnswer);
            Gson gson = new Gson();
            PracticeWriting record = new PracticeWriting(id, date, correct, writingAnswer);
            intent.putExtra("record", gson.toJson(record));
        }else{
            long idInsert = saveToSqlite(date, correct, writingAnswer);
            intent.putExtra(PracticeFillingBlankReview.ID_TEST_RECORD_KEY, idInsert);
        }

        startActivity(intent);

        finish();
    }

    private void saveToFirebase(String date, int correct, String writingAnswer){
        id = new Date().getTime();
        DatabaseReference node = reference.child( String.valueOf( id ) );
        node.child("date_time").setValue(date);
        node.child("correct").setValue(correct);
        node.child("writing_answer").setValue(writingAnswer);
    }

    private long saveToSqlite(String date, int correct, String writingAnswer){
        UserDataHelper helper = new UserDataHelper(this);
        SQLiteDatabase database = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("date_time", date);
        contentValues.put("correct", correct);
        contentValues.put("writing_answer", writingAnswer);
        return database.insert("practice_writing", null, contentValues);
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

        addFragment(new int[]{currentQuestion+1}, new WritingFragment(writings.get(currentQuestion+1-1), listBtnQuestion.get(currentQuestion+1-1)));
        currentQuestion++;
        btnQuestionNumber.setText( String.valueOf(currentQuestion) );
    }

    private void previousQuestion(){
        if(currentQuestion == 1)
            return;

        addFragment(new int[]{currentQuestion-1}, new WritingFragment(writings.get(currentQuestion-1-1), listBtnQuestion.get(currentQuestion-1-1)));
        currentQuestion--;
        btnQuestionNumber.setText( String.valueOf(currentQuestion) );
    }

    public void btnQuestionClick(View view){
        Button btn = (Button)view;
        int number = Integer.parseInt( btn.getText().toString() );

        addFragment(new int[]{number}, new WritingFragment(writings.get(number-1), listBtnQuestion.get(number-1)));
        btnQuestionNumber.setText( String.valueOf(number) );

        currentQuestion = number;
    }
}