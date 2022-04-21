package com.example.englishlearning.QuestionFragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.englishlearning.R;


public class WritingFragment extends GeneralQuestionFragment {

    EditText etAnswer;
    TextView tvParagraph;

    public WritingFragment(Button btnQuestion) {
        super(btnQuestion);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_writing, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        etAnswer = getView().findViewById(R.id.et_answer);
        tvParagraph = getView().findViewById(R.id.tv_paragraph);
    }
}