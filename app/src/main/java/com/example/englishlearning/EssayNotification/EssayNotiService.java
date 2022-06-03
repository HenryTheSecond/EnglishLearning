package com.example.englishlearning.EssayNotification;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.englishlearning.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class EssayNotiService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);

//        RemoteMessage.Notification notification = message.getNotification();
//        if(notification == null){
//            return;
//        }
//        String title = notification.getTitle();
//        String content = notification.getBody();

        Map<String, String> stringMap = message.getData();
        String subject = stringMap.get("subject");
        String point = stringMap.get("point");
        String comment = stringMap.get("comment");

        sendNotification(subject, point, comment);
    }

    private void sendNotification(String subject, String point, String comment) {
        String miniSubject = subject.substring(0, 20);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, getResources().getString(R.string.id_channel_essay))
                .setContentTitle("Essay Writing")
                .setContentText(miniSubject + "...\nYour point is: " + point)
                .setSmallIcon(R.mipmap.ic_launcher);

        Notification notification = notificationBuilder.build();
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(200, notification);
    }

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);

        Log.e("AAAAA", token);
    }
}
