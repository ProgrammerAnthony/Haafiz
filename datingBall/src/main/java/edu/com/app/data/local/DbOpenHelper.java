package edu.com.app.data.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import javax.inject.Inject;

import edu.com.app.injection.scope.ApplicationContext;


public class DbOpenHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "friends.db";
    public static final int DATABASE_VERSION = 2;

    @Inject
    public DbOpenHelper(@ApplicationContext Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        //Uncomment line below if you want to enable foreign keys
        //db.execSQL("PRAGMA foreign_keys=ON;");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.beginTransaction();
        try {
            db.execSQL(Db.FriendsProfileTable.CREATE);
            //Add other tables here
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

}