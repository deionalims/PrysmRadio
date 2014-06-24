package com.prysmradio.objects;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by fxoxe_000 on 03/05/2014.
 */
public class Radio implements Serializable {

    private static final String AAC = "AAC";
    private static final String MP3 = "MP3";

    private int id;
    private String name;
    private String description;
    private TrackCovers cover;
    private ArrayList<Stream> stream;
    private String title;
    private String artist;
    private String songCover;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public ArrayList<Stream> getStream() {
        return stream;
    }

    public void setStream(ArrayList<Stream> stream) {
        this.stream = stream;
    }

    public String getAACStreamURL(){
        for(Stream s : stream){
            if (AAC.equals(s.getStreamType())){
                return s.getStreamUrl();
            }
        }

        return null;
    }

    public String getMP3Stream(){
        for(Stream s : stream){
            if (MP3.equals(s.getStreamType())){
                return s.getStreamUrl();
            }
        }

        return null;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getSongCover() {
        return songCover;
    }

    public void setSongCover(String songCover) {
        this.songCover = songCover;
    }

    @SuppressWarnings("serial")
    public static class List extends ArrayList<Radio> {}
}
