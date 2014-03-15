package com.prysmradio.events;

/**
 * Created by fxoxe_000 on 15/03/14.
 */
public class CheckPlayingEvent {

    private boolean shouldCheckPlaying;

    public CheckPlayingEvent(boolean b){
        shouldCheckPlaying = b;
    }

    public boolean isShouldCheckPlaying() {
        return shouldCheckPlaying;
    }
}
