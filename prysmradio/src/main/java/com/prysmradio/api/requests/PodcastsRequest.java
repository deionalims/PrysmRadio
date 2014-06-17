package com.prysmradio.api.requests;

import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;
import com.prysmradio.api.PrysmApi;
import com.prysmradio.objects.Podcast;

/**
 * Created by fxoxe_000 on 23/03/2014.
 */
public class PodcastsRequest extends RetrofitSpiceRequest<Podcast.List, PrysmApi> {

    public static final String PODCAST_REQUEST = "podcastRequest";
    private String language;

    public PodcastsRequest(String lang) {
        super(Podcast.List.class, PrysmApi.class);
        language = lang;
    }

    @Override
    public Podcast.List loadDataFromNetwork() throws Exception {
        return getService().getPodcasts(language);
    }
}
