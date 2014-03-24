package com.prysmradio.bus.events;

import com.prysmradio.objects.Podcast;

/**
 * Created by fx.oxeda on 24/03/2014.
 */
public class PodcastEvent {

    private Podcast podcast;

    public PodcastEvent(Podcast pod){
        podcast = pod;
    }

    public Podcast getPodcast() {
        return podcast;
    }
}
