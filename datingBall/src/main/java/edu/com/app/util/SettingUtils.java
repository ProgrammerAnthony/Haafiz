package edu.com.app.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;



import java.io.File;
import java.lang.reflect.Field;

import edu.com.app.R;


public class SettingUtils {
    public static final String ONLY_WIFI_LOAD_IMG = "OnlyWifiLoadImg";
    public static final String CURRENT_THEME = "CurrentTheme";
    public static final String DEFAULT = "DEFAULT";

    /**
     * WIFI下加载大图
     */
    public static boolean getOnlyWifiLoadImg(Context ctx) {
        return SpUtil.getInstance(ctx).getBoolValue(ONLY_WIFI_LOAD_IMG);
    }

    public static void setOnlyWifiLoadImg(Context ctx, boolean isOn) {
        SpUtil.getInstance(ctx).setValue(ONLY_WIFI_LOAD_IMG, isOn);
    }

    /**
     * 夜间模式和设置字体
     */
    public static String getCurrentTheme(Context ctx) {
        if(TextUtils.isEmpty(SpUtil.getInstance(ctx).getStringValue(CURRENT_THEME))){
            return "TRSDayTheme_DEFAULT";
        }
        return SpUtil.getInstance(ctx).getStringValue(CURRENT_THEME);
    }

    public static void setCurrentTheme(Context ctx, String themeName) {
        SpUtil.getInstance(ctx).setValue(CURRENT_THEME, themeName);
    }



    public static int getAttrId(String attr) {
        Field f;
        try {
            f = R.attr.class.getField(attr);
            return f.getInt(null);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static String getCurrentFont(String themeName) {
        return themeName.substring(themeName.indexOf("_")+1);
    }

    public static String getCurrentFontPath(Context ctx) {
        String themeName = getCurrentTheme(ctx);
        return getCurrentFontPath(themeName);
    }

    public static String getCurrentFontPath(String themeName) {
        String fontName = getCurrentFont(themeName);
        if(fontName.equals(DEFAULT)) {
            return "";
        } else {
            return "fonts/"+ fontName + ".ttf";
        }
    }



    /**
     * 清理缓存
     */
    public static void countDirSizeTask(final Context context,final CountDirSizeListener listener) {
        new Thread() {
            public void run() {
                final long result = getDirSize(context.getCacheDir());
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

    public static void clearAppCache(final Context context,final ClearCacheListener listener) {
        new Thread() {
            public void run() {
                clearCache(context.getCacheDir());
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
        if(file == null || !file.exists()){
            return;
        }
        if(file.isDirectory()) {
            for (File child : file.listFiles()) {
                clearFile(child);
            }
        } else {
            file.delete();
        }
    }

    private static int clearFolder(File dir, long curTime) {
        int deletedFiles = 0;
        if (dir!= null && dir.isDirectory()) {
                for (File child:dir.listFiles()) {
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

    public static int getVersionCode(Context ctx){
        PackageManager packageManager = ctx.getPackageManager();
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(ctx.getPackageName(),0);
            return packInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static boolean isNeedUpdate(Context ctx){
        int local_code = convertVersionNameToInt(getVersionName(ctx));
        int remote_code = convertVersionNameToInt("2.0.0");
        return remote_code > local_code;
    }

    private static int convertVersionNameToInt(String versionName) {
        String version = versionName.replaceAll("\\.", "");
        return Integer.parseInt(version);
    }
}
