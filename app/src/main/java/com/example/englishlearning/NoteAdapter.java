package com.example.englishlearning;

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

import com.example.englishlearning.Activity.PracticeActivity.EditWordActivity;
import com.example.englishlearning.Databases.UserDataHelper;
import com.example.englishlearning.Model.NotedWord;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {
    private List<NotedWord> list;
    private Context context;

    private DatabaseReference reference;
    private ValueEventListener listener;

    public NoteAdapter(Context context, DatabaseReference reference, ValueEventListener listener){
        this.context = context;
        this.reference = reference;
        this.listener = listener;
        list = new ArrayList<>();
    }

    public NoteAdapter(Context context){
        this.context = context;
        list = new ArrayList<>();
    }

    public List<NotedWord> getListItem(){
        return list;
    }

    public List<NotedWord> getList() {
        return list;
    }

    public void setList(List<NotedWord> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NotedWord item = list.get(position);


        holder.setId(item.getId());
        holder.getTvContent().setText(item.getContent());
        holder.getTvMeaning().setText(item.getMeaning());
        holder.getTvType().setText(item.getType().toString());
        holder.btnEditWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), EditWordActivity.class);
                intent.putExtra("id", item.getId());
                intent.putExtra("content", item.getContent());
                intent.putExtra("meaning", item.getMeaning());
                intent.putExtra("type", item.getType().toString());
                context.startActivity(intent);
            }
        });
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Utils.isLoggedIn(view.getContext())){
                    deleteFromFirebase(holder, item);
                }else{
                    deleteFromSqlite(holder, item);
                }
            }
        });
    }

    private void deleteFromFirebase(ViewHolder holder, NotedWord item){
        String login = Utils.getLogin(context);
        reference = FirebaseDatabase.getInstance().getReference("note_word/"+login);
        reference.removeEventListener(listener);
        reference.child(String.valueOf(item.getId())).removeValue();
        list.remove(holder.getAdapterPosition());
        notifyItemRemoved(holder.getAdapterPosition());
    }

    private void deleteFromSqlite(ViewHolder holder, NotedWord item){
        UserDataHelper helper = new UserDataHelper(MyApplication.getAppContext());
        SQLiteDatabase database = helper.getWritableDatabase();
        // The columns for the WHERE clause
        String selection = ("id" + " = ?");
        // The values for the WHERE clause
        String[] selectionArgs = {String.valueOf(item.getId())};
        int id = database.delete("note_word", selection, selectionArgs);
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
        private TextView tvMeaning;
        private TextView tvType;
        private Button btnEditWord;
        private Button btnDelete;
        private ArrayAdapter<NotedWord.Type> adapter;

        public ViewHolder(View view) {
            super(view);
            tvContent = view.findViewById(R.id.tv_content);
            tvMeaning = view.findViewById(R.id.tv_meaning);
            tvType = view.findViewById(R.id.tv_type);
            btnEditWord =view.findViewById(R.id.btn_edit_word);
            btnDelete = view.findViewById(R.id.btn_delete);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(tvMeaning.getVisibility() == View.GONE){
                        tvMeaning.setVisibility(View.VISIBLE);
                        btnEditWord.setVisibility(View.VISIBLE);
                        btnDelete.setVisibility(View.VISIBLE);
                    } else {
                        tvMeaning.setVisibility(View.GONE);
                        btnEditWord.setVisibility(View.GONE);
                        btnDelete.setVisibility(View.GONE);
                    }
                }
            });

//            adapter = new ArrayAdapter<NotedWord.Type>(view.getContext(), android.R.layout.simple_list_item_1, NotedWord.Type.values());
//            spinnerType.setAdapter(adapter);
        }

        public TextView getTvContent() {
            return tvContent;
        }

        public TextView getTvMeaning() {
            return tvMeaning;
        }

        public TextView getTvType() {
            return tvType;
        }

        public ArrayAdapter<NotedWord.Type> getAdapter() {
            return adapter;
        }

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }
    }
}
