package edu.com.base.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class Ribot implements Comparable<Ribot>, Parcelable {

    public Profile profile;

    public Ribot() {
    }

    public Ribot(Profile profile) {
        this.profile = profile;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Ribot ribot = (Ribot) o;

        return !(profile != null ? !profile.equals(ribot.profile) : ribot.profile != null);
    }

    @Override
    public int hashCode() {
        return profile != null ? profile.hashCode() : 0;
    }

    @Override
    public int compareTo(Ribot another) {
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

    protected Ribot(Parcel in) {
        this.profile = in.readParcelable(Profile.class.getClassLoader());
    }

    public static final Creator<Ribot> CREATOR = new Creator<Ribot>() {
        public Ribot createFromParcel(Parcel source) {
            return new Ribot(source);
        }

        public Ribot[] newArray(int size) {
            return new Ribot[size];
        }
    };
}

