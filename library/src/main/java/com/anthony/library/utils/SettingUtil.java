package com.anthony.library.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import java.io.File;


public class SettingUtil {
    public static final String SP_ONLY_WIFI_LOAD_IMG = "OnlyWifiLoadImg";
    public static final String SP_CURRENT_FONT_PATH = "current_font_path";
    public static final String SP_CURRENT_FONT_SIZE = "current_font_size";


    private static final boolean DEFAULT_NO_IMAGE = false;
    private static final boolean DEFAULT_AUTO_SAVE = true;
    public static final String SP_NO_IMAGE = "no_image";


    public static final String SP_AUTO_CACHE = "auto_cache";
    public static final String SP_THEM_INDEX = "ThemeIndex";
    public static final String SP_NIGHT_MODE = "pNightMode";

    /**
     * WIFI下加载大图
     */
    public static boolean getOnlyWifiLoadImg(Context ctx) {
        return SpUtil.getBoolean(ctx, SP_ONLY_WIFI_LOAD_IMG, false);
    }

    public static void setOnlyWifiLoadImg(Context ctx, boolean isOn) {
        SpUtil.putBoolean(ctx, SP_ONLY_WIFI_LOAD_IMG, isOn);
    }

    /**
     * 设置字体
     */
//    public static List<Font> getFontList(Context ctx) {
//        List<Font> list = new ArrayList<>();
//        try {
//            String[] files = ctx.getResources().getAssets().list("fonts");
//            for (String name : files) {
//                Font font = new Font();
//                font.name = name.substring(0, name.lastIndexOf("."));
//                font.path = "fonts/" + name;
//                list.add(font);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return list;
//    }

    public static String getCurrentFont(Context ctx) {
        return SpUtil.getString(ctx, SP_CURRENT_FONT_PATH, "");
    }

    /**
     * 设置字号
     */
    public static int getCurrentFontSize(Context ctx) {
        return SpUtil.getInt(ctx, SP_CURRENT_FONT_SIZE, 0);
    }

    public static void setCurrentFontSize(Context ctx, int size) {
        SpUtil.putInt(ctx, SP_CURRENT_FONT_SIZE, size);
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
     * 是否需要更新
     *
     * @param ctx
     * @return
     */
    public static boolean isNeedUpdate(Context ctx) {
        int local_code = convertVersionNameToInt(AppUtils.getAppVersionName(ctx));
        int remote_code = convertVersionNameToInt("2.0.0");
        return remote_code > local_code;
    }

    private static int convertVersionNameToInt(String versionName) {
        String version = versionName.replaceAll("\\.", "");
        return Integer.parseInt(version);
    }

    /**
     * 无图模式
     * @param context
     * @return
     */
    public static boolean getNoImageState(Context context) {
        return SpUtil.getBoolean(context, SP_NO_IMAGE);
    }

    public static void setNoImageState(Context context, boolean state) {
        SpUtil.putBoolean(context, SP_NO_IMAGE, DEFAULT_NO_IMAGE);
    }

    /**
     * 自动缓存
     * @param context
     * @return
     */
    public static boolean getAutoCacheState(Context context) {
        return SpUtil.getBoolean(context, SP_AUTO_CACHE);
    }

    public static void setAutoCacheState(Context context,boolean state) {
        SpUtil.putBoolean(context, SP_AUTO_CACHE, DEFAULT_AUTO_SAVE);
    }

    //不同主题
    public static int getThemeIndex(Context context) {
        return SpUtil.getInt(context,SP_THEM_INDEX);
    }

    public static void setThemeIndex(Context context, int index) {
        SpUtil.putInt(context,SP_THEM_INDEX,index);
    }

    //主题模式（日间，夜间）
    public static boolean getNightModel(Context context) {
        return SpUtil.getBoolean(context,SP_NIGHT_MODE);

    }

    public static void setNightModel(Context context, boolean nightModel) {
        SpUtil.putBoolean(context,SP_NIGHT_MODE,nightModel);
    }


}
