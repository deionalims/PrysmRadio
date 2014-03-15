package com.prysmradio.events;

/**
 * Created by fxoxe_000 on 15/03/14.
 */
public class CheckPlayingReturnEvent {

    private boolean shouldReadReturn;

    public CheckPlayingReturnEvent(boolean b){
        shouldReadReturn = b;
    }

    public boolean isShouldReadReturn() {
        return shouldReadReturn;
    }
}
