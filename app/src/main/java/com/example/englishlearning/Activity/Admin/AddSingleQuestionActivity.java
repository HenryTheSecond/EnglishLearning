package com.example.englishlearning.Activity.Admin;

import static com.example.englishlearning.Activity.Admin.AdminDashBoardActivity.FILL_BLANK;
import static com.example.englishlearning.Activity.Admin.AdminDashBoardActivity.SINGLE_QUESTION;
import static com.example.englishlearning.Activity.Admin.AdminDashBoardActivity.TABLE;
import static com.example.englishlearning.Activity.Admin.QuestionListActivity.IS_ADD;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
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
import com.example.englishlearning.Model.NotedWord;
import com.example.englishlearning.MyApplication;
import com.example.englishlearning.R;
import com.example.englishlearning.UtilsAdmin;

import java.util.HashMap;

public class AddSingleQuestionActivity extends AppCompatActivity {

    EditText edtQuestion;
    Spinner spinnerLevel;
    View multipleChoice1;
    Boolean isAdd;
    Long id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_single_question);

        isAdd = getIntent().getBooleanExtra(IS_ADD, true);
        id = getIntent().getLongExtra("id", -1);
        edtQuestion = findViewById(R.id.edt_question1);
        spinnerLevel = findViewById(R.id.spinner_level);


        Button btnAdd = findViewById(R.id.btn_add_question);
        Button btnCancel = findViewById(R.id.btn_cancel);

        if(isAdd && id != -1){
            btnAdd.setText("Add");
            btnAdd.setOnClickListener( view ->{
                addSingleQuestion();
            });
        } else{
            btnAdd.setText("Edit");
            btnAdd.setOnClickListener( view ->{
                Toast.makeText(this, String.valueOf(id), Toast.LENGTH_SHORT).show();
            });
        }

        btnCancel.setOnClickListener( view ->{
            Intent intent = new Intent(AddSingleQuestionActivity.this, QuestionListActivity.class);
            intent.putExtra(TABLE, SINGLE_QUESTION);
            startActivity(intent);
        });

        ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_list_item_1, new Integer[]{1,2,3});
        spinnerLevel.setAdapter(adapter);

    }

    private void addSingleQuestion(){
        HashMap<Integer, Integer> radioMapToIndex = new HashMap<>();
        radioMapToIndex.put(R.id.rbt_answer_a, 0);
        radioMapToIndex.put(R.id.rbt_answer_b, 1);
        radioMapToIndex.put(R.id.rbt_answer_c, 2);
        radioMapToIndex.put(R.id.rbt_answer_d, 3);


        multipleChoice1 = findViewById(R.id.multiple_choice1);

        if(edtQuestion.getText().toString().trim().equals("")){
            Toast.makeText(this, "Please fulfill the question", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!UtilsAdmin.checkSingleQuestion(multipleChoice1))
            return;

        RadioGroup radioGroup = multipleChoice1.findViewById(R.id.radio_group);
        long[] listId = UtilsAdmin.insertSingleQuestion(multipleChoice1);
        String idMultipleChoice = "";
        for(long id: listId){
            idMultipleChoice += "[" + String.valueOf(id) + "]";
        }

        EnglishHelper helper = new EnglishHelper(MyApplication.getAppContext());
        SQLiteDatabase database = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("question", edtQuestion.getText().toString());
        contentValues.put("id_multiple_choice", idMultipleChoice);
        contentValues.put("id_answer", listId[ radioMapToIndex.get(radioGroup.getCheckedRadioButtonId()) ]);
        contentValues.put("level", Integer.parseInt( spinnerLevel.getSelectedItem().toString() ));

        long insertId = database.insert("single_question", null, contentValues);
        if(insertId != -1)
            Toast.makeText(this, "Insert successfully " + String.valueOf(insertId) , Toast.LENGTH_SHORT).show();
    }
}