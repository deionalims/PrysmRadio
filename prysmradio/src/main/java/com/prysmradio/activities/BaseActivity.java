package com.prysmradio.activities;

import android.support.v7.app.ActionBarActivity;

import com.octo.android.robospice.SpiceManager;
import com.prysmradio.services.PrysmRetrofitSpiceService;

/**
 * Created by fxoxe_000 on 12/05/2014.
 */
public class BaseActivity extends ActionBarActivity {

    private SpiceManager spiceManager = new SpiceManager(PrysmRetrofitSpiceService.class);

    @Override
    protected void onStart() {
        spiceManager.start(this);
        super.onStart();
    }

    @Override
    protected void onStop() {
        spiceManager.shouldStop();
        super.onStop();
    }

    public SpiceManager getSpiceManager() {
        return spiceManager;
    }
}
