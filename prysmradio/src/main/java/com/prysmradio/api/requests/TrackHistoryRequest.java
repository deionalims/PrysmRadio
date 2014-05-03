package com.prysmradio.api.requests;

import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;
import com.prysmradio.api.PrysmApi;
import com.prysmradio.objects.CurrentTrackInfo;

/**
 * Created by fxoxe_000 on 13/04/2014.
 */
public class TrackHistoryRequest extends RetrofitSpiceRequest<CurrentTrackInfo.List, PrysmApi> {

    public static final String TRACK_HISTORY_REQUEST = "trackHistoryRequest";

    private static final int LAST_PLAYED_LIMIT = 3;

    private int radioId;

    public TrackHistoryRequest(int id) {
        super(CurrentTrackInfo.List.class, PrysmApi.class);
        radioId = id;
    }

    @Override
    public CurrentTrackInfo.List loadDataFromNetwork() throws Exception {
        return getService().getTrackHistory(radioId, LAST_PLAYED_LIMIT);
    }
}
