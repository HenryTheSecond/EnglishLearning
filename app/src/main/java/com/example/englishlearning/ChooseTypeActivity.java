package com.example.englishlearning;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;

import com.example.englishlearning.Activity.DashBoard;
import com.example.englishlearning.Activity.MainActivity;
import com.example.englishlearning.Activity.PickLevelActivity;

import java.util.HashMap;

public class ChooseTypeActivity extends AppCompatActivity {
    String callingType;
    HashMap<Integer, String> testHash;
    HashMap<Integer, String> reviewHash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_type);

        testHash = new HashMap<>();
        testHash.put(R.id.btn_filling_blank, "filling_blank");
        testHash.put(R.id.btn_listening, "listening");
        testHash.put(R.id.btn_reading, "reading");
        testHash.put(R.id.btn_single_question, "single_question");
        testHash.put(R.id.btn_writing, "writing");
        testHash.put(R.id.btn_all, "all");

        reviewHash = new HashMap<>();
        reviewHash.put(R.id.btn_filling_blank, "practice_filling_blank");
        reviewHash.put(R.id.btn_listening, "practice_listening");
        reviewHash.put(R.id.btn_reading, "practice_reading");
        reviewHash.put(R.id.btn_single_question, "practice_single_question");
        reviewHash.put(R.id.btn_writing, "practice_writing");
        reviewHash.put(R.id.btn_all, "test_record");

        callingType = getIntent().getStringExtra(DashBoard.CALLING_TYPE);
    }

    public void btnClick(View view){
        int viewId = view.getId();
        if(callingType.equals("test")){
            String type = testHash.get(viewId);
            Intent intent = new Intent(ChooseTypeActivity.this, PickLevelActivity.class);
            intent.putExtra("callingType", type);
            startActivity(intent);
        }else{
            String type = reviewHash.get(viewId);
            Intent intent = new Intent(ChooseTypeActivity.this, ReviewListActivity.class);
            intent.putExtra("callingType", type);
            startActivity(intent);
        }
    }
}