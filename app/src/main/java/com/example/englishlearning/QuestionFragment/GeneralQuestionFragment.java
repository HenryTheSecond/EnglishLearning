package com.example.englishlearning.QuestionFragment;

import android.widget.Button;

import androidx.fragment.app.Fragment;

public class GeneralQuestionFragment extends Fragment {
    protected Button btnQuestion;

    public GeneralQuestionFragment(Button btnQuestion){
        this.btnQuestion = btnQuestion;
    }

    public Button getBtnQuestion() {
        return btnQuestion;
    }
}
