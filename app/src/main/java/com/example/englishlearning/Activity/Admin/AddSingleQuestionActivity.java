package com.example.englishlearning.Activity.Admin;

import static com.example.englishlearning.Activity.Admin.AdminDashBoardActivity.FILL_BLANK;
import static com.example.englishlearning.Activity.Admin.AdminDashBoardActivity.SINGLE_QUESTION;
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
import com.example.englishlearning.Model.NotedWord;
import com.example.englishlearning.Model.SingleQuestion;
import com.example.englishlearning.MyApplication;
import com.example.englishlearning.R;
import com.example.englishlearning.Utils;
import com.example.englishlearning.UtilsAdmin;

import java.util.HashMap;

public class AddSingleQuestionActivity extends AppCompatActivity {

    EditText edtQuestion;
    Spinner spinnerLevel;
    View multipleChoice1;
    Boolean isAdd;
    Long id;
    SingleQuestion singleQuestion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_single_question);

        isAdd = getIntent().getBooleanExtra(IS_ADD, true);
        id = getIntent().getLongExtra("id", -1);
        edtQuestion = findViewById(R.id.edt_question1);
        spinnerLevel = findViewById(R.id.spinner_level);
        multipleChoice1 = findViewById(R.id.multiple_choice1);

        Button btnAdd = findViewById(R.id.btn_add_question);
        Button btnCancel = findViewById(R.id.btn_cancel);

        ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_list_item_1, new Integer[]{1,2,3});
        spinnerLevel.setAdapter(adapter);

        if(isAdd && id == -1){
            btnAdd.setText("Add");
            btnAdd.setOnClickListener( view ->{
                addSingleQuestion();
            });
        } else{
            btnAdd.setText("Edit");
            setView();
            btnAdd.setOnClickListener( view ->{
                editSingleQuestion();
            });
        }

        btnCancel.setOnClickListener( view ->{
            Intent intent = new Intent(AddSingleQuestionActivity.this, QuestionListActivity.class);
            intent.putExtra(TABLE, SINGLE_QUESTION);
            startActivity(intent);
        });



    }

    private void editSingleQuestion() {
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

        EnglishHelper helper = new EnglishHelper(MyApplication.getAppContext());
        SQLiteDatabase database = helper.getWritableDatabase();

        if(edtQuestion.getText().toString().trim().equals("")){
            Toast.makeText(this, "Please fulfill the question", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!UtilsAdmin.checkSingleQuestion(multipleChoice1))
            return;

        for(int i=0; i<singleQuestion.getListChoice().size(); i++){
            EditText edtAnswer = multipleChoice1.findViewById( mapFromIndexToEdtId.get(i) );
            ContentValues contentValues = new ContentValues();
            contentValues.put("content", edtAnswer.getText().toString().trim());
            database.update("multiple_choice_answer", contentValues, "id=?", new String[]{ String.valueOf(singleQuestion.getListChoice().get(i).getId()) });
        }

        ContentValues contentValues = new ContentValues();
        contentValues.put("question", edtQuestion.getText().toString().trim());
        RadioGroup radioGroup = multipleChoice1.findViewById( R.id.radio_group );
        int idAnswer = singleQuestion.getListChoice().get(mapFromRadioIdToIndex.get( radioGroup.getCheckedRadioButtonId() )).getId();
        contentValues.put("id_answer", idAnswer);
        contentValues.put("level", Integer.parseInt(spinnerLevel.getSelectedItem().toString()));
        database.update("single_question", contentValues, "id=?", new String[]{ String.valueOf(singleQuestion.getId()) });

        finish();
    }

    private void setView() {

        HashMap<Integer, Integer> mapFromIndexToRadioBtn = new HashMap<>();
        mapFromIndexToRadioBtn.put(0, R.id.rbt_answer_a);
        mapFromIndexToRadioBtn.put(1, R.id.rbt_answer_b);
        mapFromIndexToRadioBtn.put(2, R.id.rbt_answer_c);
        mapFromIndexToRadioBtn.put(3, R.id.rbt_answer_d);

        Cursor cursor = UtilsAdmin.getQuestionById(SINGLE_QUESTION,id);
        while (cursor.moveToNext()){
            singleQuestion = new SingleQuestion( cursor.getInt(0), cursor.getString(1), cursor.getString(2),
                    cursor.getInt(3), cursor.getInt(4));
        }
        edtQuestion.setText( singleQuestion.getQuestion() );

        System.out.println("Level " + String.valueOf(singleQuestion.getLevel()));
        Utils.setTextForEdtMultipleChoice(multipleChoice1,  singleQuestion.getListChoice());

        int index = UtilsAdmin.findIndexOfAnswer(singleQuestion.getListChoice(), singleQuestion.getIdAnswer());
        RadioButton radioBtn = multipleChoice1.findViewById( mapFromIndexToRadioBtn.get(index) );
        radioBtn.setChecked(true);
        System.out.println(spinnerLevel.getChildCount());
        spinnerLevel.setSelection(singleQuestion.getLevel()-1);
        //TODO map radio
    }

    private void addSingleQuestion(){
        HashMap<Integer, Integer> radioMapToIndex = new HashMap<>();
        radioMapToIndex.put(R.id.rbt_answer_a, 0);
        radioMapToIndex.put(R.id.rbt_answer_b, 1);
        radioMapToIndex.put(R.id.rbt_answer_c, 2);
        radioMapToIndex.put(R.id.rbt_answer_d, 3);


        if(edtQuestion.getText().toString().trim().equals("")){
            Toast.makeText(this, "Please fulfill the question", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!UtilsAdmin.checkSingleQuestion(multipleChoice1))
            return;

        RadioGroup radioGroup = multipleChoice1.findViewById(R.id.radio_group);
        long[] listId = UtilsAdmin.insertSingleQuestion(multipleChoice1);
        String idMultipleChoice = "";
        for(long id: listId){
            idMultipleChoice += "[" + String.valueOf(id) + "]";
        }

        EnglishHelper helper = new EnglishHelper(MyApplication.getAppContext());
        SQLiteDatabase database = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("question", edtQuestion.getText().toString());
        contentValues.put("id_multiple_choice", idMultipleChoice);
        contentValues.put("id_answer", listId[ radioMapToIndex.get(radioGroup.getCheckedRadioButtonId()) ]);
        contentValues.put("level", Integer.parseInt( spinnerLevel.getSelectedItem().toString() ));

        long insertId = database.insert("single_question", null, contentValues);
        if(insertId != -1){
            Toast.makeText(this, "Insert successfully " + String.valueOf(insertId) , Toast.LENGTH_SHORT).show();
            Intent intent = new Intent();
            intent.putExtra("id", insertId);
            intent.putExtra("question", edtQuestion.getText().toString());
            setResult(RESULT_OK, intent);
        }
        finish();
    }
}