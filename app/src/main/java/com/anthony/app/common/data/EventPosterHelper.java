package com.anthony.app.common.data;

import android.os.Handler;
import android.os.Looper;

import com.squareup.otto.Bus;

import javax.inject.Inject;

/**
 * Created by Anthony on 2016/6/12.
 * Class Note:
 * Provides helper methods to post event to an Otto event bus
 */
public class EventPosterHelper {
    private final Bus mBus;


    @Inject
    public EventPosterHelper(Bus bus) {
        mBus = bus;
    }

    public Bus getBus() {
        return mBus;
    }

    /**
     * Helper method to post an event from a different thread to the main one.
     */
    public void postEventSafely(final Object event) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                mBus.post(event);
            }
        });
    }
}
