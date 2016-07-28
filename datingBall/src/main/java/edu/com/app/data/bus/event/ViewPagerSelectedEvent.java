package edu.com.app.data.bus.event;

import edu.com.app.data.bean.Channel;

/**
 * Created by Anthony on 2016/7/27.
 * Class Note:
 * for view pager selected in{@link edu.com.app.module.news.tab.NewsTabFragment}
 */
public class ViewPagerSelectedEvent {
    public int position;
    public Channel channel;

    public ViewPagerSelectedEvent(int position, Channel channel) {
        this.position = position;
        this.channel = channel;
    }
}
