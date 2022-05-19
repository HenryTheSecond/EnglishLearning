package com.example.englishlearning.Model.PracticeModel;

public abstract class GeneralPractice {
    protected long id;
    protected String dateTime;

    public long getId() {
        return id;
    }

    public void setId(long id) {
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
