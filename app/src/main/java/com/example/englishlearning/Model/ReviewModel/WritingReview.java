package com.example.englishlearning.Model.ReviewModel;

import com.example.englishlearning.Model.Writing;

public class WritingReview {
    private Writing writing;
    private String answer;

    public WritingReview(int id, String answer){
        writing = Writing.getWritingById(id);
        this.answer = answer;
    }

    public boolean isCorrect(){
        if(answer.toLowerCase().trim().equals( writing.getAnswer().toLowerCase().trim() ))
            return true;
        return false;
    }

    public Writing getWriting() {
        return writing;
    }

    public void setWriting(Writing writing) {
        this.writing = writing;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
