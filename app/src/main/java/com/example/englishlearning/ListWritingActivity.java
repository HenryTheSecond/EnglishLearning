package com.example.englishlearning;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import com.example.englishlearning.Model.EssayWriting.EssayWriting;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ListWritingActivity extends AppCompatActivity {

    ListView lvWriting;
    DatabaseReference ref;
    long idSubject;

    WritingAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_writing);


        idSubject = getIntent().getLongExtra("idSubject", -1);

        String user = Utils.getLogin(this);

        lvWriting = findViewById(R.id.lv_writing);
        adapter = new WritingAdapter(this);
        ref = FirebaseDatabase.getInstance().getReference("essay_writing")
                .child(user)
                .child(String.valueOf(idSubject));
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot child: snapshot.getChildren()){
                    EssayWriting item = new EssayWriting(user, idSubject, Long.parseLong(child.getKey()),
                            child.child("content").getValue(String.class));
                    if(child.child("point").exists()){
                        item.setPoint( child.child("point").getValue(Float.class) );
                        item.setFeedback(child.child("feedback").getValue(String.class));
                        item.setPointed(true);
                    }
                    adapter.getList().add(item);
                    System.out.println(adapter.getList().size());
                    lvWriting.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}