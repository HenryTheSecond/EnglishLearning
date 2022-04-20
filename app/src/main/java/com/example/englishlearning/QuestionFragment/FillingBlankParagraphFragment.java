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

import com.example.englishlearning.R;


public class FillingBlankParagraphFragment extends Fragment {
    private TextView tvParagraph;
    private Button answerA;
    private Button answerB;
    private Button answerC;
    private Button answerD;

    public FillingBlankParagraphFragment() {
        // Required empty public constructor
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

        tvParagraph = getView().findViewById(R.id.tv_paragraph);
        answerA = getView().findViewById(R.id.answer_a);
        answerB = getView().findViewById(R.id.answer_b);
        answerC = getView().findViewById(R.id.answer_c);
        answerD = getView().findViewById(R.id.answer_d);
    }
}