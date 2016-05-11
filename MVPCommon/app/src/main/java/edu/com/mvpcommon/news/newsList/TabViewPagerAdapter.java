package edu.com.mvpcommon.news.newsList;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;


import java.util.ArrayList;


import edu.com.mvplibrary.model.Channel;
import edu.com.mvplibrary.model.ViewDisplayer;

/**
 * Created by Anthony on 2016/2/24.
 * Class Note: adapter extends FragmentStatePagerAdapter for Tab+ViewPager
 */
public class TabViewPagerAdapter extends FragmentStatePagerAdapter {
    private Context mContext;
    private ArrayList<Channel> mChannels;

    public TabViewPagerAdapter(Context ctx, FragmentManager fm, ArrayList<Channel> channels) {
        super(fm);
        mContext = ctx;
        mChannels = channels;
    }

    @Override
    public Fragment getItem(int position) {
        //show child fragment
        Fragment fragment = ViewDisplayer.initialView(mContext, mChannels.get(position));
        if (fragment == null) {
            throw new IllegalArgumentException("cannnot get fragment");
        }
        return fragment;
//        return ViewDisplayer.createFragment(mContext, channel);
    }

    @Override
    public int getCount() {
        return mChannels.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mChannels.get(position).getTitle();
    }

    public ArrayList<Channel> getChannels() {
        return mChannels;
    }

    public void add(Channel item) {
        mChannels.add(item);
    }

    public void addAll(ArrayList<Channel> dataList) {
        mChannels.addAll(dataList);
    }

    public void clear() {
        mChannels.clear();
    }
}
