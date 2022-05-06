package com.example.englishlearning.Model;

import android.widget.Button;

import java.util.Date;

public class NotedWord {
    private int id;
    private String content;
    private Type type;
    private String meaning;

    //public static final String[] TYPES = {"noun", "adjective", "verb", "phrasal verb", "adverb", "idiom"};
    public static enum Type {
        none, noun, adjective, verb, phrasal_verb, adverb, idiom;

        @Override
        public String toString() {
            switch(this){
                case none: return "None";
                case noun: return "Noun";
                case adjective: return "Adjective";
                case verb: return "Verb";
                case phrasal_verb: return "Phrasal Verb";
                case adverb: return "Adverb";
                case idiom: return "Idiom";
            }
            return super.toString();
        }

        public static Type parseType(String str){
            switch(str){
                case "None": return none;
                case "Noun": return noun;
                case "Adjective": return adjective;
                case "Verb": return verb;
                case "Phrasal Verb": return phrasal_verb;
                case "Adverb": return adverb;
                case "Idiom": return idiom;
            }
            return none;
        }
    }

    public NotedWord(){
        this.content = "";
        this.type = Type.none;
        this.meaning = "";
    }

    public NotedWord(int id, String content, Type type, String meaning) {
        this.id = id;
        this.content = content;
        this.type = type;
        this.meaning = meaning;
    }

    public NotedWord(String content, String meaning) {
        this.content = content;
        this.meaning = meaning;
        this.type = Type.none;
    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getMeaning() {
        return meaning;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

}
