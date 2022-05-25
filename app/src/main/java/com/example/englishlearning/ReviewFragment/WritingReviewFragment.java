package com.example.englishlearning.ReviewFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.englishlearning.Model.ReviewModel.WritingReview;
import com.example.englishlearning.R;

public class WritingReviewFragment extends Fragment {
    EditText etAnswer;
    TextView tvParagraph;
    Button btnQuestion;
    TextView tvResult;

    WritingReview writingReview;

    public WritingReviewFragment(WritingReview writingReview, Button btnQuestion){
        this.btnQuestion = btnQuestion;
        this.writingReview = writingReview;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_writing, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        etAnswer = getView().findViewById(R.id.et_answer);
        tvParagraph = getView().findViewById(R.id.tv_paragraph);
        tvResult = getView().findViewById(R.id.tv_result);

        etAnswer.setText( writingReview.getAnswer().trim() );

        tvParagraph.setText(writingReview.getWriting().getQuestion());

        String correctAnswer = writingReview.getWriting().getAnswer();
        if(correctAnswer.trim().toLowerCase().equals( writingReview.getAnswer().trim().toLowerCase() )){
            tvResult.setText("Correct");
            tvResult.setTextColor( getResources().getColor(R.color.correct_answer) );
        }else{
            tvResult.setText("Incorrect (" + correctAnswer + ")");
            tvResult.setTextColor( getResources().getColor(R.color.incorrect_answer) );
        }
        tvResult.setVisibility(View.VISIBLE);
    }
}
