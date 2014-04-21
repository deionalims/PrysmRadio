package com.prysmradio.bus.events;

import com.prysmradio.objects.CurrentTrackInfo;

import java.util.List;

/**
 * Created by fxoxe_000 on 13/04/2014.
 */
public class TrackHistoryEvent {

    private List<CurrentTrackInfo> trackHistory;

    public TrackHistoryEvent(List<CurrentTrackInfo> list){
        trackHistory = list;
    }

    public List<CurrentTrackInfo> getTrackHistory() {
        return trackHistory;
    }

    public void setTrackHistory(List<CurrentTrackInfo> trackHistory) {
        this.trackHistory = trackHistory;
    }
}
