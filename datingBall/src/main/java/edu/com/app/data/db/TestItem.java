package edu.com.app.data.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcelable;

import com.google.auto.value.AutoValue;

import rx.functions.Func1;

/**
 * Created by Anthony on 2016/7/21.
 * Class Note:
 * {@link AutoValue} used here
 * builder pattern ,check in{ @see https://github.com/google/auto/blob/master/value/userguide/builders.md}
 * use of {@link AutoValue} ,check in { @see https://github.com/hehonghui/android-tech-frontier/blob/master/issue-49/%E6%B7%B1%E5%85%A5%E7%A0%94%E7%A9%B6AutoValue.md}
 */

@AutoValue
public abstract class TestItem implements Parcelable {
    public static final String TABLE = "test_item";

    public static final String ID = "_id";
    public static final String LIST_ID = "test_list_id";
    public static final String DESCRIPTION = "description";
    public static final String COMPLETE = "complete";


    public abstract long id();
    public abstract long listId();
    public abstract String description();
    public abstract boolean complete();


    /**
     *Func1 represents a function with one argument,Cursor here
     *Func1 will return one param,TestItem here
     */
    public static final Func1<Cursor, TestItem> MAPPER = new Func1<Cursor, TestItem>() {
        @Override public TestItem call(Cursor cursor) {
            long id = Db.getLong(cursor, ID);
            long listId = Db.getLong(cursor, LIST_ID);
            String description = Db.getString(cursor, DESCRIPTION);
            boolean complete = Db.getBoolean(cursor, COMPLETE);
            return new AutoValue_TestItem(id, listId, description, complete);
        }
    };

    public static final class Builder {
        private final ContentValues values = new ContentValues();

        public Builder id(long id) {
            values.put(ID, id);
            return this;
        }

        public Builder listId(long listId) {
            values.put(LIST_ID, listId);
            return this;
        }

        public Builder description(String description) {
            values.put(DESCRIPTION, description);
            return this;
        }

        public Builder complete(boolean complete) {
            values.put(COMPLETE, complete);
            return this;
        }

        public ContentValues build() {
            return values; // TODO defensive copy?
        }
    }
}