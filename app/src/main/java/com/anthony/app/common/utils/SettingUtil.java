package com.anthony.app.common.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Looper;

import com.anthony.app.common.base.Constants;
import com.anthony.app.common.data.bean.Font;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vincent Woo
 * Date: 2016/1/5
 * Time: 11:23
 */
public class SettingUtil {

//    @Inject
//    MyApplication mApplication;

    /**
     * WIFI下加载大图
     */
    public static boolean getOnlyWifiLoadImg(Context ctx) {
        return SpUtil.getBoolean(ctx, Constants.ONLY_WIFI_LOAD_IMG, false);
    }

    public static void setOnlyWifiLoadImg(Context ctx, boolean isOn) {
        SpUtil.putBoolean(ctx, Constants.ONLY_WIFI_LOAD_IMG, isOn);
    }

    /**
     * 设置字体
     */
    public static List<Font> getFontList(Context ctx) {
        List<Font> list = new ArrayList<>();
        try {
            String[] files = ctx.getResources().getAssets().list("fonts");
            for (String name : files) {
                Font font = new Font();
                font.name = name.substring(0, name.lastIndexOf("."));
                font.path = "fonts/" + name;
                list.add(font);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static String getCurrentFont(Context ctx) {
        return SpUtil.getString(ctx, Constants.CURRENT_FONT_PATH, "");
    }

    /**
     * 设置字号
     */
    public static int getCurrentFontSize(Context ctx) {
        return SpUtil.getInt(ctx, Constants.CURRENT_FONT_SIZE, 0);
    }

    public static void setCurrentFontSize(Context ctx, int size) {
        SpUtil.putInt(ctx, Constants.CURRENT_FONT_SIZE, size);
    }

//    public static int getThemeId(String theme) {
//        Field f;
//        try {
//            f = R.style.class.getField(theme);
//            return f.getInt(null);
//        } catch (NoSuchFieldException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        } catch (IllegalArgumentException e) {
//            e.printStackTrace();
//        }
//        return 0;
//    }
//
//    public static int getAttrId(String attr) {
//        Field f;
//        try {
//            f = R.attr.class.getField(attr);
//            return f.getInt(null);
//        } catch (NoSuchFieldException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        } catch (IllegalArgumentException e) {
//            e.printStackTrace();
//        }
//        return 0;
//    }

    /**
     * 清理缓存
     */
    public static void countDirSizeTask(final File dir, final CountDirSizeListener listener) {
        new Thread() {
            public void run() {
                final long result = getDirSize(dir);
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        listener.onResult(result);
                    }
                });
            }
        }.start();
    }

    public static void clearAppCache(final File dir, final ClearCacheListener listener) {
        new Thread() {
            public void run() {
                clearCache(dir);
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        listener.onResult();
                    }
                });
            }
        }.start();
    }

    public interface ClearCacheListener {
        void onResult();
    }

    public interface CountDirSizeListener {
        void onResult(long result);
    }

    private static long getDirSize(File dir) {
        if (dir == null) {
            return 0;
        }
        if (!dir.isDirectory()) {
            return 0;
        }
        long dirSize = 0;
        File[] files = dir.listFiles();
        for (File file : files) {
            if (file.isFile()) {
                dirSize += file.length();
            } else if (file.isDirectory()) {
                dirSize += file.length();
                dirSize += getDirSize(file); // 递归调用继续统计
            }
        }
        return dirSize;
    }

    public static String formatFileSize(long fileS) {
        java.text.DecimalFormat df = new java.text.DecimalFormat("#.00");
        String fileSizeString = "";
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "KB";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "MB";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "G";
        }

        if (fileSizeString.startsWith(".")) {
            return "0B";
        }
        return fileSizeString;
    }

    private static void clearCache(File file) {
        clearFile(file);
    }

    private static void clearFile(File file) {
        if (file == null || !file.exists()) {
            return;
        }
        if (file.isDirectory()) {
            for (File child : file.listFiles()) {
                clearFile(child);
            }
        } else {
            file.delete();
        }
    }

    private static int clearFolder(File dir, long curTime) {
        int deletedFiles = 0;
        if (dir != null && dir.isDirectory()) {
            for (File child : dir.listFiles()) {
                if (child.isDirectory()) {
                    deletedFiles += clearFolder(child, curTime);
                }
                if (child.lastModified() < curTime) {
                    if (child.delete()) {
                        deletedFiles++;
                    }
                }
            }
        }
        return deletedFiles;
    }

    /**
     * 版本管理
     */
    public static String getVersionName(Context ctx) {
        PackageManager packageManager = ctx.getPackageManager();
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(ctx.getPackageName(), 0);
            return packInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static int getVersionCode(Context ctx) {
        PackageManager packageManager = ctx.getPackageManager();
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(ctx.getPackageName(), 0);
            return packInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static boolean isNeedUpdate(Context ctx) {
        int local_code = convertVersionNameToInt(getVersionName(ctx));
        int remote_code = convertVersionNameToInt("2.0.0");
        return remote_code > local_code;
    }

    private static int convertVersionNameToInt(String versionName) {
        String version = versionName.replaceAll("\\.", "");
        return Integer.parseInt(version);
    }
}
