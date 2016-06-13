package edu.com.app.data.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class Friends implements Comparable<Friends>, Parcelable {

    public Profile profile;

    public Friends() {
    }

    public Friends(Profile profile) {
        this.profile = profile;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Friends friends = (Friends) o;

        return !(profile != null ? !profile.equals(friends.profile) : friends.profile != null);
    }

    @Override
    public int hashCode() {
        return profile != null ? profile.hashCode() : 0;
    }

    @Override
    public int compareTo(Friends another) {
        return profile.name.first.compareToIgnoreCase(another.profile.name.first);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.profile, 0);
    }

    protected Friends(Parcel in) {
        this.profile = in.readParcelable(Profile.class.getClassLoader());
    }

    public static final Creator<Friends> CREATOR = new Creator<Friends>() {
        public Friends createFromParcel(Parcel source) {
            return new Friends(source);
        }

        public Friends[] newArray(int size) {
            return new Friends[size];
        }
    };
}

