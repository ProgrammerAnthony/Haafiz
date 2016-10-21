package com.anthony.app.common.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.inputmethod.InputMethodManager;

import com.anthony.app.common.base.Constants;
import com.anthony.app.common.data.bean.NewsItem;
import com.anthony.app.common.widgets.webview.WebViewCommentActivity;

import java.io.File;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by Anthony on 2016/1/25.
 * Class Note:SwipeBackUtils class  for common operation of app
 */
public class AppUtils {

    private AppUtils() {
        throw new AssertionError();
    }

    /**
     * whether this process is named with processName
     *
     * @param context
     * @param processName
     * @return <ul>
     * return whether this process is named with processName
     * <li>if context is null, return false</li>
     * <li>if {@link ActivityManager#getRunningAppProcesses()} is null, return false</li>
     * <li>if one process of {@link ActivityManager#getRunningAppProcesses()} is equal to processName, return
     * true, otherwise return false</li>
     * </ul>
     */
    public static boolean isNamedProcess(Context context, String processName) {
        if (context == null) {
            return false;
        }

        int pid = android.os.Process.myPid();
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningAppProcessInfo> processInfoList = manager.getRunningAppProcesses();
        if (ListUtils.isEmpty(processInfoList)) {
            return false;
        }

        for (RunningAppProcessInfo processInfo : processInfoList) {
            if (processInfo != null && processInfo.pid == pid
                    && ObjectUtils.isEquals(processName, processInfo.processName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * whether application is in background
     * <ul>
     * <li>need use permission android.permission.GET_TASKS in Manifest.xml</li>
     * </ul>
     *
     * @param context
     * @return if application is in background return true, otherwise return false
     */
    public static boolean isApplicationInBackground(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> taskList = am.getRunningTasks(1);
        if (taskList != null && !taskList.isEmpty()) {
            ComponentName topActivity = taskList.get(0).topActivity;
            if (topActivity != null && !topActivity.getPackageName().equals(context.getPackageName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * get screen size width in pixels
     *
     * @param context
     * @return the pixels of the screen width
     */
    public static int getWidth(Activity context) {
        DisplayMetrics dm = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    public static String getUrlFileName(String url) {
        return url.substring(url.lastIndexOf("/") + 1);
    }

    public static String getUrlPath(String url) {
        return url.substring(0, url.lastIndexOf("/") + 1);
    }

    /**
     * get screen size height in pixels
     *
     * @param context
     * @return the pixels of the screen height
     */
    public static int getHeight(Activity context) {
        DisplayMetrics dm = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }


    /**
     * convert dip to px
     *
     * @param context
     * @param dpValue
     * @return
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * convert px to dip
     *
     * @param context
     * @param pxValue
     * @return
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * convert px to sp
     *
     * @param context
     * @param pxValue
     * @return
     */
    public static int px2sp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * convert sp to px
     *
     * @param context
     * @param spValue
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (spValue * scale + 0.5f);
    }

    /**
     * get the package information of the context
     *
     * @param context
     * @return
     */
    public static PackageInfo getPackageInfo(Context context) {
        PackageInfo info = null;
        try {
            info = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace(System.err);
        }
        if (info == null)
            info = new PackageInfo();
        return info;
    }

    /**
     * whether the external storage (SDCard) exists
     *
     * @return
     */
    public static boolean isStorageExists() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return true;
        }
        return false;
    }

    /**
     * hide keyboard
     *
     * @param activity
     */
    public static void hidekeyboard(Activity activity) {
        try {
            ((InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * whether the network is connected
     *
     * @param context
     * @return
     */
    public static boolean isNetworkConnected(Context context) {
        if (null == context) {
            return false;
        }
        // 获取手机所有连接管理对象（包括wifi，net等连接的管理）
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            // 获取网络连接管理的对象
            NetworkInfo info = connectivityManager.getActiveNetworkInfo();
            if (info != null && info.isConnected()) {
                // 判断当前网络是否已经连接
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 没有网络
     */
    public static final int NETWORKTYPE_INVALID = 0;
    /**
     * wap网络
     */
    public static final int NETWORKTYPE_WAP = 1;
    /**
     * 2G网络
     */
    public static final int NETWORKTYPE_2G = 2;
    /**
     * 3G和3G以上网络，或统称为快速网络
     */
    public static final int NETWORKTYPE_3G = 3;
    /**
     * wifi网络
     */
    public static final int NETWORKTYPE_WIFI = 4;

    /**
     * get the network type (wifi,wap,2g,3g)
     *
     * @param context
     * @return
     */
    public static int getNetWorkType(Context context) {

        int mNetWorkType = NETWORKTYPE_INVALID;
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            String type = networkInfo.getTypeName();
            if (type.equalsIgnoreCase("WIFI")) {
                mNetWorkType = NETWORKTYPE_WIFI;
            } else if (type.equalsIgnoreCase("MOBILE")) {
                String proxyHost = android.net.Proxy.getDefaultHost();
                mNetWorkType = TextUtils.isEmpty(proxyHost)
                        ? (isFastMobileNetwork(context) ? NETWORKTYPE_3G : NETWORKTYPE_2G)
                        : NETWORKTYPE_WAP;
            }
        } else {
            mNetWorkType = NETWORKTYPE_INVALID;
        }
        return mNetWorkType;
    }

    private static boolean isFastMobileNetwork(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        switch (telephonyManager.getNetworkType()) {
            case TelephonyManager.NETWORK_TYPE_1xRTT:
                return false; // ~ 50-100 kbps
            case TelephonyManager.NETWORK_TYPE_CDMA:
                return false; // ~ 14-64 kbps
            case TelephonyManager.NETWORK_TYPE_EDGE:
                return false; // ~ 50-100 kbps
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
                return true; // ~ 400-1000 kbps
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
                return true; // ~ 600-1400 kbps
            case TelephonyManager.NETWORK_TYPE_GPRS:
                return false; // ~ 100 kbps
            case TelephonyManager.NETWORK_TYPE_HSDPA:
                return true; // ~ 2-14 Mbps
            case TelephonyManager.NETWORK_TYPE_HSPA:
                return true; // ~ 700-1700 kbps
            case TelephonyManager.NETWORK_TYPE_HSUPA:
                return true; // ~ 1-23 Mbps
            case TelephonyManager.NETWORK_TYPE_UMTS:
                return true; // ~ 400-7000 kbps
            case TelephonyManager.NETWORK_TYPE_EHRPD:
                return true; // ~ 1-2 Mbps
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
                return true; // ~ 5 Mbps
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                return true; // ~ 10-20 Mbps
            case TelephonyManager.NETWORK_TYPE_IDEN:
                return false; // ~25 kbps
            case TelephonyManager.NETWORK_TYPE_LTE:
                return true; // ~ 10+ Mbps
            case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                return false;
            default:
                return false;
        }
    }

    /**
     * show dialog to choose whether go to the view of network setting
     *
     * @param context
     */
    public static void setNetworkMethod(final Context context) {
        // 提示对话框
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        try {
            builder.setTitle("网络设置提示")
                    .setMessage("网络连接不可用,是否进行设置?")
                    .setPositiveButton("设置",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    Intent intent = null;
                                    // 判断手机系统的版本 即API大于10 就是3.0或以上版本
                                    if (android.os.Build.VERSION.SDK_INT > 10) {
                                        intent = new Intent(
                                                android.provider.Settings.ACTION_WIRELESS_SETTINGS);
                                    } else {
                                        intent = new Intent();
                                        ComponentName component = new ComponentName(
                                                "com.android.settings",
                                                "com.android.settings.WirelessSettings");
                                        intent.setComponent(component);
                                        intent.setAction("android.intent.action.VIEW");
                                    }
                                    context.startActivity(intent);
                                }
                            })
                    .setNegativeButton("取消",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    dialog.dismiss();
                                }
                            }).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * return if str is empty
     *
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        if (str == null || str.length() == 0 || str.equalsIgnoreCase("null") || str.isEmpty() || str.equals("")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * get format date
     *
     * @param timemillis
     * @return
     */
    public static String getFormatDate(long timemillis) {
        return new SimpleDateFormat("yyyy年MM月dd日").format(new Date(timemillis));
    }

    /**
     * decode Unicode string
     *
     * @param s
     * @return
     */
    public static String decodeUnicodeStr(String s) {
        StringBuilder sb = new StringBuilder(s.length());
        char[] chars = s.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            if (c == '\\' && chars[i + 1] == 'u') {
                char cc = 0;
                for (int j = 0; j < 4; j++) {
                    char ch = Character.toLowerCase(chars[i + 2 + j]);
                    if ('0' <= ch && ch <= '9' || 'a' <= ch && ch <= 'f') {
                        cc |= (Character.digit(ch, 16) << (3 - j) * 4);
                    } else {
                        cc = 0;
                        break;
                    }
                }
                if (cc > 0) {
                    i += 5;
                    sb.append(cc);
                    continue;
                }
            }
            sb.append(c);
        }
        return sb.toString();
    }

    /**
     * encode Unicode string
     *
     * @param s
     * @return
     */
    public static String encodeUnicodeStr(String s) {
        StringBuilder sb = new StringBuilder(s.length() * 3);
        for (char c : s.toCharArray()) {
            if (c < 256) {
                sb.append(c);
            } else {
                sb.append("\\u");
                sb.append(Character.forDigit((c >>> 12) & 0xf, 16));
                sb.append(Character.forDigit((c >>> 8) & 0xf, 16));
                sb.append(Character.forDigit((c >>> 4) & 0xf, 16));
                sb.append(Character.forDigit((c) & 0xf, 16));
            }
        }
        return sb.toString();
    }

    /**
     * convert time str
     *
     * @param time
     * @return
     */
    public static String convertTime(int time) {

        time /= 1000;
        int minute = time / 60;
        int second = time % 60;
        minute %= 60;
        return String.format("%02d:%02d", minute, second);
    }

    /**
     * url is usable
     *
     * @param url
     * @return
     */
    public static boolean isUrlUsable(String url) {
        if (AppUtils.isEmpty(url)) {
            return false;
        }

        URL urlTemp = null;
        HttpURLConnection connt = null;
        try {
            urlTemp = new URL(url);
            connt = (HttpURLConnection) urlTemp.openConnection();
            connt.setRequestMethod("HEAD");
            int returnCode = connt.getResponseCode();
            if (returnCode == HttpURLConnection.HTTP_OK) {
                return true;
            }
        } catch (Exception e) {
            return false;
        } finally {
            connt.disconnect();
        }
        return false;
    }

    /**
     * is url
     *
     * @param url
     * @return
     */
    public static boolean isUrl(String url) {
        Pattern pattern = Pattern.compile("^([hH][tT]{2}[pP]://|[hH][tT]{2}[pP][sS]://)(([A-Za-z0-9-~]+).)+([A-Za-z0-9-~\\/])+$");
        return pattern.matcher(url).matches();
    }

    /**
     * get toolbar height
     *
     * @param context
     * @return
     */
    public static int getToolbarHeight(Context context) {
        final TypedArray styledAttributes = context.getTheme().obtainStyledAttributes(
                new int[]{android.R.attr.actionBarSize});
        int toolbarHeight = (int) styledAttributes.getDimension(0, 0);
        styledAttributes.recycle();

        return toolbarHeight;
    }

    /**
     * W020160906622900105417.png W开头图片转换规则
     * http://m.gzgov.gov.cn/webpic/+文件名前八位+/+文件名前十位+/文件名
     * http://m.gzgov.gov.cn/webpic/W0201609/W020160906/W020160906622900105417.png
     *
     * @param originalStr
     * @return
     */
    public static String convertPicStartWithW(String originalStr) {
        StringBuilder newStr = new StringBuilder();
        newStr.append(Constants.Remote_BASE_END_POINT + "webpic/" + originalStr.substring(0, 8) + "/" + originalStr.substring(0, 10) + "/" + originalStr);
        return newStr.toString();
    }

    /**
     * 加载网页，每个网页都需要NewsItem对象
     *
     * @param context
     * @param newsItem
     */
    public static void loadWebViewActivity(Context context, NewsItem newsItem) {
        Intent intent = new Intent(context, WebViewCommentActivity.class);
        intent.putExtra(WebViewCommentActivity.WEB_VIEW_ITEM, newsItem);
        context.startActivity(intent);
    }

    /**
     * install APK File from folder
     * @param context
     * @param apkFullFileName  apk path+apk name
     */
    public static void installApk(Context context,String apkFullFileName) {
//        String saveFileName = Environment.getExternalStorageDirectory().getAbsolutePath()
//                + "/Download/" + downloadFileName;
        File apkFile = new File(apkFullFileName);
        if (!apkFile.exists()) {
            return;
        }
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);

    }
}
