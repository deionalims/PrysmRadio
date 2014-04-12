package com.prysmradio.api.requests;

import com.prysmradio.api.PrysmApi;
import com.prysmradio.api.PrysmCallback;
import com.prysmradio.api.PrysmRequest;
import com.prysmradio.bus.events.BusManager;
import com.prysmradio.bus.events.RetroFitErrorEvent;
import com.prysmradio.bus.events.UpdateCoverEvent;
import com.prysmradio.objects.CurrentTrackInfo;

import retrofit.RetrofitError;

/**
 * Created by fxoxe_000 on 12/04/2014.
 */
public class CurrentTrackInfoRequest extends PrysmRequest<CurrentTrackInfo> {

    public CurrentTrackInfoRequest() {
        super(new PrysmCallback<CurrentTrackInfo>() {
            @Override
            public void success(PrysmRequest<CurrentTrackInfo> request, CurrentTrackInfo result) {
                UpdateCoverEvent event = new UpdateCoverEvent(result);
                BusManager.getInstance().getBus().post(event);
            }

            @Override
            public void failure(PrysmRequest<CurrentTrackInfo> request, RetrofitError error) {
                BusManager.getInstance().getBus().post(new RetroFitErrorEvent(error));
            }
        });
    }

    @Override
    public CurrentTrackInfo execute(PrysmApi prysmApi) {
        return prysmApi.getCurrentTrackInfo();
    }
}
