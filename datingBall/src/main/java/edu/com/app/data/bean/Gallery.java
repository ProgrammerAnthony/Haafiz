package edu.com.app.data.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVObject;

import edu.com.app.data.dao.GalleryDao;


/**
 * Created by YX201603-6 on 2016/4/25.
 */

@AVClassName(GalleryDao.TABLENAME)
public class Gallery extends AVObject implements Parcelable {
    private String imgUrl;
    private String authorId;
    private User user;

    protected Gallery(Parcel in) {
        super(in);
        imgUrl = in.readString();
        authorId = in.readString();
        user = in.readParcelable(User.class.getClassLoader());
        height = in.readInt();
        width = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(imgUrl);
        dest.writeString(authorId);
        dest.writeParcelable(user, flags);
        dest.writeInt(height);
        dest.writeInt(width);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Gallery> CREATOR = new Creator<Gallery>() {
        @Override
        public Gallery createFromParcel(Parcel in) {
            return new Gallery(in);
        }

        @Override
        public Gallery[] newArray(int size) {
            return new Gallery[size];
        }
    };

    public int getHeight() {
        return getInt(GalleryDao.HEIGHT);
    }

    public void setHeight(int height) {
        put(GalleryDao.HEIGHT, height);
    }

    private int height;

    public int getWidth() {
        return getInt(GalleryDao.WIDTH);
    }

    public void setWidth(int width) {
        put(GalleryDao.WIDTH, width);
    }

    private int width;

    public User getUser() {
        return (User) get(GalleryDao.AUTHOR);
    }

    public void setUser(User user) {
        put(GalleryDao.AUTHOR, user);
    }


    public String getImgUrl() {
        return getString(GalleryDao.IMGURL);
    }

    public void setImgUrl(String imgUrl) {
        put(GalleryDao.IMGURL, imgUrl);

    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Gallery gallery = (Gallery) o;

        return objectId.equals(gallery.objectId);

    }

    @Override
    public int hashCode() {
        return objectId.hashCode();
    }


    public Gallery() {
    }


    public String getAuthorId() {
        return getString(GalleryDao.AUTHORID);
    }

    public void setAuthorId(String authorId) {
        put(GalleryDao.AUTHORID, authorId);
    }
}
