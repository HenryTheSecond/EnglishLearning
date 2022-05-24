package com.example.englishlearning.Model;

public class Question {
    private long id;

    public long getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Question{" +
                "id=" + id +
                ", question='" + question + '\'' +
                '}';
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    private String question;

    public Question(long id, String question) {
        this.id = id;
        this.question = question;
    }

}
