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

    static final String RADIOS = "/radios";
    static final String RADIO = "/radio";
    static final String PODCAST = "/podcast";
    static final String CURRENT = "/current";
    static final String NEWS = "/news";
    static final String LAST = "/last";
    static final String FR = "/fr";

    @GET(RADIOS)
    Radio.List getRadios();

    @GET(RADIO + "/{id}")
    Radio getRadioDetails(@Path("id") int id);

    @GET(CURRENT + "/{id}")
    CurrentTrackInfo getCurrentTrackInfo(@Path("id") int id);

    @GET(LAST + "/{id}")
    CurrentTrackInfo.List getTrackHistory(@Path("id") int id, @Query("limit") int limit);

    @GET(PODCAST + FR)
    Podcast.List getPodcasts();

    @GET(PODCAST + FR + "/{id}")
    Podcast getPodcast(@Path("id") int id);

    @GET(NEWS + FR)
    News.List getNews(@Query("limit") String limit);

    @GET(NEWS + FR + "/{id}")
    News getNews(@Path("id") int newsId);
}
