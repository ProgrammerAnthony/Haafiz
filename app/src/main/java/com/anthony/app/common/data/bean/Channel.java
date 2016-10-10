package com.anthony.app.common.data.bean;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.ArrayList;


@DatabaseTable(tableName = "tb_channel")
public class Channel implements Serializable {
    @DatabaseField(generatedId = true, columnName = "c_id")
    private int c_id;


    @DatabaseField(columnName = "parentChannelId")
    private int parentChannelId;

    @DatabaseField(columnName = "id")
    @SerializedName(value = "channelId")
    private String id;

    @DatabaseField(columnName = "title")
    private String title;

    @DatabaseField(columnName = "type")
    @SerializedName(value = "type", alternate = {"channelType"})
    private String type;

    @DatabaseField(columnName = "img", dataType = DataType.SERIALIZABLE)
    @SerializedName(value = "image", alternate = {"ic", "images", "icon", "picture", "pic", "img", "RelPhoto", "RelPhotos"})
    private ArrayList<String> imgs;

    @DatabaseField(columnName = "url")
    @SerializedName(value = "url", alternate = {"link"})
    private String url;

    @DatabaseField(columnName = "isFix")
    private int isFix;

    @DatabaseField(columnName = "isSubscribe")
    @SerializedName(value = "isSubscribe", alternate = {"isSubscrible"})
    private int isSubscribe;

    @DatabaseField(columnName = "sort")
    private long sort = -1;

    @DatabaseField(columnName = "lrt") //Last Refresh Time
    private long lrt = 0;

    public Channel(String title, String url) {
        this.title = title;
        this.url = url;
    }


    @Override
    public boolean equals(Object o) {
        if (o instanceof Channel) {
            Channel c = (Channel) o;
            return this.getTitle().equals(c.getTitle());
        }
        return false;
    }

    public ArrayList<String> getImgs() {
        return imgs;
    }

    public void setImg(ArrayList<String> imgs) {
        this.imgs = imgs;
    }

    public String getType() {
        return type;
    }

    public String getUrl() {
        return url;
    }


    public String getTitle() {
        return title;
    }

    public int getC_id() {
        return c_id;
    }

    public void setC_id(int c_id) {
        this.c_id = c_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setType(String type) {
        this.type = type;
    }


    public void setUrl(String url) {
        this.url = url;
    }

    public int getIsFix() {
        return isFix;
    }

    public void setIsFix(int isFix) {
        this.isFix = isFix;
    }

    public int getIsSubscribe() {
        return isSubscribe;
    }

    public void setIsSubscribe(int isSubscribe) {
        this.isSubscribe = isSubscribe;
    }

    public long getSort() {
        return sort;
    }

    public void setSort(long sort) {
        this.sort = sort;
    }

    public long getLrt() {
        return lrt;
    }

    public void setLrt(long lrt) {
        this.lrt = lrt;
    }

    public int getParentChannelId() {
        return parentChannelId;
    }

    public void setParentChannelId(int parentChannelId) {
        this.parentChannelId = parentChannelId;
    }

}
