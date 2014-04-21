package com.prysmradio.bus.events;

import com.prysmradio.objects.CurrentTrackInfo;

/**
 * Created by fxoxe_000 on 12/04/2014.
 */
public class UpdateCoverEvent {

    private CurrentTrackInfo currentTrackInfo;

    public UpdateCoverEvent(CurrentTrackInfo info){
        currentTrackInfo = info;
    }

    public CurrentTrackInfo getCurrentTrackInfo() {
        return currentTrackInfo;
    }

    public void setCurrentTrackInfo(CurrentTrackInfo currentTrackInfo) {
        this.currentTrackInfo = currentTrackInfo;
    }
}
