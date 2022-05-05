package com.example.englishlearning.Model.ReviewModel;

import com.example.englishlearning.MULTIPLE_CHOICE_ANSWER_ENUM;
import com.example.englishlearning.Model.FillingBlank;

import java.util.ArrayList;
import java.util.List;

public class FillingBlankReview {
    private FillingBlank fillingBlank;
    private List<Integer> idAnswers;

    public FillingBlankReview(int id, String rawAnswer){
        idAnswers = new ArrayList<>();
        fillingBlank = FillingBlank.getFillingBlankById(id);

        String replaceRegex = rawAnswer.replace("[", "");
        String[] list = replaceRegex.split("]");
        for(String item: list){
            String[] tmp = item.split(",");
            int idAnswer = Integer.parseInt(tmp[1]);
            idAnswers.add(idAnswer);
        }
    }

    public FillingBlank getFillingBlank() {
        return fillingBlank;
    }

    public void setFillingBlank(FillingBlank fillingBlank) {
        this.fillingBlank = fillingBlank;
    }

    public List<Integer> getIdAnswers() {
        return idAnswers;
    }

    public void setIdAnswers(List<Integer> idAnswers) {
        this.idAnswers = idAnswers;
    }

    public boolean isCorrect(int index){
        if(idAnswers.get(index) == fillingBlank.getListQuestions().get(index).getIdAnswer())
            return true;
        return false;
    }
}
