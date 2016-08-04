package edu.com.app.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.squareup.sqlbrite.BriteDatabase;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import edu.com.app.data.bean.Channel;
import edu.com.app.data.bean.Constants;
import edu.com.app.data.db.Db;
import edu.com.app.data.retrofit.ChannelTypeAdapter;
import edu.com.app.data.retrofit.HttpResult;
import edu.com.app.data.retrofit.HttpResultFunc;
import edu.com.app.data.retrofit.ItemJsonDeserializer;
import edu.com.app.data.retrofit.RemoteApi;
import edu.com.app.injection.scope.ApplicationContext;
import edu.com.app.util.FileUtil;
import okhttp3.ResponseBody;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.exceptions.Exceptions;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Anthony on 2016/6/12.
 * Class Note:
 * data entrance
 * todo more to do
 */


public class DataManager {

    @Inject
    HttpHelper httpHelper;

    @Inject
    PreferencesHelper mPreferencesHelper;

    @Inject
    EventPosterHelper mEventPoster;

    private Context mContext;

    @Inject
    BriteDatabase mDb;

    public List<Channel> mMyChannels = new ArrayList<>();//my channels
    public List<Channel> mOtherChannels = new ArrayList<>();//other channels

    @Inject
    public DataManager(@ApplicationContext Context context) {
        this.mContext = context;
    }

    public PreferencesHelper getPreferencesHelper() {
        return mPreferencesHelper;
    }


    private Action0 postEventAction(final Object event) {
        return new Action0() {
            @Override
            public void call() {
                mEventPoster.postEventSafely(event);
            }
        };
    }

    /**
     * load String local or online
     */
    public Observable<String> loadString(String url) {
        if (url.startsWith(Constants.LOCAL_FILE_BASE_END_POINT)) {
            try {
                String s = FileUtil.getString(mContext, url);
                return Observable.just(s);
            } catch (IOException e) {
                e.printStackTrace();
                throw Exceptions.propagate(e);
            }
        } else {
            String path = url.substring(Constants.Remote_BASE_END_POINT.length());
            return httpHelper.getService(RemoteApi.class)
                    .loadString(path)
                    .flatMap(new Func1<ResponseBody, Observable<String>>() {
                        @Override
                        public Observable<String> call(ResponseBody responseBody) {
                            try {
                                String result = responseBody.string();
                                return Observable.just(result);
                            } catch (IOException e) {
                                e.printStackTrace();
                                throw new RuntimeException("IOException when convert Response Body to String");
                            }
                        }
                    });
        }
    }


    /**
     * load channels data online or offline
     * step 1 {@link #loadString(String)} online or offline
     * step 2 Rx flatMap load string data to  {@link HttpResult}
     * step 3 Rx map  get data field in {@link HttpResult}
     * and return {@link HttpResultFunc}in which List<Channel> contain
     */
    public Observable<List<Channel>> loadChannelList(String url) {
        final Type type = new TypeToken<HttpResult<List<Channel>>>() {
        }.getType();

        final Gson channelGson = new GsonBuilder().registerTypeAdapter(type, new ChannelTypeAdapter()).create();
        return loadString(url)
                .flatMap(new Func1<String, Observable<HttpResult<List<Channel>>>>() {
                    @Override
                    public Observable<HttpResult<List<Channel>>> call(String s) {
                        HttpResult<List<Channel>> obj = channelGson.fromJson(s, type);
                        return Observable.just(obj);
                    }
                })
                .map(new HttpResultFunc<List<Channel>>());
    }


    /**
     * save channel list to db
     * step1 delete all of the data before
     * step2 insert channel list
     * todo get data online,if not exists anymore ,
     * todo if exists in local ,delete local data.do nothing ,if not exists in local,add.
     */
    public Action1<List<Channel>> saveChannelListToDb = new Action1<List<Channel>>() {
        @Override
        public void call(List<Channel> channels) {
            //delete all of the data before
            mDb.delete(Channel.TABLE, null, new String[]{});
            //insert channel list
            for (int i = 0; i < channels.size(); i++) {
                Channel channel = channels.get(i);
                mDb.insert(Channel.TABLE, new Channel.Builder()
                        .title(channel.title())
                        .url(channel.url())
                        .type(channel.type())
                        .isFix(channel.isFix())
                        .isSubscribe(channel.isSubscribe())
                        .build());
            }
        }
    };


    /**
     * use BriteDatabase to query list Channels data ,
     * then map to list channel
     *
     * @return
     */
    public Observable<List<Channel>> queryChannelList() {
        return mDb.createQuery(Channel.TABLE, Channel.QUERY_CHANNEL_LIST, new String[]{})
                .mapToList(new Func1<Cursor, Channel>() {
                    @Override
                    public Channel call(Cursor cursor) {
                        String title = Db.getString(cursor, Channel.TITLE);
                        int type = Db.getInt(cursor, Channel.TYPE);
                        String url = Db.getString(cursor, Channel.URL);
                        int is_fix = Db.getInt(cursor, Channel.IS_FIX);
                        int is_subscribe = Db.getInt(cursor, Channel.IS_SUBSCRIBE);
                        return Channel.create(title, type, url, is_fix, is_subscribe);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    /**
     * update channel table using {@link Channel}
     *  searching by channel title
     * @param channel
     */
    public void updateChannelInDb(Channel channel) {
        ContentValues contentValues = new Channel.Builder()
                .title(channel.title())
                .url(channel.url())
                .isFix(channel.isFix())
                .isSubscribe(channel.isSubscribe())
                .type(channel.type()).build();
        mDb.update(Channel.TABLE,contentValues,"title = '"+channel.title()+"'");
    }




    /**
     * post string to server
     */
    public Observable<String> postString(String url, Map<String, String> paramMap) {
        return httpHelper.getService(RemoteApi.class)
                .postString(url, paramMap)
                .flatMap(new Func1<ResponseBody, Observable<String>>() {
                    @Override
                    public Observable<String> call(ResponseBody responseBody) {
                        try {
                            String result = responseBody.string();
                            return Observable.just(result);
                        } catch (IOException e) {
                            e.printStackTrace();
                            throw new RuntimeException("IOException when convert Response Body to String");
                        }
                    }
                });
    }


    /**
     * load String and Gson Deserialize
     */
    public <T> Observable<T> loadData(String url, final Class<T> clazz) {
        final Gson gson = new GsonBuilder().registerTypeAdapter(clazz, new ItemJsonDeserializer<T>()).create();
        return loadString(url)
                .flatMap(new Func1<String, Observable<T>>() {
                    @Override
                    public Observable<T> call(String s) {
                        T obj = gson.fromJson(s, clazz);
                        return Observable.just(obj);
                    }
                });
    }

}