package com.example.fitnesshelper;

import static com.example.fitnesshelper.NotificationHelper.channelID;
import static com.example.fitnesshelper.NotificationHelper.channelName;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

public class AlertReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {


        //Toast.makeText(context, "Don't panic but your time is up!!!!.",Toast.LENGTH_LONG).show();
        String title;
        String message;

        Bundle extras = intent.getExtras();
        if (extras != null){
            title = extras.getString("title");
            message = extras.getString("message");
        }else{
            title = "nem jó cím";
            message = "nem jó üzenet";
        }
        NotificationHelper notificationHelper = new NotificationHelper(context);
        NotificationCompat.Builder nb = notificationHelper.getChannelNotification(title, message);
        notificationHelper.getManager().notify((int)System.currentTimeMillis(), nb.build());

    }
}
