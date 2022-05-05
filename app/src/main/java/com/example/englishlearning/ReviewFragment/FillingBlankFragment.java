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


import com.example.englishlearning.Model.ReviewModel.FillingBlankReview;
import com.example.englishlearning.MultipleChoice;
import com.example.englishlearning.R;
import com.example.englishlearning.Utils;

public class FillingBlankFragment extends Fragment {
    private TextView tvParagraph;

    private TextView tvQuestion1;
    private MultipleChoice multipleChoice1;
    private TextView tvQuestion2;
    private MultipleChoice multipleChoice2;
    private TextView tvQuestion3;
    private MultipleChoice multipleChoice3;
    private TextView tvQuestion4;
    private MultipleChoice multipleChoice4;

    private FillingBlankReview fillingBlankReview;

    public FillingBlankFragment(FillingBlankReview fillingBlankReview, Button btnQuestion1, Button btnQuestion2, Button btnQuestion3, Button btnQuestion4){
        this.fillingBlankReview = fillingBlankReview;

        multipleChoice1 = new MultipleChoice();
        multipleChoice1.setBtnQuestion(btnQuestion1);

        multipleChoice2 = new MultipleChoice();
        multipleChoice2.setBtnQuestion(btnQuestion2);

        multipleChoice3 = new MultipleChoice();
        multipleChoice3.setBtnQuestion(btnQuestion3);

        multipleChoice4 = new MultipleChoice();
        multipleChoice4.setBtnQuestion(btnQuestion4);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_filling_blank_paragraph, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvParagraph = getView().findViewById(R.id.tv_paragraph);
        tvQuestion1 = getView().findViewById(R.id.tv_question1);
        multipleChoice1.setMultipleChoice( getView().findViewById(R.id.multiple_choice1) );
        multipleChoice1.setAnswer( Utils.getMultipleChoiceAnswer(getContext(), Integer.parseInt(multipleChoice1.getBtnQuestion().getText().toString())));

        tvQuestion2 = getView().findViewById(R.id.tv_question2);
        multipleChoice2.setMultipleChoice( getView().findViewById(R.id.multiple_choice2) );
        //multipleChoice2.setAnswer( Utils.getMultipleChoiceAnswer(getContext(), Integer.parseInt(multipleChoice2.getBtnQuestion().getText().toString())));

        tvQuestion3 = getView().findViewById(R.id.tv_question3);
        multipleChoice3.setMultipleChoice( getView().findViewById(R.id.multiple_choice3) );
        //multipleChoice3.setAnswer( Utils.getMultipleChoiceAnswer(getContext(), Integer.parseInt(multipleChoice3.getBtnQuestion().getText().toString())));

        tvQuestion4 = getView().findViewById(R.id.tv_question4);
        multipleChoice4.setMultipleChoice( getView().findViewById(R.id.multiple_choice4) );
        //multipleChoice4.setAnswer( Utils.getMultipleChoiceAnswer(getContext(), Integer.parseInt(multipleChoice4.getBtnQuestion().getText().toString())));

        tvParagraph.setText( fillingBlankReview.getFillingBlank().getParagraph() );
        tvQuestion1.setText( multipleChoice1.getBtnQuestion().getText().toString());
        tvQuestion2.setText( multipleChoice2.getBtnQuestion().getText().toString());
        tvQuestion3.setText( multipleChoice3.getBtnQuestion().getText().toString());
        tvQuestion4.setText( multipleChoice4.getBtnQuestion().getText().toString());

        Utils.setTextForMultipleChoice( multipleChoice1.getMultipleChoice(), fillingBlankReview.getFillingBlank().getListQuestions().get(0).getListChoice() );
        Utils.setTextForMultipleChoice( multipleChoice2.getMultipleChoice(), fillingBlankReview.getFillingBlank().getListQuestions().get(1).getListChoice() );
        Utils.setTextForMultipleChoice( multipleChoice3.getMultipleChoice(), fillingBlankReview.getFillingBlank().getListQuestions().get(2).getListChoice() );
        Utils.setTextForMultipleChoice( multipleChoice4.getMultipleChoice(), fillingBlankReview.getFillingBlank().getListQuestions().get(3).getListChoice() );

        Utils.colorAnswerReview(multipleChoice1, fillingBlankReview.getIdAnswers().get(0),
                fillingBlankReview.getFillingBlank().getListQuestions().get(0).getListChoice(),
                fillingBlankReview.getFillingBlank().getListQuestions().get(0).getIdAnswer());

        Utils.colorAnswerReview(multipleChoice2, fillingBlankReview.getIdAnswers().get(1),
                fillingBlankReview.getFillingBlank().getListQuestions().get(1).getListChoice(),
                fillingBlankReview.getFillingBlank().getListQuestions().get(1).getIdAnswer());

        Utils.colorAnswerReview(multipleChoice3, fillingBlankReview.getIdAnswers().get(2),
                fillingBlankReview.getFillingBlank().getListQuestions().get(2).getListChoice(),
                fillingBlankReview.getFillingBlank().getListQuestions().get(2).getIdAnswer());

        Utils.colorAnswerReview(multipleChoice4, fillingBlankReview.getIdAnswers().get(3),
                fillingBlankReview.getFillingBlank().getListQuestions().get(3).getListChoice(),
                fillingBlankReview.getFillingBlank().getListQuestions().get(3).getIdAnswer());
    }
}
