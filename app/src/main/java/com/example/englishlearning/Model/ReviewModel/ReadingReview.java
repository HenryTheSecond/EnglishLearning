package com.example.englishlearning.Model.ReviewModel;

import com.example.englishlearning.Model.Reading;

import java.util.ArrayList;
import java.util.List;

public class ReadingReview {
    private Reading reading;
    private List<Integer> idAnswers;

    public ReadingReview(int id, String rawAnswer){
        idAnswers = new ArrayList<>();
        reading = Reading.getReadingById(id);

        String replaceRegex = rawAnswer.replace("[", "");
        String[] list = replaceRegex.split("]");
        for(String item: list){
            String[] tmp = item.split(",");
            int idAnswer = Integer.parseInt(tmp[1]);
            idAnswers.add(idAnswer);
        }
    }

    public Reading getReading() {
        return reading;
    }

    public void setReading(Reading reading) {
        this.reading = reading;
    }

    public List<Integer> getIdAnswers() {
        return idAnswers;
    }

    public void setIdAnswers(List<Integer> idAnswers) {
        this.idAnswers = idAnswers;
    }

    public boolean isCorrect(int index){
        if(idAnswers.get(index) == reading.getListQuestions().get(index).getIdAnswer())
            return true;
        return false;
    }
}
