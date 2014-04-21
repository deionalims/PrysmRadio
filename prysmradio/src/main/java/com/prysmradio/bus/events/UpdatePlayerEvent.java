package com.prysmradio.bus.events;

/**
 * Created by fxoxe_000 on 15/03/14.
 */
public class UpdatePlayerEvent {

    private boolean buffering;
    private boolean playing;
    private int duration;

    public UpdatePlayerEvent(boolean p, boolean b){
        playing = p;
        buffering = b;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public boolean isPlaying() {
        return playing;
    }

    public boolean isBuffering() {
        return buffering;
    }
}
