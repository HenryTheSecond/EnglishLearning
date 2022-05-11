package com.example.englishlearning.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.englishlearning.R;

public class DashBoard extends AppCompatActivity {

    Button btnListen, btnRead, btnWrite, btnTest, btnNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);

        btnListen = findViewById(R.id.btn_listen);
        btnRead = findViewById(R.id.btn_read);
        btnWrite = findViewById(R.id.btn_write);
        btnTest = findViewById(R.id.btn_test);
        btnNote = findViewById(R.id.btn_note);

        btnNote.setOnClickListener(view ->{
            moveToActivity(NoteActivity.class);
        });
    }

    private void moveToActivity(Class activity) {
        Intent intent = new Intent(DashBoard.this, NoteActivity.class);
        startActivity(intent);
    }


}