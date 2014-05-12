package com.prysmradio.bus.events;

/**
 * Created by fxoxe_000 on 12/05/2014.
 */
public class SendCurrentTrackInfoRequestEvent {

    private boolean shouldSendRequest;

    public SendCurrentTrackInfoRequestEvent(boolean b){
        shouldSendRequest = b;
    }

    public boolean shouldSendRequest() {
        return shouldSendRequest;
    }

    public void setShouldSendRequest(boolean shouldSendRequest) {
        this.shouldSendRequest = shouldSendRequest;
    }
}
