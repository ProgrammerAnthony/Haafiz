package edu.com.app.data;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import edu.com.app.data.bean.Channel;
import edu.com.app.data.bean.Friends;
import edu.com.app.data.bean.Menu;
import edu.com.app.data.local.DatabaseHelper;
import edu.com.app.data.local.PreferencesHelper;
import edu.com.app.data.remote.FriendsService;
import edu.com.app.data.retrofit.HttpResult;
import edu.com.app.data.retrofit.HttpService;
import edu.com.app.data.retrofit.ItemJsonDeserializer;
import edu.com.app.data.retrofit.RemoteApi;
import edu.com.app.injection.scope.ApplicationContext;
import edu.com.app.util.FileUtil;
import okhttp3.ResponseBody;
import rx.Observable;
import rx.functions.Action0;
import rx.functions.Func1;

/**
 * Created by Anthony on 2016/6/12.
 * Class Note:
 * 所有数据的入口
 * todo 还需要进一步整理格式
 */
@Singleton
public class DataManager {


    @Inject
    FriendsService friendsService;

    @Inject
    HttpService httpService;
    @Inject
    DatabaseHelper mDatabaseHelper;
    @Inject
    PreferencesHelper mPreferencesHelper;
    @Inject
    EventPosterHelper mEventPoster;

    private Context mContext;

    @Inject
    public DataManager(@ApplicationContext Context context) {
        this.mContext = context;
    }

    public PreferencesHelper getPreferencesHelper() {
        return mPreferencesHelper;
    }


    public Observable<Friends> syncFriends() {
        return friendsService.getFriends()
                .concatMap(new Func1<List<Friends>, Observable<Friends>>() {
                    @Override
                    public Observable<Friends> call(List<Friends> friends) {
                        return mDatabaseHelper.setFriends(friends);
                    }
                });
    }


    public Observable<List<Friends>> getFriends() {
//        if()
        return mDatabaseHelper.getFriends().distinct();
    }


    /// Helper method to post events from doOnCompleted.
    private Action0 postEventAction(final Object event) {
        return new Action0() {
            @Override
            public void call() {
                mEventPoster.postEventSafely(event);
            }
        };
    }

    /**
     * 请求本地或远程String函数
     * 注意:直接获取的String，下面的loadData、loadMenu、loadChannel都基于此函数
     * 只是下面函数进行了相应的Gson反序列化
     */
    public Observable<String> loadString(String url) {
        if (url.startsWith(Constants.LOCAL_FILE_BASE_END_POINT)) {
            try {
                return Observable.just(FileUtil.getString(mContext, url));
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            String path = url.substring(Constants.Remote_BASE_END_POINT.length());
            return httpService.getService(RemoteApi.class)
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
     * 请求本地或远程数据函数
     * 注意:按照传入的Class进行了Gson数据反序列化
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


    /**
     * 请求本地或远程一级栏目数据函数
     * 注意:Gson数据反序列化为Menu类型
     */
    public Observable<Menu> loadMenu(String url) {
//        return null;
        final Gson gson = new GsonBuilder().registerTypeAdapter(Menu.class, new ItemJsonDeserializer<Menu>()).create();
        return loadString(url).flatMap(new Func1<String, Observable<Menu>>() {
            @Override
            public Observable<Menu> call(String s) {
                return Observable.just(gson.fromJson(s, Menu.class));
            }
        });
    }

    /**
     * 请求本地或远程频道数据函数
     * 注意:Gson数据反序列化为HttpResult<List<Channel>>类型
     */
    public Observable<HttpResult<List<Channel>>> loadChannel(String url) {
      final   Type type = new TypeToken<HttpResult<List<Channel>>>() {
        }.getType();
        final Gson gson = new GsonBuilder().registerTypeAdapter(type, new ItemJsonDeserializer<HttpResult<List<Channel>>>()).create();
        return loadString(url).flatMap(new Func1<String, Observable<HttpResult<List<Channel>>>>() {
            @Override
            public Observable<HttpResult<List<Channel>>> call(String s) {
                HttpResult<List<Channel>> obj =gson.fromJson(s,type);
                return Observable.just(obj);
            }
        });
    }
    /**
     * 通过POST请求发送参数到服务器
     */
    public Observable<String> postString(String url, Map<String, String> paramMap){
        return httpService.getService(RemoteApi.class)
                .postString(url,paramMap)
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