package com.anthony.app.common.data.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

import com.anthony.app.R;
import com.anthony.app.common.base.Constants;
import com.anthony.app.common.utils.SpUtil;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;
import rx.schedulers.Schedulers;


public class DatabaseHelper extends OrmLiteSqliteOpenHelper {
    private Context mContext;

    public DatabaseHelper(Context context, String dbName) {
        super(context, dbName, null, 1);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            String[] tb = mContext.getResources().getStringArray(R.array.db_tb);
            for (int i = 0; i < tb.length; i++) {
                Class clazz = Class.forName(tb[i]);
                TableUtils.createTable(connectionSource, clazz);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource,
                          int oldVersion, int newVersion) {
        try {
            String[] tb = mContext.getResources().getStringArray(R.array.db_tb);
            for (int i = 0; i < tb.length; i++) {
                Class clazz = Class.forName(tb[i]);
                TableUtils.dropTable(connectionSource, clazz, true);
            }
            onCreate(database, connectionSource);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
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

    public void exportDb() {
        Observable.just(SpUtil.getString(mContext, Constants.CURRENT_USER))
                .map(new Func1<String, String>() {
                    @Override
                    public String call(String usrName) {
                        if (TextUtils.isEmpty(usrName)) {
                            throw new RuntimeException("null name");
                        } else
                            return usrName;
                    }
                })
                .map(new Func1<String, File>() {
                    @Override
                    public File call(String dbName) {
                        File dbFile = mContext.getDatabasePath(dbName + ".db");
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
                    public void onCompleted() {}

                    @Override
                    public void onError(Throwable e) {
                        Log.e("Export DB", "Export DB Error: " + e.getMessage());
                    }

                    @Override
                    public void onNext(File file) {
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
