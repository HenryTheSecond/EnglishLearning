package com.example.englishlearning.QuestionFragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.englishlearning.AnswerButtonOnClickListener;
import com.example.englishlearning.MultipleChoice;
import com.example.englishlearning.R;
import com.example.englishlearning.Utils;


public class FillingBlankParagraphFragment extends Fragment {
    private TextView tvParagraph;

    private TextView tvQuestion1;
    private MultipleChoice multipleChoice1;
    private TextView tvQuestion2;
    private MultipleChoice multipleChoice2;
    private TextView tvQuestion3;
    private MultipleChoice multipleChoice3;
    private TextView tvQuestion4;
    private MultipleChoice multipleChoice4;


    public FillingBlankParagraphFragment(Button btnQuestion1, Button btnQuestion2, Button btnQuestion3, Button btnQuestion4) {
        multipleChoice1 = new MultipleChoice();
        multipleChoice1.setBtnQuestion(btnQuestion1);

        multipleChoice2 = new MultipleChoice();
        multipleChoice2.setBtnQuestion(btnQuestion2);

        multipleChoice3 = new MultipleChoice();
        multipleChoice3.setBtnQuestion(btnQuestion3);

        multipleChoice4 = new MultipleChoice();
        multipleChoice4.setBtnQuestion(btnQuestion4);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_filling_blank_paragraph, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Binding
        tvParagraph = getView().findViewById(R.id.tv_paragraph);
        tvQuestion1 = getView().findViewById(R.id.tv_question1);
        multipleChoice1.setMultipleChoice( getView().findViewById(R.id.multiple_choice1) );
        multipleChoice1.setAnswer( Utils.getMultipleChoiceAnswer(getContext(), Integer.parseInt(multipleChoice1.getBtnQuestion().getText().toString())));

        tvQuestion2 = getView().findViewById(R.id.tv_question2);
        multipleChoice2.setMultipleChoice( getView().findViewById(R.id.multiple_choice2) );
        multipleChoice2.setAnswer( Utils.getMultipleChoiceAnswer(getContext(), Integer.parseInt(multipleChoice2.getBtnQuestion().getText().toString())));

        tvQuestion3 = getView().findViewById(R.id.tv_question3);
        multipleChoice3.setMultipleChoice( getView().findViewById(R.id.multiple_choice3) );
        multipleChoice3.setAnswer( Utils.getMultipleChoiceAnswer(getContext(), Integer.parseInt(multipleChoice3.getBtnQuestion().getText().toString())));

        tvQuestion4 = getView().findViewById(R.id.tv_question4);
        multipleChoice4.setMultipleChoice( getView().findViewById(R.id.multiple_choice4) );
        multipleChoice4.setAnswer( Utils.getMultipleChoiceAnswer(getContext(), Integer.parseInt(multipleChoice4.getBtnQuestion().getText().toString())));

        Utils.colorAnswer(multipleChoice1); Utils.colorAnswer(multipleChoice2); Utils.colorAnswer(multipleChoice3); Utils.colorAnswer(multipleChoice4);

        Utils.setOnClickListener(multipleChoice1); Utils.setOnClickListener(multipleChoice2); Utils.setOnClickListener(multipleChoice3); Utils.setOnClickListener(multipleChoice4);
    }


}