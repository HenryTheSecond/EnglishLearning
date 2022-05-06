package com.example.englishlearning.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.englishlearning.R;

public class PickLevelActivity extends AppCompatActivity {

    public static final String LEVEL_KEY = "level";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_level);

        TextView tvLevel1 = findViewById(R.id.tv_level_1);
        TextView tvLevel2 = findViewById(R.id.tv_level_2);
        TextView tvLevel3 = findViewById(R.id.tv_level_3);

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
    }

    private void pickLevel(View view){
        Button btn = (Button) view;
        int level = Integer.parseInt(btn.getText().toString().substring(6));
        Intent intent = new Intent(PickLevelActivity.this, TestQuestionActivity.class);
        intent.putExtra(LEVEL_KEY, level);
        startActivity(intent);
        finish();
    }
}