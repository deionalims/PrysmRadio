package com.prysmradio;

import android.app.Application;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.prysmradio.api.PrysmApiProvider;
import com.prysmradio.utils.NotificationHandler;
import com.squareup.otto.Bus;

/**
 * Created by fxoxe_000 on 14/03/14.
 */
public class PrysmApplication extends Application {

    private NotificationHandler notificationHandler;
    private boolean serviceIsRunning;
    private Bus bus;

    @Override
    public void onCreate() {
        super.onCreate();
        PrysmApiProvider.setDomainIfNull(this);
        notificationHandler = new NotificationHandler(this);
        bus = new Bus();

        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(this));
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

    public Bus getBus() {
        return bus;
    }
}
