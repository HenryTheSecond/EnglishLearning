package com.example.englishlearning.Activity.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.englishlearning.Databases.EnglishHelper;
import com.example.englishlearning.MyApplication;
import com.example.englishlearning.R;
import com.example.englishlearning.UtilsAdmin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AddEssayActivity extends AppCompatActivity {
    EditText edtParagraph;
    List<EditText> listEdtQuestion;
    List<View> listMultipleChoice;
    Spinner spinnerLevel;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_essay);

        listEdtQuestion = new ArrayList<>();
        listMultipleChoice = new ArrayList<>();

        edtParagraph = findViewById(R.id.edt_paragraph);
        listEdtQuestion.add(findViewById(R.id.edt_question1)); listEdtQuestion.add(findViewById(R.id.edt_question2)); listEdtQuestion.add(findViewById(R.id.edt_question3));
        listMultipleChoice.add(findViewById(R.id.multiple_choice1)); listMultipleChoice.add(findViewById(R.id.multiple_choice2)); listMultipleChoice.add(findViewById(R.id.multiple_choice3));
        spinnerLevel = findViewById(R.id.spinner_level);

        ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_list_item_1, new Integer[]{1,2,3});
        spinnerLevel.setAdapter(adapter);

        Button btnAdd = findViewById(R.id.btn_add_question);

        btnAdd.setOnClickListener( view ->{
            addEssay();
        });
    }

    private void addEssay(){
        EnglishHelper helper = new EnglishHelper(MyApplication.getAppContext());
        SQLiteDatabase database = helper.getWritableDatabase();

        HashMap<Integer, Integer> radioMapToIndex = new HashMap<>();
        radioMapToIndex.put(R.id.rbt_answer_a, 0);
        radioMapToIndex.put(R.id.rbt_answer_b, 1);
        radioMapToIndex.put(R.id.rbt_answer_c, 2);
        radioMapToIndex.put(R.id.rbt_answer_d, 3);

        if(checkInput()==false)
            return;


        String idQuestion = "";
        for(int i=0; i<listMultipleChoice.size(); i++){
            RadioGroup radioGroup = listMultipleChoice.get(i).findViewById(R.id.radio_group);
            long[] listId = UtilsAdmin.insertSingleQuestion( listMultipleChoice.get(i) );
            String idMultipleChoice = "";
            for(long id: listId){
                idMultipleChoice += "[" + String.valueOf(id) + "]";
            }

            ContentValues contentValues = new ContentValues();
            contentValues.put("question", listEdtQuestion.get(i).getText().toString());
            contentValues.put("id_multiple_choice", idMultipleChoice);
            contentValues.put("id_answer", listId[ radioMapToIndex.get(radioGroup.getCheckedRadioButtonId()) ]);
            long idInsert = database.insert("reading_question", null, contentValues);
            idQuestion += "[" + String.valueOf(idInsert) + "]";
        }

        ContentValues contentValues = new ContentValues();
        contentValues.put("paragraph", edtParagraph.getText().toString());
        contentValues.put("id_questions", idQuestion);
        contentValues.put("level", Integer.parseInt( spinnerLevel.getSelectedItem().toString() ));
        long idInsert = database.insert("reading", null, contentValues);
        if(idInsert != -1)
            Toast.makeText(this, "Insert successfully " + String.valueOf(idInsert) , Toast.LENGTH_SHORT).show();


    }

    private boolean checkInput(){
        if(edtParagraph.getText().toString().trim().equals("")){
            Toast.makeText(this, "Please fulfill paragraph", Toast.LENGTH_SHORT).show();
            return false;
        }
        for(int i=0; i<listEdtQuestion.size(); i++){
            if(listEdtQuestion.get(i).getText().toString().trim().equals("")){
                Toast.makeText(this, "Please fulfill all the question", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        for(int i=0; i<listMultipleChoice.size(); i++){
            if(!UtilsAdmin.checkSingleQuestion(listMultipleChoice.get(i))){
                return false;
            }
        }
        return true;
    }
}