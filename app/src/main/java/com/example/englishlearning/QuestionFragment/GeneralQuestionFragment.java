package com.example.englishlearning.QuestionFragment;

import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.example.englishlearning.MULTIPLE_CHOICE_ANSWER;

public class GeneralQuestionFragment extends Fragment {
    protected Button btnQuestion;
    //protected MULTIPLE_CHOICE_ANSWER multipleChoiceAnswer;

    public GeneralQuestionFragment(Button btnQuestion){
        this.btnQuestion = btnQuestion;
    }

    public Button getBtnQuestion() {
        return btnQuestion;
    }


}
