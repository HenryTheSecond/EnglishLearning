package com.example.englishlearning;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

public class AddEssaySubjectActivity extends AppCompatActivity {

    EditText etSubject;
    Button btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_essay_subject);

        etSubject = findViewById(R.id.et_subject);
        btnAdd = findViewById(R.id.btn_add);

        btnAdd.setOnClickListener(view -> {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("essay_subject");
            Date date = new Date();
            long id = date.getTime();
            String content = etSubject.getText().toString().trim();
            reference.child(String.valueOf(id)).child("subject").setValue(content);
            AddEssaySubjectActivity.this.finish();
        });
    }
}