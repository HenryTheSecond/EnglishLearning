package com.example.englishlearning.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.englishlearning.ErrorDialogFragment;
import com.example.englishlearning.Model.NotedWord;
import com.example.englishlearning.NoteAdapter;
import com.example.englishlearning.R;

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


        List<NotedWord> list = new ArrayList<>();
        list.add( new NotedWord("Go", "Di"));
        list.add(new NotedWord("Visit", NotedWord.Type.verb, "Ghe tham"));

        adapter = new NoteAdapter(list);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Add Listener
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                NotedWord item = new NotedWord();
//                adapter.getListItem().add(item);
//                recyclerView.setAdapter(adapter);

                ErrorDialogFragment.newInstance(
                        false,
                        true,
                        true
                ).show(fragmentManager, ErrorDialogFragment.class.getSimpleName());
            }
        });
    }
}