package com.example.englishlearning.AnswerStore;

public class WritingStore extends AnswerStore {
    private String answer;


    public WritingStore(int idBtnQuestion, String answer) {
        super(idBtnQuestion);
        this.answer = answer;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

}
