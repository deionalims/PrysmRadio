package com.prysmradio.services;

import com.octo.android.robospice.retrofit.RetrofitGsonSpiceService;
import com.prysmradio.R;
import com.prysmradio.api.PrysmApi;

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
}
