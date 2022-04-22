package com.example.englishlearning;


import android.view.View;
import android.widget.Button;

import com.example.englishlearning.QuestionFragment.GeneralQuestionFragment;
import com.example.englishlearning.QuestionFragment.SingleQuestionFragment;

public class AnswerButtonOnClickListener implements View.OnClickListener {
    private GeneralQuestionFragment fragment;
    private boolean isAnswered;


    public AnswerButtonOnClickListener(GeneralQuestionFragment singleQuestionFragment) {
        this.fragment = singleQuestionFragment;
        if(fragment.getAnswer() == MULTIPLE_CHOICE_ANSWER.None)
            isAnswered = false;
        else
            isAnswered = true;
    }

    @Override
    public void onClick(View view) {
        fragment.getAnswerA().setBackgroundTintList(view.getContext().getResources().getColorStateList(R.color.blue));
        fragment.getAnswerB().setBackgroundTintList(view.getContext().getResources().getColorStateList(R.color.blue));
        fragment.getAnswerC().setBackgroundTintList(view.getContext().getResources().getColorStateList(R.color.blue));
        fragment.getAnswerD().setBackgroundTintList(view.getContext().getResources().getColorStateList(R.color.blue));

        view.setBackgroundTintList(view.getContext().getResources().getColorStateList(R.color.teal_200));

        Button btn = (Button) view;
        switch (btn.getText().charAt(0)){
            case 'A': {
                fragment.setAnswer(MULTIPLE_CHOICE_ANSWER.answerA);
                break;
            }
            case 'B':{
                fragment.setAnswer(MULTIPLE_CHOICE_ANSWER.answerB);
                break;
            }
            case 'C':{
                fragment.setAnswer(MULTIPLE_CHOICE_ANSWER.answerC);
                break;
            }
            case 'D':{
                fragment.setAnswer(MULTIPLE_CHOICE_ANSWER.answerD);
                break;
            }
            default: {
                fragment.setAnswer(MULTIPLE_CHOICE_ANSWER.None);
                break;
            }
        }

        System.out.println("Clicked " + fragment.getAnswer().toString());

        if(!isAnswered)
            fragment.getBtnQuestion().setBackgroundTintList(view.getContext().getResources().getColorStateList(android.R.color.holo_green_dark));
    }
}
