package com.anthony.app.common.data.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class Image implements Parcelable, Serializable {
    public int id;

    @SerializedName(value = "title", alternate = {"pictitle"})
    public String title;

    @SerializedName(value = "url", alternate = {"picurl"})
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

    public static final Creator<Image> CREATOR = new Creator<Image>() {
        @Override
        public Image[] newArray(int size) {
            return new Image[size];
        }

        @Override
        public Image createFromParcel(Parcel in) {
            Image img = new Image();
            img.id = in.readInt();
            img.title = in.readString();
            img.url = in.readString();
            img.des = in.readString();
            return img;
        }
    };
}
