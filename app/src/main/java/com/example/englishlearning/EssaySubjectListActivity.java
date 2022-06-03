package com.example.englishlearning;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Pair;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EssaySubjectListActivity extends AppCompatActivity {
    DatabaseReference reference;
    ListView lvSubject;
    EssaySubjectAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_essay_subject_list);

        lvSubject = findViewById(R.id.lv_essay_subject);

        adapter = new EssaySubjectAdapter(this);

        reference = FirebaseDatabase.getInstance().getReference("essay_subject");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot child: snapshot.getChildren()){
                    Pair<Long, String> item = new Pair<>( Long.parseLong(child.getKey()), child.child("subject").getValue(String.class) );
                    adapter.getList().add(item);
                    lvSubject.setAdapter(adapter);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }
}