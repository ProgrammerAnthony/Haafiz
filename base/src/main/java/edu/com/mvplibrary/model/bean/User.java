package edu.com.mvplibrary.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.avos.avoscloud.AVUser;

import edu.com.mvplibrary.model.dao.UserDao;

/**
 * Created by Administrator on 2016/4/17.
 */
public class User extends AVUser implements Parcelable {
    private String nick;
    private String avatar;
    private String city;
    private String loverusername;
    private String sex;
    private String anotherUserID;
    private String InstallationId;
    private String LoverInstallationId;
    private String Background;
    private long lovetimeStamp;

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getLoverAvatar() {
        return getString(UserDao.LOVERAVATAR);
    }

    public void setLoverAvatar(String loverAvatar) {
        put(UserDao.LOVERAVATAR, loverAvatar);
    }

    public String getLoverNick() {
        return getString(UserDao.LOVERNICK);
    }

    public void setLoverNick(String loverNick) {
        put(UserDao.LOVERNICK, loverNick);
    }

    private String loverNick;
    private String loverAvatar;

    public String getLoverBackGround() {
        return getString(UserDao.LOVERBACKGROUND);
    }

    public void setLoverBackGround(String loverBackGround) {
        put(UserDao.LOVERBACKGROUND, loverBackGround);
    }

    private String LoverBackGround;

    public String getBackground() {
        return getString(UserDao.BACKGROUND);
    }

    public void setBackground(String background) {
        put(UserDao.BACKGROUND, background);
    }


    public long getLovetimeStamp() {
        return getLong(UserDao.LOVETIMESTAMP);
    }

    public void setLovetimeStamp(long lovetimeStamp) {
        put(UserDao.LOVETIMESTAMP, lovetimeStamp);
    }


    public User() {

    }

    public String getLoverusername() {
        return getString(UserDao.LOVERUSERNAME);
    }

    public void setLoverusername(String loverusername) {
        put(UserDao.LOVERUSERNAME, loverusername);
    }

    public String getCity() {
        return getString(UserDao.CITY);
    }

    public void setCity(String city) {
        put(UserDao.CITY, city);
    }


    public String getNick() {
        return getString(UserDao.NICK);
    }

    public void setNick(String nick) {
        put(UserDao.NICK, nick);
    }

    public String getAvatar() {
        return getString(UserDao.AVATARURL);
    }

    public void setAvatar(String avatar) {
        put(UserDao.AVATARURL, avatar);
    }


    public String getSex() {
        return getString(UserDao.SEX);
    }

    public void setSex(String sex) {
        put(UserDao.SEX, sex);
    }

    public String getAnotherUserID() {
        return getString(UserDao.LOVERID);
    }

    public void setAnotherUserID(String anotherUserID) {
        put(UserDao.LOVERID, anotherUserID);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.nick);
        dest.writeString(this.avatar);
        dest.writeString(this.city);
        dest.writeString(this.loverusername);
        dest.writeString(this.sex);
        dest.writeString(this.anotherUserID);
    }

    protected User(Parcel in) {
        this.nick = in.readString();
        this.avatar = in.readString();
        this.city = in.readString();
        this.loverusername = in.readString();
        this.sex = in.readString();
        this.anotherUserID = in.readString();
    }

    public String getInstallationId() {
        return getString(UserDao.INSTALLATIONID);
    }

    public void setInstallationId(String installationId) {
        put(UserDao.INSTALLATIONID, installationId);
    }

    public String getLoverInstallationId() {
        return getString(UserDao.LOVERINSTALLATIONID);
    }

    public void setLoverInstallationId(String loverInstallationId) {
        put(UserDao.LOVERINSTALLATIONID, loverInstallationId);
    }


}
