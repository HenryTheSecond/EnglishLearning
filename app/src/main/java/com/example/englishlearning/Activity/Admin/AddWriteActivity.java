package com.example.englishlearning.Activity.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.englishlearning.Databases.EnglishHelper;
import com.example.englishlearning.Databases.UserDataHelper;
import com.example.englishlearning.MyApplication;
import com.example.englishlearning.R;

public class AddWriteActivity extends AppCompatActivity {

    private Button btnCancel;
    private Button btnAddQuestion;
    private EditText etQuestion;
    private EditText etAnswer;
    Spinner spinnerLevel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_write);
        btnCancel = findViewById(R.id.btn_cancel);
        btnAddQuestion = findViewById(R.id.btn_add_question);
        etQuestion = findViewById(R.id.et_question);
        etAnswer = findViewById(R.id.et_answer);
        spinnerLevel = findViewById(R.id.spinner_level);

        btnAddQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddNewQuestion();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_list_item_1, new Integer[]{1,2,3});
        spinnerLevel.setAdapter(adapter);
    }

    private void AddNewQuestion() {
        EnglishHelper helper = new EnglishHelper(MyApplication.getAppContext());
        SQLiteDatabase database = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("question", etQuestion.getText().toString());
        contentValues.put("answer", etAnswer.getText().toString());
        contentValues.put("level", Integer.valueOf(spinnerLevel.getSelectedItem().toString()));
        long idInsert = database.insert("writing", null, contentValues);
        if(idInsert != -1){
            Toast.makeText(this, "Add success", Toast.LENGTH_SHORT).show();
        }
    }
}