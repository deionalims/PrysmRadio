package com.prysmradio.bus.events;

import com.squareup.otto.Bus;

/**
 * Created by fx.oxeda on 24/03/2014.
 */
public class BusManager {

    private static BusManager instance;
    private Bus bus;

    private BusManager(){
        bus = new Bus();
    }

    public static BusManager getInstance() {
        if (instance == null) {
            instance = new BusManager();
        }
        return instance;
    }

    public Bus getBus() {
        return bus;
    }
}
