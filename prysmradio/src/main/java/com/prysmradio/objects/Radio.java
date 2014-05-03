package com.prysmradio.objects;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by fxoxe_000 on 03/05/2014.
 */
public class Radio implements Serializable {

    private int id;
    private String name;
    private String description;
    private TrackCovers cover;
    private ArrayList<Stream> stream;

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

    @SuppressWarnings("serial")
    public static class List extends ArrayList<Radio> {}
}
