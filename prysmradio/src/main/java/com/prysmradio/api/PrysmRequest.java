package com.prysmradio.api;

import java.io.Serializable;

/**
 * Created by fxoxe_000 on 23/03/2014.
 */
public abstract class PrysmRequest<T> implements Serializable {

    private PrysmCallback<T> callback;

    public PrysmRequest(){}

    public PrysmRequest(PrysmCallback callback){
        this.callback = callback;
    }

    public PrysmCallback<T> getCallback() {
        return callback;
    }

    public void setCallback(PrysmCallback<T> callback) {
        this.callback = callback;
    }

    public abstract T execute(PrysmApi prysmApi);
}
