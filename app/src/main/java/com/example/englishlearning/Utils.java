package com.example.englishlearning;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.view.View;

import java.io.IOException;

public class Utils {
    public static MediaPlayer playListeningAudio(Context context, String fileName){
        MediaPlayer mediaPlayer = new MediaPlayer();
        try{
            //MediaPlayer mediaPlayer = new MediaPlayer();
            AssetFileDescriptor afd = context.getAssets().openFd("ListeningFiles/" + fileName);
            mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            afd.close();
            mediaPlayer.prepare();
        }catch (IOException e) {
            System.out.println("MEDIA Error");
            e.printStackTrace();
        }
        return mediaPlayer;
    }
}
