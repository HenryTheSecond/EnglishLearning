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
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.englishlearning.QuestionFragment.SingleQuestionFragment;
import com.example.englishlearning.R;
import com.example.englishlearning.Utils;

import java.io.IOException;
import java.io.InputStream;

public class TestQuestionActivity extends AppCompatActivity {
    private CountDownTimer countDownTimer;
    private TextView tvCountDownTimer;
    private FrameLayout questionContent;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_question);

        //Binding
        tvCountDownTimer = findViewById(R.id.tv_countdown_timer);
        questionContent = findViewById(R.id.question_content);


        MediaPlayer mediaPlayer = Utils.playListeningAudio(this, "test.mp3");
        mediaPlayer.start();

        addFragment(new SingleQuestionFragment( findViewById(R.id.q_2) ));
        startTimer();
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
}