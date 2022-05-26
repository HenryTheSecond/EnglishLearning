package com.example.englishlearning.Activity.Admin;

import static com.example.englishlearning.Activity.Admin.AdminDashBoardActivity.FILL_BLANK;
import static com.example.englishlearning.Activity.Admin.AdminDashBoardActivity.READ;
import static com.example.englishlearning.Activity.Admin.AdminDashBoardActivity.TABLE;
import static com.example.englishlearning.Activity.Admin.QuestionListActivity.IS_ADD;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.englishlearning.Databases.EnglishHelper;
import com.example.englishlearning.Model.FillingBlank;
import com.example.englishlearning.Model.MultipleChoiceAnswer;
import com.example.englishlearning.Model.Reading;
import com.example.englishlearning.MyApplication;
import com.example.englishlearning.R;
import com.example.englishlearning.Utils;
import com.example.englishlearning.UtilsAdmin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AddFillBlankActivity extends AppCompatActivity {

    EditText edtParagraph;
    List<View> listMultipleChoice;
    Spinner spinnerLevel;
    Boolean isAdd;
    Long id;
    FillingBlank fillingBlank;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_fill_blank);
        listMultipleChoice = new ArrayList<>();

        isAdd = getIntent().getBooleanExtra(IS_ADD, true);
        id = getIntent().getLongExtra("id", -1);
        edtParagraph = findViewById(R.id.edt_paragraph);
        listMultipleChoice.add(findViewById(R.id.multiple_choice1));
        listMultipleChoice.add(findViewById(R.id.multiple_choice2));
        listMultipleChoice.add(findViewById(R.id.multiple_choice3));
        listMultipleChoice.add(findViewById(R.id.multiple_choice4));
        spinnerLevel = findViewById(R.id.spinner_level);

        ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_list_item_1, new Integer[]{1,2,3});
        spinnerLevel.setAdapter(adapter);

        Button btnAdd = findViewById(R.id.btn_add_question);

        if(isAdd && id == -1){
            btnAdd.setText("Add");
            btnAdd.setOnClickListener( view ->{
                addEssay();
            });
        } else{
            btnAdd.setText("Edit");
            setView();
            btnAdd.setOnClickListener( view ->{
                EditEssay();
            });
        }

        Button btnCancel = findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener( view ->{
            Intent intent = new Intent(AddFillBlankActivity.this, QuestionListActivity.class);
            intent.putExtra(TABLE, FILL_BLANK);
            startActivity(intent);
        });
    }

    private void EditEssay() {
       //TODO
        HashMap<Integer, Integer> mapFromIndexToEdtId = new HashMap<>();
        mapFromIndexToEdtId.put(0, R.id.answer_a);
        mapFromIndexToEdtId.put(1, R.id.answer_b);
        mapFromIndexToEdtId.put(2, R.id.answer_c);
        mapFromIndexToEdtId.put(3, R.id.answer_d);

        HashMap<Integer, Integer> mapFromRadioIdToIndex = new HashMap<>();
        mapFromRadioIdToIndex.put(R.id.rbt_answer_a, 0);
        mapFromRadioIdToIndex.put(R.id.rbt_answer_b, 1);
        mapFromRadioIdToIndex.put(R.id.rbt_answer_c, 2);
        mapFromRadioIdToIndex.put(R.id.rbt_answer_d, 3);

        EnglishHelper helper = new EnglishHelper(this);
        SQLiteDatabase database = helper.getWritableDatabase();

        //TODO
        for(int i=0; i<listMultipleChoice.size(); i++){
            List<MultipleChoiceAnswer> listMul = fillingBlank.getListQuestions().get(i).getListChoice();

            //Update Multiple Choice
            for(int j=0; j<listMul.size(); j++){
                EditText edtAnswer = listMultipleChoice.get(i).findViewById( mapFromIndexToEdtId.get(j) );
                ContentValues contentValues = new ContentValues();
                contentValues.put("content", edtAnswer.getText().toString().trim());
                database.update("multiple_choice_answer", contentValues, "id=?", new String[]{ String.valueOf(listMul.get(j).getId()) });
            }

            //Update question
            ContentValues contentValues = new ContentValues();
            RadioGroup radioGroup = listMultipleChoice.get(i).findViewById( R.id.radio_group );
            int idAnswer = fillingBlank.getListQuestions().get(i).getListChoice().get( mapFromRadioIdToIndex.get( radioGroup.getCheckedRadioButtonId() ) ).getId();
            contentValues.put("id_answer", idAnswer);
            database.update("filling_blank_question", contentValues, "id=?", new String[]{ String.valueOf(fillingBlank.getListQuestions().get(i).getId()) });
        }

        //Update paragraph
        ContentValues contentValues = new ContentValues();
        contentValues.put("paragraph", edtParagraph.getText().toString().trim());
        contentValues.put("level", Integer.parseInt(spinnerLevel.getSelectedItem().toString()));
        database.update("filling_blank", contentValues, "id=?", new String[]{ String.valueOf(fillingBlank.getId()) });

        finish();
    }

    private void setView() {
        HashMap<Integer, Integer> mapFromIndexToRadioBtn = new HashMap<>();
        mapFromIndexToRadioBtn.put(0, R.id.rbt_answer_a);
        mapFromIndexToRadioBtn.put(1, R.id.rbt_answer_b);
        mapFromIndexToRadioBtn.put(2, R.id.rbt_answer_c);
        mapFromIndexToRadioBtn.put(3, R.id.rbt_answer_d);

        Cursor cursor = UtilsAdmin.getQuestionById(FILL_BLANK,id);
        while (cursor.moveToNext()){
            fillingBlank = new FillingBlank( cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getInt(3) );
        }
        edtParagraph.setText( fillingBlank.getParagraph() );
        for(int i=0; i<listMultipleChoice.size(); i++){
            int answer = fillingBlank.getListQuestions().get(i).getIdAnswer();
            int index = UtilsAdmin.findIndexOfAnswer(fillingBlank.getListQuestions().get(i).getListChoice(), answer);
            RadioButton radioBtn = listMultipleChoice.get(i).findViewById( mapFromIndexToRadioBtn.get(index) );
            radioBtn.setChecked(true);
            Utils.setTextForEdtMultipleChoice(listMultipleChoice.get(i),  fillingBlank.getListQuestions().get(i).getListChoice());
        }
        spinnerLevel.setSelection(fillingBlank.getLevel()-1);
        //TODO map radio
    }
    private void addEssay() {
        EnglishHelper helper = new EnglishHelper(MyApplication.getAppContext());
        SQLiteDatabase database = helper.getWritableDatabase();

        HashMap<Integer, Integer> radioMapToIndex = new HashMap<>();
        radioMapToIndex.put(R.id.rbt_answer_a, 0);
        radioMapToIndex.put(R.id.rbt_answer_b, 1);
        radioMapToIndex.put(R.id.rbt_answer_c, 2);
        radioMapToIndex.put(R.id.rbt_answer_d, 3);

        if(checkInput()==false)
            return;


        String idQuestion = "";
        for(int i=0; i<listMultipleChoice.size(); i++){
            RadioGroup radioGroup = listMultipleChoice.get(i).findViewById(R.id.radio_group);
            long[] listId = UtilsAdmin.insertSingleQuestion( listMultipleChoice.get(i) );
            String idMultipleChoice = "";
            for(long id: listId){
                idMultipleChoice += "[" + String.valueOf(id) + "]";
            }

            ContentValues contentValues = new ContentValues();
            contentValues.put("id_multiple_choice", idMultipleChoice);
            contentValues.put("id_answer", listId[ radioMapToIndex.get(radioGroup.getCheckedRadioButtonId()) ]);
            long idInsert = database.insert("filling_blank_question", null, contentValues);
            idQuestion += "[" + String.valueOf(idInsert) + "]";
        }

        ContentValues contentValues = new ContentValues();
        contentValues.put("paragraph", edtParagraph.getText().toString());
        contentValues.put("id_questions", idQuestion);
        contentValues.put("level", Integer.parseInt( spinnerLevel.getSelectedItem().toString() ));
        long idInsert = database.insert("filling_blank", null, contentValues);
        if(idInsert != -1){
            Toast.makeText(this, "Insert successfully " + String.valueOf(idInsert) , Toast.LENGTH_SHORT).show();
            Intent intent = new Intent();
            intent.putExtra("id", idInsert);
            intent.putExtra("question", edtParagraph.getText().toString());
            setResult(RESULT_OK, intent);
        }
        finish();
    }

    private boolean checkInput(){
        if(edtParagraph.getText().toString().trim().equals("")){
            Toast.makeText(this, "Please fulfill paragraph", Toast.LENGTH_SHORT).show();
            return false;
        }
        for(int i=0; i<listMultipleChoice.size(); i++){
            if(!UtilsAdmin.checkSingleQuestion(listMultipleChoice.get(i))){
                return false;
            }
        }
        return true;
    }
}