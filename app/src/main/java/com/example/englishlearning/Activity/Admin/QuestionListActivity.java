package com.example.englishlearning.Activity.Admin;

import static com.example.englishlearning.Activity.Admin.AdminDashBoardActivity.TABLE;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.englishlearning.Activity.DashBoard;
import com.example.englishlearning.Activity.NoteActivity;
import com.example.englishlearning.Databases.EnglishHelper;
import com.example.englishlearning.Databases.UserDataHelper;
import com.example.englishlearning.Model.NotedWord;
import com.example.englishlearning.Model.Question;
import com.example.englishlearning.NoteAdapter;
import com.example.englishlearning.QuestionAdapter;
import com.example.englishlearning.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class QuestionListActivity extends AppCompatActivity {

    private QuestionAdapter adapter;
    private RecyclerView recyclerView;
    private Button btnAdd;
    private String table;
    public static final String IS_ADD = "isAdd";
    private boolean isAdd;
    public static int REQUEST_CODE = 1;
    private View header;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_list);

        isAdd = true;
        table = getIntent().getStringExtra(TABLE);
        recyclerView = findViewById(R.id.recycler_item);
        btnAdd = findViewById(R.id.btn_add);
        adapter = new QuestionAdapter(this, table);
        getData(table);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        header = findViewById(R.id.header);

        header.findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), AdminDashBoardActivity.class);
                startActivity(intent);
            }
        });


        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (table){
                    case "writing":
                        moveToActivity(AddWriteActivity.class);
                        break;
                    case "reading":
                        moveToActivity(AddEssayActivity.class);
                        break;
                    case "single_question":
                        moveToActivity(AddSingleQuestionActivity.class);
                        break;
                    case "filling_blank":
                        moveToActivity(AddFillBlankActivity.class);
                        break;
                }
            }
        });
    }

    private void moveToActivity(Class activity) {
        Intent intent = new Intent(QuestionListActivity.this, activity);
        intent.putExtra(IS_ADD, isAdd);
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_CODE){
            if(resultCode == RESULT_OK) {
                long id = data.getLongExtra("id", -1);
                String question = data.getStringExtra("question");
                adapter.getList().add(new Question(id, question));
                adapter.notifyDataSetChanged();
            }

        }

    }

    private void getData(String table) {
        EnglishHelper helper = new EnglishHelper(this);
        SQLiteDatabase database = helper.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + table;
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                adapter.getList().add( new Question(cursor.getInt(0), cursor.getString(1)));
            } while (cursor.moveToNext());
        }
        database.close();
    }


}