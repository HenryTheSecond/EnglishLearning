package com.example.englishlearning.QuestionFragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.englishlearning.R;


public class FillingBlankParagraphFragment extends Fragment {
    private TextView tvParagraph;


    public FillingBlankParagraphFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_filling_blank_paragraph, container, false);
    }
}