package com.example.fitnesshelper;

import android.annotation.TargetApi;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import androidx.core.app.NotificationCompat;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;


public class NotificationHelper extends ContextWrapper {
    public static final String channelID = "channelID";
    public static final String channelName = "Channel Name";

    private NotificationManager mManager;

    public NotificationHelper(Context base) {
        super(base);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel();
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    private void createChannel() {
        NotificationChannel channel = new NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_HIGH);

        getManager().createNotificationChannel(channel);
    }

    public NotificationManager getManager() {
        if (mManager == null) {
            mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }

        return mManager;
    }
    //https://firebasestorage.googleapis.com/v0/b/fitnesshelper-ab986.appspot.com/o/exercises%2Fback%2FScreenshot_11.png?alt=media&token=799c4184-9fd5-4d0e-861f-5374b4bca5bd

    /*
    URL url;

    {
        try {
            url = new URL("https://firebasestorage.googleapis.com/v0/b/fitnesshelper-ab986.appspot.com/o/exercises%2Fback%2FScreenshot_11.png?alt=media&token=799c4184-9fd5-4d0e-861f-5374b4bca5bd");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    Bitmap image;

    {
        try {
            image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

     */

    Bitmap picture = BitmapFactory.decodeResource(getResources(),R.drawable.supplement);

    public NotificationCompat.Builder getChannelNotification(String title, String message) {
        return new NotificationCompat.Builder(getApplicationContext(), channelID)
                .setSmallIcon(R.drawable.ic_add_white)
                .setContentTitle(title)
                .setContentText(message)
                .setLargeIcon(picture);
    }
}
