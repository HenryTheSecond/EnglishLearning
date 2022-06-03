package com.example.englishlearning.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.example.englishlearning.Databases.UserDataHelper;
import com.example.englishlearning.Model.PracticeModel.GeneralPractice;
import com.example.englishlearning.Model.PracticeModel.PracticeFillingBlank;
import com.example.englishlearning.Model.PracticeModel.PracticeListening;
import com.example.englishlearning.Model.PracticeModel.PracticeReading;
import com.example.englishlearning.Model.PracticeModel.PracticeSingleQuestion;
import com.example.englishlearning.Model.PracticeModel.PracticeWriting;
import com.example.englishlearning.Model.ReviewModel.RawTestRecord;
import com.example.englishlearning.Model.ReviewModel.TestRecord;
import com.example.englishlearning.R;
import com.example.englishlearning.ReviewAdapter;
import com.example.englishlearning.Utils;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ReviewListActivity extends AppCompatActivity {

    String tableName;
    ListView lvItem;
    ReviewAdapter adapter;
    String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_list);


        tableName = getIntent().getStringExtra("callingType");
        lvItem = findViewById(R.id.lv_result);
        user = getIntent().getStringExtra("name");

        adapter = new ReviewAdapter(this);

        if(Utils.isLoggedIn(this)){
            if (user != null){
                getDataFromFirebase(user);
            } else{
                getDataFromFirebase();
            }
        }else{
            getDataFromSqlite();
        }
        lvItem.setAdapter(adapter);

        Utils.createMenu(this);
    }

    private void getDataFromSqlite(){
        UserDataHelper helper = new UserDataHelper(this);
        SQLiteDatabase database = helper.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM " + tableName, null);


        switch(tableName){
            case "practice_filling_blank":{
                while(cursor.moveToNext()){
                    adapter.getList().add( new PracticeFillingBlank(cursor.getInt(0), cursor.getString(1),
                            cursor.getInt(2), cursor.getString(3), cursor.getString(4)) );
                }
                break;
            }
            case "practice_listening":{
                while (cursor.moveToNext()){
                    adapter.getList().add( new PracticeListening(cursor.getInt(0), cursor.getString(1),
                            cursor.getInt(2), cursor.getString(3), cursor.getString(4)) );
                }
                break;
            }
            case "practice_reading":{
                while (cursor.moveToNext()){
                    adapter.getList().add( new PracticeReading(cursor.getInt(0), cursor.getString(1),
                            cursor.getInt(2), cursor.getString(3), cursor.getString(4)) );
                }
                break;
            }
            case "practice_single_question":{
                while(cursor.moveToNext()){
                    adapter.getList().add( new PracticeSingleQuestion(cursor.getInt(0), cursor.getString(1),
                            cursor.getInt(2), cursor.getString(3)) );
                }
                break;
            }
            case "practice_writing":{
                while(cursor.moveToNext()){
                    adapter.getList().add( new PracticeWriting(cursor.getInt(0), cursor.getString(1), cursor.getInt(2), cursor.getString(3)));
                }
                break;
            }
            case "test_record":{
                while(cursor.moveToNext()){
                    adapter.getList().add(new RawTestRecord(cursor.getInt(0), cursor.getString(1),
                            cursor.getDouble(2), cursor.getString(3),
                            cursor.getString(4), cursor.getString(5),
                            cursor.getString(6), cursor.getString(7),
                            cursor.getString(8),cursor.getString(9),
                            cursor.getString(10)));
                }
                break;
            }
        }

    }

    private void getDataFromFirebase(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(tableName + "/" + Utils.getLogin(this));
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                adapter.getList().add( getItem(snapshot) );
                lvItem.setAdapter(adapter);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getDataFromFirebase(String user){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(tableName + "/" + user);
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                adapter.getList().add( getItem(snapshot) );
                lvItem.setAdapter(adapter);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private GeneralPractice getItem(DataSnapshot snapshot){
        long id = Long.parseLong(snapshot.getKey());
        String dateTime = snapshot.child("date_time").getValue(String.class);
        int correct = -1;
        double point = -1;
        if(tableName.equals("test_record"))
            point = snapshot.child("point").getValue(Double.class);
        else
            correct = snapshot.child("correct").getValue(Integer.class);
        switch(tableName){
            case "practice_filling_blank":{
                String idFillingBlanks = snapshot.child("id_filling_blanks").getValue(String.class);
                String answer = snapshot.child("filling_blank_answer").getValue(String.class);
                return new PracticeFillingBlank(id, dateTime, correct, idFillingBlanks, answer);
            }
            case "practice_listening":{
                String idListenings = snapshot.child("id_listenings").getValue(String.class);
                String answer = snapshot.child("listening_answer").getValue(String.class);
                return new PracticeListening(id, dateTime, correct, idListenings, answer);
            }
            case "practice_reading":{
                String idReadings = snapshot.child("id_readings").getValue(String.class);
                String answer = snapshot.child("reading_answer").getValue(String.class);
                return new PracticeReading(id, dateTime, correct, idReadings, answer);
            }
            case "practice_single_question":{
                String answer = snapshot.child("single_answer").getValue(String.class);
                return new PracticeSingleQuestion(id, dateTime, correct, answer);
            }
            case "practice_writing":{
                String answer = snapshot.child("writing_answer").getValue(String.class);
                return new PracticeWriting(id, dateTime, correct, answer);
            }
            case "test_record":{
                String fillingBlankAnswer = snapshot.child("filling_blank_answer").getValue(String.class);
                String idFillingBlank = snapshot.child("id_filling_blank").getValue(String.class);
                String idListenings = snapshot.child("id_listenings").getValue(String.class);
                String idReadings = snapshot.child("id_reading").getValue(String.class);
                String listeningAnswer = snapshot.child("listening_answer").getValue(String.class);
                String readingAnswer = snapshot.child("reading_answer").getValue(String.class);
                String singleQuestion = snapshot.child("single_question").getValue(String.class);
                String writing = snapshot.child("writing").getValue(String.class);
                return new RawTestRecord(id, dateTime, point, idListenings, listeningAnswer, idFillingBlank, fillingBlankAnswer,
                                           idReadings, readingAnswer, singleQuestion, writing);
            }
        }
        return null;
    }
}