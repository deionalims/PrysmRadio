package com.prysmradio.api.requests;

import com.prysmradio.api.PrysmApi;
import com.prysmradio.api.PrysmCallback;
import com.prysmradio.api.PrysmRequest;
import com.prysmradio.bus.events.BusManager;
import com.prysmradio.bus.events.NewsEvent;
import com.prysmradio.objects.News;

import java.util.List;

import retrofit.RetrofitError;

/**
 * Created by fxoxe_000 on 29/04/2014.
 */
public class NewsRequest extends PrysmRequest<List<News>> {

    private static final int LIMIT = 10;
    private int start;

    public NewsRequest(int start) {
        super(new PrysmCallback<List<News>>() {
            @Override
            public void success(PrysmRequest<List<News>> request, List<News> result) {
                BusManager.getInstance().getBus().post(new NewsEvent(result));
            }

            @Override
            public void failure(PrysmRequest request, RetrofitError error) {

            }
        });

        this.start = start;
    }

    @Override
    public List<News> execute(PrysmApi prysmApi) {

        String param = String.format("%d,%d", start, LIMIT);

        return prysmApi.getNews(param);
    }
}
