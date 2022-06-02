package com.example.englishlearning.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.FragmentManager;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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

    SearchView searchView;
    Spinner spinnerType;
    Spinner spinnerContentMeaning;


    ValueEventListener listener;
    DatabaseReference reference;

//    private int[] id = {0};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        FragmentManager fragmentManager = getFragmentManager();

        //Binding view
        recyclerView = findViewById(R.id.recycler_item);
        btnAdd = findViewById(R.id.btn_add);
        searchView = findViewById(R.id.search_view);
        spinnerType = findViewById(R.id.spinner_type);
        spinnerContentMeaning = findViewById(R.id.spinner_content_meaning);

        ArrayAdapter<String> adapterType = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, new ArrayList<String>());
        adapterType.add("All");
        for(NotedWord.Type type: NotedWord.Type.values()){
            adapterType.add(type.toString());
        }
        spinnerType.setAdapter(adapterType);
        spinnerType.setSelection(0);

        ArrayAdapter<String> adapterContentMeaning = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, new String[]{"Content", "Meaning"});
        spinnerContentMeaning.setAdapter(adapterContentMeaning);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                adapter.setType( spinnerType.getSelectedItem().toString() );
                adapter.setContentMeaning( spinnerContentMeaning.getSelectedItem().toString() );
                adapter.getFilter().filter(searchView.getQuery().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinnerContentMeaning.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                adapter.setType( spinnerType.getSelectedItem().toString() );
                adapter.setContentMeaning( spinnerContentMeaning.getSelectedItem().toString() );
                adapter.getFilter().filter(searchView.getQuery().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.setType( spinnerType.getSelectedItem().toString() );
                adapter.setContentMeaning( spinnerContentMeaning.getSelectedItem().toString() );
                adapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        String[] data = null;


        if(Utils.isLoggedIn(this)){
            listener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot data: snapshot.getChildren()){
                        NotedWord word = new NotedWord( Long.parseLong(data.getKey()),
                                data.child("content").getValue(String.class),
                                data.child("meaning").getValue(String.class),
                                NotedWord.Type.parseType( data.child("type").getValue(String.class)) );
                        adapter.getOriginList().add(word);
                    }
                    adapter.setList( adapter.getOriginList() );
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
                if(reference != null)
                    reference.removeEventListener(listener);
                ErrorDialogFragment.newInstance(
                        adapter,
                        true,
                        true

                ).show(fragmentManager, ErrorDialogFragment.class.getSimpleName());
            }
        });

        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);
    }

    private void getData() {
        UserDataHelper helper = new UserDataHelper(this);
        SQLiteDatabase database = helper.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + "note_word";
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                adapter.getOriginList().add( new NotedWord(cursor.getInt(0), cursor.getString(1), cursor.getString(2), NotedWord.Type.parseType(cursor.getString(3))));
            } while (cursor.moveToNext());
        }

        adapter.setList( adapter.getOriginList() );
        database.close();
    }

    private void getDataFromFirebase(){
        reference = FirebaseDatabase.getInstance().getReference("note_word").child(Utils.getLogin(this));

        reference.addValueEventListener( listener );

    }

    @Override
    public void onBackPressed() {
        if(!searchView.isIconified()){
            searchView.setIconified(true);
            return;
        }
        super.onBackPressed();
    }
}