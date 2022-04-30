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
import com.example.englishlearning.Model.SingleQuestion;
import com.example.englishlearning.MultipleChoice;
import com.example.englishlearning.R;
import com.example.englishlearning.Utils;


public class SingleQuestionFragment extends Fragment {

    private TextView tvQuestion;
    private MultipleChoice multipleChoice;

    private SingleQuestion singleQuestion;

    public SingleQuestionFragment(SingleQuestion singleQuestion, Button btnQuestion) {
        this.multipleChoice = new MultipleChoice();
        this.multipleChoice.setBtnQuestion(btnQuestion);

        this.singleQuestion = singleQuestion;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_single_question, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Binding
        tvQuestion = getView().findViewById(R.id.tv_question);
        multipleChoice.setMultipleChoice( getView().findViewById(R.id.multiple_choice) );
        multipleChoice.setAnswer( Utils.getMultipleChoiceAnswer(getContext(), Integer.parseInt(multipleChoice.getBtnQuestion().getText().toString())));

        Utils.colorAnswer(multipleChoice);
        View.OnClickListener buttonOnClickListenr = new AnswerButtonOnClickListener(multipleChoice);
        multipleChoice.getMultipleChoice().findViewById(R.id.answer_a).setOnClickListener(buttonOnClickListenr);
        multipleChoice.getMultipleChoice().findViewById(R.id.answer_b).setOnClickListener(buttonOnClickListenr);
        multipleChoice.getMultipleChoice().findViewById(R.id.answer_c).setOnClickListener(buttonOnClickListenr);
        multipleChoice.getMultipleChoice().findViewById(R.id.answer_d).setOnClickListener(buttonOnClickListenr);

        tvQuestion.setText(singleQuestion.getQuestion());
        Utils.setTextForMultipleChoice( multipleChoice.getMultipleChoice(), singleQuestion.getListChoice());
    }

}