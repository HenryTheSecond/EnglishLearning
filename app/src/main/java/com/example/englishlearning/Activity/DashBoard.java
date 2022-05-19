package com.example.englishlearning.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.englishlearning.R;
import com.example.englishlearning.Utils;

public class DashBoard extends AppCompatActivity {

    Button btnListen, btnRead, btnWrite, btnTest, btnNote, btnReview;
    public static final String CALLING_TYPE = "type";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);


        btnTest = findViewById(R.id.btn_test);
        btnNote = findViewById(R.id.btn_note);
        btnReview = findViewById(R.id.btn_review);

        btnTest.setOnClickListener(view -> {typeClick(view);});
        btnReview.setOnClickListener(view -> {typeClick(view);});

        btnNote.setOnClickListener(view ->{
            moveToActivity(NoteActivity.class);
        });

        Utils.createMenu(this);
    }

    private void moveToActivity(Class activity) {
        Intent intent = new Intent(DashBoard.this, NoteActivity.class);
        startActivity(intent);
    }

    private void typeClick(View view){
        int viewId = view.getId();
        Intent intent = new Intent(DashBoard.this, ChooseTypeActivity.class);
        if(viewId == R.id.btn_test){
            intent.putExtra(CALLING_TYPE, "test");
        }else{
            intent.putExtra(CALLING_TYPE, "review");
        }
        startActivity(intent);
    }




}