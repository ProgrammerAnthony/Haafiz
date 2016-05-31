package edu.com.mvplibrary.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVObject;

import edu.com.mvplibrary.model.dao.SouvenirDao;


/**
 * Created by Administrator on 2016/4/20.
 */
@AVClassName(SouvenirDao.TABLENAME)
public class Souvenir extends AVObject implements Parcelable {
    private String Content;
    private String Picture;
    private User Author;
    private boolean IsLikedMine;
    private boolean IsLikedOther;
    private String AuthorId;
    private String ohterUserId;

//    public Souvenir(AVObject object, User avUser) {
//        this.Content = object.getString(SouvenirDao.SOUVENIR_CONTENT);
//        if (object.getCreatedAt() != null) {
//            this.timeStamp = object.getCreatedAt().getTime();
//        }
//        this.Picture = object.getString(SouvenirDao.SOUVENIR_PICTUREURL);
//        this.Author = new User(avUser);
//        this.IsLikedMine = object.getBoolean(SouvenirDao.SOUVENIR_ISLIKEME);
//        this.objectId = object.getObjectId();
//        this.IsLikedOther = object.getBoolean(SouvenirDao.SOUVENIR_ISLIKEOTHER);
//        this.AuthorId = avUser.getObjectId();
//        this.ohterUserId = avUser.getString(SouvenirDao.SOUVENIR_OTHERUSERID);
//    }

    public Souvenir() {

    }

    public String getContent() {
        return getString(SouvenirDao.SOUVENIR_CONTENT);
    }

    public void setContent(String content) {
        put(SouvenirDao.SOUVENIR_CONTENT, content);
    }


    public String getPicture() {
        return getString(SouvenirDao.SOUVENIR_PICTUREURL);
    }

    public void setPicture(String picture) {
        put(SouvenirDao.SOUVENIR_PICTUREURL, picture);
    }

    public User getAuthor() {
        return (User) get(SouvenirDao.SOUVENIR_AUTHOR);
    }

    public void setAuthor(User author) {
        put(SouvenirDao.SOUVENIR_AUTHOR, author);
    }


    public boolean isLikedOther() {
        return getBoolean(SouvenirDao.SOUVENIR_ISLIKEOTHER);
    }

    public void setLikedOther(boolean likedOther) {
        put(SouvenirDao.SOUVENIR_ISLIKEOTHER, likedOther);
    }

    public boolean isLikedMine() {
        return getBoolean(SouvenirDao.SOUVENIR_ISLIKEME);
    }

    public void setLikedMine(boolean likedMine) {
        put(SouvenirDao.SOUVENIR_ISLIKEME, likedMine);
    }


    public String getAuthorId() {
        return getString(SouvenirDao.SOUVENIR_AUTHORID);
    }

    public void setAuthorId(String authorId) {
        put(SouvenirDao.SOUVENIR_AUTHORID, authorId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Souvenir souvenir = (Souvenir) o;

        return objectId.equals(souvenir.objectId);

    }

    @Override
    public int hashCode() {
        return objectId.hashCode();
    }


    public String getOhterUserId() {
        return getString(SouvenirDao.SOUVENIR_OTHERUSERID);
    }

    public void setOhterUserId(String ohterUserId) {
        put(SouvenirDao.SOUVENIR_OTHERUSERID, ohterUserId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.Content);
        dest.writeString(this.Picture);
        dest.writeParcelable(this.Author, flags);
        dest.writeByte(IsLikedMine ? (byte) 1 : (byte) 0);
        dest.writeString(this.objectId);
        dest.writeByte(IsLikedOther ? (byte) 1 : (byte) 0);
        dest.writeString(this.AuthorId);
        dest.writeString(this.ohterUserId);
    }

    public Souvenir(Parcel in) {
        this.Content = in.readString();
        this.Picture = in.readString();
        this.Author = in.readParcelable(User.class.getClassLoader());
        this.IsLikedMine = in.readByte() != 0;
        this.objectId = in.readString();
        this.IsLikedOther = in.readByte() != 0;
        this.AuthorId = in.readString();
        this.ohterUserId = in.readString();
    }

    public static final Creator<Souvenir> CREATOR = new Creator<Souvenir>() {
        @Override
        public Souvenir createFromParcel(Parcel source) {
            return new Souvenir(source);
        }

        @Override
        public Souvenir[] newArray(int size) {
            return new Souvenir[size];
        }
    };
}
