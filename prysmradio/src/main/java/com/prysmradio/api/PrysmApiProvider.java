package com.prysmradio.api;

import android.content.Context;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.prysmradio.R;

import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

/**
 * Created by fxoxe_000 on 23/03/2014.
 */
public class PrysmApiProvider {

    static String DOMAIN;

    public static PrysmApi get(){
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.BASIC)
                .setEndpoint(PrysmApiProvider.DOMAIN)
                .setConverter(new GsonConverter(gson))
                .build();

        return restAdapter.create(PrysmApi.class);
    }

    public static void setDomainIfNull(Context context){
        if (null == PrysmApiProvider.DOMAIN){
            PrysmApiProvider.DOMAIN = context.getString(R.string.prysm_url);
        }
    }

}
