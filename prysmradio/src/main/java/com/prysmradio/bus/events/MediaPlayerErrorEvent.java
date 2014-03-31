package com.prysmradio.bus.events;

/**
 * Created by fx.oxeda on 01/04/2014.
 */
public class MediaPlayerErrorEvent {

    private String message;

    public MediaPlayerErrorEvent(String msg){
        message = msg;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
