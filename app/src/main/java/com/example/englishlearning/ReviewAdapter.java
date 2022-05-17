package com.example.englishlearning;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.englishlearning.Activity.PracticeActivity.PracticeFillingBlankReview;
import com.example.englishlearning.Activity.PracticeActivity.PracticeListeningReview;
import com.example.englishlearning.Activity.PracticeActivity.PracticeReadingReview;
import com.example.englishlearning.Activity.PracticeActivity.PracticeSingleQuestionReview;
import com.example.englishlearning.Activity.PracticeActivity.PracticeWritingReview;
import com.example.englishlearning.Activity.ReviewResult;
import com.example.englishlearning.Model.PracticeModel.GeneralPractice;
import com.example.englishlearning.Model.PracticeModel.PracticeFillingBlank;
import com.example.englishlearning.Model.PracticeModel.PracticeListening;
import com.example.englishlearning.Model.PracticeModel.PracticeReading;
import com.example.englishlearning.Model.PracticeModel.PracticeSingleQuestion;
import com.example.englishlearning.Model.PracticeModel.PracticeWriting;
import com.example.englishlearning.Model.ReviewModel.DisplayReview;
import com.example.englishlearning.Model.ReviewModel.RawTestRecord;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ReviewAdapter extends BaseAdapter {
    private Activity context;
    private List<GeneralPractice> list;


    public ReviewAdapter(Activity context){
        this.context = context;
        list = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public GeneralPractice getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return list.get(i).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = this.context.getLayoutInflater();
        view = inflater.inflate(R.layout.result_item, null);

        TextView tvDate = view.findViewById(R.id.tv_date);
        TextView tvResult = view.findViewById(R.id.tv_result);

        GeneralPractice item = getItem(i);
        tvDate.setText( item.getDateTime() );
        tvResult.setText( item.getResult() );

        view.setOnClickListener( v->{
            moveToReview(item);
        });

        return view;
    }


    private void moveToReview(GeneralPractice item){
        HashMap<Class, Class> map = new HashMap<>();
        map.put(RawTestRecord.class, ReviewResult.class);
        map.put(PracticeFillingBlank.class, PracticeFillingBlankReview.class);
        map.put(PracticeListening.class, PracticeListeningReview.class);
        map.put(PracticeReading.class, PracticeReadingReview.class);
        map.put(PracticeSingleQuestion.class, PracticeSingleQuestionReview.class);
        map.put(PracticeWriting.class, PracticeWritingReview.class);
        Intent intent = new Intent(context, map.get(item.getClass()));
        if(Utils.isLoggedIn(context)){
            Gson gson = new Gson();
            intent.putExtra("record", gson.toJson(item, item.getClass()));
        }else{
            intent.putExtra("id_test_record_key", (long) item.getId());
        }
        context.startActivity(intent);
    }


    public Activity getContext() {
        return context;
    }

    public void setContext(Activity context) {
        this.context = context;
    }

    public List<GeneralPractice> getList() {
        return list;
    }

    public void setList(List<GeneralPractice> list) {
        this.list = list;
    }


}
