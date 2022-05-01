package com.example.englishlearning;

import androidx.annotation.NonNull;

public enum MULTIPLE_CHOICE_ANSWER_ENUM {
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
        return "None";
    }


    public static int getPositionOfAnswer(MULTIPLE_CHOICE_ANSWER_ENUM answer){
        switch(answer){
            case answerA: return 0;
            case answerB: return 1;
            case answerC: return 2;
            case answerD: return 3;
        }
        return -1;
    }
}
