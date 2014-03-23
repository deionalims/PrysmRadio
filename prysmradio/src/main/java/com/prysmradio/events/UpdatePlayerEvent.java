package com.prysmradio.events;

/**
 * Created by fxoxe_000 on 15/03/14.
 */
public class UpdatePlayerEvent {

    private boolean buffering;
    private boolean playing;

    public UpdatePlayerEvent(boolean p, boolean b){
        playing = p;
        buffering = b;
    }

    public boolean isPlaying() {
        return playing;
    }

    public boolean isBuffering() {
        return buffering;
    }
}
