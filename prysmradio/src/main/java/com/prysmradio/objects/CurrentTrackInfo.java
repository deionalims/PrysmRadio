package com.prysmradio.objects;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by fxoxe_000 on 12/04/2014.
 */
public class CurrentTrackInfo implements Serializable {

    private String artist;
    private String title;
    private long timestamp;
    private long duration;
    private TrackCovers cover;

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public TrackCovers getCover() {
        return cover;
    }

    public void setCover(TrackCovers cover) {
        this.cover = cover;
    }

    @SuppressWarnings("serial")
    public static class List extends ArrayList<CurrentTrackInfo> {}
}
