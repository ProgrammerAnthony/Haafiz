package edu.com.mvpcommon.news.newsChannel;

import android.content.Context;

import org.json.JSONException;

import edu.com.mvplibrary.util.ToastUtils;

/**
 * Created by Anthony on 2016/5/3.
 * Class Note: Presenter in MVP
 *see {@link NewsChannelContract}-----Manager role of MVP
 * &{@link NewsChannelPresenter}------Presenter
 * &{@link NewsChannelFragment}-------------View
 * &{@link NewsChannelData}--------Model
 */
public class NewsChannelPresenter implements NewsChannelContract.Presenter,NewsChannelContract.onGetChannelListListener{
    private NewsChannelContract.View mView;
    private Context mContext;
    private NewsChannelData mData;

    public NewsChannelPresenter(NewsChannelContract.View mView, Context mContext){
        this.mView =mView;
        this.mContext=mContext;

        mData=new NewsChannelData(mContext,this);//init news channel data
    }

    @Override
    public void getChannels() {
    //利用网络加载数据
        try {
            mData.initChannelsData("{\n" +
                    "  \"type\": \"\",\n" +
                    "  \"channels\": [\n" +
                    "    {\n" +
                    "      \"title\": \"nba\",\n" +
                    "      \"type\": \"2002\",\n" +
                    "      \"url\": \"raw://nba_data\"\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"title\": \"cba\",\n" +
                    "      \"type\": \"2002\",\n" +
                    "      \"url\": \"raw://nba_data\"\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"title\": \"足球\",\n" +
                    "      \"type\": \"2002\",\n" +
                    "      \"url\": \"raw://nba_data\"\n" +
                    "    }\n" +
                    "  ]\n" +
                    "}");
            mView.onChannelsGet(mData.getChannels());
        }catch (JSONException e){
            this.onError();
            e.printStackTrace();
        }

    }



    @Override
    public void onSuccess() {

    }

    @Override
    public void onError() {
        ToastUtils.getInstance().showToast("on error");
    }
}
