package com.anthony.app.common.data.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.anthony.app.common.data.bean.NewsItem;
import com.anthony.app.common.injection.scope.ApplicationContext;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;
import rx.schedulers.Schedulers;


/**
 * Database helper class used to manage the creation and upgrading of your database.
 * This class also usually provide the DAOs used by the other classes.
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {
    private Context mContext;


    // name of the database file for your application -- change to something appropriate for your app
    private static final String DATABASE_NAME = "dbtest.db";
    // any time you make changes to your database objects, you may have to increase the database version
    private static final int DATABASE_VERSION = 1;
    // the DAO object we use to access the NewsItem table
    private Dao<NewsItem, Integer> simpleDao = null;

    /**
     * Returns the Database Access Object (DAO) for our NewsItem class. I
     * t will create it or just give the cached value.
     */
    public Dao<NewsItem, Integer> getNewsItemDao() throws SQLException {
        if (simpleDao == null) {
            simpleDao = getDao(NewsItem.class);
        }
        return simpleDao;
    }


    @Inject
    public DatabaseHelper(@ApplicationContext Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, NewsItem.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
//        try {
//            String[] tb = mContext.getResources().getStringArray(R.array.db_tb);
//            for (int i = 0; i < tb.length; i++) {
//                Class clazz = Class.forName(tb[i]);
//                TableUtils.createTable(connectionSource, clazz);
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource,
                          int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, NewsItem.class, true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
//        try {
//            String[] tb = mContext.getResources().getStringArray(R.array.db_tb);
//            for (int i = 0; i < tb.length; i++) {
//                Class clazz = Class.forName(tb[i]);
//                TableUtils.dropTable(connectionSource, clazz, true);
//            }
//            onCreate(database, connectionSource);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
    }

    /**
     * 释放资源
     */
    @Override
    public void close() {
        super.close();

//        instance = null;
        mContext = null;
    }


    /**
     * 导出数据库
     */
    public void exportDb() {
        Observable.just(DATABASE_NAME)
                .map(new Func1<String, File>() {
                    @Override
                    public File call(String dbName) {
                        File dbFile = mContext.getDatabasePath(dbName);
                        if (!dbFile.exists()) {
                            throw new RuntimeException("no file");
                        } else
                            return dbFile;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new Subscriber<File>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("Export DB", "Export DB Error: " + e.getMessage());
//                        Toast.makeText(mContext, "导出数据库失败 " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(File file) {
                        Log.e("Export DB", "Export DB Success: " + file.getPath());
//                        Toast.makeText(mContext, "导出数据成功 " + file.getPath(), Toast.LENGTH_SHORT).show();
                        InputStream fis = null;
                        OutputStream fos = null;
                        try {
                            fis = new FileInputStream(file);
                            fos = new FileOutputStream(new File(mContext.getCacheDir().getAbsolutePath() + "/" + file.getName()));
                            byte[] buf = new byte[1024];
                            int length;
                            while ((length = fis.read(buf)) != -1) {
                                fos.write(buf, 0, length);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            try {
                                if (fis != null)
                                    fis.close();
                                if (fos != null)
                                    fos.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
    }

    public void importDb() {

    }

//    private static DatabaseHelper instance;
//    /**
//     * 单例获取该Helper
//     * @param context
//     * @return
//     */
//    public static synchronized DatabaseHelper getHelper(Context context, String dbName) {
//        context = context.getApplicationContext();
//        if (instance == null) {
//            synchronized (DatabaseHelper.class) {
//                if (instance == null)
//                    instance = new DatabaseHelper(context, dbName);
//            }
//        }
//
//        return instance;
//    }
}
