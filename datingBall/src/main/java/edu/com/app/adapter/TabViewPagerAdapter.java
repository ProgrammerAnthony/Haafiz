package edu.com.app.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

import edu.com.app.data.bean.Channel;
import edu.com.app.widget.ViewDisplay;

/**
 * Created by Anthony on 2016/7/27.
 * Class Note:
 * get a channel list
 * using different {@link edu.com.app.data.bean.Channel} to create different Fragment
 */

public class TabViewPagerAdapter extends FragmentPagerAdapter {


    protected Context mContext;
    protected List<Channel> mChannels;
    private ViewDisplay mViewDisplay;

    public TabViewPagerAdapter(Context context, List<Channel> channels, FragmentManager fm, ViewDisplay viewDisplay) {
        super(fm);
        mContext = context;
        mChannels = channels;
        mViewDisplay = viewDisplay;
    }

    @Override
    public Fragment getItem(int position) {
        Channel channel = mChannels.get(position);
        String type = "";
        Bundle bundle = null;
        if (channel != null) {
            bundle = new Bundle();
            bundle.putString("title", channel.title());
            bundle.putString("url", channel.url());
        }

        switch (channel.type()) {
            case 0:
                type = "NewsListFragment";
                break;
            case 1:
                type = "MultiListFragment";
                break;
            case 2:
                type = "GridFragment";
                break;
            case 3:
                type = "StaggerFragment";
                break;
            case 4:
                type = "VideoFragment";
                break;
        }
        Fragment fragment = new Fragment();
//        Timber.i(mViewDisplay.getFragmentName("VideoFragment"));
        Fragment fragmentCreated =mViewDisplay.createFragment(mContext,channel,type);
//        Fragment fragmentCreated = mViewDisplay.createFragment(mContext, type, bundle == null ? null : bundle);
        return fragmentCreated == null ? fragment : fragmentCreated;
    }


    @Override
    public int getCount() {
        return mChannels.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mChannels.get(position).title();
    }

    public List<Channel> getChannels() {
        return mChannels;
    }

    public void add(Channel item) {
        mChannels.add(item);
    }

    public void addAll(List<Channel> dataList) {
        mChannels.addAll(dataList);
    }

    public void clear() {
        mChannels.clear();
    }
}
