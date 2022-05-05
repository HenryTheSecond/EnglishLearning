package com.example.englishlearning.QuestionFragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.englishlearning.AnswerStore.AnswerStore;
import com.example.englishlearning.AnswerStore.WritingStore;
import com.example.englishlearning.Model.Writing;
import com.example.englishlearning.R;
import com.example.englishlearning.Utils;


public class WritingFragment extends Fragment {

    EditText etAnswer;
    TextView tvParagraph;
    Button btnQuestion;

    private Writing writing;


    public WritingFragment(Writing writing, Button btnQuestion) {
        this.btnQuestion = btnQuestion;
        this.writing = writing;
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


        etAnswer.setText( Utils.getWritingAnswer(getContext(), Integer.parseInt(btnQuestion.getText().toString())) );

        etAnswer.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.toString().equals("")){
                    btnQuestion.setBackgroundTintList(view.getContext().getResources().getColorStateList(android.R.color.holo_green_dark));
                }else
                    btnQuestion.setBackgroundTintList(view.getContext().getResources().getColorStateList(android.R.color.holo_purple));
            }

            @Override
            public void afterTextChanged(Editable editable) {
                int questionNumber = Integer.parseInt(btnQuestion.getText().toString());
                Utils.setWritingAnswer(getContext(), questionNumber, etAnswer.getText().toString());
            }
        });

        tvParagraph.setText(writing.getQuestion());

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}