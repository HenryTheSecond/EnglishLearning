package com.example.englishlearning.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.FragmentManager;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.englishlearning.Databases.UserDataHelper;
import com.example.englishlearning.ErrorDialogFragment;
import com.example.englishlearning.Model.NotedWord;
import com.example.englishlearning.NoteAdapter;
import com.example.englishlearning.R;
import com.example.englishlearning.Utils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NoteActivity extends AppCompatActivity {
    private NoteAdapter adapter;
    private RecyclerView recyclerView;
    private Button btnAdd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        FragmentManager fragmentManager = getFragmentManager();

        //Binding view
        recyclerView = findViewById(R.id.recycler_item);
        btnAdd = findViewById(R.id.btn_add);



        String[] data = null;


        adapter = new NoteAdapter(this);

        if(Utils.isLoggedIn(this))
            getDataFromFirebase();
        else
            getData();


        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Add Listener
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ErrorDialogFragment.newInstance(
                        -1,
                        null,
                        true,
                        true,
                        true
                ).show(fragmentManager, ErrorDialogFragment.class.getSimpleName());
            }
        });
    }

    private void getData() {
        UserDataHelper helper = new UserDataHelper(this);
        SQLiteDatabase database = helper.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + "note_word";
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                adapter.getList().add( new NotedWord(cursor.getInt(0), cursor.getString(1), cursor.getString(2), NotedWord.Type.parseType(cursor.getString(3))));
            } while (cursor.moveToNext());
        }
        database.close();
    }

    private void getDataFromFirebase(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("note_word").child(Utils.getLogin(this));
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data: snapshot.getChildren()){
                    System.out.println(data);
                    NotedWord word = new NotedWord( Integer.parseInt(data.getKey()),
                            data.child("content").getValue(String.class),
                            data.child("meaning").getValue(String.class),
                            NotedWord.Type.parseType( data.child("type").getValue(String.class)));
                    adapter.getList().add(word);
                    System.out.println(word.getContent());
                }
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}