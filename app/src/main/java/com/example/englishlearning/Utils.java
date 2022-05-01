package com.example.englishlearning;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.view.View;
import android.widget.Button;

import com.example.englishlearning.Databases.EnglishHelper;
import com.example.englishlearning.Model.FillingBlank;
import com.example.englishlearning.Model.Listening;
import com.example.englishlearning.Model.MultipleChoiceAnswer;
import com.example.englishlearning.Model.Reading;
import com.example.englishlearning.Model.SingleQuestion;
import com.example.englishlearning.Model.Writing;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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

    public static MULTIPLE_CHOICE_ANSWER_ENUM getMultipleChoiceAnswer(Context context , int questionNumber){
        SharedPreferences sharedPreferences = context.getSharedPreferences(FILE_NAME_ANSWER_STORE_TEMP, Context.MODE_PRIVATE);

        String answer = sharedPreferences.getString(QUESTION_KEY + String.valueOf(questionNumber), "");

        System.out.println("Get " + answer.toString());
        switch(answer){
            case "A": return MULTIPLE_CHOICE_ANSWER_ENUM.answerA;
            case "B": return MULTIPLE_CHOICE_ANSWER_ENUM.answerB;
            case "C": return MULTIPLE_CHOICE_ANSWER_ENUM.answerC;
            case "D": return MULTIPLE_CHOICE_ANSWER_ENUM.answerD;
        }
        return MULTIPLE_CHOICE_ANSWER_ENUM.None;
    }

    public static void setMultipleChoiceAnswer(Context context, int questionNumber, MULTIPLE_CHOICE_ANSWER_ENUM answer){
        System.out.println("Saved " + answer.toString());
        SharedPreferences sharedPreferences = context.getSharedPreferences(FILE_NAME_ANSWER_STORE_TEMP, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(QUESTION_KEY + String.valueOf(questionNumber), answer.toString());
        editor.commit();
    }

    public static void clearAnswerStoreFile(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(FILE_NAME_ANSWER_STORE_TEMP, Context.MODE_PRIVATE);
        sharedPreferences.edit().clear().commit();
    }

    public static void colorAnswer(MultipleChoice multipleChoice){
        switch (multipleChoice.getAnswer()){
            case answerA:{
                multipleChoice.getMultipleChoice().findViewById(R.id.answer_a).setBackgroundTintList(multipleChoice.getMultipleChoice().getContext().getResources().getColorStateList(R.color.teal_200));
                break;
            }
            case answerB:{
                multipleChoice.getMultipleChoice().findViewById(R.id.answer_b).setBackgroundTintList(multipleChoice.getMultipleChoice().getContext().getResources().getColorStateList(R.color.teal_200));
                break;
            }
            case answerC:{
                multipleChoice.getMultipleChoice().findViewById(R.id.answer_c).setBackgroundTintList(multipleChoice.getMultipleChoice().getContext().getResources().getColorStateList(R.color.teal_200));
                break;
            }
            case answerD:{
                multipleChoice.getMultipleChoice().findViewById(R.id.answer_d).setBackgroundTintList(multipleChoice.getMultipleChoice().getContext().getResources().getColorStateList(R.color.teal_200));
                break;
            }
        }
    }

    public static void setOnClickListener(MultipleChoice multipleChoice){
        View.OnClickListener buttonOnClickListenr = new AnswerButtonOnClickListener(multipleChoice);
        multipleChoice.getMultipleChoice().findViewById(R.id.answer_a).setOnClickListener(buttonOnClickListenr);
        multipleChoice.getMultipleChoice().findViewById(R.id.answer_b).setOnClickListener(buttonOnClickListenr);
        multipleChoice.getMultipleChoice().findViewById(R.id.answer_c).setOnClickListener(buttonOnClickListenr);
        multipleChoice.getMultipleChoice().findViewById(R.id.answer_d).setOnClickListener(buttonOnClickListenr);
    }


    public static Cursor getRandomQuestions(String tableName, int amount){
        EnglishHelper helper = new EnglishHelper(MyApplication.getAppContext());
        SQLiteDatabase database = helper.getReadableDatabase();
        Cursor cursor = database.rawQuery("Select * from " + tableName + " Order by RANDOM() LIMIT " + amount, null);
        return cursor;
    }

    public static void setTextForMultipleChoice(View btnMultiChoiceView, List<MultipleChoiceAnswer> listAnswer){
        Button btnA = btnMultiChoiceView.findViewById(R.id.answer_a);
        Button btnB = btnMultiChoiceView.findViewById(R.id.answer_b);
        Button btnC = btnMultiChoiceView.findViewById(R.id.answer_c);
        Button btnD = btnMultiChoiceView.findViewById(R.id.answer_d);

        btnA.setText("A." + listAnswer.get(0).getContent());
        btnB.setText("B." + listAnswer.get(1).getContent());
        btnC.setText("C." + listAnswer.get(2).getContent());
        btnD.setText("D." + listAnswer.get(3).getContent());
    }

    public static String getCurrentTimeString(){
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Date date = new Date();
        String strDate = sdf.format(date);
        return strDate;
    }

    public static String getIdAnswer(Context context, int questionNumber, List<MultipleChoiceAnswer> listChoice){
        String idAnswer = "-1";
        MULTIPLE_CHOICE_ANSWER_ENUM answer = Utils.getMultipleChoiceAnswer(context, questionNumber);
        int position = MULTIPLE_CHOICE_ANSWER_ENUM.getPositionOfAnswer(answer);
        if(position != -1){
            idAnswer = String.valueOf(listChoice.get(position).getId());
        }
        return idAnswer;
    }

    public static double getPoint(Listening.ListeningQuestion question, int idAnswer){
        if(question.getIdAnswer() == idAnswer)
            return 0.5;
        return 0;
    }

    public static double getPoint(FillingBlank.FillingBlankQuestion question, int idAnswer){
        if(question.getIdAnswer() == idAnswer)
            return 0.5;
        return 0;
    }

    public static double getPoint(Reading.ReadingQuestion question, int idAnswer){
        if(question.getIdAnswer() == idAnswer)
            return 0.5;
        return 0;
    }

    public static double getPoint(SingleQuestion question, int idAnswer){
        if(question.getIdAnswer() == idAnswer)
            return 0.5;
        return 0;
    }

    public static double getPoint(Writing question, String answer){
        if(question.getQuestion().trim() == answer.trim())
            return 0.5;
        return 0;
    }
}
