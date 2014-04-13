package com.prysmradio.bus.events;

/**
 * Created by fxoxe_000 on 13/04/2014.
 */
public class UpdateEpisodeProgressEvent {

    private int progress;

    public UpdateEpisodeProgressEvent(int p){
        progress = p;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }
}
