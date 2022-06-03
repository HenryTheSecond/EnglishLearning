package com.example.englishlearning.Activity.Admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.englishlearning.R;
import com.example.englishlearning.UserAdapter;
import com.example.englishlearning.Utils;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class ListUserActivity extends AppCompatActivity {

    RecyclerView lvUser;
    UserAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_user);
        lvUser = findViewById(R.id.lv_user);
        adapter = new UserAdapter(this);
        getDataFromFirebase();
    }
    private void getDataFromFirebase(){
        DatabaseReference database = FirebaseDatabase.getInstance().getReference("user");
        database.orderByChild("username").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                System.out.println(snapshot.getKey());
                adapter.getList().add(snapshot.getKey());
                lvUser.setAdapter(adapter);
                lvUser.setLayoutManager(new LinearLayoutManager(ListUserActivity.this));
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
}