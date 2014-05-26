package com.prysmradio.api.requests;

import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;
import com.prysmradio.api.PrysmApi;
import com.prysmradio.objects.News;

/**
 * Created by fxoxe_000 on 29/04/2014.
 */
public class NewsRequest extends RetrofitSpiceRequest<News.List, PrysmApi> {

    public static String NEWS_REQUEST = "newsRequest";

    private static final int LIMIT = 10;
    private int start;
    private String lang;

    public NewsRequest(int start, String lang) {
        super(News.List.class, PrysmApi.class);
        this.start = start;
        this.lang = lang;
    }

    @Override
    public News.List loadDataFromNetwork() throws Exception {
        String param = String.format("%d,%d", start, LIMIT);
        return getService().getNews(lang, param);
    }
}
