package com.example.englishlearning.AnswerStore;

public abstract class AnswerStore {
    protected int idBtnQuestion;

    public AnswerStore(int idBtnQuestion) {
        this.idBtnQuestion = idBtnQuestion;
    }

    public int getIdBtnQuestion() {
        return idBtnQuestion;
    }
}
