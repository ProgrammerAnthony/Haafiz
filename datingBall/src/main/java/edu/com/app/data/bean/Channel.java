package edu.com.app.data.bean;

/**
 * Created by Anthony on 2016/5/4.
 * Class Note:
 */

import com.google.auto.value.AutoValue;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@AutoValue
public abstract class Channel implements Serializable{
    public static final String TABLE = "channel";

    public static final String ID = "_id";
    public static final String CHANNEL_ID = "c_id";
    public static final String TITLE = "title";
    public static final String TYPE = "type";
    public static final String IS_FIX = "is_fix";

    public static final String IS_SUBSCRIBE = "type";
    public static final String SORT = "type";
    public static final String LRT = "type";

    public abstract int id();
    public abstract String c_id();
    public abstract String title();

    @SerializedName(value = "type", alternate = {"channelType"})
    public abstract String type();

    @SerializedName(value = "image", alternate = {"ic", "icon", "picture", "pic", "img"})
    public abstract String img();

    @SerializedName(value = "url", alternate = {"link"})
    public abstract String url();
    public abstract int isFix();
    public abstract int isSubscribe();
    public abstract long sort();
    public abstract long lrt();


}
