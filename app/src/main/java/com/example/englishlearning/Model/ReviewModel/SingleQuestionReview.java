package com.example.englishlearning.Model.ReviewModel;

import com.example.englishlearning.Model.SingleQuestion;

public class SingleQuestionReview {
    private SingleQuestion singleQuestion;
    private int idAnswer;

    public SingleQuestionReview(int id, int idAnswer){
        singleQuestion = SingleQuestion.getSingleQuestionById(id);
        this.idAnswer = idAnswer;
    }

    public SingleQuestion getSingleQuestion() {
        return singleQuestion;
    }

    public void setSingleQuestion(SingleQuestion singleQuestion) {
        this.singleQuestion = singleQuestion;
    }

    public int getIdAnswer() {
        return idAnswer;
    }

    public void setIdAnswer(int idAnswer) {
        this.idAnswer = idAnswer;
    }

    public boolean isCorrect(){
        if(singleQuestion.getIdAnswer() == idAnswer)
            return true;
        return false;
    }
}
