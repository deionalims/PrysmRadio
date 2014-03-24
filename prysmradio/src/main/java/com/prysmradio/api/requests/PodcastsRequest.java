package com.prysmradio.api.requests;

import com.prysmradio.api.PrysmApi;
import com.prysmradio.api.PrysmCallback;
import com.prysmradio.api.PrysmRequest;
import com.prysmradio.bus.events.BusManager;
import com.prysmradio.bus.events.PodcastsListEvent;
import com.prysmradio.bus.events.RetroFitErrorEvent;
import com.prysmradio.objects.Podcast;

import java.util.List;

import retrofit.RetrofitError;

/**
 * Created by fxoxe_000 on 23/03/2014.
 */
public class PodcastsRequest extends PrysmRequest<List<Podcast>> {

    public PodcastsRequest() {
        super(new PrysmCallback<List<Podcast>>() {
            @Override
            public void success(PrysmRequest<List<Podcast>> request, List<Podcast> result) {
                BusManager.getInstance().getBus().post(new PodcastsListEvent(result));
            }

            @Override
            public void failure(PrysmRequest<List<Podcast>> request, RetrofitError error) {
                BusManager.getInstance().getBus().post(new RetroFitErrorEvent(error));
            }
        });
    }

    @Override
    public List<Podcast> execute(PrysmApi prysmApi) {
        List<Podcast> list = prysmApi.getPodcasts();

        return list;
    }
}
