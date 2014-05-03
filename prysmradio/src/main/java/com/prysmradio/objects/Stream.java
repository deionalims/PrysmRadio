package com.prysmradio.objects;

import java.io.Serializable;

/**
 * Created by fxoxe_000 on 03/05/2014.
 */
public class Stream implements Serializable {

    private int id;
    private int stationId;
    private String streamType;
    private int streamBr;
    private String streamUrl;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStationId() {
        return stationId;
    }

    public void setStationId(int stationId) {
        this.stationId = stationId;
    }

    public String getStreamType() {
        return streamType;
    }

    public void setStreamType(String streamType) {
        this.streamType = streamType;
    }

    public int getStreamBr() {
        return streamBr;
    }

    public void setStreamBr(int streamBr) {
        this.streamBr = streamBr;
    }

    public String getStreamUrl() {
        return streamUrl;
    }

    public void setStreamUrl(String streamUrl) {
        this.streamUrl = streamUrl;
    }
}
