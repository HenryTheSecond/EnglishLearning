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
import com.example.englishlearning.Model.FillingBlank;
import com.example.englishlearning.Model.PracticeModel.PracticeFillingBlank;
import com.example.englishlearning.QuestionFragment.FillingBlankParagraphFragment;
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

public class PracticeFillingBlankActivity extends AppCompatActivity {

    private FrameLayout questionContent;
    private Button btnQuestionNumber;
    private TableLayout tableQuestion;
    private Button btnSubmit;
    private Button btnPrevious;
    private Button btnNext;

    private List<Button> listBtnQuestion;
    private int currentQuestion;

    private List<FillingBlank> fillingBlanks;

    private long id = 0;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice_filling_blank);

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
            reference = FirebaseDatabase.getInstance().getReference("practice_filling_blank").child(Utils.getLogin(this));
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
        getQuestions(level);


        addFragment(new int[]{1,2,3,4}, new FillingBlankParagraphFragment(fillingBlanks.get(0), listBtnQuestion.get(0), listBtnQuestion.get(1),
                listBtnQuestion.get(2), listBtnQuestion.get(3)));
        currentQuestion = 1;
        btnQuestionNumber.setText("1-4");
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
        fillingBlanks = new ArrayList<>();

        Cursor cursor = Utils.getRandomQuestions("filling_blank", 3, level);
        while(cursor.moveToNext()){
            fillingBlanks.add(new FillingBlank(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getInt(3)));
        }
    }

    private void btnSubmitClicked(){
        AlertDialog.Builder builder = new AlertDialog.Builder(PracticeFillingBlankActivity.this);
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

        String idFillingBlanks =  "[" + String.valueOf(fillingBlanks.get(0).getId()) + "]" + "[" + String.valueOf(fillingBlanks.get(1).getId()) + "]" + "[" + String.valueOf(fillingBlanks.get(2).getId()) +"]";
        String fillingBlankAnswer = "";
        for(FillingBlank item: fillingBlanks){
            for( FillingBlank.FillingBlankQuestion question: item.getListQuestions() ){
                String idAnswer = Utils.getIdAnswer(this, questionNumber, question.getListChoice());
                fillingBlankAnswer += "[" + String.valueOf(question.getId())  +  ","  +  idAnswer   + "]";

                if(Integer.parseInt(idAnswer) == question.getIdAnswer())
                    correct++;
                questionNumber++;
            }
            fillingBlankAnswer += "/";
        }


        Intent intent = new Intent(PracticeFillingBlankActivity.this, PracticeFillingBlankReview.class);
        if(Utils.isLoggedIn(this)){
            saveToFirebase(date, correct, idFillingBlanks, fillingBlankAnswer);
            Gson gson = new Gson();
            PracticeFillingBlank record = new PracticeFillingBlank((int)id, date, correct, idFillingBlanks, fillingBlankAnswer);
            intent.putExtra("record", gson.toJson(record));
        }else{
            long idInsert = saveToSqlite(date, correct, idFillingBlanks, fillingBlankAnswer);
            intent.putExtra(PracticeFillingBlankReview.ID_TEST_RECORD_KEY, idInsert);
        }

        startActivity(intent);

        finish();
    }

    private void saveToFirebase(String date, int correct, String idFillingBlanks, String fillingBlankAnswer){
        id++;
        DatabaseReference node = reference.child( String.valueOf( id ) );
        node.child("date_time").setValue(date);
        node.child("correct").setValue(correct);
        node.child("id_filling_blanks").setValue(idFillingBlanks);
        node.child("filling_blank_answer").setValue(fillingBlankAnswer);
    }

    private long saveToSqlite(String date, int correct, String idFillingBlanks, String fillingBlankAnswer){
        UserDataHelper helper = new UserDataHelper(this);
        SQLiteDatabase database = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("date_time", date);
        contentValues.put("correct", correct);
        contentValues.put("id_filling_blanks", idFillingBlanks);
        contentValues.put("filling_blank_answer", fillingBlankAnswer);
        long idInsert = database.insert("practice_filling_blank", null, contentValues);
        return idInsert;
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
            case 3:
            case 4:{
                addFragment(new int[]{5,6,7,8}, new FillingBlankParagraphFragment(fillingBlanks.get(1), listBtnQuestion.get(4),
                        listBtnQuestion.get(5), listBtnQuestion.get(6), listBtnQuestion.get(7)));
                btnQuestionNumber.setText("5-8");
                currentQuestion = 5;
                break;
            }
            case 5:
            case 6:
            case 7:
            case 8:{
                addFragment(new int[]{9,10,11,12}, new FillingBlankParagraphFragment(fillingBlanks.get(2), listBtnQuestion.get(8),
                        listBtnQuestion.get(9), listBtnQuestion.get(10), listBtnQuestion.get(11)));
                btnQuestionNumber.setText("9-12");
                currentQuestion = 9;
                break;
            }

        }
    }

    private void previousQuestion(){
        switch(currentQuestion){
            case 5:
            case 6:
            case 7:
            case 8:{
                addFragment(new int[]{1,2,3,4}, new FillingBlankParagraphFragment(fillingBlanks.get(0), listBtnQuestion.get(0),
                        listBtnQuestion.get(1), listBtnQuestion.get(2), listBtnQuestion.get(3)));
                btnQuestionNumber.setText("1-4");
                currentQuestion = 4;
                break;
            }
            case 9:
            case 10:
            case 11:
            case 12:{
                addFragment(new int[]{5,6,7,8}, new FillingBlankParagraphFragment(fillingBlanks.get(1), listBtnQuestion.get(4),
                        listBtnQuestion.get(5), listBtnQuestion.get(6), listBtnQuestion.get(7)));
                btnQuestionNumber.setText("5-8");
                currentQuestion = 5;
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
            case 3:
            case 4:{
                addFragment(new int[]{1,2,3,4}, new FillingBlankParagraphFragment(fillingBlanks.get(0), listBtnQuestion.get(0),
                                                  listBtnQuestion.get(1), listBtnQuestion.get(2), listBtnQuestion.get(3)));
                btnQuestionNumber.setText("1-4");
                break;
            }
            case 5:
            case 6:
            case 7:
            case 8:{
                addFragment(new int[]{5,6,7,8}, new FillingBlankParagraphFragment(fillingBlanks.get(1), listBtnQuestion.get(4),
                        listBtnQuestion.get(5), listBtnQuestion.get(6), listBtnQuestion.get(7)));
                btnQuestionNumber.setText("5-8");
                break;
            }
            case 9:
            case 10:
            case 11:
            case 12:{
                addFragment(new int[]{9,10,11,12}, new FillingBlankParagraphFragment(fillingBlanks.get(2), listBtnQuestion.get(8),
                        listBtnQuestion.get(9), listBtnQuestion.get(10), listBtnQuestion.get(11)));
                btnQuestionNumber.setText("9-12");
                break;
            }

        }
        currentQuestion = number;
    }
}