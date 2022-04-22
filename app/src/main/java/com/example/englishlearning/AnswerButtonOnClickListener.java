package com.example.englishlearning;


import android.view.View;
import android.widget.Button;

public class AnswerButtonOnClickListener implements View.OnClickListener {
    private Button answerA;
    private Button answerB;
    private Button answerC;
    private Button answerD;
    private Button btnQuestion;
    private MULTIPLE_CHOICE_ANSWER answer;
    private boolean isAnswered;

    public AnswerButtonOnClickListener(Button answerA, Button answerB, Button answerC, Button answerD, Button btnQuestion, MULTIPLE_CHOICE_ANSWER answer) {
        this.answerA = answerA;
        this.answerB = answerB;
        this.answerC = answerC;
        this.answerD = answerD;
        this.btnQuestion = btnQuestion;
        this.answer = answer;
        isAnswered = false;
    }

    @Override
    public void onClick(View view) {
        answerA.setBackgroundTintList(view.getContext().getResources().getColorStateList(R.color.blue));
        answerB.setBackgroundTintList(view.getContext().getResources().getColorStateList(R.color.blue));
        answerC.setBackgroundTintList(view.getContext().getResources().getColorStateList(R.color.blue));
        answerD.setBackgroundTintList(view.getContext().getResources().getColorStateList(R.color.blue));

        view.setBackgroundTintList(view.getContext().getResources().getColorStateList(R.color.teal_200));

        Button btn = (Button) view;
        switch (btn.getText().charAt(0)){
            case 'A': {
                answer = MULTIPLE_CHOICE_ANSWER.answerA;
                break;
            }
            case 'B':{
                answer = MULTIPLE_CHOICE_ANSWER.answerB;
                break;
            }
            case 'C':{
                answer = MULTIPLE_CHOICE_ANSWER.answerC;
                break;
            }
            case 'D':{
                answer = MULTIPLE_CHOICE_ANSWER.answerD;
                break;
            }
            default: {
                answer = MULTIPLE_CHOICE_ANSWER.None;
                break;
            }
        }

        if(!isAnswered)
            btnQuestion.setBackgroundTintList(view.getContext().getResources().getColorStateList(android.R.color.holo_green_dark));
    }
}
