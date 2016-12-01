package com.anthony.library.data.bean;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;


public class Menu {
    private String id;
    private String type;
    @SerializedName(value = "channel", alternate = {"channels"})
    private List<Channel> channels = new ArrayList<>();

    public String getType() {
        return type;
    }

    public List<Channel> getChannels() {
        return channels;
    }
}
