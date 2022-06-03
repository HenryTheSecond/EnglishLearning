package com.example.englishlearning;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.englishlearning.Model.EssayWriting.EssayWriting;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class WritingAdapter extends BaseAdapter {
    private Activity context;
    private List<EssayWriting> list;

    public WritingAdapter(Activity context){
        this.context = context;
        list = new ArrayList<>();
    }

    public List<EssayWriting> getList() {
        return list;
    }

    public void setList(List<EssayWriting> list) {
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
        return list.get(i).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = this.context.getLayoutInflater();
        view = inflater.inflate(R.layout.item_writing_essay, null);

        TextView tvContent = view.findViewById(R.id.tv_content);
        TextView tvPoint = view.findViewById(R.id.tv_point);

        EssayWriting item = list.get(i);
        tvContent.setText(item.getContent());

        if(item.isPointed())
            tvPoint.setText( String.valueOf(item.getPoint()) );
        else
            tvPoint.setText("Waiting");

        view.setOnClickListener(v->{
            Gson gson = new Gson();
            String json = gson.toJson(item);
            Intent intent = new Intent(context, DetailWritingActivity.class);
            intent.putExtra("item", json);
            context.startActivity(intent);
        });

        return view;
    }
}
