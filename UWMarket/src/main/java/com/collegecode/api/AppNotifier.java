package com.collegecode.api;

import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationCompat;

/**
 * Created by saurabh on 5/29/14.
 */
public class AppNotifier {

    NotificationManager nm;
    NotificationCompat.Builder builder;
    int id;
    Context cntx;

    public AppNotifier(Context cntx){
        this.cntx = cntx;
    }

    public void showNotification(String title, String message, int icon){
        nm = (NotificationManager) cntx.getSystemService(Context.NOTIFICATION_SERVICE);
        builder = new NotificationCompat.Builder(cntx);
        builder.setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(icon)
                .setOngoing(true);
        //Create unique
        id = (int) (System.currentTimeMillis());
        builder.setProgress(0, 0, true);
        // Displays the progress bar for the first time.
        nm.notify(id, builder.build());
    }

    public void removeNotification(String message){
        builder.setContentText(message)
                .setOngoing(false)
                .setProgress(0,0,false);
        nm.notify(id, builder.build());
    }
}
