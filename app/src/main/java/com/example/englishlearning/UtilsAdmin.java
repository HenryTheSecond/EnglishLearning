package com.example.englishlearning;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.englishlearning.Databases.EnglishHelper;
import com.example.englishlearning.Model.MultipleChoiceAnswer;

import java.util.HashMap;
import java.util.List;

public class UtilsAdmin {
    public static long insertToMultipleChoiceAnswer(String content){
        EnglishHelper helper = new EnglishHelper(MyApplication.getAppContext());
        SQLiteDatabase database = helper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("content", content);
        return database.insert("multiple_choice_answer", null, contentValues);
    }


    public static boolean checkSingleQuestion(View multipleChoice){
        EditText edtAnswerA = multipleChoice.findViewById(R.id.answer_a);
        EditText edtAnswerB = multipleChoice.findViewById(R.id.answer_b);
        EditText edtAnswerC = multipleChoice.findViewById(R.id.answer_c);
        EditText edtAnswerD = multipleChoice.findViewById(R.id.answer_d);
        if(edtAnswerA.getText().toString().trim().equals("") || edtAnswerB.getText().toString().trim().equals("") ||
                edtAnswerC.getText().toString().trim().equals("") ||  edtAnswerD.getText().toString().trim().equals("")){
            Toast.makeText(multipleChoice.getContext(), "Please fulfill the answer content", Toast.LENGTH_SHORT).show();
            return false;
        }
        RadioGroup radioGroup = multipleChoice.findViewById(R.id.radio_group);
        if(radioGroup.getCheckedRadioButtonId() == -1){
            Toast.makeText(multipleChoice.getContext(), "Please select the answer", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public static long[] insertSingleQuestion(View multipleChoice){
        HashMap<Integer, Integer> indexMapToEdt = new HashMap<>();
        indexMapToEdt.put(0, R.id.answer_a);
        indexMapToEdt.put(1, R.id.answer_b);
        indexMapToEdt.put(2, R.id.answer_c);
        indexMapToEdt.put(3, R.id.answer_d);

        long[] listId = new long[4];

        for(int i=0; i<4; i++){
            EditText edtTemp = multipleChoice.findViewById(indexMapToEdt.get(i));
            listId[i] = UtilsAdmin.insertToMultipleChoiceAnswer( edtTemp.getText().toString() );
        }
        return listId;
    }

    public static int findIndexOfAnswer(List<MultipleChoiceAnswer> list, int idAnswer){
        for(int i=0; i<list.size(); i++){
            if(list.get(i).getId() == idAnswer)
                return i;
        }
        return -1;
    }

    public static Cursor getQuestionById(String tableName, long id){
        EnglishHelper helper = new EnglishHelper(MyApplication.getAppContext());
        SQLiteDatabase database = helper.getReadableDatabase();
        Cursor cursor = database.rawQuery("Select * from " + tableName + " Where id = " + id, null);
        return cursor;
    }
}
