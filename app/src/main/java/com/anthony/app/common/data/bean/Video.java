package com.anthony.app.common.data.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Video implements Parcelable, Serializable {
    public int id;

    @SerializedName(value = "title", alternate = {"videotitle"})
    public String title;

    @SerializedName(value = "url", alternate = {"dURL"})
    public String url;

    @SerializedName(value = "description", alternate = {"des"})
    public String des;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(url);
        dest.writeString(des);
    }

    public static final Creator<Video> CREATOR = new Creator<Video>() {
        @Override
        public Video[] newArray(int size) {
            return new Video[size];
        }

        @Override
        public Video createFromParcel(Parcel in) {
            Video video = new Video();
            video.id = in.readInt();
            video.title = in.readString();
            video.url = in.readString();
            video.des = in.readString();
            return video;
        }
    };
}
