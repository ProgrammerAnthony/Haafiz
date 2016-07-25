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
import com.google.gson.annotations.SerializedName;

import edu.com.app.data.db.Db;
import rx.functions.Func1;

@AutoValue
public abstract class Channel implements Parcelable {
    public static final String TABLE = "channel_table";

    public static final String ID = "_id";

    public static final String MENU_ID = "m_id";//foreign key : menu id

    public static final String TITLE = "title";
    public static final String TYPE = "type";
    public static final String URL = "url";
    public static final String IS_FIX = "is_fix";
    public static final String IS_SUBSCRIBE = "is_subscribe";
    public static final String SORT = "sort";
    public static final String LRT = "lrt";
    public static final String IMG = "img";

    public abstract long id();

    public abstract long menu_id();

    public abstract String title();

    @SerializedName(value = "type", alternate = {"channelType"})
    public abstract String type();

    @SerializedName(value = "url", alternate = {"link"})
    public abstract String url();

    public abstract int isFix();

    public abstract int isSubscribe();

    public abstract long sort();

    public abstract long lrt();

    @SerializedName(value = "image", alternate = {"ic", "icon", "picture", "pic", "img"})
    public abstract String img();


    public static final Func1<Cursor, Channel> MAPPER = new Func1<Cursor, Channel>() {
        @Override
        public Channel call(Cursor cursor) {
            long id = Db.getLong(cursor, ID);
            long menuId = Db.getLong(cursor, MENU_ID);

            String title = Db.getString(cursor, TITLE);
            String type = Db.getString(cursor, TYPE);
            String url = Db.getString(cursor, URL);
            int is_fix = Db.getInt(cursor, IS_FIX);
            int is_subscribe = Db.getInt(cursor, IS_SUBSCRIBE);
            long sort = Db.getLong(cursor, SORT);
            long lrt = Db.getLong(cursor, LRT);
            String img =Db.getString(cursor,IMG);
            return new AutoValue_Channel(id,menuId,title,type,url,is_fix,is_subscribe,sort,lrt,img);
        }
    };


    public static final class Builder {
        private final ContentValues values = new ContentValues();

        public Builder id(long id) {
            values.put(ID,id);
            return this;
        }
        public Builder menuId(long c_id){
            values.put(MENU_ID,c_id);
            return this;
        }

        public Builder title(String title){
            values.put(TITLE,title);
            return  this;
        }

        public Builder type(String type){
            values.put(TYPE,type);
            return this;
        }
        public Builder url(String url){
            values.put(URL,url);
            return this;
        }

        public Builder isFix(int isFix){
            values.put(IS_FIX,isFix);
            return this;
        }

        public Builder isSubscribe(int isSubsribe){
            values.put(IS_SUBSCRIBE,isSubsribe);
            return this;
        }

        public Builder sort(long sort){
            values.put(SORT,sort);
            return this;
        }

        public Builder lrt(long lrt){
            values.put(LRT,lrt);
            return this;
        }
        public Builder img(String img){
            values.put(IMG,img);
            return this;
        }

        public ContentValues build() {
            return values;
        }

    }


}

