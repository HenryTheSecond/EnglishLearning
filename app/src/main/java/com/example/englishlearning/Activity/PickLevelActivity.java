package com.example.englishlearning.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.englishlearning.Activity.PracticeActivity.PracticeFillingBlankActivity;
import com.example.englishlearning.Activity.PracticeActivity.PracticeListeningActivity;
import com.example.englishlearning.Activity.PracticeActivity.PracticeReadingActivity;
import com.example.englishlearning.Activity.PracticeActivity.PracticeSingleQuestionActivity;
import com.example.englishlearning.Activity.PracticeActivity.PracticeWritingActivity;
import com.example.englishlearning.Model.PracticeModel.PracticeListening;
import com.example.englishlearning.R;
import com.example.englishlearning.Utils;

import java.util.HashMap;

public class PickLevelActivity extends AppCompatActivity {

    public static final String LEVEL_KEY = "level";
    String callingType;
    HashMap<String, Class> testHash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_level);

        TextView tvLevel1 = findViewById(R.id.tv_level_1);
        TextView tvLevel2 = findViewById(R.id.tv_level_2);
        TextView tvLevel3 = findViewById(R.id.tv_level_3);

        callingType = getIntent().getStringExtra("callingType");
        testHash = new HashMap<>();
        testHash.put("filling_blank", PracticeFillingBlankActivity.class);
        testHash.put("listening", PracticeListeningActivity.class);
        testHash.put("reading", PracticeReadingActivity.class);
        testHash.put("single_question", PracticeSingleQuestionActivity.class);
        testHash.put("writing", PracticeWritingActivity.class);
        testHash.put("all", TestQuestionActivity.class);

        tvLevel1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickLevel(view);
            }
        });

        tvLevel2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickLevel(view);
            }
        });

        tvLevel3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickLevel(view);
            }
        });

        Utils.createMenu(this);
    }

    private void pickLevel(View view){
        Button btn = (Button) view;
        int level = Integer.parseInt(btn.getText().toString().substring(6));
        Intent intent = new Intent(PickLevelActivity.this, testHash.get(callingType));
        intent.putExtra(LEVEL_KEY, level);
        startActivity(intent);
        finish();
    }
}