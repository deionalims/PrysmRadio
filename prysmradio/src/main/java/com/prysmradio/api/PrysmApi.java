package com.prysmradio.api;

import com.prysmradio.objects.CurrentTrackInfo;
import com.prysmradio.objects.Podcast;

import java.util.List;

import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by fxoxe_000 on 23/03/2014.
 */
public interface PrysmApi {

    static final String API = "/bomgmt/api";
    static final String PODCAST = "/podcast";
    static final String CURRENT = "/current";

    @GET(API + PODCAST)
    List<Podcast> getPodcasts();

    @GET(API + PODCAST + "/{id}")
    Podcast getPodcast(@Path("id") int id);

    @GET(API + CURRENT)
    CurrentTrackInfo getCurrentTrackInfo();
}
