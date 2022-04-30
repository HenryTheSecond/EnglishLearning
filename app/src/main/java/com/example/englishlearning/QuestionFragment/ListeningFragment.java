package com.example.englishlearning.QuestionFragment;

import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.englishlearning.Model.Listening;
import com.example.englishlearning.MultipleChoice;
import com.example.englishlearning.R;
import com.example.englishlearning.Utils;


public class ListeningFragment extends Fragment {

    private SeekBar timer;
    private String fileName;
    private MediaPlayer player;
    private Handler handler = new Handler();
    private Runnable runnable;

    private Button btnPlayAudio;

    private TextView tvQuestion1;
    private MultipleChoice multipleChoice1;
    private TextView tvQuestion2;
    private MultipleChoice multipleChoice2;
    private TextView tvQuestion3;
    private MultipleChoice multipleChoice3;

    private Listening listening;



    public ListeningFragment(Listening listening, Button btnQuestion1, Button btnQuestion2, Button btnQuestion3) {
        this.fileName = listening.getFileName();

        multipleChoice1 = new MultipleChoice();
        multipleChoice1.setBtnQuestion(btnQuestion1);

        multipleChoice2 = new MultipleChoice();
        multipleChoice2.setBtnQuestion(btnQuestion2);

        multipleChoice3 = new MultipleChoice();
        multipleChoice3.setBtnQuestion(btnQuestion3);

        this.listening = listening;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_listening, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Binding
        timer = getView().findViewById(R.id.seek_bar_timer);
        btnPlayAudio = getView().findViewById(R.id.btn_play_audio);

        tvQuestion1 = getView().findViewById(R.id.tv_question1);
        multipleChoice1.setMultipleChoice( getView().findViewById(R.id.multiple_choice1) );
        multipleChoice1.setAnswer( Utils.getMultipleChoiceAnswer(getContext(), Integer.parseInt(multipleChoice1.getBtnQuestion().getText().toString())));

        tvQuestion2 = getView().findViewById(R.id.tv_question2);
        multipleChoice2.setMultipleChoice( getView().findViewById(R.id.multiple_choice2) );
        multipleChoice2.setAnswer( Utils.getMultipleChoiceAnswer(getContext(), Integer.parseInt(multipleChoice2.getBtnQuestion().getText().toString())));

        tvQuestion3 = getView().findViewById(R.id.tv_question3);
        multipleChoice3.setMultipleChoice( getView().findViewById(R.id.multiple_choice3) );
        multipleChoice3.setAnswer( Utils.getMultipleChoiceAnswer(getContext(), Integer.parseInt(multipleChoice3.getBtnQuestion().getText().toString())));

        Utils.colorAnswer(multipleChoice1); Utils.colorAnswer(multipleChoice2); Utils.colorAnswer(multipleChoice3);
        Utils.setOnClickListener(multipleChoice1); Utils.setOnClickListener(multipleChoice2); Utils.setOnClickListener(multipleChoice3);

        tvQuestion1.setText( multipleChoice1.getBtnQuestion().getText().toString() + " " + listening.getListQuestions().get(0).getQuestion());
        tvQuestion2.setText( multipleChoice2.getBtnQuestion().getText().toString() + " " +  listening.getListQuestions().get(1).getQuestion());
        tvQuestion3.setText( multipleChoice3.getBtnQuestion().getText().toString() + " " +  listening.getListQuestions().get(2).getQuestion());

        Utils.setTextForMultipleChoice( multipleChoice1.getMultipleChoice(), listening.getListQuestions().get(0).getListChoice() );
        Utils.setTextForMultipleChoice( multipleChoice2.getMultipleChoice(), listening.getListQuestions().get(1).getListChoice() );
        Utils.setTextForMultipleChoice( multipleChoice3.getMultipleChoice(), listening.getListQuestions().get(2).getListChoice() );



        //Playing Audio
        player = Utils.playListeningAudio(getView().getContext(), fileName);
        System.out.println(fileName); System.out.println(player);
        timer.setMax(player.getDuration());
        timer.setProgress(0);
        runnable = new Runnable() {
            @Override
            public void run() {
                timer.setProgress(player.getCurrentPosition());
                handler.postDelayed(this, 500);
            }
        };

        timer.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                player.seekTo(seekBar.getProgress());

                //When the player finishes, we have to play it again
                if(!player.isPlaying())
                    player.start();
            }
        });

        btnPlayAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!player.isPlaying()){
                    btnPlayAudio.setBackgroundResource(R.drawable.ic_pause_audio);
                    player.start();
                    handler.postDelayed(runnable, 0);
                }else{
                    btnPlayAudio.setBackgroundResource(R.drawable.ic_play_audio);
                    player.pause();
                    handler.removeCallbacks(runnable);
                }
            }
        });

        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                btnPlayAudio.setBackgroundResource(R.drawable.ic_play_audio);
            }
        });
    }
    


    @Override
    public void onDestroy() {
        super.onDestroy();

        if(player != null){
            player.stop();
        }
    }
}