package com.example.englishlearning.ReviewFragment;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.englishlearning.Model.ReviewModel.ListeningReview;
import com.example.englishlearning.MultipleChoice;
import com.example.englishlearning.R;
import com.example.englishlearning.Utils;

public class ListeningReviewFragment extends Fragment {
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

    private ListeningReview listeningReview;

    public ListeningReviewFragment(ListeningReview listeningReview, Button btnQuestion1, Button btnQuestion2, Button btnQuestion3){
        multipleChoice1 = new MultipleChoice();
        multipleChoice1.setBtnQuestion(btnQuestion1);

        multipleChoice2 = new MultipleChoice();
        multipleChoice2.setBtnQuestion(btnQuestion2);

        multipleChoice3 = new MultipleChoice();
        multipleChoice3.setBtnQuestion(btnQuestion3);

        this.listeningReview = listeningReview;
        fileName = listeningReview.getListening().getFileName();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_listening, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        timer = getView().findViewById(R.id.seek_bar_timer);
        btnPlayAudio = getView().findViewById(R.id.btn_play_audio);

        tvQuestion1 = getView().findViewById(R.id.tv_question1);
        multipleChoice1.setMultipleChoice( getView().findViewById(R.id.multiple_choice1) );
        //multipleChoice1.setAnswer( Utils.getMultipleChoiceAnswer(getContext(), Integer.parseInt(multipleChoice1.getBtnQuestion().getText().toString())));

        tvQuestion2 = getView().findViewById(R.id.tv_question2);
        multipleChoice2.setMultipleChoice( getView().findViewById(R.id.multiple_choice2) );
        //multipleChoice2.setAnswer( Utils.getMultipleChoiceAnswer(getContext(), Integer.parseInt(multipleChoice2.getBtnQuestion().getText().toString())));

        tvQuestion3 = getView().findViewById(R.id.tv_question3);
        multipleChoice3.setMultipleChoice( getView().findViewById(R.id.multiple_choice3) );
        //multipleChoice3.setAnswer( Utils.getMultipleChoiceAnswer(getContext(), Integer.parseInt(multipleChoice3.getBtnQuestion().getText().toString())));

        tvQuestion1.setText( multipleChoice1.getBtnQuestion().getText().toString() + " " + listeningReview.getListening().getListQuestions().get(0).getQuestion());
        tvQuestion2.setText( multipleChoice2.getBtnQuestion().getText().toString() + " " +  listeningReview.getListening().getListQuestions().get(1).getQuestion());
        tvQuestion3.setText( multipleChoice3.getBtnQuestion().getText().toString() + " " +  listeningReview.getListening().getListQuestions().get(2).getQuestion());

        Utils.setTextForMultipleChoice( multipleChoice1.getMultipleChoice(), listeningReview.getListening().getListQuestions().get(0).getListChoice() );
        Utils.setTextForMultipleChoice( multipleChoice2.getMultipleChoice(), listeningReview.getListening().getListQuestions().get(1).getListChoice() );
        Utils.setTextForMultipleChoice( multipleChoice3.getMultipleChoice(), listeningReview.getListening().getListQuestions().get(2).getListChoice() );


        Utils.colorAnswerReview(multipleChoice1, listeningReview.getIdAnswers().get(0),
                listeningReview.getListening().getListQuestions().get(0).getListChoice(),
                listeningReview.getListening().getListQuestions().get(0).getIdAnswer());

        Utils.colorAnswerReview(multipleChoice2, listeningReview.getIdAnswers().get(1),
                listeningReview.getListening().getListQuestions().get(1).getListChoice(),
                listeningReview.getListening().getListQuestions().get(1).getIdAnswer());

        Utils.colorAnswerReview(multipleChoice3, listeningReview.getIdAnswers().get(2),
                listeningReview.getListening().getListQuestions().get(2).getListChoice(),
                listeningReview.getListening().getListQuestions().get(2).getIdAnswer());

        //Playing Audio
        player = Utils.playListeningAudio(getView().getContext(), fileName);

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
                if(!player.isPlaying()){
                    player.start();
                    btnPlayAudio.setBackgroundResource(R.drawable.ic_pause_audio);
                    handler.postDelayed(runnable, 0);
                }
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
