package com.prysmradio.api;

import java.io.Serializable;

import retrofit.RetrofitError;

/**
 * Created by fxoxe_000 on 23/03/2014.
 */
public abstract class PrysmCallback<T> implements Serializable {

    public abstract void success(PrysmRequest<T> request, T result);
    public abstract void failure(PrysmRequest<T> request, RetrofitError error);

}
