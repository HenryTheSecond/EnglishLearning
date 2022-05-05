package com.example.englishlearning.ReviewFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.englishlearning.Model.ReviewModel.SingleQuestionReview;
import com.example.englishlearning.MultipleChoice;
import com.example.englishlearning.R;
import com.example.englishlearning.Utils;

public class SingleQuestionReviewFragment extends Fragment {
    private TextView tvQuestion;
    private MultipleChoice multipleChoice;

    private SingleQuestionReview singleQuestionReview;

    public SingleQuestionReviewFragment(SingleQuestionReview singleQuestionReview, Button btnQuestion){
        this.multipleChoice = new MultipleChoice();
        this.multipleChoice.setBtnQuestion(btnQuestion);

        this.singleQuestionReview = singleQuestionReview;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_single_question, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvQuestion = getView().findViewById(R.id.tv_question);
        multipleChoice.setMultipleChoice( getView().findViewById(R.id.multiple_choice) );

        tvQuestion.setText(singleQuestionReview.getSingleQuestion().getQuestion());

        Utils.setTextForMultipleChoice( multipleChoice.getMultipleChoice(), singleQuestionReview.getSingleQuestion().getListChoice());
        Utils.colorAnswerReview(multipleChoice, singleQuestionReview.getIdAnswer(), singleQuestionReview.getSingleQuestion().getListChoice(),
                singleQuestionReview.getSingleQuestion().getIdAnswer());

    }
}
