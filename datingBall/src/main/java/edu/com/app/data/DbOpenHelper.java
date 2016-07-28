package edu.com.app.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import edu.com.app.data.bean.Channel;
import edu.com.app.data.db.TestData;
import edu.com.app.data.db.TestItem;
import edu.com.app.data.db.TestList;

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







    @Override public void onCreate(SQLiteDatabase db) {
        //insert some test data here
        db.execSQL(CREATE_LIST);
        db.execSQL(CREATE_ITEM);
        db.execSQL(CREATE_ITEM_LIST_ID_INDEX);


        db.execSQL(Channel.CREATE_CHANNEL);



        //add data for test!
        TestData testData =new TestData(db);
        testData.initTestList();

    }

    @Override public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}

