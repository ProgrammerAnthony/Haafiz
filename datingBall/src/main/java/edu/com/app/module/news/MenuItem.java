package edu.com.app.module.news;

import android.database.Cursor;
import android.os.Parcelable;

import com.google.auto.value.AutoValue;

import java.util.Arrays;
import java.util.Collection;

import edu.com.app.data.bean.Channel;
import edu.com.app.data.bean.Menu;
import edu.com.app.data.db.Db;
import rx.functions.Func1;
import timber.log.Timber;

/**
 * Created by Anthony on 2016/7/25.
 * Class Note:
 *
 */
@AutoValue
public abstract class MenuItem implements Parcelable {
    public static Collection<String> TABLES = Arrays.asList(Menu.TABLE, Channel.TABLE);


    private static String ALIAS_MENU = "menu";
    private static String ALIAS_ITEM = "item";//menu item is channel!

    private static String MENU_ID = ALIAS_MENU + "." + Menu.ID;
    private static String MENU_TYPE = ALIAS_MENU + "." + Menu.TYPE;
    private static String ITEM_COUNT = "item_count";
    private static String ITEM_ID = ALIAS_ITEM + "." + Channel.ID;
    private static String ITEM_MENU_ID = ALIAS_ITEM + "." + Channel.MENU_ID;


    public static String QUERY_LIST = ""
            + "SELECT " + MENU_ID + ", " + MENU_TYPE + ", COUNT(" + ITEM_ID + ") as " + ITEM_COUNT
            + " FROM " + Menu.TABLE + " AS " + ALIAS_MENU
            + " LEFT OUTER JOIN " + Channel.TABLE + " AS " + ALIAS_ITEM + " ON " + MENU_ID + " = " + ITEM_MENU_ID
            + " GROUP BY " + MENU_ID;

    public abstract long id();

    public abstract String name();

    public abstract int itemCount();

    static Func1<Cursor, MenuItem> MAPPER = new Func1<Cursor, MenuItem>() {
        @Override
        public MenuItem call(Cursor cursor) {
            long id = Db.getLong(cursor, Menu.ID);
            String name = Db.getString(cursor, Menu.TYPE);
            int itemCount = Db.getInt(cursor, ITEM_COUNT);
            Timber.i("id "+id+" name "+name+" itemCount "+itemCount);
            return new AutoValue_MenuItem(id,name,itemCount);
        }
    };
}
