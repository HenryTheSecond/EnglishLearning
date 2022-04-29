package com.example.englishlearning;

import android.view.View;
import android.widget.Button;

public class MultipleChoice {
    private View multipleChoice;
    private MULTIPLE_CHOICE_ANSWER_ENUM answer;
    private Button btnQuestion;

    public MultipleChoice() {
    }

    public MultipleChoice(View multipleChoice, MULTIPLE_CHOICE_ANSWER_ENUM answer, Button btnQuestion) {
        this.multipleChoice = multipleChoice;
        this.answer = answer;
        this.btnQuestion = btnQuestion;
    }

    public View getMultipleChoice() {
        return multipleChoice;
    }

    public void setMultipleChoice(View multipleChoice) {
        this.multipleChoice = multipleChoice;
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

    public void setBtnQuestion(Button btnQuestion) {
        this.btnQuestion = btnQuestion;
    }
}
