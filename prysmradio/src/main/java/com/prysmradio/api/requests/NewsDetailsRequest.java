package com.prysmradio.api.requests;

import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;
import com.prysmradio.api.PrysmApi;
import com.prysmradio.objects.News;

/**
 * Created by fxoxe_000 on 12/05/2014.
 */
public class NewsDetailsRequest extends RetrofitSpiceRequest<News, PrysmApi> {

    public static String NEWS_DETAILS_REQUEST = "newsDetailsRequest";

    private int newsId;

    public NewsDetailsRequest(int id) {
        super(News.class, PrysmApi.class);
        this.newsId = id;
    }

    @Override
    public News loadDataFromNetwork() throws Exception {
        return getService().getNews(newsId);
    }
}
