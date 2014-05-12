package com.prysmradio.bus.events;

import com.prysmradio.objects.CurrentTrackInfo;

/**
 * Created by fxoxe_000 on 12/05/2014.
 */
public class UpdateCurrentTrackInfoEvent {

    private CurrentTrackInfo currentTrackInfo;

    public UpdateCurrentTrackInfoEvent(CurrentTrackInfo trackInfo){
        currentTrackInfo = trackInfo;
    }

    public CurrentTrackInfo getCurrentTrackInfo() {
        return currentTrackInfo;
    }

    public void setCurrentTrackInfo(CurrentTrackInfo currentTrackInfo) {
        this.currentTrackInfo = currentTrackInfo;
    }
}
