package com.example.englishlearning.Model.PracticeModel;

public abstract class GeneralPractice {
    protected int id;
    protected String dateTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public abstract String getResult();
}
