package com.prysmradio.bus.events;

/**
 * Created by fxoxe_000 on 15/03/14.
 */
public class UpdateMetaDataEvent {

    private String streamTitle;

    public UpdateMetaDataEvent(String title){
        streamTitle = title;
    }


    public String getStreamTitle() {
        return streamTitle;
    }

    public void setStreamTitle(String streamTitle) {
        this.streamTitle = streamTitle;
    }
}
