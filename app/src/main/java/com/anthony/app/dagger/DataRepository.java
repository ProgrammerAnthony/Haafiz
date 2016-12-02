package com.anthony.app.dagger;

import android.content.Context;

import com.anthony.app.dagger.scope.ApplicationContext;
import com.anthony.app.module.github.GithubApi;
import com.anthony.app.module.github.GithubUser;
import com.anthony.app.module.weather.WeatherApi;
import com.anthony.app.module.weather.WeatherData;
import com.anthony.app.module.wechatlist.WXItemBean;
import com.anthony.app.module.wechatlist.WechatApi;
import com.anthony.app.module.zhihu.ZhihuDailyListBean;
import com.anthony.app.module.zhihu.ZhiHuApi;
import com.anthony.app.module.zhihu.ZhihuDailyDetailBean;
import com.anthony.library.Constants;
import com.anthony.library.DataManager;
import com.anthony.library.data.bean.NewsItem;
import com.anthony.library.data.bean.NormalJsonInfo;
import com.anthony.library.data.net.HttpResult;
import com.anthony.library.data.net.HttpResultFunc;
import com.anthony.library.utils.LogUtil;
import com.anthony.library.utils.RxUtils;
import com.anthony.library.utils.SpUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import okhttp3.ResponseBody;
import rx.Observable;
import rx.functions.Action1;
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

    /**
     * load Zhihu Daily list data from Zhihu API,
     * http://news-at.zhihu.com/api/3/news/latest
     *
     * @return
     */
    public Observable<ZhihuDailyListBean> getDailyData() {
        return getHttpHelper().getApi(ZhiHuApi.class)
                .getDailyList()
                .compose(RxUtils.rxSchedulerHelper());
    }

    /**
     * load Zhihu Daily Detail data from zhihu API
     * @param id
     * @return
     */
    public Observable<ZhihuDailyDetailBean> getZhihuDetail(int id) {
        return getHttpHelper().getApi(ZhiHuApi.class)
                .getDetailInfo(id)
                .compose(RxUtils.defaultSchedulers());
    }


    /**
     * load news list info
     *
     * @param url url to load
     * @return NormalJsonInfo<NewsItem>
     */

    public Observable<NormalJsonInfo<NewsItem>> loadNewsJsonInfo(final String url) {
        final Gson gson = new GsonBuilder().create();
        return loadString(url)
                .doOnNext(new Action1<String>() {
                    @Override
                    public void call(String result) {
                        SpUtil.putString(mContext, url, result);
                        LogUtil.i("currently data string news ---> " + SpUtil.getString(mContext, url));
                    }
                })
                .flatMap(new Func1<String, Observable<HttpResult<NormalJsonInfo<NewsItem>>>>() {
                    @Override
                    public Observable<HttpResult<NormalJsonInfo<NewsItem>>> call(String s) {
                        HttpResult<NormalJsonInfo<NewsItem>> obj = gson.fromJson(s,
                                new TypeToken<HttpResult<NormalJsonInfo<NewsItem>>>() {
                                }.getType());
                        return Observable.just(obj);
                    }
                }).map(new HttpResultFunc<>())
                .compose(RxUtils.defaultSchedulers());
    }


}
