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
import com.example.englishlearning.R;


public class SingleQuestionFragment extends GeneralQuestionFragment {

    private TextView tvQuestion;
    private Button answerA;
    private Button answerB;
    private Button answerC;
    private Button answerD;

    public SingleQuestionFragment(Button btnQuestion) {
        super(btnQuestion);
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
        answerA = getView().findViewById(R.id.answer_a);
        answerB = getView().findViewById(R.id.answer_b);
        answerC = getView().findViewById(R.id.answer_c);
        answerD = getView().findViewById(R.id.answer_d);

        View.OnClickListener buttonOnClickListenr = new AnswerButtonOnClickListener(answerA, answerB, answerC, answerD, btnQuestion);
        answerA.setOnClickListener(buttonOnClickListenr);
        answerB.setOnClickListener(buttonOnClickListenr);
        answerC.setOnClickListener(buttonOnClickListenr);
        answerD.setOnClickListener(buttonOnClickListenr);
    }
}