package com.prysmradio.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.prysmradio.R;
import com.prysmradio.activities.MainActivity;

/**
 * Created by fxoxe_000 on 14/03/14.
 */
public class NotificationHandler {

    private Notification mNotifyObj;
    private RemoteViews mNotifyView;
    private Context context;

    public NotificationHandler(Context ctx){
        context = ctx;

        mNotifyView = new RemoteViews(context.getPackageName(), R.layout.notification);

        // The PendingIntent to launch our activity if the user selects this notification
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, new Intent(context, MainActivity.class), 0);

        mNotifyObj = new Notification.Builder(context)
                .setContent(mNotifyView)
                .setSmallIcon(R.drawable.prysm_logo)
                .setOngoing(true)
                .setContentIntent(contentIntent)
                .getNotification();
    }

    public void destroy()
    {
        if(context == null )
            return;

        NotificationManager notifyMngr = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        notifyMngr.cancelAll();
    }

    public Notification getNotification() {
        return mNotifyObj;
    }
}
