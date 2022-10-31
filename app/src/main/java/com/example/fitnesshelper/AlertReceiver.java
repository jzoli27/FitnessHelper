package com.example.fitnesshelper;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;

public class AlertReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
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
        notificationHelper.getManager().notify(1, nb.build());
    }
}
