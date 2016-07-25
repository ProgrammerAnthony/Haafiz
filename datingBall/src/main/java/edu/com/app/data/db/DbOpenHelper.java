package edu.com.app.data.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import edu.com.app.data.bean.Channel;
import edu.com.app.data.bean.Menu;

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
            + TestList.NAME + " TEXT NOT NULL"
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

   //create menu data
    private static final String CREATE_MENU_LIST=""
            + "CREATE TABLE " + Menu.TABLE + "("
            + Menu.ID + " INTEGER NOT NULL PRIMARY KEY,"
            + Menu.TYPE + " TEXT NOT NULL"
            + ")";

    //create channel, notice! channel is the item of menu
    private static final String CREATE_CHANNEL =""
            + "CREATE TABLE " + Channel.TABLE + "("
            + Channel.ID + " INTEGER NOT NULL PRIMARY KEY,"
            + Channel.MENU_ID + " INTEGER NOT NULL REFERENCES " + Menu.TABLE + "(" + Menu.ID + "),"
            + Channel.TITLE + " TEXT NOT NULL,"
            + Channel.TYPE + " TEXT NOT NULL,"
            + Channel.URL + " TEXT NOT NULL,"
            + Channel.IS_FIX + " INTEGER NOT NULL DEFAULT 0,"
            + Channel.IS_SUBSCRIBE + " INTEGER NOT NULL DEFAULT 0,"
            + Channel.SORT + " INTEGER NOT NULL DEFAULT 0,"
            + Channel.LRT + " INTEGER NOT NULL DEFAULT 0,"
            + Channel.IMG + " TEXT NOT NULL"
            + ")";

    //create index(SQL CREATE UNIQUE INDEX  can be used here)
    private static final String CREATE_ITEM_MENU_ID_INDEX =
            "CREATE INDEX item_menu_id ON " + Channel.TABLE + " (" + Channel.MENU_ID + ")";


    @Override public void onCreate(SQLiteDatabase db) {
        //insert some test data here
        db.execSQL(CREATE_LIST);
        db.execSQL(CREATE_ITEM);
        db.execSQL(CREATE_ITEM_LIST_ID_INDEX);

        db.execSQL(CREATE_MENU_LIST);
        db.execSQL(CREATE_CHANNEL);
        db.execSQL(CREATE_ITEM_MENU_ID_INDEX);


        //add data for test!
        TestData testData =new TestData(db);
        testData.initTestList();
        testData.initTestMenu();

    }

    @Override public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}

