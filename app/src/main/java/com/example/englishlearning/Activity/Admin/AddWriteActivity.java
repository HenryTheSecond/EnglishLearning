package com.example.englishlearning.Activity.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.englishlearning.Databases.UserDataHelper;
import com.example.englishlearning.MyApplication;
import com.example.englishlearning.R;

public class AddWriteActivity extends AppCompatActivity {

    private Button btnCancel;
    private Button btnAddQuestion;
    private EditText etQuestion;
    private EditText etAnswer;
    private EditText etLevel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_write);
        btnCancel = findViewById(R.id.btn_cancel);
        btnAddQuestion = findViewById(R.id.btn_add_question);
        etQuestion = findViewById(R.id.et_question);
        etAnswer = findViewById(R.id.et_answer);
        etLevel = findViewById(R.id.et_level);

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
    }

    private void AddNewQuestion() {
        UserDataHelper helper = new UserDataHelper(MyApplication.getAppContext());
        SQLiteDatabase database = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("question", etQuestion.getText().toString());
        contentValues.put("answer", etAnswer.getText().toString());
        contentValues.put("level", Integer.valueOf(etLevel.getText().toString()));
        long idInsert = database.insert("writing", null, contentValues);
        if(idInsert != -1){
            Toast.makeText(this, "Add success", Toast.LENGTH_SHORT).show();
        }
    }
}