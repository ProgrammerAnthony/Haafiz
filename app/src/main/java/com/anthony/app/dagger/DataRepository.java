package com.anthony.app.dagger;

import android.content.Context;

import com.anthony.app.dagger.scope.ApplicationContext;
import com.anthony.app.module.github.GithubApi;
import com.anthony.app.module.github.GithubUser;
import com.anthony.app.module.weather.WeatherApi;
import com.anthony.app.module.weather.WeatherData;
import com.anthony.app.module.wechatlist.WXItemBean;
import com.anthony.app.module.wechatlist.WechatApi;
import com.anthony.library.Constants;
import com.anthony.library.DataManager;
import com.anthony.library.utils.RxUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import okhttp3.ResponseBody;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by Anthony on 2016/12/1.
 * Class Note:
 */

public class DataRepository extends DataManager {

    @Inject
    public DataRepository(@ApplicationContext Context context) {
        super(context);
    }

    /**
     * load weather data from Baidu API
     *
     * @param location
     * @return
     */
    public Observable<WeatherData> loadWeatherData(String location) {
        Map<String, String> params = new HashMap<>();
        params.put("location", location);
        params.put("language", "zh-Hans");
        params.put("unit", "c");
        params.put("start", "0");
        params.put("days", "3");
        return getHttpHelper().getApi(WeatherApi.class)
                .loadWeatherData(params)
                .compose(RxUtils.defaultSchedulers());
    }

    /**
     * load  following list of github users
     *
     * @return Observable<String>
     */
    public Observable<String> loadUserFollowingString(String userName) {
        return getHttpHelper().getApi(GithubApi.class)
                .loadUserFollowingString(userName)
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
                })
                .compose(RxUtils.defaultSchedulers());
    }

    /**
     * load  following list of github users
     *
     * @return Observable<List<GithubUser>>
     */
    public Observable<List<GithubUser>> loadUserFollowingList(String userName) {
        return getHttpHelper().getApi(GithubApi.class)
                .loadUserFollowingList(userName)
                .compose(RxUtils.defaultSchedulers());
    }

    public Observable<List<WXItemBean>> getWechatData(int num, int page) {
        return getHttpHelper().getApi(WechatApi.class)
                .getWXHot(Constants.WEIXIN_KEY, num, page)
//                .map(new HttpResultFunc<>())  和下方语句一样效果
                .compose(RxUtils.handleResult())
                .compose(RxUtils.defaultSchedulers());
    }


}
