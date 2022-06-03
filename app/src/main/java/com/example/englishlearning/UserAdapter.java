package com.example.englishlearning;

import static com.example.englishlearning.Activity.Admin.QuestionListActivity.IS_ADD;
import static com.example.englishlearning.Activity.DashBoard.CALLING_TYPE;

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
import com.example.englishlearning.Activity.ChooseTypeActivity;
import com.example.englishlearning.Activity.DashBoard;
import com.example.englishlearning.Activity.PracticeActivity.EditWordActivity;
import com.example.englishlearning.Activity.ReviewListActivity;
import com.example.englishlearning.Databases.EnglishHelper;
import com.example.englishlearning.Databases.UserDataHelper;
import com.example.englishlearning.Model.NotedWord;
import com.example.englishlearning.Model.Question;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    private List<String> list;
    private Context context;

    public UserAdapter(Context context){
        this.context = context;
        list = new ArrayList<>();
    }

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public UserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, false);
        return new UserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.ViewHolder holder, int position) {
        String item = list.get(position);

        holder.getName().setText(item);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ChooseTypeActivity.class);
                intent.putExtra("name", item);
                intent.putExtra(CALLING_TYPE, "review");
                context.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return list.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView getName() {
            return name;
        }

        public void setName(TextView name) {
            this.name = name;
        }

        private TextView name;

        public ViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.tv_name);
        }

    }
}
