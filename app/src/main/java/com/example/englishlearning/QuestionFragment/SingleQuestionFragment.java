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
import com.example.englishlearning.MULTIPLE_CHOICE_ANSWER;
import com.example.englishlearning.R;
import com.example.englishlearning.Utils;


public class SingleQuestionFragment extends GeneralQuestionFragment {

    private TextView tvQuestion;

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
    }




}