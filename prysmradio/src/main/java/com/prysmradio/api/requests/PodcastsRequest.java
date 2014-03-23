package com.prysmradio.api.requests;

import com.prysmradio.api.PrysmApi;
import com.prysmradio.api.PrysmCallback;
import com.prysmradio.api.PrysmRequest;
import com.prysmradio.objects.Podcast;

import java.util.List;

/**
 * Created by fxoxe_000 on 23/03/2014.
 */
public class PodcastsRequest extends PrysmRequest<List<Podcast>> {

    public PodcastsRequest(PrysmCallback<List<Podcast>> callback) {
        super(callback);
    }

    @Override
    public List<Podcast> execute(PrysmApi prysmApi) {
        List<Podcast> list = prysmApi.getPodcasts();

        return list;
    }
}
