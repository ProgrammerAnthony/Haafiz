package edu.com.mvpcommon.news.newsList;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import edu.com.mvplibrary.model.Channel;
import edu.com.mvplibrary.model.config.Constants;
import edu.com.mvplibrary.util.JSONObjectHelper;

/**
 * Created by Anthony on 2016/5/3.
 * Class Note:
 * news channel data
 * "raw://news_channels   raw://nba_list"
 */
public class NewsListData {


    private ArrayList<Channel> mChannels;
    private Context mContext;
    private NewsListContract.onGetChannelListListener mListener;

    public NewsListData(Context mContext, NewsListContract.onGetChannelListListener mListener) {
        this.mContext = mContext;
        this.mListener = mListener;

        mChannels = new ArrayList<>();
    }

    /**
     * // TODO: 2016/5/4 根据传递的url,在此进行网络数据访问, 将数据转换为channel数据
     * @param url
     * @throws JSONException
     */
    public void parseUrl(String url) throws JSONException{

        for(int i=0;i<=2;i++){
            Channel channel=new Channel();
            channel.setType("2002");
            channel.setTitle("nba"+i);
            mChannels.add(channel);
        }
        if (mChannels != null) {
            mListener.onSuccess();
        } else {
            mListener.onError();
        }
//        this.initListData("{\n" +
//                "  \"type\": \"2001\",\n" +
//                "  \"channels\": [\n" +
//                "    {\n" +
//                "      \"title\": \"nba\",\n" +
//                "      \"type\": \"2002\",\n" +
//                "      \"url\": \"raw://nba_list\"\n" +
//                "    },\n" +
//                "    {\n" +
//                "      \"title\": \"cba\",\n" +
//                "      \"type\": \"2002\",\n" +
//                "      \"url\": \"raw://nba_list\"\n" +
//                "    },\n" +
//                "    {\n" +
//                "      \"title\": \"足球\",\n" +
//                "      \"type\": \"2002\",\n" +
//                "      \"url\": \"raw://nba_list\"\n" +
//                "    }\n" +
//                "  ]\n" +
//                "}");

    }

    public void initListData(String objStr) throws JSONException {
        this.initListData(new JSONObject(objStr));
    }

    public void initListData(JSONObject obj) throws JSONException {

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
