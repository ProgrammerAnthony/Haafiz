package com.anthony.app.module.weather;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.anthony.app.R;
import com.anthony.app.common.base.AbsBaseActivity;
import com.anthony.app.common.DataManager;
import com.anthony.app.common.data.bean.WeatherData;
import com.anthony.app.common.data.net.HttpSubscriber;
import com.anthony.app.common.injection.component.ActivityComponent;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import rx.Subscription;

/**
 * Created by Anthony on 2016/10/11.
 * Class Note:
 * using Retrofit to load weather info from Baidu API
 * weather data from
 * {@see "http://apistore.baidu.com/apiworks/servicedetail/2573.html?qq-pf-to=pcqq.c2c"}
 * <p>
 * 通过retrofit加载来自百度天气接口的天气信息
 * 天气数据来自
 * {@see "http://apistore.baidu.com/apiworks/servicedetail/2573.html?qq-pf-to=pcqq.c2c"}
 */

public class WeatherActivity extends AbsBaseActivity {

    @BindView(R.id.weather_bg)
    ImageView weatherBg;
    @BindView(R.id.weather_city)
    TextView weatherCity;
    @BindView(R.id.weather_temp_tod)
    TextView weatherTempTod;
    @BindView(R.id.weather_date_tod)
    TextView weatherDateTod;
    @BindView(R.id.weather_img_tom)
    ImageView weatherImgTom;
    @BindView(R.id.weather_temp_tom)
    TextView weatherTempTom;
    @BindView(R.id.weather_date_tom)
    TextView weatherDateTom;
    @BindView(R.id.weather_img_ttom)
    ImageView weatherImgTtom;
    @BindView(R.id.weather_temp_ttom)
    TextView weatherTempTtom;
    @BindView(R.id.weather_date_ttom)
    TextView weatherDateTtom;
    @BindView(R.id.weather_layout)
    RelativeLayout weatherLayout;

    @Inject
    DataManager mDataManager;

    private String cityWeatherToCheck;

    @Override
    protected int getContentViewID() {
        return R.layout.prj_weather_activity;
    }

    @Override
    protected void initViewsAndEvents(Bundle savedInstanceState) {
        cityWeatherToCheck = "成都";
        weatherCity.setText(cityWeatherToCheck);
        loadWeatherData();
    }

    private void loadWeatherData() {
        Subscription subscription = mDataManager.loadWeatherData(cityWeatherToCheck).subscribe(new HttpSubscriber<WeatherData>() {
            @Override
            public void onNext(WeatherData weatherData) {
                onWeatherDataLoaded(weatherData);
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                showToast("加载天气信息失败");
            }
        });
        mSubscriptions.add(subscription);
    }

    /**
     * 对天气数据进行解析
     *
     * @param weatherData
     */
    private void onWeatherDataLoaded(WeatherData weatherData) {
        List<WeatherData.Daily> dailyWeathers = weatherData.getResults().get(0).getDaily();
        WeatherData.Daily todayWeather = dailyWeathers.get(0);//今日天气
        WeatherData.Daily tomWeather = dailyWeathers.get(1);//明日天气
        WeatherData.Daily tTomWeather = dailyWeathers.get(2);//后天天气

        //今日温度
        weatherTempTod.setText(todayWeather.getLow() + "~" + todayWeather.getHigh() + "℃");
        //今日日期和天气
        weatherDateTod.setText(todayWeather.getDate() + " " + weatherTodayStr(todayWeather.getTextDay(), todayWeather.getTextNight()));

        //明天天气的图片
        weatherImgTom.setImageResource(getWeatherPicThroughName(tomWeather.getTextDay()));
        //明天温度
        weatherTempTom.setText(tomWeather.getLow() + "~" + tomWeather.getHigh() + "℃");
        //明天日期和天气
        weatherDateTom.setText(tomWeather.getDate() + " " + weatherTodayStr(tomWeather.getTextDay(), tomWeather.getTextNight()));


        //后天天气的图片
        weatherImgTtom.setImageResource(getWeatherPicThroughName(tTomWeather.getTextDay()));
        //后天温度
        weatherTempTtom.setText(tTomWeather.getLow() + "~" + tTomWeather.getHigh() + "℃");
        //后天日期和天气
        weatherDateTtom.setText(tTomWeather.getDate() + " " + weatherTodayStr(tTomWeather.getTextDay(), tTomWeather.getTextNight()));


    }

    /**
     * 如果白天天气和晚间天气有变化则“白天天气+“转”+“晚间天气””，比如多云转小雨
     *
     * @param textDay
     * @param textNight
     * @return
     */
    private String weatherTodayStr(String textDay, String textNight) {
        if (textDay.equals(textNight))
            return textDay;
        else {
            return textDay + "转" + textNight;
        }
    }

    @Override
    protected void injectDagger(ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    /**
     * 通过相应的天气字符串获取相应的图标
     *
     * @param name
     * @return
     */
    public int getWeatherPicThroughName(String name) {
        int picPath = 0;
        if (name.equals("多云")) {
            picPath = R.mipmap.weather_cloudy1;
        } else if (name.equals("晴")) {
            picPath = R.mipmap.weather_sunny;
        } else if (name.equals("中雪")) {
            picPath = R.mipmap.weather_med_snow;
        } else if (name.equals("大雨")) {
            picPath = R.mipmap.weather_big_rainy;
        } else if (name.equals("大雪")) {
            picPath = R.mipmap.weather_big_snow;
        } else if (name.equals("小雨")) {
            picPath = R.mipmap.weather_small_rain;
        } else if (name.equals("小雪")) {
            picPath = R.mipmap.weather_small_snow;
        } else if (name.equals("扬尘")) {
            picPath = R.mipmap.weather_raise_dust;
        } else if (name.equals("暴雨")) {
            picPath = R.mipmap.weather_heavy_rain;
        } else if (name.equals("暴雪")) {
            picPath = R.mipmap.weather_heavy_snow;
        } else if (name.equals("沙尘暴")) {
            picPath = R.mipmap.weather_sand_storm;
        } else if (name.equals("浮尘")) {
            picPath = R.mipmap.weather_fly_ash;
        } else if (name.equals("阴")) {
            picPath = R.mipmap.weather_overcast;
        } else if (name.equals("阵雨")) {
            picPath = R.mipmap.weather_shower;
        } else if (name.equals("雨夹雪")) {
            picPath = R.mipmap.weather_rain_with_snow;
        } else if (name.equals("雷阵雨")) {
            picPath = R.mipmap.weather_thunder_rain;
        } else if (name.equals("雷阵雨伴有冰雹")) {
            picPath = R.mipmap.weather_thunder_rain_hail;
        } else if (name.equals("雾")) {
            picPath = R.mipmap.weather_fog;
        } else if (name.equals("霾")) {
            picPath = R.mipmap.weather_haze;
        }

        if (picPath != 0) {
            return picPath;
        } else
            return R.mipmap.weather_overcast;//默认显示阴天的图标
    }
}
