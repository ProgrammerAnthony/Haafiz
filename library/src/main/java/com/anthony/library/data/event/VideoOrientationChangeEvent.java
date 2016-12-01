package com.anthony.library.data.event;

import android.content.res.Configuration;

/**
 * Created by Vincent Woo
 * Date: 2016/7/13
 * Time: 18:34
 */
public class VideoOrientationChangeEvent {
    public Configuration newConfig;

    public VideoOrientationChangeEvent(Configuration newConfig) {
        this.newConfig = newConfig;
    }
}
