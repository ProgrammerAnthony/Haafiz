package edu.com.app.data.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcelable;

import com.google.auto.value.AutoValue;

import java.util.ArrayList;
import java.util.List;

import rx.functions.Func1;

/**
 * Created by Anthony on 2016/7/21.
 * Class Note:
 * {@link AutoValue} used here
 * builder pattern ,check in{ @see https://github.com/google/auto/blob/master/value/userguide/builders.md}
 * use of {@link AutoValue} ,check in { @see https://github.com/hehonghui/android-tech-frontier/blob/master/issue-49/%E6%B7%B1%E5%85%A5%E7%A0%94%E7%A9%B6AutoValue.md}
 */
@AutoValue
public abstract class TestList implements Parcelable {
    public static final String TABLE = "test_list";

    public static final String ID = "_id";
    public static final String NAME = "name";
    public static final String ARCHIVED = "archived";

    public abstract long id();
    public abstract String name();
    public abstract boolean archived();

    /**
     *Func1 represents a function with one argument,cursor here
     *Func1 will return one param,List<TestList> here
     */
    public static Func1<Cursor, List<TestList>> MAP = new Func1<Cursor, List<TestList>>() {
        @Override public List<TestList> call(final Cursor cursor) {
            try {
                List<TestList> values = new ArrayList<>(cursor.getCount());

                while (cursor.moveToNext()) {
                    long id = Db.getLong(cursor, ID);
                    String name = Db.getString(cursor, NAME);
                    boolean archived = Db.getBoolean(cursor, ARCHIVED);
                    values.add(new AutoValue_TestList(id, name, archived));
                }
                return values;
            } finally {
                cursor.close();
            }
        }
    };

    public static final class Builder {
        private final ContentValues values = new ContentValues();

        public Builder id(long id) {
            values.put(ID, id);
            return this;
        }

        public Builder name(String name) {
            values.put(NAME, name);
            return this;
        }

        public Builder archived(boolean archived) {
            values.put(ARCHIVED, archived);
            return this;
        }

        public ContentValues build() {
            return values; // TODO defensive copy?
        }
    }
}
