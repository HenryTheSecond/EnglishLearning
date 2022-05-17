package com.example.englishlearning.Model.ReviewModel;

public class DisplayReview {
    private int id;
    private String dateTime;
    private String result;

    public DisplayReview(int id, String dateTime, String result) {
        this.id = id;
        this.dateTime = dateTime;
        this.result = result;
    }

    public DisplayReview(int id, String dateTime, int correct, String type){
        this.id = id;
        this.dateTime = dateTime;

        switch(type){
            case "practice_filling_blank":{
                this.result = String.valueOf(correct) + "/12";
                break;
            }
            case "practice_listening":
            case "practice_reading":{
                this.result = String.valueOf(correct) + "/9";
                break;
            }
            case "practice_single_question":
            case "practice_writing":{
                this.result = String.valueOf(correct) + "/5";
                break;
            }
        }
    }

    public DisplayReview(int id, String dateTime, double score){
        this.id = id;
        this.dateTime = dateTime;
        this.result = String.valueOf(score) + "/10";
    }

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

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
