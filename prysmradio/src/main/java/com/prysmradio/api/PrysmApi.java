package com.prysmradio.api;

import com.prysmradio.objects.CurrentTrackInfo;
import com.prysmradio.objects.News;
import com.prysmradio.objects.Podcast;
import com.prysmradio.objects.Radio;

import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by fxoxe_000 on 23/03/2014.
 */
public interface PrysmApi {

    //static final String API = "/bomgmt/api";
    static final String RADIOS = "/radios";
    static final String RADIO = "/radio";
    static final String PODCAST = "/podcast";
    static final String CURRENT = "/current";
    static final String NEWS = "/news";
    static final String LAST = "/last";

    @GET(RADIOS)
    Radio.List getRadios();

    @GET(RADIO + "/{id}")
    Radio getRadioDetails(@Path("id") int id);

    @GET(CURRENT + "/{id}")
    CurrentTrackInfo getCurrentTrackInfo(@Path("id") int id);

    @GET(LAST + "/{id}")
    CurrentTrackInfo.List getTrackHistory(@Path("id") int id, @Query("limit") int limit);

    @GET(PODCAST)
    Podcast.List getPodcasts();

    @GET(PODCAST + "/{id}")
    Podcast getPodcast(@Path("id") int id);

    @GET(NEWS)
    News.List getNews(@Query("limit") String limit);
}
