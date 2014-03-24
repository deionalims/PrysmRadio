package com.prysmradio.bus.events;

import com.prysmradio.objects.Podcast;

import java.util.List;

/**
 * Created by fxoxe_000 on 23/03/2014.
 */
public class PodcastsListEvent {

    private List<Podcast> podcasts;

    public PodcastsListEvent(List<Podcast> list){
        podcasts = list;
    }

    public List<Podcast> getPodcasts() {
        return podcasts;
    }
}
