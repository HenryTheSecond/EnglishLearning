package com.example.englishlearning;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;

import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.example.englishlearning.Activity.LoginActivity;
import com.example.englishlearning.Databases.EnglishHelper;
import com.example.englishlearning.Databases.UserDataHelper;
import com.example.englishlearning.Model.FillingBlank;
import com.example.englishlearning.Model.Listening;
import com.example.englishlearning.Model.MultipleChoiceAnswer;
import com.example.englishlearning.Model.NotedWord;
import com.example.englishlearning.Model.PracticeModel.PracticeFillingBlank;
import com.example.englishlearning.Model.PracticeModel.PracticeListening;
import com.example.englishlearning.Model.PracticeModel.PracticeReading;
import com.example.englishlearning.Model.PracticeModel.PracticeSingleQuestion;
import com.example.englishlearning.Model.PracticeModel.PracticeWriting;
import com.example.englishlearning.Model.Reading;
import com.example.englishlearning.Model.ReviewModel.RawTestRecord;
import com.example.englishlearning.Model.SingleQuestion;
import com.example.englishlearning.Model.Writing;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Utils {
    private static final String FILE_NAME_ANSWER_STORE_TEMP = "AnswerStoreTemp";
    private static final String FILE_NAME_SAVE_LOG_IN = "LoginSave";
    private static final String QUESTION_KEY = "question";
    private static final String USERNAME_KEY = "username";


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


    public static Cursor getRandomQuestions(String tableName, int amount, int level){
        EnglishHelper helper = new EnglishHelper(MyApplication.getAppContext());
        SQLiteDatabase database = helper.getReadableDatabase();
        Cursor cursor = database.rawQuery("Select * from " + tableName + " Where level = " + level + " Order by RANDOM() LIMIT " + amount, null);
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

    public static void setTextForEdtMultipleChoice(View edtMultiChoiceView, List<MultipleChoiceAnswer> listAnswer){
        EditText edtA = edtMultiChoiceView.findViewById(R.id.answer_a);
        EditText edtB = edtMultiChoiceView.findViewById(R.id.answer_b);
        EditText edtC = edtMultiChoiceView.findViewById(R.id.answer_c);
        EditText edtD = edtMultiChoiceView.findViewById(R.id.answer_d);

        edtA.setText(listAnswer.get(0).getContent());
        edtB.setText(listAnswer.get(1).getContent());
        edtC.setText(listAnswer.get(2).getContent());
        edtD.setText(listAnswer.get(3).getContent());
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

    public static void colorAnswerReview(MultipleChoice multipleChoice, int idAnswer,
                                         List<MultipleChoiceAnswer> listChoice, int idCorrectAnswer  ){

        Map<Integer, Integer> map = new HashMap<>();
        map.put(0, R.id.answer_a);
        map.put(1, R.id.answer_b);
        map.put(2, R.id.answer_c);
        map.put(3, R.id.answer_d);

        int indexAnswer = -1;
        int indexCorrectAnswer = -1;
        for(int i=0; i<listChoice.size(); i++){
            if(idAnswer == listChoice.get(i).getId())
                indexAnswer = i;
            if(idCorrectAnswer == listChoice.get(i).getId())
                indexCorrectAnswer = i;
        }

        if(idAnswer == idCorrectAnswer){
            multipleChoice.getMultipleChoice().findViewById(map.get(indexAnswer)).setBackgroundTintList(multipleChoice.getMultipleChoice().getContext().getResources().getColorStateList(R.color.correct_answer));
        }
        else{
            if(indexAnswer != -1)
                multipleChoice.getMultipleChoice().findViewById(map.get(indexAnswer)).setBackgroundTintList(multipleChoice.getMultipleChoice().getContext().getResources().getColorStateList(R.color.chosen_answer));


            multipleChoice.getMultipleChoice().findViewById(map.get(indexCorrectAnswer)).setBackgroundTintList(multipleChoice.getMultipleChoice().getContext().getResources().getColorStateList(R.color.incorrect_answer));
        }
    }

    public static String HashPassword(String password){
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest( password.getBytes(StandardCharsets.UTF_8) );
            BigInteger number = new BigInteger(1, hash);
            StringBuilder hexString = new StringBuilder(number.toString(16));
            while (hexString.length() < 64)
            {
                hexString.insert(0, '0');
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean checkInternet(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getActiveNetworkInfo() !=null && connectivityManager.getActiveNetworkInfo().isConnected()){
            return true;
        }
        Toast.makeText(context, "No internet available", Toast.LENGTH_SHORT).show();
        return false;

    }


    public static void saveLogin(Context context, String username){
        SharedPreferences sharedPreferences = context.getSharedPreferences(FILE_NAME_SAVE_LOG_IN, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USERNAME_KEY, username);
        editor.commit();
    }

    public static String getLogin(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(FILE_NAME_SAVE_LOG_IN, Context.MODE_PRIVATE);
        return sharedPreferences.getString(USERNAME_KEY, "");
    }

    public static void removeLogin(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(FILE_NAME_SAVE_LOG_IN, Context.MODE_PRIVATE);
        sharedPreferences.edit().clear().commit();
    }

    public static boolean isLoggedIn(Context context){
        return !getLogin(context).equals("");
    }


    public static void createMenu(Activity context){
        Toolbar toolbar = context.findViewById(R.id.tool_bar);

        if(!Utils.isLoggedIn(context))
            toolbar.getMenu().getItem(1).setTitle("Log in");

        toolbar.getMenu().getItem(0).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if(Utils.checkInternet(context))
                    if(Utils.isLoggedIn(context)){
                        synchronizeData(context);
                    }else{
                        Intent intent = new Intent(context, LoginActivity.class);
                        intent.putExtra("isSynchronize", true);
                        context.startActivity(intent);
                    }
                return true;
            }
        });
        toolbar.getMenu().getItem(1).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Intent intent = new Intent(context, LoginActivity.class);
                if(Utils.isLoggedIn(context)){
                    removeLogin(context);
                    context.finish();
                }
                context.startActivity(intent);
                return true;
            }
        });
    }

    public static void synchronizeData(Activity context){
        String login = Utils.getLogin(context);
        UserDataHelper helper = new UserDataHelper(context);
        SQLiteDatabase database = helper.getReadableDatabase();

        //note_word
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("note_word/" + login);
        Cursor cursor = database.rawQuery("SELECT * FROM note_word", null);
        NotedWord notedWord;
        long id;
        while(cursor.moveToNext()){
            id = new Date().getTime();
            notedWord = new NotedWord(id, cursor.getString(1), cursor.getString(2), NotedWord.Type.parseType(cursor.getString(3)));
            ref.child(String.valueOf(id)).setValue(notedWord);
        }

        //filling_blank
        ref = FirebaseDatabase.getInstance().getReference("practice_filling_blank/" + login);
        cursor = database.rawQuery("Select * from practice_filling_blank", null);
        while(cursor.moveToNext()){
            id = new Date().getTime();

            DatabaseReference node = ref.child(String.valueOf(id));
            node.child("date_time").setValue(cursor.getString(1));
            node.child("correct").setValue(cursor.getInt(2));
            node.child("id_filling_blanks").setValue(cursor.getString(3));
            node.child("filling_blank_answer").setValue(cursor.getString(4));
        }

        //listening
        ref = FirebaseDatabase.getInstance().getReference("practice_listening/" + login);
        cursor = database.rawQuery("Select * from practice_listening", null);
        while(cursor.moveToNext()){
            id = new Date().getTime();
            DatabaseReference node = ref.child(String.valueOf(id));
            node.child("date_time").setValue(cursor.getString(1));
            node.child("correct").setValue(cursor.getInt(2));
            node.child("id_listenings").setValue(cursor.getString(3));
            node.child("listening_answer").setValue(cursor.getString(4));
        }

        //reading
        ref = FirebaseDatabase.getInstance().getReference("practice_reading/" + login);
        cursor = database.rawQuery("Select * from practice_reading", null);
        while(cursor.moveToNext()){
            id = new Date().getTime();
            DatabaseReference node = ref.child(String.valueOf(id));
            node.child("date_time").setValue(cursor.getString(1));
            node.child("correct").setValue(cursor.getInt(2));
            node.child("id_readings").setValue(cursor.getString(3));
            node.child("reading_answer").setValue(cursor.getString(4));
        }

        //single_question
        ref = FirebaseDatabase.getInstance().getReference("practice_single_question/" + login);
        cursor = database.rawQuery("Select * from practice_single_question", null);
        while(cursor.moveToNext()){
            id = new Date().getTime();
            DatabaseReference node = ref.child(String.valueOf(id));
            node.child("date_time").setValue(cursor.getString(1));
            node.child("correct").setValue(cursor.getInt(2));
            node.child("single_answer").setValue(cursor.getString(3));
        }

        //writing
        ref = FirebaseDatabase.getInstance().getReference("practice_writing/" + login);
        cursor = database.rawQuery("Select * from practice_writing", null);
        while(cursor.moveToNext()){
            id = new Date().getTime();
            DatabaseReference node = ref.child(String.valueOf(id));
            node.child("date_time").setValue(cursor.getString(1));
            node.child("correct").setValue(cursor.getInt(2));
            node.child("writing_answer").setValue(cursor.getString(3));
        }

        //all
        ref = FirebaseDatabase.getInstance().getReference("test_record/" + login);
        cursor = database.rawQuery("Select * from test_record", null);
        while(cursor.moveToNext()){
            id = new Date().getTime();

            DatabaseReference node = ref.child(String.valueOf(id));
            node.child("date_time").setValue(cursor.getString(1));
            node.child("point").setValue(cursor.getDouble(2));
            node.child("id_listenings").setValue(cursor.getString(3));
            node.child("listening_answer").setValue(cursor.getString(4));
            node.child("id_filling_blank").setValue(cursor.getString(5));
            node.child("filling_blank_answer").setValue(cursor.getString(6));
            node.child("id_reading").setValue(cursor.getString(7));
            node.child("reading_answer").setValue(cursor.getString(8));
            node.child("single_question").setValue(cursor.getString(9));
            node.child("writing").setValue(cursor.getString(10));
        }

        SQLiteDatabase writeDatabse = helper.getWritableDatabase();

        writeDatabse.execSQL("DELETE FROM note_word");
        writeDatabse.execSQL("DELETE FROM practice_filling_blank");
        writeDatabse.execSQL("DELETE FROM practice_listening");
        writeDatabse.execSQL("DELETE FROM practice_reading");
        writeDatabse.execSQL("DELETE FROM practice_single_question");
        writeDatabse.execSQL("DELETE FROM practice_writing");
        writeDatabse.execSQL("DELETE FROM test_record");


    }
}
