package com.anthony.app.module.weather;


import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;
/*{
        "results": [
        {
        "location": {
        "id": "WKEZD7MXE04F",
        "name": "贵阳",
        "country": "CN",
        "path": "贵阳,贵阳,贵州,中国",
        "timezone": "Asia/Shanghai",
        "timezone_offset": "+08:00"
        },
        "daily": [
        {
        "date": "2016-09-22",
        "text_day": "多云",
        "code_day": "4",
        "text_night": "多云",
        "code_night": "4",
        "high": "24",
        "low": "15",
        "precip": "",
        "wind_direction": "东南",
        "wind_direction_degree": "135",
        "wind_speed": "10",
        "wind_scale": "2"
        },
        {
        "date": "2016-09-23",
        "text_day": "多云",
        "code_day": "4",
        "text_night": "多云",
        "code_night": "4",
        "high": "24",
        "low": "16",
        "precip": "",
        "wind_direction": "东南",
        "wind_direction_degree": "135",
        "wind_speed": "10",
        "wind_scale": "2"
        },
        {
        "date": "2016-09-24",
        "text_day": "多云",
        "code_day": "4",
        "text_night": "多云",
        "code_night": "4",
        "high": "26",
        "low": "19",
        "precip": "",
        "wind_direction": "东南",
        "wind_direction_degree": "135",
        "wind_speed": "10",
        "wind_scale": "2"
        }
        ],
        "last_update": "2016-09-22T11:00:00+08:00"
        }
        ]
        }*/

/**
 * Created by Anthony on 2016/9/22.
 * Class Note:
 * 天气数据接口来自
 * http://apistore.baidu.com/apiworks/servicedetail/2573.html?qq-pf-to=pcqq.c2c
 * 数据{@link WeatherData} 包含一个{@link Result}的列表数据
 * {@link Result}数据列表目前只有一个数据对象
 * {@link Result}包含{@link Location}和{@link Daily}
 * {@link Location}为基础位置数据
 * {@link Daily}包含从今天开始未来三天的数据
 */
public class WeatherData {

    @SerializedName("results")
    private List<Result> results = new ArrayList<Result>();

    /**
     * @return The results
     */
    public List<Result> getResults() {
        return results;
    }

    /**
     * @param results The results
     */
    public void setResults(List<Result> results) {
        this.results = results;
    }


    public class Result {
        @SerializedName("location")
        private Location location;
        @SerializedName("daily")
        private List<Daily> daily = new ArrayList<Daily>();
        @SerializedName("last_update")
        private String lastUpdate;

        /**
         * @return The location
         */
        public Location getLocation() {
            return location;
        }

        /**
         * @param location The location
         */
        public void setLocation(Location location) {
            this.location = location;
        }

        /**
         * @return The daily
         */
        public List<Daily> getDaily() {
            return daily;
        }

        /**
         * @param daily The daily
         */
        public void setDaily(List<Daily> daily) {
            this.daily = daily;
        }

        /**
         * @return The lastUpdate
         */
        public String getLastUpdate() {
            return lastUpdate;
        }

        /**
         * @param lastUpdate The last_update
         */
        public void setLastUpdate(String lastUpdate) {
            this.lastUpdate = lastUpdate;
        }
    }

    public class Location {

        @SerializedName("id")
        private String id;
        @SerializedName("name")
        private String name;
        @SerializedName("country")
        private String country;
        @SerializedName("path")
        private String path;
        @SerializedName("timezone")
        private String timezone;
        @SerializedName("timezone_offset")
        private String timezoneOffset;

        /**
         * @return The id
         */
        public String getId() {
            return id;
        }

        /**
         * @param id The id
         */
        public void setId(String id) {
            this.id = id;
        }

        /**
         * @return The name
         */
        public String getName() {
            return name;
        }

        /**
         * @param name The name
         */
        public void setName(String name) {
            this.name = name;
        }

        /**
         * @return The country
         */
        public String getCountry() {
            return country;
        }

        /**
         * @param country The country
         */
        public void setCountry(String country) {
            this.country = country;
        }

        /**
         * @return The path
         */
        public String getPath() {
            return path;
        }

        /**
         * @param path The path
         */
        public void setPath(String path) {
            this.path = path;
        }

        /**
         * @return The timezone
         */
        public String getTimezone() {
            return timezone;
        }

        /**
         * @param timezone The timezone
         */
        public void setTimezone(String timezone) {
            this.timezone = timezone;
        }

        /**
         * @return The timezoneOffset
         */
        public String getTimezoneOffset() {
            return timezoneOffset;
        }

        /**
         * @param timezoneOffset The timezone_offset
         */
        public void setTimezoneOffset(String timezoneOffset) {
            this.timezoneOffset = timezoneOffset;
        }
    }

    public class Daily {

        @SerializedName("date")
        private String date;
        @SerializedName("text_day")
        private String textDay;
        @SerializedName("code_day")
        private String codeDay;
        @SerializedName("text_night")
        private String textNight;
        @SerializedName("code_night")
        private String codeNight;
        @SerializedName("high")
        private String high;
        @SerializedName("low")
        private String low;
        @SerializedName("precip")
        private String precip;
        @SerializedName("wind_direction")
        private String windDirection;
        @SerializedName("wind_direction_degree")
        private String windDirectionDegree;
        @SerializedName("wind_speed")
        private String windSpeed;
        @SerializedName("wind_scale")
        private String windScale;

        /**
         *
         * @return
         * The date
         */
        public String getDate() {
            return date;
        }

        /**
         *
         * @param date
         * The date
         */
        public void setDate(String date) {
            this.date = date;
        }

        /**
         *
         * @return
         * The textDay
         */
        public String getTextDay() {
            return textDay;
        }

        /**
         *
         * @param textDay
         * The text_day
         */
        public void setTextDay(String textDay) {
            this.textDay = textDay;
        }

        /**
         *
         * @return
         * The codeDay
         */
        public String getCodeDay() {
            return codeDay;
        }

        /**
         *
         * @param codeDay
         * The code_day
         */
        public void setCodeDay(String codeDay) {
            this.codeDay = codeDay;
        }

        /**
         *
         * @return
         * The textNight
         */
        public String getTextNight() {
            return textNight;
        }

        /**
         *
         * @param textNight
         * The text_night
         */
        public void setTextNight(String textNight) {
            this.textNight = textNight;
        }

        /**
         *
         * @return
         * The codeNight
         */
        public String getCodeNight() {
            return codeNight;
        }

        /**
         *
         * @param codeNight
         * The code_night
         */
        public void setCodeNight(String codeNight) {
            this.codeNight = codeNight;
        }

        /**
         *
         * @return
         * The high
         */
        public String getHigh() {
            return high;
        }

        /**
         *
         * @param high
         * The high
         */
        public void setHigh(String high) {
            this.high = high;
        }

        /**
         *
         * @return
         * The low
         */
        public String getLow() {
            return low;
        }

        /**
         *
         * @param low
         * The low
         */
        public void setLow(String low) {
            this.low = low;
        }

        /**
         *
         * @return
         * The precip
         */
        public String getPrecip() {
            return precip;
        }

        /**
         *
         * @param precip
         * The precip
         */
        public void setPrecip(String precip) {
            this.precip = precip;
        }

        /**
         *
         * @return
         * The windDirection
         */
        public String getWindDirection() {
            return windDirection;
        }

        /**
         *
         * @param windDirection
         * The wind_direction
         */
        public void setWindDirection(String windDirection) {
            this.windDirection = windDirection;
        }

        /**
         *
         * @return
         * The windDirectionDegree
         */
        public String getWindDirectionDegree() {
            return windDirectionDegree;
        }

        /**
         *
         * @param windDirectionDegree
         * The wind_direction_degree
         */
        public void setWindDirectionDegree(String windDirectionDegree) {
            this.windDirectionDegree = windDirectionDegree;
        }

        /**
         *
         * @return
         * The windSpeed
         */
        public String getWindSpeed() {
            return windSpeed;
        }

        /**
         *
         * @param windSpeed
         * The wind_speed
         */
        public void setWindSpeed(String windSpeed) {
            this.windSpeed = windSpeed;
        }

        /**
         *
         * @return
         * The windScale
         */
        public String getWindScale() {
            return windScale;
        }

        /**
         *
         * @param windScale
         * The wind_scale
         */
        public void setWindScale(String windScale) {
            this.windScale = windScale;
        }

    }

}