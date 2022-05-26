package com.example.englishlearning;

import static com.example.englishlearning.Activity.Admin.QuestionListActivity.IS_ADD;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.englishlearning.Activity.Admin.AddEssayActivity;
import com.example.englishlearning.Activity.Admin.AddFillBlankActivity;
import com.example.englishlearning.Activity.Admin.AddSingleQuestionActivity;
import com.example.englishlearning.Activity.Admin.AddWriteActivity;
import com.example.englishlearning.Activity.Admin.AdminDashBoardActivity;
import com.example.englishlearning.Activity.Admin.QuestionListActivity;
import com.example.englishlearning.Activity.PracticeActivity.EditWordActivity;
import com.example.englishlearning.Databases.EnglishHelper;
import com.example.englishlearning.Databases.UserDataHelper;
import com.example.englishlearning.Model.NotedWord;
import com.example.englishlearning.Model.Question;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.ViewHolder> {
    private List<Question> list;
    private Context context;
    private String type;
    private Boolean isAdd = false;


    public QuestionAdapter(Context context, String type){
        this.context = context;
        this.type = type;
        list = new ArrayList<>();
    }

    public List<Question> getListItem(){
        return list;
    }

    public List<Question> getList() {
        return list;
    }

    public void setList(List<Question> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public QuestionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.question_item, parent, false);
        return new QuestionAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionAdapter.ViewHolder holder, int position) {
        Question item = list.get(position);


        holder.setId(item.getId());
        holder.getTvContent().setText(item.getQuestion());
        holder.btnEditWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println(holder.id);
                System.out.println(holder.getAdapterPosition());
                System.out.println(list.size());

                switch (type){
                    case "writing":
                        Intent intent = new Intent(view.getContext(), AddWriteActivity.class);
                        intent.putExtra(IS_ADD, isAdd);
                        intent.putExtra("id", item.getId());
                        context.startActivity(intent);
                        break;
                    case "reading":
                        Intent intent1 = new Intent(view.getContext(), AddEssayActivity.class);
                        intent1.putExtra(IS_ADD, isAdd);
                        intent1.putExtra("id", item.getId());
                        context.startActivity(intent1);
                        break;
                    case "single_question":
                        Intent intent2 = new Intent(view.getContext(), AddSingleQuestionActivity.class);
                        intent2.putExtra(IS_ADD, isAdd);
                        intent2.putExtra("id", item.getId());
                        context.startActivity(intent2);
                        break;
                    case "filling_blank":
                        Intent intent3 = new Intent(view.getContext(), AddFillBlankActivity.class);
                        intent3.putExtra(IS_ADD, isAdd);
                        intent3.putExtra("id", item.getId());
                        context.startActivity(intent3);
                        break;
                }
            }

        });
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteFromSqlite(holder, item);
            }
        });
    }

    private void deleteFromSqlite(QuestionAdapter.ViewHolder holder, Question item){
        EnglishHelper helper = new EnglishHelper(MyApplication.getAppContext());
        SQLiteDatabase database = helper.getWritableDatabase();
        // The columns for the WHERE clause
        String selection = ("id" + " = ?");
        // The values for the WHERE clause
        String[] selectionArgs = {String.valueOf(item.getId())};
        int id = database.delete(type, selection, selectionArgs);
        list.remove(holder.getAdapterPosition());
        notifyItemRemoved(holder.getAdapterPosition());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        private long id;
        private TextView tvContent;
        private Button btnEditWord;
        private Button btnDelete;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public TextView getTvContent() {
            return tvContent;
        }

        public void setTvContent(TextView tvContent) {
            this.tvContent = tvContent;
        }

        public Button getBtnEditWord() {
            return btnEditWord;
        }

        public void setBtnEditWord(Button btnEditWord) {
            this.btnEditWord = btnEditWord;
        }

        public Button getBtnDelete() {
            return btnDelete;
        }

        public void setBtnDelete(Button btnDelete) {
            this.btnDelete = btnDelete;
        }

        public ViewHolder(View view) {
            super(view);
            tvContent = view.findViewById(R.id.tv_question);
            btnEditWord =view.findViewById(R.id.btn_edit_question);
            btnDelete = view.findViewById(R.id.btn_delete_question);
        }

    }
}
