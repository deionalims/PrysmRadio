package com.prysmradio.api;

import com.prysmradio.objects.CurrentTrackInfo;
import com.prysmradio.objects.News;
import com.prysmradio.objects.Podcast;

import java.util.List;

import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by fxoxe_000 on 23/03/2014.
 */
public interface PrysmApi {

    //static final String API = "/bomgmt/api";
    static final String PODCAST = "/podcast";
    static final String CURRENT = "/current";
    static final String NEWS = "/news";
    static final String LAST = "/last";

    @GET(PODCAST)
    List<Podcast> getPodcasts();

    @GET(PODCAST + "/{id}")
    Podcast getPodcast(@Path("id") int id);

    @GET(CURRENT)
    CurrentTrackInfo getCurrentTrackInfo();

    @GET(LAST)
    List<CurrentTrackInfo> getTrackHistory(@Query("limit") int limit);

    @GET(NEWS)
    List<News> getNews(@Query("limit") String limit);
}
