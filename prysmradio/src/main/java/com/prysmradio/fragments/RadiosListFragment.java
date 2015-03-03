package com.prysmradio.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.prysmradio.PrysmApplication;
import com.prysmradio.activities.PlayerActivity;
import com.prysmradio.adapters.RadiosAdapter;
import com.prysmradio.api.requests.RadiosListRequest;
import com.prysmradio.objects.Radio;
import com.prysmradio.services.RadioPlayerService;
import com.prysmradio.utils.Constants;
import com.prysmradio.utils.CurrentStreamInfo;

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
            if (CurrentStreamInfo.getInstance().getCurrentRadio() == null &&
                    CurrentStreamInfo.getInstance().getPodcastEpisode() == null){
                CurrentStreamInfo.getInstance().setCurrentRadio(radios.get(0));
            }
        }
    }

    @Override
    void onItemClicked(int position) {
        Radio radio = items.get(position);
        CurrentStreamInfo.getInstance().setCurrentRadio(radio);

        ((PrysmApplication) getActivity().getApplicationContext()).setServiceIsRunning(true);
        Intent intent = new Intent(getActivity(), RadioPlayerService.class);
        intent.setAction(Constants.START_RADIO_SERVICE_ACTION);

        intent.putExtra(Constants.AUDIO_URL_EXTRA, radio.getAACStreamURL());

        getActivity().startService(intent);

        Intent podcastIntent = new Intent(getActivity(), PlayerActivity.class);
        podcastIntent.putExtra(Constants.RADIO_EXTRA, radio);
        podcastIntent.putExtra(Constants.STREAM_TITLE_EXTRA, getPrysmActivity().getBottomPlayerFragment().getStreamTitle());
        getActivity().startActivityForResult(podcastIntent, Constants.METADATA_REQUEST_CODE);
    }
}
