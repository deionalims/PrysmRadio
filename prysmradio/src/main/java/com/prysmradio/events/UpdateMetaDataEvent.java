package com.prysmradio.events;

/**
 * Created by fxoxe_000 on 15/03/14.
 */
public class UpdateMetaDataEvent {

    private String artist;
    private String title;

    public UpdateMetaDataEvent(String a, String t){
        artist = a;
        title = t;
    }

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
}
