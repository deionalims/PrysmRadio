package com.prysmradio;

import android.app.Application;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.prysmradio.api.PrysmApiProvider;
import com.prysmradio.utils.NotificationHandler;

/**
 * Created by fxoxe_000 on 14/03/14.
 */
public class PrysmApplication extends Application {

    private NotificationHandler notificationHandler;
    private boolean serviceIsRunning;

    @Override
    public void onCreate() {
        super.onCreate();
        PrysmApiProvider.setDomainIfNull(this);
        notificationHandler = new NotificationHandler(this);

        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .showImageOnLoading(R.drawable.prysm_logo_square)
                .displayer(new FadeInBitmapDisplayer(300))
                .build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .defaultDisplayImageOptions(defaultOptions)
                .build();

        ImageLoader.getInstance().init(config);
    }

    public NotificationHandler getNotificationHandler() {
        return notificationHandler;
    }

    public void setNotificationHandler(NotificationHandler notificationHandler) {
        this.notificationHandler = notificationHandler;
    }

    public boolean isServiceIsRunning() {
        return serviceIsRunning;
    }

    public void setServiceIsRunning(boolean serviceIsRunning) {
        this.serviceIsRunning = serviceIsRunning;
    }
}
