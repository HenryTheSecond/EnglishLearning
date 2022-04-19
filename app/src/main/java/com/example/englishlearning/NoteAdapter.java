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

        holder.getEtContent().setText(item.getContent());
        holder.getEtMeaning().setText(item.getMeaning());

        int typePosition = holder.getAdapter().getPosition(item.getType());
        holder.getSpinnerType().setSelection(typePosition);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        private EditText etContent;
        private Spinner spinnerType;
        private EditText etMeaning;
        private ArrayAdapter<NotedWord.Type> adapter;

        public ViewHolder(View view) {
            super(view);

            etContent = view.findViewById(R.id.et_content);
            spinnerType = view.findViewById(R.id.spinner_type);
            etMeaning = view.findViewById(R.id.et_meaning);

            adapter = new ArrayAdapter<NotedWord.Type>(view.getContext(), android.R.layout.simple_list_item_1, NotedWord.Type.values());
            spinnerType.setAdapter(adapter);
        }

        public EditText getEtContent() {
            return etContent;
        }

        public Spinner getSpinnerType() {
            return spinnerType;
        }

        public EditText getEtMeaning() {
            return etMeaning;
        }

        public ArrayAdapter<NotedWord.Type> getAdapter() {
            return adapter;
        }
    }
}
