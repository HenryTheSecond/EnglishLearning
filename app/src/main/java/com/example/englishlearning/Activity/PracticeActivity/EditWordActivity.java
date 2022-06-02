package com.example.englishlearning.Activity.PracticeActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.englishlearning.Activity.NoteActivity;
import com.example.englishlearning.Databases.UserDataHelper;
import com.example.englishlearning.Model.NotedWord;
import com.example.englishlearning.MyApplication;
import com.example.englishlearning.R;
import com.example.englishlearning.Utils;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditWordActivity extends AppCompatActivity {

    private EditText edtContent;
    private EditText edtMeaning;
    private ArrayAdapter<NotedWord.Type> adapter;
    private Spinner spinner;
    private View btnEdit;
    private View btnCancel;

    long id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_word);
        edtContent = findViewById(R.id.edt_content);
        edtMeaning = findViewById(R.id.edt_meaning);
        spinner = findViewById(R.id.spinner_type);
        adapter = new ArrayAdapter<NotedWord.Type>(this, android.R.layout.simple_list_item_1, NotedWord.Type.values());
        spinner.setAdapter(adapter);
        btnEdit = findViewById(R.id.btnOne);
        btnCancel = findViewById(R.id.btnTwo);

        id = getIntent().getLongExtra("id", -1);
        edtContent.setText(getIntent().getStringExtra("content"));
        edtMeaning.setText(getIntent().getStringExtra("meaning"));
        spinner.setSelection(adapter.getPosition(NotedWord.Type.parseType(getIntent().getStringExtra("type"))));

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Utils.isLoggedIn(view.getContext())){
                    saveToFirebase(view);
                }else
                    saveToSqlite(view);
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), NoteActivity.class);
                startActivity(intent);
            }
        });

        Utils.createMenu(this);
    }

    private void saveToFirebase(View view){
        String login = Utils.getLogin(this);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("note_word/" + login).child(String.valueOf(id));

        reference.child("content").setValue( edtContent.getText().toString() );
        reference.child("meaning").setValue( edtMeaning.getText().toString() );
        reference.child("type").setValue( spinner.getSelectedItem().toString() );
        Intent intent = new Intent(view.getContext(), NoteActivity.class);
        startActivity(intent);
        finish();
    }

    private void saveToSqlite(View view){
        UserDataHelper helper = new UserDataHelper(MyApplication.getAppContext());
        SQLiteDatabase database = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("content", edtContent.getText().toString());
        contentValues.put("meaning", edtMeaning.getText().toString());
        contentValues.put("type", spinner.getSelectedItem().toString());
        System.out.print(edtContent.getText().toString());
        System.out.print(edtMeaning.getText().toString());
        // The columns for the WHERE clause
        String selection = ("id" + " = ?");
        // The values for the WHERE clause
        String[] selectionArgs = {String.valueOf(id)};
        int idUpdate = database.update("note_word", contentValues, selection, selectionArgs);
        if(idUpdate != -1){
            Intent intent = new Intent(view.getContext(), NoteActivity.class);
            startActivity(intent);
            finish();
        }
    }
}