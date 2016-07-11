package edu.com.app.data;

import android.content.Context;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import edu.com.app.data.bean.Friends;
import edu.com.app.data.http.HttpService;
import edu.com.app.data.http.RemoteApi;
import edu.com.app.data.local.DatabaseHelper;
import edu.com.app.data.local.PreferencesHelper;
import edu.com.app.data.remote.FriendsService;
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
//        gson = new GsonBuilder()
//                .registerTypeAdapter(TRSNewsItem.class, new ItemJsonDeserializer<TRSNewsItem>())
//                .create();
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
     * 通过POST请求发送参数到服务器
     */
    public Observable<String> postString(String url, Map<String, String> paramMap) {
        return httpService.getService(RemoteApi.class)
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




}
