package com.prysmradio.objects;

import java.io.Serializable;

/**
 * Created by fxoxe_000 on 23/03/2014.
 */
public class PodcastInfo implements Serializable {

    private String subtitle;
    private String summary;
    private String description;
    private TrackCovers cover;
    private String author;
    private String copyright;

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TrackCovers getCover() {
        return cover;
    }

    public void setCover(TrackCovers cover) {
        this.cover = cover;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCopyright() {
        return copyright;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }
}
