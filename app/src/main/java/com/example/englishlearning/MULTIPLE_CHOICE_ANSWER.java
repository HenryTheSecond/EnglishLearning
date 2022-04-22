package com.example.englishlearning;

import androidx.annotation.NonNull;

public enum MULTIPLE_CHOICE_ANSWER {
    answerA, answerB, answerC, answerD, None;
    @NonNull
    @Override
    public String toString() {
        switch(this){
            case answerA: return "A";
            case answerB: return "B";
            case answerC: return "C";
            case answerD: return "D";
        }
        return "";
    }
}
