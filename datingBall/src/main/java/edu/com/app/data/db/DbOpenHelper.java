package edu.com.app.data.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Anthony on 2016/7/21.
 * Class Note:
 * A normal implement of {@link SQLiteOpenHelper} ,
 * with initialization of some test data
 *
 */
public class DbOpenHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    public DbOpenHelper(Context context) {
        super(context, "sqlbrite.db", null /* factory */, VERSION);
    }
    private static final String CREATE_LIST = ""
            + "CREATE TABLE " + TestList.TABLE + "("
            + TestList.ID + " INTEGER NOT NULL PRIMARY KEY,"
            + TestList.NAME + " TEXT NOT NULL,"
            + TestList.ARCHIVED + " INTEGER NOT NULL DEFAULT 0"
            + ")";

    private static final String CREATE_ITEM = ""
            + "CREATE TABLE " + TestItem.TABLE + "("
            + TestItem.ID + " INTEGER NOT NULL PRIMARY KEY,"
            + TestItem.LIST_ID + " INTEGER NOT NULL REFERENCES " + TestList.TABLE + "(" + TestList.ID + "),"
            + TestItem.DESCRIPTION + " TEXT NOT NULL,"
            + TestItem.COMPLETE + " INTEGER NOT NULL DEFAULT 0"
            + ")";

    private static final String CREATE_ITEM_LIST_ID_INDEX =
            "CREATE INDEX item_list_id ON " + TestItem.TABLE + " (" + TestItem.LIST_ID + ")";



    @Override public void onCreate(SQLiteDatabase db) {
        //insert some test data here
        db.execSQL(CREATE_LIST);
        db.execSQL(CREATE_ITEM);
        db.execSQL(CREATE_ITEM_LIST_ID_INDEX);
//list 1
        long groceryListId = db.insert(TestList.TABLE, null, new TestList.Builder()
                .name("Friend Group 1 ")
                .build());
        db.insert(TestItem.TABLE, null, new TestItem.Builder()
                .listId(groceryListId)
                .description("1A")
                .build());
        db.insert(TestItem.TABLE, null, new TestItem.Builder()
                .listId(groceryListId)
                .description("1B")
                .build());
        db.insert(TestItem.TABLE, null, new TestItem.Builder()
                .listId(groceryListId)
                .description("1C")
                .build());

//list 2
        long holidayPresentsListId = db.insert(TestList.TABLE, null, new TestList.Builder()
                .name("Friend Group 2")
                .build());
        db.insert(TestItem.TABLE, null, new TestItem.Builder()
                .listId(holidayPresentsListId)
                .description("2A")
                .build());
        db.insert(TestItem.TABLE, null, new TestItem.Builder()
                .listId(holidayPresentsListId)
                .description("2B")
                .build());
        db.insert(TestItem.TABLE, null, new TestItem.Builder()
                .listId(holidayPresentsListId)
                .description("2C")
                .build());
        db.insert(TestItem.TABLE, null, new TestItem.Builder()
                .listId(holidayPresentsListId)
                .description("2D")
                .build());

    }

    @Override public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}

