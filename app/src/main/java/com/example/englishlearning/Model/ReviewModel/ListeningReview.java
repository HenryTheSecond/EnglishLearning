package com.example.englishlearning.Model.ReviewModel;

import com.example.englishlearning.Model.Listening;

import java.util.ArrayList;
import java.util.List;

public class ListeningReview {
    private Listening listening;
    private List<Integer> idAnswers;

    public ListeningReview(int id, String rawAnswer){
        idAnswers = new ArrayList<>();
        listening = Listening.getListeningById(id);

        String replaceRegex = rawAnswer.replace("[", "");
        String[] list = replaceRegex.split("]");
        for(String item: list){
            String[] tmp = item.split(",");
            int idAnswer = Integer.parseInt(tmp[1]);
            idAnswers.add(idAnswer);
        }
    }

    public Listening getListening() {
        return listening;
    }

    public void setListening(Listening listening) {
        this.listening = listening;
    }

    public List<Integer> getIdAnswers() {
        return idAnswers;
    }

    public void setIdAnswers(List<Integer> idAnswers) {
        this.idAnswers = idAnswers;
    }

    public boolean isCorrect(int index){
        if(idAnswers.get(index) == listening.getListQuestions().get(index).getIdAnswer())
            return true;
        return false;
    }
}
