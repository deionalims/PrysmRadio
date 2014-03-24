package com.prysmradio.bus.events;

import retrofit.RetrofitError;

/**
 * Created by fx.oxeda on 25/03/2014.
 */
public class RetroFitErrorEvent {

    private RetrofitError error;

    public RetroFitErrorEvent(RetrofitError err){
        error = err;
    }

    public RetrofitError getError() {
        return error;
    }
}
