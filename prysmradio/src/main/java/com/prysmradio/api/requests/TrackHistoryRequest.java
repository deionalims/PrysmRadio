package com.prysmradio.api.requests;

import com.prysmradio.api.PrysmApi;
import com.prysmradio.api.PrysmCallback;
import com.prysmradio.api.PrysmRequest;
import com.prysmradio.bus.events.BusManager;
import com.prysmradio.bus.events.RetroFitErrorEvent;
import com.prysmradio.bus.events.TrackHistoryEvent;
import com.prysmradio.objects.CurrentTrackInfo;

import java.util.List;

import retrofit.RetrofitError;

/**
 * Created by fxoxe_000 on 13/04/2014.
 */
public class TrackHistoryRequest extends PrysmRequest<List<CurrentTrackInfo>> {

    private static final int LAST_PLAYED_LIMIT = 3;

    public TrackHistoryRequest() {
        super(new PrysmCallback<List<CurrentTrackInfo>>() {
            @Override
            public void success(PrysmRequest<List<CurrentTrackInfo>> request, List<CurrentTrackInfo> result) {
                TrackHistoryEvent event = new TrackHistoryEvent(result);
                BusManager.getInstance().getBus().post(event);
            }

            @Override
            public void failure(PrysmRequest<List<CurrentTrackInfo>> request, RetrofitError error) {
                BusManager.getInstance().getBus().post(new RetroFitErrorEvent(error));
            }
        });
    }

    @Override
    public List<CurrentTrackInfo> execute(PrysmApi prysmApi) {
        return prysmApi.getTrackHistory(LAST_PLAYED_LIMIT);
    }

}
