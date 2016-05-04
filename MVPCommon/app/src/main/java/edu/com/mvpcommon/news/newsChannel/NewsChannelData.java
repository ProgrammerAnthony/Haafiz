package edu.com.mvpcommon.news.newsChannel;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import edu.com.mvplibrary.model.Channel;
import edu.com.mvplibrary.model.ViewDisplayer;
import edu.com.mvplibrary.model.config.Constants;
import edu.com.mvplibrary.util.JSONObjectHelper;

/**
 * Created by Anthony on 2016/5/3.
 * Class Note:
 * news channel data
 */
public class NewsChannelData {


    private ArrayList<Channel> mChannels;
    private Context mContext;
    private NewsChannelContract.onGetChannelListListener mListener;

    public NewsChannelData(Context mContext, NewsChannelContract.onGetChannelListListener mListener) {
        this.mContext = mContext;
        this.mListener = mListener;
        mChannels = new ArrayList<>();
    }

    public void initData(String url) throws JSONException{
        // TODO: 2016/5/4 在此进行网络数据访问

        this.initChannelsData("string data get from network or local");
    }

    public void initChannelsData(String objStr) throws JSONException {
        this.initChannelsData(new JSONObject(objStr));
    }

    public void initChannelsData(JSONObject obj) throws JSONException {

        JSONObjectHelper helper = new JSONObjectHelper(obj);
        JSONArray array = helper.getJSONArray(Constants.CHANNEL_NAMES, null);
        for (int i = 0; i < array.length(); i++) {
            Channel channel = new Channel(array.getJSONObject(i));
            mChannels.add(channel);
        }
        if (mChannels != null) {
            mListener.onSuccess();
        } else {
            mListener.onError();
        }

    }



    public ArrayList<Channel> getChannels() {
        return mChannels;
    }
}
