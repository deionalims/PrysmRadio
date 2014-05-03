package com.prysmradio.fragments;

import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;

import com.octo.android.robospice.SpiceManager;
import com.prysmradio.activities.PrysmActivity;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

/**
 * Created by fxoxe_000 on 09/03/14.
 */
public class PrysmFragment extends Fragment {

    protected View rootView;

    protected PrysmActivity getPrysmActivity(){
        return (PrysmActivity) getActivity();
    }

    protected SpiceManager getSpiceManager(){
        PrysmActivity activity = getPrysmActivity();
        if (activity != null){
            return activity.getSpiceManager();
        }
        return null;
    }


    protected void showError(String error){
        Crouton.showText(getPrysmActivity(), error, Style.ALERT, (ViewGroup)rootView);
    }
}
