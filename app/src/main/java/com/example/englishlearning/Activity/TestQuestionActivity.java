package com.example.englishlearning.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.englishlearning.AnswerStore.AnswerStore;
import com.example.englishlearning.AnswerStore.WritingStore;
import com.example.englishlearning.QuestionFragment.FillingBlankParagraphFragment;
import com.example.englishlearning.QuestionFragment.ListeningFragment;
import com.example.englishlearning.QuestionFragment.ReadingParagraphFragment;
import com.example.englishlearning.QuestionFragment.SingleQuestionFragment;
import com.example.englishlearning.QuestionFragment.WritingFragment;
import com.example.englishlearning.R;
import com.example.englishlearning.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class TestQuestionActivity extends AppCompatActivity {
    private CountDownTimer countDownTimer;
    private TextView tvCountDownTimer;
    private FrameLayout questionContent;

    private List<Button> listBtnQuestion;
    private int currentQuestion;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_question);

        Utils.clearAnswerStoreFile(this);
        currentQuestion = 1;

        //Binding
        tvCountDownTimer = findViewById(R.id.tv_countdown_timer);
        questionContent = findViewById(R.id.question_content);

        listBtnQuestion = new ArrayList<>();


        //MediaPlayer mediaPlayer = Utils.playListeningAudio(this, "test.mp3");
        //mediaPlayer.start();

        findAllQuestionButtons();
        runTest();
        startTimer();
    }

    private void findAllQuestionButtons() {
        ViewGroup view = findViewById(R.id.table_question);
        for(int i=0; i<view.getChildCount(); i++){
            ViewGroup childView = (ViewGroup) view.getChildAt(i);
            for(int j=0; j<childView.getChildCount(); j++){
                listBtnQuestion.add( (Button)childView.getChildAt(j) );
            }
        }
    }

    private void runTest() {
        Button q2 = findViewById(R.id.q_2);
        Button q3 = findViewById(R.id.q_3);

    }


    private void startTimer(){
        countDownTimer = new CountDownTimer(1000*60*2, 1000) {
            @Override
            public void onTick(long l) {
                String second = String.format("%02d", (l/1000)%60);
                tvCountDownTimer.setText(String.valueOf(l/1000/60) + ":" + second );
            }

            @Override
            public void onFinish() {
                tvCountDownTimer.setText("00:00");
            }
        };
        countDownTimer.start();
    }


    private void addFragment(Fragment fragment){
        if (fragment != null) {
            FragmentManager fmgr = getSupportFragmentManager();
            FragmentTransaction ft = fmgr.beginTransaction();
            ft.replace(R.id.question_content, fragment);
            //ft.addToBackStack(fragment.getClass().getSimpleName());
            ft.commit();
        }
    }

    public void btnQuestionClick(View view) {
        Button btn = (Button)view;
        int number = Integer.parseInt( btn.getText().toString() );
        switch(number){
            case 1:
            case 2:
            case 3:
            case 4:{
                addFragment(new FillingBlankParagraphFragment(listBtnQuestion.get(0), listBtnQuestion.get(1),
                                                            listBtnQuestion.get(2), listBtnQuestion.get(3)));
                break;
            }
            case 5:{
                addFragment(new SingleQuestionFragment((Button) view));
                break;
            }
            case 6:
            case 7:
            case 8:{
                if( currentQuestion == 6 || currentQuestion == 7 || currentQuestion == 8 )
                    break;
                addFragment(new ListeningFragment("test.mp3", listBtnQuestion.get(5), listBtnQuestion.get(6),
                                                 listBtnQuestion.get(7)));
            }
        }
        currentQuestion = number;
    }
}