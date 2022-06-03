package com.example.englishlearning;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.englishlearning.Databases.UserDataHelper;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

public class WritingEssayActivity extends AppCompatActivity {

    TextView tvEssaySubject;
    EditText etWriting;
    Button btnSubmit;
    long idSubject;
    String essaySubject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_writing_essay);

        idSubject = getIntent().getLongExtra("idSubject", -1);
        essaySubject = getIntent().getStringExtra("essaySubject");

        tvEssaySubject = findViewById(R.id.tv_essay_subject);
        etWriting = findViewById(R.id.et_writing);
        btnSubmit = findViewById(R.id.btn_submit);

        tvEssaySubject.setText(essaySubject);
        tvEssaySubject.setMovementMethod(new ScrollingMovementMethod());

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitWriting();
            }
        });

        checkSaveWriting();
    }

    private void checkSaveWriting(){
        UserDataHelper helper = new UserDataHelper(this);
        SQLiteDatabase database = helper.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM writing_essay WHERE id=" + String.valueOf(idSubject), null);
        if(cursor.moveToNext()){
            etWriting.setText( cursor.getString(1) );
        }
    }

    private void submitWriting(){
        String login = Utils.getLogin(this);
        DatabaseReference ref = FirebaseDatabase.getInstance()
                .getReference("essay_writing/" + login + "/" + String.valueOf(idSubject));
        Date date = new Date();
        long id = date.getTime();
        ref.child(String.valueOf(id)).child("content").setValue(etWriting.getText().toString().trim());

        ref = FirebaseDatabase.getInstance().getReference("waiting_essay").child(login + "-" + String.valueOf(idSubject) + "-" + String.valueOf(id));
        ref.child("content").setValue(etWriting.getText().toString().trim());
        ref.child("subject").setValue(tvEssaySubject.getText().toString().trim());
        deleteSaveWriting();
        finish();
    }

    private void deleteSaveWriting(){
        UserDataHelper helper = new UserDataHelper(this);
        SQLiteDatabase databaseWrite = helper.getWritableDatabase();
        int count = databaseWrite.delete("writing_essay", "id = ?", new String[]{String.valueOf(idSubject)});
        databaseWrite.close();
        Toast.makeText(this, String.valueOf(count), Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStop() {
        UserDataHelper helper = new UserDataHelper(this);
        SQLiteDatabase database = helper.getReadableDatabase();
        SQLiteDatabase databaseWrite = helper.getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM writing_essay WHERE id=" + String.valueOf(idSubject), null);
        if(cursor.getCount()>0){
            ContentValues contentValues = new ContentValues();
            contentValues.put("content", etWriting.getText().toString().trim());
            databaseWrite.update("writing_essay", contentValues, "id = ?", new String[]{String.valueOf(idSubject)});
        }else{
            ContentValues contentValues = new ContentValues();
            contentValues.put("id", idSubject);
            contentValues.put("content", etWriting.getText().toString().trim());
            databaseWrite.insert("writing_essay", null, contentValues);
        }
        databaseWrite.close();
        database.close();
        super.onStop();
    }
}