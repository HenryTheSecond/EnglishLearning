package com.example.englishlearning.ReviewFragment;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.englishlearning.Model.ReviewModel.ReadingReview;
import com.example.englishlearning.MultipleChoice;
import com.example.englishlearning.R;
import com.example.englishlearning.Utils;

public class ReadingReviewFragment extends Fragment {
    private TextView tvParagraph;

    private TextView tvQuestion1;
    private MultipleChoice multipleChoice1;
    private TextView tvQuestion2;
    private MultipleChoice multipleChoice2;
    private TextView tvQuestion3;
    private MultipleChoice multipleChoice3;

    private ReadingReview readingReview;

    public ReadingReviewFragment(ReadingReview readingReview, Button btnQuestion1, Button btnQuestion2, Button btnQuestion3){
        multipleChoice1 = new MultipleChoice();
        multipleChoice1.setBtnQuestion(btnQuestion1);

        multipleChoice2 = new MultipleChoice();
        multipleChoice2.setBtnQuestion(btnQuestion2);

        multipleChoice3 = new MultipleChoice();
        multipleChoice3.setBtnQuestion(btnQuestion3);

        this.readingReview = readingReview;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_reading_paragraph, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvParagraph = getView().findViewById(R.id.tv_paragraph);
        tvParagraph.setMovementMethod(new ScrollingMovementMethod());

        tvQuestion1 = getView().findViewById(R.id.tv_question1);
        multipleChoice1.setMultipleChoice( getView().findViewById(R.id.multiple_choice1) );

        tvQuestion2 = getView().findViewById(R.id.tv_question2);
        multipleChoice2.setMultipleChoice( getView().findViewById(R.id.multiple_choice2) );

        tvQuestion3 = getView().findViewById(R.id.tv_question3);
        multipleChoice3.setMultipleChoice( getView().findViewById(R.id.multiple_choice3) );

        tvParagraph.setText( readingReview.getReading().getParagraph() );
        tvQuestion1.setText( multipleChoice1.getBtnQuestion().getText().toString() + " " + readingReview.getReading().getListQuestions().get(0).getQuestion());
        tvQuestion2.setText( multipleChoice2.getBtnQuestion().getText().toString() + " " + readingReview.getReading().getListQuestions().get(1).getQuestion());
        tvQuestion3.setText( multipleChoice3.getBtnQuestion().getText().toString() + " " + readingReview.getReading().getListQuestions().get(2).getQuestion());

        Utils.setTextForMultipleChoice( multipleChoice1.getMultipleChoice(), readingReview.getReading().getListQuestions().get(0).getListChoice() );
        Utils.setTextForMultipleChoice( multipleChoice2.getMultipleChoice(), readingReview.getReading().getListQuestions().get(1).getListChoice() );
        Utils.setTextForMultipleChoice( multipleChoice3.getMultipleChoice(), readingReview.getReading().getListQuestions().get(2).getListChoice() );

        Utils.colorAnswerReview(multipleChoice1, readingReview.getIdAnswers().get(0),
                readingReview.getReading().getListQuestions().get(0).getListChoice(),
                readingReview.getReading().getListQuestions().get(0).getIdAnswer());

        Utils.colorAnswerReview(multipleChoice2, readingReview.getIdAnswers().get(1),
                readingReview.getReading().getListQuestions().get(1).getListChoice(),
                readingReview.getReading().getListQuestions().get(1).getIdAnswer());

        Utils.colorAnswerReview(multipleChoice3, readingReview.getIdAnswers().get(2),
                readingReview.getReading().getListQuestions().get(2).getListChoice(),
                readingReview.getReading().getListQuestions().get(2).getIdAnswer());
    }
}
