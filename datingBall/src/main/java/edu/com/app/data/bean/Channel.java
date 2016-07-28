package edu.com.app.data.bean;

/**
 * Created by Anthony on 2016/5/4.
 * Class Note:
 * channel data is subComponent of Menu
 */

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcelable;

import com.google.auto.value.AutoValue;

import edu.com.app.data.db.Db;
import rx.functions.Func1;

@AutoValue
public abstract class Channel implements Parcelable {
    public static final String TABLE = "channel_table";

    public static final String ID = "_id";

//    public static final String MENU_ID = "m_id";//foreign key : menu id

    public static final String TITLE = "title";
    public static final String TYPE = "type";
    public static final String URL = "url";
    public static final String IS_FIX = "is_fix";
    public static final String IS_SUBSCRIBE = "is_subscribe";
//    public static final String SORT = "sort";
//    public static final String LRT = "lrt";
//    public static final String IMG = "img";



    //create channel
    public static final String CREATE_CHANNEL =""
            + "CREATE TABLE " + Channel.TABLE + "("
            + Channel.ID + " INTEGER NOT NULL PRIMARY KEY,"
            + Channel.TITLE + " TEXT NOT NULL,"
            + Channel.TYPE + " TEXT NOT NULL,"
            + Channel.URL + " TEXT NOT NULL,"
            + Channel.IS_FIX + " INTEGER NOT NULL DEFAULT 0,"
            + Channel.IS_SUBSCRIBE + " INTEGER NOT NULL DEFAULT 0"
            + ")";
    
    public static final String QUERY_CHANNEL_LIST="SELECT * FROM "
            + Channel.TABLE;
//    public abstract long id();

//    public abstract long menu_id();

    public abstract String title();

//    @SerializedName(value = "type", alternate = {"channelType"})
    public abstract int type();

//    @SerializedName(value = "url", alternate = {"link"})
    public abstract String url();

    public abstract int isFix();

    public abstract int isSubscribe();

//    public abstract long sort();

//    public abstract long lrt();

//    @SerializedName(value = "image", alternate = {"ic", "icon", "picture", "pic", "img"})
//    public abstract String img();


    /**
     * get from Db using cursor, and return Channel
     * this is used when you save to Db before save to Channel class
     */
    public static final Func1<Cursor, Channel> MAPPER = new Func1<Cursor, Channel>() {
        @Override
        public Channel call(Cursor cursor) {
//            long id = Db.getLong(cursor, ID);
//            long menuId = Db.getLong(cursor, MENU_ID);

            String title = Db.getString(cursor, TITLE);
            int type = Db.getInt(cursor, TYPE);
            String url = Db.getString(cursor, URL);
            int is_fix = Db.getInt(cursor, IS_FIX);
            int is_subscribe = Db.getInt(cursor, IS_SUBSCRIBE);
//            long sort = Db.getLong(cursor, SORT);
//            long lrt = Db.getLong(cursor, LRT);
//            String img = Db.getString(cursor, IMG);
            return create(  title, type, url, is_fix, is_subscribe);
//            return new AutoValue_Channel(id,menuId,title,type,url,is_fix,is_subscribe);
        }
    };

   public static Channel create( String title, int type, String url, int is_fix, int is_subscribe) {
        return new AutoValue_Channel( title, type, url, is_fix, is_subscribe);
    }



    public static final class Builder {
        private final ContentValues values = new ContentValues();

//        public Builder id(long id) {
//            values.put(ID, id);
//            return this;
//        }

//        public Builder menuId(long c_id) {
//            values.put(MENU_ID, c_id);
//            return this;
//        }

        public Builder title(String title) {
            values.put(TITLE, title);
            return this;
        }

        public Builder type(int type) {
            values.put(TYPE, type);
            return this;
        }

        public Builder url(String url) {
            values.put(URL, url);
            return this;
        }

        public Builder isFix(int isFix) {
            values.put(IS_FIX, isFix);
            return this;
        }

        public Builder isSubscribe(int isSubsribe) {
            values.put(IS_SUBSCRIBE, isSubsribe);
            return this;
        }

//        public Builder sort(long sort) {
//            values.put(SORT, sort);
//            return this;
//        }
//
//        public Builder lrt(long lrt) {
//            values.put(LRT, lrt);
//            return this;
//        }
//
//        public Builder img(String img) {
//            values.put(IMG, img);
//            return this;
//        }

        public ContentValues build() {
            return values;
        }

    }


}

