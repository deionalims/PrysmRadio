package com.prysmradio.api.requests;

import com.prysmradio.api.PrysmApi;
import com.prysmradio.api.PrysmCallback;
import com.prysmradio.api.PrysmRequest;
import com.prysmradio.bus.events.BusManager;
import com.prysmradio.bus.events.PodcastEvent;
import com.prysmradio.bus.events.RetroFitErrorEvent;
import com.prysmradio.objects.Podcast;

import retrofit.RetrofitError;

/**
 * Created by fx.oxeda on 24/03/2014.
 */
public class EpisodeRequest extends PrysmRequest<Podcast> {

    private Podcast podcast;

    public EpisodeRequest(Podcast pod) {
        super(new PrysmCallback<Podcast>() {
            @Override
            public void success(PrysmRequest<Podcast> request, Podcast result) {
                BusManager.getInstance().getBus().post(new PodcastEvent(result));
            }

            @Override
            public void failure(PrysmRequest<Podcast> request, RetrofitError error) {
                BusManager.getInstance().getBus().post(new RetroFitErrorEvent(error));
            }
        });
        podcast = pod;
    }

    @Override
    public Podcast execute(PrysmApi prysmApi) {
        return prysmApi.getPodcast(podcast.getId());
    }
}
