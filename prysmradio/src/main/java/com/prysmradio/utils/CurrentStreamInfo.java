package com.prysmradio.utils;

import com.prysmradio.objects.PodcastEpisode;
import com.prysmradio.objects.Radio;

/**
 * Created by fxoxe_000 on 04/05/2014.
 */
public class CurrentStreamInfo {

    private static CurrentStreamInfo instance;
    private Radio currentRadio;
    private PodcastEpisode podcastEpisode;

    private CurrentStreamInfo(){}

    public static CurrentStreamInfo getInstance() {
        if (instance == null) {
            instance = new CurrentStreamInfo();
        }
        return instance;
    }

    public Radio getCurrentRadio() {
        return currentRadio;
    }

    public void setCurrentRadio(Radio currentRadio) {
        this.currentRadio = currentRadio;
        this.podcastEpisode = null;
    }

    public PodcastEpisode getPodcastEpisode() {
        return podcastEpisode;
    }

    public void setPodcastEpisode(PodcastEpisode podcastEpisode) {
        this.podcastEpisode = podcastEpisode;
        this.currentRadio = null;
    }
}
