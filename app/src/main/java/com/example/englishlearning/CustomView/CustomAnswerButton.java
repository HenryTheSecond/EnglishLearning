package com.example.englishlearning.CustomView;

import android.content.Context;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;

import com.example.englishlearning.R;

public class CustomAnswerButton extends androidx.appcompat.widget.AppCompatButton {

    public CustomAnswerButton(@NonNull Context context) {
        super(context);

        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Click");
            }
        });
    }
}
