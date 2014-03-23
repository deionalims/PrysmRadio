package com.prysmradio.api;

import android.content.Context;

import com.prysmradio.utils.BackgroundExecutor;

import retrofit.RetrofitError;
import retrofit.android.MainThreadExecutor;

/**
 * Created by fxoxe_000 on 23/03/2014.
 */
public class ApiManager {

    private static ApiManager apiManager;

    private PrysmApi prysmApi;
    private MainThreadExecutor mainThreadExecutor;

    private ApiManager(){
        this.prysmApi = PrysmApiProvider.get();
        mainThreadExecutor = new MainThreadExecutor();
    }

    public static ApiManager getInstance(){
        if (null == apiManager){
            apiManager = new ApiManager();
        }

        return apiManager;
    }

    public <T> void invoke(final Context context, final PrysmRequest<T> request){
        BackgroundExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    final T result = request.execute(prysmApi);
                    if (request.getCallback() != null){
                        mainThreadExecutor.execute(new Runnable() {
                            @Override
                            public void run() {
                                request.getCallback().success(request, result);
                            }
                        });
                    }
                } catch (final RetrofitError retrofitError){
                    if (request.getCallback() != null){
                        mainThreadExecutor.execute(new Runnable() {
                            @Override
                            public void run() {
                                request.getCallback().failure(request, retrofitError);
                            }
                        });
                    }
                }
            }
        });
    }
}
