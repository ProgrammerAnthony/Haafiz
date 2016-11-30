package com.anthony.app.common.widgets;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.anthony.app.common.base.AbsBaseFragment;
import com.anthony.app.common.Constants;
import com.anthony.app.common.data.bean.Channel;
import com.anthony.app.common.utils.FileUtil;
import com.anthony.app.common.widgets.webview.WebViewCommentActivity;

import java.io.Serializable;
import java.util.HashMap;


/**
 * Create By Anthony on 2016/1/15
 * Class Note:
 * use this class to directly create activity & create fragment
 * <p/>
 * 1 get view(fragment/activity) using the name we set in the map
 * 2 map （xxx.properties） file restore in  res/raw Folder
 * <p/>
 * 使用当前类直接创建activity或者fragment
 * <p/>
 * 1 根据不同的Map对应关系，解析出对应的View（Fragment或者Activity）
 * <p/>
 * 2 在res/raw文件夹下保存map（xxx.properties） 文件
 */

public class ViewDisplay {
    public final String TAG = "ViewDisplay";
    private HashMap<String, String> mTypeActivityNameMap = new HashMap<>();
    private HashMap<String, String> mTypeFragmentNameMap = new HashMap<>();


    public ViewDisplay(Context context) {
        mTypeActivityNameMap.putAll(FileUtil.simpleProperty2HashMap(context, Constants.BASE_TYPE_ACTIVITY_MAP_PATH));
        mTypeFragmentNameMap.putAll(FileUtil.simpleProperty2HashMap(context, Constants.BASE_TYPE_FRAGMENT_MAP_PATH));
    }

    /**
     * show activity
     *
     * @param context
     * @param type    type/name of the activity
     */
    public void showActivity(Context context, String type) {
        String activityName = mTypeActivityNameMap.get(type);
        if (activityName != null) {
            Intent intent = new Intent();
            intent.setClassName(context, activityName);
            if (!(context instanceof Activity)) {
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            context.startActivity(intent);
        }
    }
    public void showActivityWithBundle(Context context,String type,Serializable item){
        String activityName = mTypeActivityNameMap.get(type);
        if (activityName != null) {
            Intent intent = new Intent();
            intent.setClassName(context, activityName);
            if (!(context instanceof Activity)) {
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            if(context instanceof WebViewCommentActivity){
                intent.putExtra(WebViewCommentActivity.WEB_VIEW_ITEM, item);
            }
            context.startActivity(intent);
        }
    }

    /**
     * create fragment
     *
     * @param context
     * @param type      type/name of the fragment
     * @param arguments fragment arguments
     * @return fragment created
     */
    public Fragment createFragment(Context context, String type, Bundle arguments) {
        String fragmentName = mTypeFragmentNameMap.get(type);
        Fragment fragment = null;
        if (fragmentName != null && fragmentName.length() > 0) {
            fragment = Fragment.instantiate(context, fragmentName);
            if (arguments != null) {
                fragment.setArguments(arguments);
            }
        }
        return fragment;
    }

    /**
     * create fragment with channel
     *
     * @param context
     * @param channelInfo channel data
     * @param typeName    name to define fragment
     * @return fragment created
     */
    public Fragment createFragment(Context context, Channel channelInfo, String typeName) {

//        String typeCode =Integer.toString(channel.type());
        String name = mTypeFragmentNameMap.get(typeName);
        Fragment fragment = null;
        if (name != null && name.length() > 0) {
            try {


                // 通过配置初始化一个fragment.
                fragment = Fragment.instantiate(context, name);
                // 给fragment赋值.
                addArguments(fragment, channelInfo);

                return fragment;
            } catch (Exception e) {
                Log.w(TAG, String.format("Instant fragment %s error %s", name, e));
            }
        }

        return null;
    }

    /**
     * add arguments for fragment,used in method {@link #createFragment(Context, Channel, String)}
     *
     * @param fragment
     * @param c
     */
    private void addArguments(Fragment fragment, Channel c) {
        if (fragment == null) {
            return;
        }
        if (fragment instanceof AbsBaseFragment) {
            Bundle bundle = fragment.getArguments();
            bundle = bundle == null ? new Bundle() : bundle;
            bundle.putString(AbsBaseFragment.EXTRA_URL, c.getUrl());
            fragment.setArguments(bundle);
        }

    }

    /**
     * get activity full name
     *
     * @param type type/name of the activity
     * @return full name of the activity
     */
    public String getActivityName(String type) {
        return mTypeActivityNameMap.get(type);
    }

    /**
     * get fragment full name
     *
     * @param type type/name of the fragment
     * @return full name of the fragment
     */
    public String getFragmentName(String type) {
        return mTypeFragmentNameMap.get(type);
    }
}