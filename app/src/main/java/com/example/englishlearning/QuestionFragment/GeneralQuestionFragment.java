package com.example.englishlearning.QuestionFragment;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.englishlearning.MULTIPLE_CHOICE_ANSWER_ENUM;
import com.example.englishlearning.R;
import com.example.englishlearning.Utils;

public class GeneralQuestionFragment extends Fragment {
    protected Button btnQuestion;
    protected Button answerA;
    protected Button answerB;
    protected Button answerC;
    protected Button answerD;
    protected MULTIPLE_CHOICE_ANSWER_ENUM answer;


    public GeneralQuestionFragment(Button btnQuestion){
        this.btnQuestion = btnQuestion;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        answerA = getView().findViewById(R.id.answer_a);
        answerB = getView().findViewById(R.id.answer_b);
        answerC = getView().findViewById(R.id.answer_c);
        answerD = getView().findViewById(R.id.answer_d);
        answer = Utils.getMultipleChoiceAnswer(getContext(), Integer.parseInt(btnQuestion.getText().toString()));

        colorAnswer();

        /*View.OnClickListener buttonOnClickListenr = new AnswerButtonOnClickListener(this);
        answerA.setOnClickListener(buttonOnClickListenr);
        answerB.setOnClickListener(buttonOnClickListenr);
        answerC.setOnClickListener(buttonOnClickListenr);
        answerD.setOnClickListener(buttonOnClickListenr);*/
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        int questionNumber = Integer.parseInt(btnQuestion.getText().toString());
        Utils.setMultipleChoiceAnswer(getContext(), questionNumber, answer);
    }

    private void colorAnswer(){
        switch (answer){
            case answerA:{
                answerA.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.teal_200));
                break;
            }
            case answerB:{
                answerB.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.teal_200));
                break;
            }
            case answerC:{
                answerC.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.teal_200));
                break;
            }
            case answerD:{
                answerD.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.teal_200));
                break;
            }
        }
    }

    public Button getAnswerA() {
        return answerA;
    }

    public void setAnswerA(Button answerA) {
        this.answerA = answerA;
    }

    public Button getAnswerB() {
        return answerB;
    }

    public void setAnswerB(Button answerB) {
        this.answerB = answerB;
    }

    public Button getAnswerC() {
        return answerC;
    }

    public void setAnswerC(Button answerC) {
        this.answerC = answerC;
    }

    public Button getAnswerD() {
        return answerD;
    }

    public void setAnswerD(Button answerD) {
        this.answerD = answerD;
    }

    public MULTIPLE_CHOICE_ANSWER_ENUM getAnswer() {
        return answer;
    }

    public void setAnswer(MULTIPLE_CHOICE_ANSWER_ENUM answer) {
        this.answer = answer;
    }


    public Button getBtnQuestion() {
        return btnQuestion;
    }


}
