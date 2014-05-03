package com.prysmradio.api.requests;

import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;
import com.prysmradio.api.PrysmApi;
import com.prysmradio.objects.CurrentTrackInfo;

/**
 * Created by fxoxe_000 on 12/04/2014.
 */
public class CurrentTrackInfoRequest extends RetrofitSpiceRequest<CurrentTrackInfo, PrysmApi> {

    public static final String CURRENT_TRACK_REQUEST = "currentTrackRequest";

    private int radioId;

    public CurrentTrackInfoRequest(int id) {
        super(CurrentTrackInfo.class, PrysmApi.class);
        radioId = id;
    }

    @Override
    public CurrentTrackInfo loadDataFromNetwork() throws Exception {
        return getService().getCurrentTrackInfo(radioId);
    }
}
