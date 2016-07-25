package edu.com.app.data.bean;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcelable;

import com.google.auto.value.AutoValue;
import com.google.gson.annotations.SerializedName;

import edu.com.app.data.db.Db;
import rx.functions.Func1;

/**
 * Created by Anthony on 2016/7/13.
 * Class Note:
 * menu data
 */

@AutoValue
public abstract class Menu implements Parcelable {

    public static final String TABLE = "menu_table";

    public static final String ID = "_id";

    public static final String TYPE = "type";

    public abstract long id();

    @SerializedName(value = "type", alternate = {"menuType"})
    public abstract String type();

    public static final Func1<Cursor, Menu> MAPPER = new Func1<Cursor, Menu>() {
        @Override
        public Menu call(Cursor cursor) {
            long id = Db.getLong(cursor, ID);
            String type = Db.getString(cursor, TYPE);
            return new AutoValue_Menu(id, type);
        }
    };


    public static final class Builder {
        private final ContentValues values = new ContentValues();

        public Builder id(long id) {
            values.put(ID, id);
            return this;
        }

        public Builder type(String type ){
            values.put(TYPE,type);
            return this;
        }

        public ContentValues build() {
            return values;
        }
    }


}

