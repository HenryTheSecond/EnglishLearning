package com.example.englishlearning;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.view.View;

import androidx.annotation.NonNull;

import java.io.IOException;

public class Utils {
    private static final String FILE_NAME_ANSWER_STORE_TEMP = "AnswerStoreTemp";
    private static final String QUESTION_KEY = "question";


    public static MediaPlayer playListeningAudio(Context context, String fileName){
        MediaPlayer mediaPlayer = new MediaPlayer();
        try{
            //MediaPlayer mediaPlayer = new MediaPlayer();
            AssetFileDescriptor afd = context.getAssets().openFd("ListeningFiles/" + fileName);
            mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            afd.close();
            mediaPlayer.prepare();
        }catch (IOException e) {
            System.out.println("MEDIA Error");
            e.printStackTrace();
        }
        return mediaPlayer;
    }

    public static String getWritingAnswer(Context context, int questionNumber){
        SharedPreferences sharedPreferences = context.getSharedPreferences(FILE_NAME_ANSWER_STORE_TEMP, Context.MODE_PRIVATE);
        return sharedPreferences.getString(QUESTION_KEY + String.valueOf(questionNumber), "");
    }

    public static void setWritingAnswer(Context context, int questionNumber, String answer){
        SharedPreferences sharedPreferences = context.getSharedPreferences(FILE_NAME_ANSWER_STORE_TEMP, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(QUESTION_KEY + String.valueOf(questionNumber), answer);
        editor.commit();
    }

    public static MULTIPLE_CHOICE_ANSWER getMultipleChoiceAnswer(Context context ,int questionNumber){
        SharedPreferences sharedPreferences = context.getSharedPreferences(FILE_NAME_ANSWER_STORE_TEMP, Context.MODE_PRIVATE);

        String answer = sharedPreferences.getString(QUESTION_KEY + String.valueOf(questionNumber), "");
        System.out.println("get");
        System.out.println(answer);
        switch(answer){
            case "A": return MULTIPLE_CHOICE_ANSWER.answerA;
            case "B": return MULTIPLE_CHOICE_ANSWER.answerB;
            case "C": return MULTIPLE_CHOICE_ANSWER.answerC;
            case "D": return MULTIPLE_CHOICE_ANSWER.answerD;
        }
        return MULTIPLE_CHOICE_ANSWER.None;
    }

    public static void setMultipleChoiceAnswer(Context context, int questionNumber, MULTIPLE_CHOICE_ANSWER answer){
        System.out.println("set");
        System.out.println(answer);
        SharedPreferences sharedPreferences = context.getSharedPreferences(FILE_NAME_ANSWER_STORE_TEMP, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(QUESTION_KEY + String.valueOf(questionNumber), answer.toString());
        editor.commit();
    }

    public static void clearAnswerStoreFile(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(FILE_NAME_ANSWER_STORE_TEMP, Context.MODE_PRIVATE);
        sharedPreferences.edit().clear().commit();
    }
}
