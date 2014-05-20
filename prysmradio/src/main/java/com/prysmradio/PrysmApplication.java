package com.prysmradio;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.prysmradio.objects.PodcastEpisode;
import com.prysmradio.objects.Radio;
import com.prysmradio.utils.Constants;
import com.prysmradio.utils.CurrentStreamInfo;
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

        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

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

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        Gson gson = new Gson();
        String json = preferences.getString(Constants.RADIO_PREF, null);
        if (json != null){
            Radio radio = gson.fromJson(json, Radio.class);
            CurrentStreamInfo.getInstance().setCurrentRadio(radio);
        } else {
            String podcast = preferences.getString(Constants.PODCAST_EPISODE_PREF, null);
            if (podcast != null){
                PodcastEpisode episode = gson.fromJson(podcast, PodcastEpisode.class);
                CurrentStreamInfo.getInstance().setPodcastEpisode(episode);
            }
        }

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
