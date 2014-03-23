package com.prysmradio.api;

import com.prysmradio.objects.Podcast;

import java.util.List;

import retrofit.http.GET;

/**
 * Created by fxoxe_000 on 23/03/2014.
 */
public interface PrysmApi {

    static final String API = "/bomgmt/api";
    static final String PODCAST = "/podcast";

    @GET(API + PODCAST)
    List<Podcast> getPodcasts();
}
