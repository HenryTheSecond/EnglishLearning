package com.example.englishlearning;

import android.app.Activity;
import android.content.Intent;
import android.text.method.ScrollingMovementMethod;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Scroller;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EssaySubjectAdapter extends BaseAdapter {
    private Activity context;
    private List< Pair<Long, String> > list;

    public EssaySubjectAdapter(Activity context){
        this.context = context;
        list = new ArrayList<>();
    }

    public List<Pair<Long, String>> getList() {
        return list;
    }

    public void setList(List<Pair<Long, String>> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return list.get(i).first;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = this.context.getLayoutInflater();
        view = inflater.inflate(R.layout.item_essay_subject, null);

        TextView tvEssaySubject = view.findViewById(R.id.tv_essay_subject);
        Button btnWrite = view.findViewById(R.id.btn_write);
        Button btnReview = view.findViewById(R.id.btn_review);


        tvEssaySubject.setMovementMethod(new ScrollingMovementMethod());

        Pair<Long, String> item = (Pair<Long, String>) getItem(i);
        tvEssaySubject.setText( item.second );

        btnWrite.setOnClickListener( v -> {
            Intent intent = new Intent(context, WritingEssayActivity.class);
            intent.putExtra("idSubject", item.first);
            intent.putExtra("essaySubject", item.second);
            context.startActivity(intent);
        });

        btnReview.setOnClickListener(v ->{
            Intent intent = new Intent(context, ListWritingActivity.class);
            intent.putExtra("idSubject", item.first);
            context.startActivity(intent);
        });

        return view;
    }
}
