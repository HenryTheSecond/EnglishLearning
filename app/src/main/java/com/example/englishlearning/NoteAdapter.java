package com.example.englishlearning;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.englishlearning.Model.NotedWord;

import java.util.ArrayList;
import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {
    private List<NotedWord> list;

    public NoteAdapter(List<NotedWord> list) {
        this.list = list;
    }

    public NoteAdapter(){
        list = new ArrayList<>();
    }

    public List<NotedWord> getListItem(){
        return list;
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

        holder.getTvContent().setText(item.getContent());
        holder.getTvMeaning().setText(item.getMeaning());
        holder.getTvType().setText(item.getType().toString());

//        int typePosition = holder.getAdapter().getPosition(item.getType());
//        holder.getSpinnerType().setSelection(typePosition);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvContent;
        private TextView tvMeaning;
        private TextView tvType;
        private ArrayAdapter<NotedWord.Type> adapter;

        public ViewHolder(View view) {
            super(view);

            tvContent = view.findViewById(R.id.tv_content);
            tvMeaning = view.findViewById(R.id.tv_meaning);
            tvType = view.findViewById(R.id.tv_type);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(tvMeaning.getVisibility() == View.GONE){
                        tvMeaning.setVisibility(View.VISIBLE);
                    } else {
                        tvMeaning.setVisibility(View.GONE);
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
    }
}
