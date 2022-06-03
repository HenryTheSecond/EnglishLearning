package com.example.englishlearning;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.englishlearning.Model.EssayWriting.EssayWriting;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

public class DetailWritingActivity extends AppCompatActivity {

    EssayWriting item;
    TextView tvEssaySubject, tvWriting, tvPoint, tvComment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_writing);

        tvEssaySubject = findViewById(R.id.tv_essay_subject);
        tvWriting = findViewById(R.id.tv_writing);
        tvPoint = findViewById(R.id.tv_point);
        tvComment = findViewById(R.id.tv_comment);

        Gson gson = new Gson();
        item = gson.fromJson( getIntent().getStringExtra("item"), EssayWriting.class );

        tvWriting.setText( item.getContent() );
        if (item.isPointed()) {
            tvPoint.setText( String.valueOf(item.getPoint()) );
            tvComment.setText(item.getFeedback());
        }else{
            tvPoint.setText( "Waiting" );
            tvComment.setText( "Waiting" );
        }

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("essay_subject")
                .child(String.valueOf(item.getIdSubject()))
                .child("subject");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tvEssaySubject.setText(snapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}