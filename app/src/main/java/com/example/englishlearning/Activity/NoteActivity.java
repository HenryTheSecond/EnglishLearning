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

    ValueEventListener listener;
    DatabaseReference reference;

    private int[] id = {0};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        FragmentManager fragmentManager = getFragmentManager();

        //Binding view
        recyclerView = findViewById(R.id.recycler_item);
        btnAdd = findViewById(R.id.btn_add);



        String[] data = null;


        if(Utils.isLoggedIn(this)){
            listener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot data: snapshot.getChildren()){
                        if(Integer.parseInt(data.getKey()) > id[0]){
                            id[0] = Integer.parseInt(data.getKey());
                        }
                        System.out.println(data);
                        NotedWord word = new NotedWord( Integer.parseInt(data.getKey()),
                                data.child("content").getValue(String.class),
                                data.child("meaning").getValue(String.class),
                                NotedWord.Type.parseType( data.child("type").getValue(String.class)) );
                        adapter.getList().add(word);
                    }
                    id[0]++;
                    recyclerView.setAdapter(adapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            };
            adapter = new NoteAdapter(this, reference, listener);
            getDataFromFirebase();
        }
        else{
            adapter = new NoteAdapter(this);
            getData();
        }


        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Add Listener
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reference.removeEventListener(listener);
                ErrorDialogFragment.newInstance(
                        adapter,
                        true,
                        true,
                        id
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
        reference = FirebaseDatabase.getInstance().getReference("note_word").child(Utils.getLogin(this));

        reference.addValueEventListener( listener );

    }
}