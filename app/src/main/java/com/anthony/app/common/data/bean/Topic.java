package com.anthony.app.common.data.bean;

import com.google.gson.annotations.SerializedName;


public class Topic {
    private String id;
    private String title;
    private String type;
    @SerializedName(value = "image", alternate = {"ic", "icon", "picture", "pic", "img"})
    private String img;
    @SerializedName(value = "url", alternate = {"link"})
    private String url;
}
