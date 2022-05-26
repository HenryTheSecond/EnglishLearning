package com.example.englishlearning.Activity.Admin;

import static com.example.englishlearning.Activity.Admin.AdminDashBoardActivity.FILL_BLANK;
import static com.example.englishlearning.Activity.Admin.AdminDashBoardActivity.TABLE;
import static com.example.englishlearning.Activity.Admin.AdminDashBoardActivity.WRITE;
import static com.example.englishlearning.Activity.Admin.QuestionListActivity.IS_ADD;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
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
import com.example.englishlearning.Model.Writing;
import com.example.englishlearning.MyApplication;
import com.example.englishlearning.R;
import com.example.englishlearning.Utils;
import com.example.englishlearning.UtilsAdmin;

public class AddWriteActivity extends AppCompatActivity {

    private Button btnAddQuestion;
    private EditText etQuestion;
    private EditText etAnswer;
    Spinner spinnerLevel;
    Boolean isAdd;
    Long id;
    Writing writing;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_write);
        btnAddQuestion = findViewById(R.id.btn_add_question);
        etQuestion = findViewById(R.id.et_question);
        etAnswer = findViewById(R.id.et_answer);
        spinnerLevel = findViewById(R.id.spinner_level);
        isAdd = getIntent().getBooleanExtra(IS_ADD, true);
        id = getIntent().getLongExtra("id", -1);


        if(isAdd && id == -1){
            btnAddQuestion.setText("Add");
            btnAddQuestion.setOnClickListener( view ->{
                AddNewQuestion();
            });
        } else{
            btnAddQuestion.setText("Edit");
            setView();
            btnAddQuestion.setOnClickListener( view ->{
                EditQuestion();
            });
        }

        Button btnCancel = findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener( view ->{
            Intent intent = new Intent(AddWriteActivity.this, QuestionListActivity.class);
            intent.putExtra(TABLE, WRITE);
            startActivity(intent);
        });

        ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_list_item_1, new Integer[]{1,2,3});
        spinnerLevel.setAdapter(adapter);
    }

    private void EditQuestion() {
        EnglishHelper helper = new EnglishHelper(MyApplication.getAppContext());
        SQLiteDatabase database = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("question", etQuestion.getText().toString());
        contentValues.put("answer", etAnswer.getText().toString());
        contentValues.put("level", Integer.valueOf(spinnerLevel.getSelectedItem().toString()));
        String selection = ("id" + " = ?");
        String[] selectionArgs = {String.valueOf(id)};
        int idUpdate = database.update("writing", contentValues, selection, selectionArgs);
        if(idUpdate != -1){
            Intent intent = new Intent(AddWriteActivity.this, QuestionListActivity.class);
            intent.putExtra(TABLE, WRITE);
            startActivity(intent);
        }
    }

    private void setView() {
        Cursor cursor = UtilsAdmin.getQuestionById(WRITE,id);
        while (cursor.moveToNext()) {
            writing = new Writing(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getInt(3));
        }
        etQuestion.setText(writing.getQuestion());
        etAnswer.setText(writing.getAnswer());
        spinnerLevel.setSelection(writing.getLevel());
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