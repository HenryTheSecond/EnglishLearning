package com.example.englishlearning.Model.ReviewModel;


import java.util.ArrayList;
import java.util.List;

public class TestRecord {
    private int id;
    private String dateTime;
    private double point;
    private List<ListeningReview> listeningReviews;
    private FillingBlankReview fillingBlankReview;
    private ReadingReview readingReview;
    private List<SingleQuestionReview> singleQuestionReviews;
    private List<WritingReview> writingReviews;

    public TestRecord(){
        listeningReviews = new ArrayList<>();
        singleQuestionReviews = new ArrayList<>();
        writingReviews = new ArrayList<>();
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

    public double getPoint() {
        return point;
    }

    public void setPoint(double point) {
        this.point = point;
    }

    public List<ListeningReview> getListeningReviews() {
        return listeningReviews;
    }

    public void setListeningReviews(List<ListeningReview> listeningReviews) {
        this.listeningReviews = listeningReviews;
    }

    public FillingBlankReview getFillingBlankReview() {
        return fillingBlankReview;
    }

    public void setFillingBlankReview(FillingBlankReview fillingBlankReview) {
        this.fillingBlankReview = fillingBlankReview;
    }

    public ReadingReview getReadingReview() {
        return readingReview;
    }

    public void setReadingReview(ReadingReview readingReview) {
        this.readingReview = readingReview;
    }

    public List<SingleQuestionReview> getSingleQuestionReviews() {
        return singleQuestionReviews;
    }

    public void setSingleQuestionReviews(List<SingleQuestionReview> singleQuestionReviews) {
        this.singleQuestionReviews = singleQuestionReviews;
    }

    public List<WritingReview> getWritingReviews() {
        return writingReviews;
    }

    public void setWritingReviews(List<WritingReview> writingReviews) {
        this.writingReviews = writingReviews;
    }
}
