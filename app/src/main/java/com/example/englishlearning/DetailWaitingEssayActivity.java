package com.example.englishlearning;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.englishlearning.EssayNotification.UtilsEssay;
import com.example.englishlearning.Model.EssayWriting.WaitingEssay;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

public class DetailWaitingEssayActivity extends AppCompatActivity {

    WaitingEssay item;
    TextView tvEssaySubject, tvWriting;
    EditText etPoint, etComment;
    Button btnDone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_waiting_essay);

        tvEssaySubject = findViewById(R.id.tv_essay_subject);
        tvWriting = findViewById(R.id.tv_writing);
        etPoint = findViewById(R.id.et_point);
        etComment = findViewById(R.id.et_comment);
        btnDone = findViewById(R.id.btn_done);

        Gson gson = new Gson();
        item = gson.fromJson( getIntent().getStringExtra("item"), WaitingEssay.class );

        tvEssaySubject.setText( item.getSubject() ); tvWriting.setText(item.getContent());

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnDoneClick();
            }
        });
    }

    private void btnDoneClick(){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("essay_writing")
                .child(item.getUser().trim())
                .child(String.valueOf(item.getIdSubject()))
                .child(String.valueOf(item.getId()));
        if(validatePoint()){
            ref.child("point").setValue(Float.parseFloat(etPoint.getText().toString()));
            ref.child("feedback").setValue(etComment.getText().toString());
            ref = FirebaseDatabase.getInstance().getReference("waiting_essay")
                    .child(item.getUser() + "-" + String.valueOf(item.getIdSubject()) + "-" + String.valueOf(item.getId()));
            ref.removeValue();
            UtilsEssay.sendNotification(item.getSubject(), etPoint.getText().toString(), etComment.getText().toString());

            Intent intent = new Intent();
            intent.putExtra("position", getIntent().getIntExtra("position", -1));
            setResult(RESULT_OK, intent);

            finish();
        }else
            Toast.makeText(this, "Point is not valid", Toast.LENGTH_SHORT).show();
    }

    private boolean validatePoint(){
        float point = Float.parseFloat(etPoint.getText().toString());
        if( point>=0 && point<=10 )
            return true;
        return false;
    }
}