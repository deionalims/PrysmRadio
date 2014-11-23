package com.prysmradio.services;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.octo.android.robospice.retrofit.RetrofitGsonSpiceService;
import com.prysmradio.R;
import com.prysmradio.api.PrysmApi;

import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

/**
 * Created by fxoxe_000 on 03/05/2014.
 */
public class PrysmRetrofitSpiceService extends RetrofitGsonSpiceService {

    @Override
    public void onCreate() {
        super.onCreate();
        addRetrofitInterface(PrysmApi.class);
    }

    @Override
    protected String getServerUrl() {
        return getString(R.string.prysm_url);
    }

    @Override
    protected RestAdapter.Builder createRestAdapterBuilder() {
        RestAdapter.Builder restAdapter = super.createRestAdapterBuilder();

        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();

        restAdapter
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setConverter(new GsonConverter(gson));
        return restAdapter;
    }
}
