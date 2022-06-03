package com.example.englishlearning;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import com.example.englishlearning.Model.EssayWriting.WaitingEssay;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ListWaitingEssayActivity extends AppCompatActivity {

    ListView lvWaitingEssay;
    WaitingEssayAdapter adapter;
    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_waiting_essay);

        lvWaitingEssay = findViewById(R.id.lv_waiting_essay);
        adapter = new WaitingEssayAdapter(this);

        ref = FirebaseDatabase.getInstance().getReference("waiting_essay");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot child: snapshot.getChildren()){
                    WaitingEssay item = WaitingEssay.parse(child.getKey(), child.child("content").getValue(String.class), child.child("subject").getValue(String.class));
                    adapter.getList().add(item);
                    lvWaitingEssay.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Utils.createMenu(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK) {
            int position = data.getIntExtra("position", -1);
            adapter.getList().remove(position);
            lvWaitingEssay.setAdapter(adapter);
        }
    }
}