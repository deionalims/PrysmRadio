package com.prysmradio.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.prysmradio.adapters.RadiosAdapter;
import com.prysmradio.api.requests.RadiosListRequest;
import com.prysmradio.objects.Radio;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

/**
 * Created by fxoxe_000 on 03/05/2014.
 */
public class RadiosListFragment extends PrysmListFragment<Radio> implements RequestListener<Radio.List> {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        adapter = new RadiosAdapter(getActivity());
        listView.setAdapter(adapter);

        RadiosListRequest request = new RadiosListRequest();
        getSpiceManager().execute(request, RadiosListRequest.RADIOS_LIST_REQUEST, DurationInMillis.ONE_MINUTE, this);
    }

    @Override
    public void onRequestFailure(SpiceException spiceException) {
        Crouton.showText(getPrysmActivity(), spiceException.getMessage(), Style.ALERT, (ViewGroup)rootView);
    }

    @Override
    public void onRequestSuccess(Radio.List radios) {
        progressBar.setVisibility(View.GONE);
        if (radios != null && radios.size() > 0){
            notifyDataSetChanged(radios);
        }
    }

    @Override
    void onItemClicked(int position) {

    }
}
