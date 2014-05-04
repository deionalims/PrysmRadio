package com.prysmradio.api.requests;

import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;
import com.prysmradio.api.PrysmApi;
import com.prysmradio.objects.Podcast;

/**
 * Created by fx.oxeda on 24/03/2014.
 */
public class EpisodeRequest extends RetrofitSpiceRequest<Podcast, PrysmApi> {

    public static String EPISODE_REQUEST = "edpisodeRequest";

    private int podcastId;

    public EpisodeRequest(int pod) {
        super(Podcast.class, PrysmApi.class);
        podcastId = pod;
    }

    @Override
    public Podcast loadDataFromNetwork() throws Exception {
        return getService().getPodcast(podcastId);
    }
}
