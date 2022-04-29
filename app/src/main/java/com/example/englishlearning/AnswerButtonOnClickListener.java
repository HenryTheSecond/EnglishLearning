package com.example.englishlearning;


import android.view.View;
import android.widget.Button;

public class AnswerButtonOnClickListener implements View.OnClickListener {
    private MultipleChoice multipleChoice;

    private boolean isAnswered;


    public AnswerButtonOnClickListener(MultipleChoice multipleChoice) {
        this.multipleChoice = multipleChoice;
        if(multipleChoice.getAnswer() == MULTIPLE_CHOICE_ANSWER_ENUM.None)
            isAnswered = false;
        else
            isAnswered = true;
    }

    @Override
    public void onClick(View view) {
        multipleChoice.getMultipleChoice().findViewById(R.id.answer_a).setBackgroundTintList(view.getContext().getResources().getColorStateList(R.color.blue));
        multipleChoice.getMultipleChoice().findViewById(R.id.answer_b).setBackgroundTintList(view.getContext().getResources().getColorStateList(R.color.blue));
        multipleChoice.getMultipleChoice().findViewById(R.id.answer_c).setBackgroundTintList(view.getContext().getResources().getColorStateList(R.color.blue));
        multipleChoice.getMultipleChoice().findViewById(R.id.answer_d).setBackgroundTintList(view.getContext().getResources().getColorStateList(R.color.blue));

        view.setBackgroundTintList(view.getContext().getResources().getColorStateList(R.color.teal_200));

        Button btn = (Button) view;
        switch (btn.getText().charAt(0)){
            case 'A': {
                multipleChoice.setAnswer(MULTIPLE_CHOICE_ANSWER_ENUM.answerA);
                break;
            }
            case 'B':{
                multipleChoice.setAnswer(MULTIPLE_CHOICE_ANSWER_ENUM.answerB);
                break;
            }
            case 'C':{
                multipleChoice.setAnswer(MULTIPLE_CHOICE_ANSWER_ENUM.answerC);
                break;
            }
            case 'D':{
                multipleChoice.setAnswer(MULTIPLE_CHOICE_ANSWER_ENUM.answerD);
                break;
            }
            default: {
                multipleChoice.setAnswer(MULTIPLE_CHOICE_ANSWER_ENUM.None);
                break;
            }
        }

        System.out.println("Clicked " + multipleChoice.getAnswer().toString());

        if(!isAnswered)
            multipleChoice.getBtnQuestion().setBackgroundTintList(view.getContext().getResources().getColorStateList(android.R.color.holo_green_dark));

        int questionNumber = Integer.parseInt(multipleChoice.getBtnQuestion().getText().toString());
        Utils.setMultipleChoiceAnswer(multipleChoice.getMultipleChoice().getContext(), questionNumber, multipleChoice.getAnswer());
    }
}
