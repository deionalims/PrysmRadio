package com.prysmradio;

import android.app.Application;

import com.prysmradio.utils.NotificationHandler;
import com.squareup.otto.Bus;

/**
 * Created by fxoxe_000 on 14/03/14.
 */
public class PrysmApplication extends Application {

    private NotificationHandler notificationHandler;
    private Bus bus;

    @Override
    public void onCreate() {
        super.onCreate();
        bus = new Bus();
        notificationHandler = new NotificationHandler(this);
    }

    public NotificationHandler getNotificationHandler() {
        return notificationHandler;
    }

    public Bus getBus() {
        return bus;
    }
}
