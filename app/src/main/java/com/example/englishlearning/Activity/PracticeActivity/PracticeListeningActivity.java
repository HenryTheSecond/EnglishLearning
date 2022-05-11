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
import com.example.englishlearning.Activity.TestQuestionActivity;
import com.example.englishlearning.Databases.UserDataHelper;
import com.example.englishlearning.Model.FillingBlank;
import com.example.englishlearning.Model.Listening;
import com.example.englishlearning.Model.PracticeModel.PracticeListening;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class PracticeListeningActivity extends AppCompatActivity {
    private FrameLayout questionContent;
    private Button btnQuestionNumber;
    private TableLayout tableQuestion;
    private Button btnSubmit;
    private Button btnPrevious;
    private Button btnNext;

    private List<Button> listBtnQuestion;
    private int currentQuestion;

    private List<Listening> listenings;

    private long id;
    DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice_listening);

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
            reference = FirebaseDatabase.getInstance().getReference("practice_listening").child(Utils.getLogin(this));
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot data: snapshot.getChildren()){
                        if(Long.parseLong(data.getKey())>id)
                            id = Long.parseLong(data.getKey());
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) { }
            });
        }

        findAllQuestionButtons();

        int level = getIntent().getIntExtra(PickLevelActivity.LEVEL_KEY, 1);
        getQuestions(level);//Get random question by level

        addFragment(new int[]{1,2,3}, new ListeningFragment(listenings.get(0), listBtnQuestion.get(0), listBtnQuestion.get(1),
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
        listenings = new ArrayList<>();

        Cursor cursor = Utils.getRandomQuestions("listening", 3, level);
        while(cursor.moveToNext()){
            listenings.add(new Listening(cursor.getInt(0), cursor.getString(1), cursor.getString(2),
                    cursor.getString(3), cursor.getInt(4)));
        }
    }

    private void btnSubmitClicked(){
        AlertDialog.Builder builder = new AlertDialog.Builder(PracticeListeningActivity.this);
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

        String idListenings =  "[" + String.valueOf(listenings.get(0).getId()) + "]" + "[" + String.valueOf(listenings.get(1).getId()) + "]" + "[" + String.valueOf(listenings.get(2).getId()) +"]";
        String listeningAnswer = "";
        for(Listening item: listenings){
            for( Listening.ListeningQuestion question: item.getListQuestions() ){
                String idAnswer = Utils.getIdAnswer(this, questionNumber, question.getListChoice());
                listeningAnswer += "[" + String.valueOf(question.getId())  +  ","  +  idAnswer   + "]";

                if(Integer.parseInt(idAnswer) == question.getIdAnswer())
                    correct++;
                questionNumber++;
            }
            listeningAnswer += "/";
        }

        Intent intent = new Intent(PracticeListeningActivity.this, PracticeListeningReview.class);
        if(Utils.isLoggedIn(this)){
            saveToFirebase(date, correct, idListenings, listeningAnswer);
            Gson gson = new Gson();
            PracticeListening record = new PracticeListening((int)id, date, correct, idListenings, listeningAnswer);
            intent.putExtra("record", gson.toJson(record));
        }else{
            long idInsert = saveToSqlite(date, correct, idListenings, listeningAnswer);
            intent.putExtra(PracticeListeningReview.ID_TEST_RECORD_KEY, idInsert);
        }


        startActivity(intent);

        finish();
    }

    private void saveToFirebase(String date, int correct, String idListenings, String listeningAnswer){
        id++;
        DatabaseReference node = reference.child( String.valueOf( id ) );
        node.child("date_time").setValue(date);
        node.child("correct").setValue(correct);
        node.child("id_listenings").setValue(idListenings);
        node.child("listening_answer").setValue(listeningAnswer);
    }

    private long saveToSqlite(String date, int correct, String idListenings, String listeningAnswer){
        UserDataHelper helper = new UserDataHelper(this);
        SQLiteDatabase database = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("date_time", date);
        contentValues.put("correct", correct);
        contentValues.put("id_listenings", idListenings);
        contentValues.put("listening_answer", listeningAnswer);
        return database.insert("practice_listening", null, contentValues);
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
                addFragment(new int[]{7,8,9}, new ListeningFragment(listenings.get(2), listBtnQuestion.get(6), listBtnQuestion.get(7),
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
                addFragment(new int[]{1,2,3}, new ListeningFragment(listenings.get(0), listBtnQuestion.get(0), listBtnQuestion.get(1),
                        listBtnQuestion.get(2)));
                btnQuestionNumber.setText("1-3");
                currentQuestion = 3;
                break;
            }

            //Filling Blank
            case 7:
            case 8:
            case 9:{
                addFragment(new int[]{4,5,6}, new ListeningFragment(listenings.get(1), listBtnQuestion.get(3), listBtnQuestion.get(4),
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
            case 9:{
                addFragment(new int[]{7,8,9}, new ListeningFragment(listenings.get(2), listBtnQuestion.get(6), listBtnQuestion.get(7),
                        listBtnQuestion.get(8)));
                btnQuestionNumber.setText("7-9");
                break;
            }

        }
        currentQuestion = number;
    }
}