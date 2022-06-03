package com.example.englishlearning;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.englishlearning.Model.EssayWriting.WaitingEssay;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class WaitingEssayAdapter extends BaseAdapter {
    private Activity context;
    private List<WaitingEssay> list;

    public WaitingEssayAdapter(Activity context){
        this.context = context;
        list = new ArrayList<>();
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

    public List<WaitingEssay> getList() {
        return list;
    }

    public void setList(List<WaitingEssay> list) {
        this.list = list;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = this.context.getLayoutInflater();
        view = inflater.inflate(R.layout.item_waiting_essay, null);

        TextView tvUser = view.findViewById(R.id.tv_user);
        TextView tvSubject = view.findViewById(R.id.tv_subject);
        TextView tvContent = view.findViewById(R.id.tv_content);

        WaitingEssay item = list.get(i);
        tvUser.setText( item.getUser() );
        tvSubject.setText(item.getSubject());
        tvContent.setText(item.getContent());

        view.setOnClickListener(v->{
            Gson gson = new Gson();
            String object = gson.toJson(item);
            Intent intent = new Intent(context, DetailWaitingEssayActivity.class);
            intent.putExtra("item", object);
            intent.putExtra("position", i);
            context.startActivityForResult(intent, 101);
        });

        return view;
    }
}
