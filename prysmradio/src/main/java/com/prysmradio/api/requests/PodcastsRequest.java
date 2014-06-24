package com.prysmradio.api.requests;

import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;
import com.prysmradio.api.PrysmApi;
import com.prysmradio.objects.Podcast;

/**
 * Created by fxoxe_000 on 23/03/2014.
 */
public class PodcastsRequest extends RetrofitSpiceRequest<Podcast.List, PrysmApi> {

    public static final String PODCAST_REQUEST = "podcastRequest";

    public PodcastsRequest() {
        super(Podcast.List.class, PrysmApi.class);
    }

    @Override
    public Podcast.List loadDataFromNetwork() throws Exception {
        return getService().getPodcasts();
    }
}
