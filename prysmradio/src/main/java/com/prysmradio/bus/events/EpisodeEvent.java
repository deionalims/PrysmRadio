package com.prysmradio.bus.events;

import com.prysmradio.objects.PodcastEpisode;

/**
 * Created by fxoxe_000 on 13/04/2014.
 */
public class EpisodeEvent {

    private PodcastEpisode episode;

    public EpisodeEvent(PodcastEpisode e){
        episode = e;
    }

    public PodcastEpisode getEpisode() {
        return episode;
    }

    public void setEpisode(PodcastEpisode episode) {
        this.episode = episode;
    }
}
