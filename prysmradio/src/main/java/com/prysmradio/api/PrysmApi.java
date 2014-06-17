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

    @GET(RADIOS)
    Radio.List getRadios();

    @GET(RADIO + "/{id}")
    Radio getRadioDetails(@Path("id") int id);

    @GET(CURRENT + "/{id}")
    CurrentTrackInfo getCurrentTrackInfo(@Path("id") int id);

    @GET(LAST + "/{id}")
    CurrentTrackInfo.List getTrackHistory(@Path("id") int id, @Query("limit") int limit);

    @GET(PODCAST + "/{language}")
    Podcast.List getPodcasts(@Path("language") String language);

    @GET(PODCAST + "/{language}" + "/{id}")
    Podcast getPodcast(@Path("language") String language, @Path("id") int id);

    @GET(NEWS + "/{language}")
    News.List getNews(@Path("language") String language, @Query("limit") String limit);

    @GET(NEWS + "/{language}" + "/{id}")
    News getNews(@Path("language") String language, @Path("id") int newsId);
}
