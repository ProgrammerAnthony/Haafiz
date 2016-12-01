package com.anthony.library.data.event;


import com.anthony.library.data.bean.Channel;

/**
 * Created by Anthony on 2016/7/27.
 * Class Note:
 * for view pager selected in
 */
public class ViewPagerSelectedEvent {
    public int position;
    public Channel channel;

    public ViewPagerSelectedEvent(int position, Channel channel) {
        this.position = position;
        this.channel = channel;
    }
}
