package edu.com.app.data.bean;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anthony on 2016/7/13.
 * Class Note:
 * menu data
 */
public class Menu {
    private String id;
    private String type;

    @SerializedName(value = "channel",alternate = {"channels"})
    private List<Channel> channles =new ArrayList<>();

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public List<Channel> getChannles() {
        return channles;
    }
}
