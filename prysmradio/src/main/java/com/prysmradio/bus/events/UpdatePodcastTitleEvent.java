package com.prysmradio.bus.events;

/**
 * Created by fx.oxeda on 31/03/2014.
 */
public class UpdatePodcastTitleEvent {

    private String podcastTitle;
    private String episodeTitle;

    public UpdatePodcastTitleEvent(String podcastTitle, String episodeTitle){
        this.podcastTitle = podcastTitle;
        this.episodeTitle = episodeTitle;
    }

    public String getPodcastTitle() {
        return podcastTitle;
    }

    public void setPodcastTitle(String podcastTitle) {
        this.podcastTitle = podcastTitle;
    }

    public String getEpisodeTitle() {
        return episodeTitle;
    }

    public void setEpisodeTitle(String episodeTitle) {
        this.episodeTitle = episodeTitle;
    }
}
