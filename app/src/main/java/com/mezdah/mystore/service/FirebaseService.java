package com.mezdah.mystore.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.mezdah.mystore.MainActivity;
import com.mezdah.mystore.R;

import java.util.Random;

public class FirebaseService extends FirebaseMessagingService {
    public final static String CHANNEL_ID = "mychannel";
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);
        Intent i = new Intent(this, MainActivity.class);
        NotificationManager notificationManager = (NotificationManager)
                                            getSystemService(Context.NOTIFICATION_SERVICE);
        int notificationId = new Random().nextInt(); // generate radnom number as id for my notification
        createNotificationChannel(notificationManager);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,i,PendingIntent.FLAG_ONE_SHOT);
        Notification notification = new NotificationCompat.Builder(this,CHANNEL_ID)
                        .setContentTitle(message.getData().get("title"))
                        .setContentText(message.getData().get("message"))
                        .setSmallIcon(R.drawable.ic_cart)
                        .setAutoCancel(true)
                        .setContentIntent(pendingIntent)
                        .build();
        notificationManager.notify(notificationId,notification);
     }
     @RequiresApi(api = Build.VERSION_CODES.O)
     private void createNotificationChannel(NotificationManager manager){
        String channelName = "channelName";
         NotificationChannel channel = new NotificationChannel(CHANNEL_ID,channelName,NotificationManager.IMPORTANCE_HIGH);
         channel.setDescription("My channel description");
         channel.enableLights(true);
         channel.setLightColor(Color.GREEN);
         manager.createNotificationChannel(channel);
     }
}
