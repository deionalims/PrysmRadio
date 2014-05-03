package com.prysmradio.api.requests;

import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;
import com.prysmradio.api.PrysmApi;
import com.prysmradio.objects.Radio;

/**
 * Created by fxoxe_000 on 03/05/2014.
 */
public class RadiosListRequest extends RetrofitSpiceRequest<Radio.List, PrysmApi> {

    public static final String RADIOS_LIST_REQUEST = "radioListRequest";

    public RadiosListRequest() {
        super(Radio.List.class, PrysmApi.class);
    }

    @Override
    public Radio.List loadDataFromNetwork() throws Exception {
        return getService().getRadios();
    }
}
