package com.prysmradio.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.widget.RemoteViews;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.prysmradio.R;
import com.prysmradio.activities.MainActivity;

/**
 * Created by fxoxe_000 on 14/03/14.
 */
public class NotificationHandler {

    private Notification mNotifyObj;
    private RemoteViews mNotifyView;
    private Context context;
    private DisplayImageOptions displayOptions;
    private ImageSize minImageSize;

    public NotificationHandler(Context ctx){
        context = ctx;

        displayOptions = DisplayImageOptions.createSimple();
        minImageSize = new ImageSize(70, 70);

        mNotifyView = new RemoteViews(context.getPackageName(), R.layout.notification);

        // The PendingIntent to launch our activity if the user selects this notification
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, new Intent(context, MainActivity.class), 0);

        Intent stopIntent = new Intent(context, MainActivity.class);
        stopIntent.putExtra(Constants.STOP_EXTRA, true);
        PendingIntent stopPendingIntent = PendingIntent.getActivity(context, 1, stopIntent, 0);

        mNotifyView.setOnClickPendingIntent(R.id.notification_stop_button, stopPendingIntent);

        mNotifyObj = new NotificationCompat.Builder(context)
                .setContent(mNotifyView)
                .setSmallIcon(R.drawable.prysm_logo)
                .setOngoing(true)
                .setContentIntent(contentIntent)
                .build();
    }

    public void destroy()
    {
        if(context == null )
            return;

        NotificationManager notifyMngr = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        notifyMngr.cancelAll();
    }

    public Notification getNotification() {
        mNotifyView.setTextViewText(R.id.notification_artist_textView, "Loading...");
        return mNotifyObj;
    }

    public Notification getNotification(String imageURL, final String artist, final String title){

        final NotificationManager notifyMngr = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        ImageLoader.getInstance()
                .loadImage(imageURL, minImageSize, displayOptions, new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        mNotifyView.setImageViewBitmap(R.id.notification_imageView, loadedImage);
                        notifyMngr.notify(Constants.NOTIFICATION_ID, mNotifyObj);
                    }
                });
        mNotifyView.setTextViewText(R.id.notification_artist_textView, artist);
        mNotifyView.setTextViewText(R.id.notification_title_textView, title);
        return mNotifyObj;
    }

    public void updateNotification(String imageURL, final String artist, final String title){

        final NotificationManager notifyMngr = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        ImageLoader.getInstance()
                .loadImage(imageURL, minImageSize, displayOptions, new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        mNotifyView.setImageViewBitmap(R.id.notification_imageView, loadedImage);
                        mNotifyView.setTextViewText(R.id.notification_artist_textView, artist);
                        mNotifyView.setTextViewText(R.id.notification_title_textView, title);
                        notifyMngr.notify(Constants.NOTIFICATION_ID, mNotifyObj);
                    }
                });
    }
}
