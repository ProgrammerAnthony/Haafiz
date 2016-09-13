package com.anthony.app.common.data.upload;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Anthony on 2016/7/8.
 * Class Note:
 *  upload params
 */
public class UploadParam implements Parcelable {
    public String key;
    public String value;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(key);
        dest.writeString(value);
    }

    public static final Creator<UploadParam> CREATOR = new Creator<UploadParam>() {
        @Override
        public UploadParam[] newArray(int size) {
            return new UploadParam[size];
        }

        @Override
        public UploadParam createFromParcel(Parcel in) {
            UploadParam param = new UploadParam();
            param.key = in.readString();
            param.value = in.readString();
            return param;
        }
    };
}
