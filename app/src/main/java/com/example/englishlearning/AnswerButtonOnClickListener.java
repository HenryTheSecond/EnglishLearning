package com.example.englishlearning;


import android.view.View;
import android.widget.Button;

public class AnswerButtonOnClickListener implements View.OnClickListener {
    private Button answerA;
    private Button answerB;
    private Button answerC;
    private Button answerD;
    private Button btnQuestion;
    private boolean isAnswered;

    public AnswerButtonOnClickListener(Button answerA, Button answerB, Button answerC, Button answerD, Button btnQuestion) {
        this.answerA = answerA;
        this.answerB = answerB;
        this.answerC = answerC;
        this.answerD = answerD;
        this.btnQuestion = btnQuestion;
        isAnswered = false;
    }

    @Override
    public void onClick(View view) {
        answerA.setBackgroundTintList(view.getContext().getResources().getColorStateList(R.color.blue));
        answerB.setBackgroundTintList(view.getContext().getResources().getColorStateList(R.color.blue));
        answerC.setBackgroundTintList(view.getContext().getResources().getColorStateList(R.color.blue));
        answerD.setBackgroundTintList(view.getContext().getResources().getColorStateList(R.color.blue));

        view.setBackgroundTintList(view.getContext().getResources().getColorStateList(R.color.teal_200));

        if(!isAnswered)
            btnQuestion.setBackgroundTintList(view.getContext().getResources().getColorStateList(android.R.color.holo_green_dark));
    }
}
