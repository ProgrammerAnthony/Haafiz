
package edu.com.app.ui.friends.test;

import android.database.Cursor;
import android.os.Parcelable;

import com.google.auto.value.AutoValue;

import java.util.Arrays;
import java.util.Collection;

import edu.com.app.data.db.Db;
import edu.com.app.data.db.TestItem;
import edu.com.app.data.db.TestList;
import rx.functions.Func1;

@AutoValue
abstract class ListsItem implements Parcelable {
  private static String ALIAS_LIST = "list";
  private static String ALIAS_ITEM = "item";

  private static String LIST_ID = ALIAS_LIST + "." + TestList.ID;
  private static String LIST_NAME = ALIAS_LIST + "." + TestList.NAME;
  private static String ITEM_COUNT = "item_count";
  private static String ITEM_ID = ALIAS_ITEM + "." + TestItem.ID;
  private static String ITEM_LIST_ID = ALIAS_ITEM + "." + TestItem.LIST_ID;

  public static Collection<String> TABLES = Arrays.asList(TestList.TABLE, TestItem.TABLE);

  public static String QUERY_LIST = ""
      + "SELECT " + LIST_ID + ", " + LIST_NAME + ", COUNT(" + ITEM_ID + ") as " + ITEM_COUNT
      + " FROM " + TestList.TABLE + " AS " + ALIAS_LIST
      + " LEFT OUTER JOIN " + TestItem.TABLE + " AS " + ALIAS_ITEM + " ON " + LIST_ID + " = " + ITEM_LIST_ID
      + " GROUP BY " + LIST_ID;

  abstract long id();
  abstract String name();
  abstract int itemCount();

  static Func1<Cursor, ListsItem> MAPPER = new Func1<Cursor, ListsItem>() {
    @Override
    public ListsItem call(Cursor cursor) {
      long id = Db.getLong(cursor, TestList.ID);
      String name = Db.getString(cursor, TestList.NAME);
      int itemCount = Db.getInt(cursor, ITEM_COUNT);
      return new AutoValue_ListsItem(id, name, itemCount);
    }
  };
}
