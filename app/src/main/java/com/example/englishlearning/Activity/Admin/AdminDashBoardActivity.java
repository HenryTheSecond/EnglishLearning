package com.example.englishlearning.Activity.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.englishlearning.Activity.ChooseTypeActivity;
import com.example.englishlearning.Activity.DashBoard;
import com.example.englishlearning.Activity.NoteActivity;
import com.example.englishlearning.R;
import com.example.englishlearning.Utils;

public class AdminDashBoardActivity extends AppCompatActivity {

    public static final String TABLE = "table";
    public static final String WRITE = "writing";
    public static final String FILL_BLANK = "filling_blank";
    public static final String READ = "reading";
    public static final String SINGLE_QUESTION = "single_question";
    private Button btnFill, btnRead, btnWrite, btnSingle, btnUserTestReview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dash_board);
        btnFill = findViewById(R.id.btn_filling_blank);
        btnSingle = findViewById(R.id.btn_single_question);
        btnRead = findViewById(R.id.btn_reading);
        btnWrite = findViewById(R.id.btn_writing);
        btnUserTestReview = findViewById(R.id.btn_user);

        btnFill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveToActivity(FILL_BLANK);
            }
        });

        btnSingle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveToActivity(SINGLE_QUESTION);
            }
        });

        btnRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveToActivity(READ);
            }
        });


        btnWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveToActivity(WRITE);
            }
        });

        btnUserTestReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminDashBoardActivity.this, ListUserActivity.class);
                startActivity(intent);
            }
        });

        Utils.createMenu(this);
    }

    private void moveToActivity(String type) {
        Intent intent = new Intent(AdminDashBoardActivity.this, QuestionListActivity.class);
        intent.putExtra(TABLE, type);
        startActivity(intent);
    }
}