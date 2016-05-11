package edu.com.mvplibrary.model;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;


import java.util.HashMap;

import edu.com.mvplibrary.AbsApplication;
import edu.com.mvplibrary.model.config.Constants;
import edu.com.mvplibrary.ui.fragment.AbsBaseFragment;
import edu.com.mvplibrary.util.FileUtil2;

/**
 * Create By Anthony on 2016/1/15
 * Class Note:
 * 1 Displayer for Activity & Fragment,
 * set "type" to get corresponding activity
 * 2 create fragment using channel
 * 3 define activity & fragment in "raw" folder under "res" folder
 */
public class ViewDisplayer {
    public static final String TAG = "ViewDisplayer";
    private static HashMap<String, String> mTypeActivityNameMap = new HashMap<>();
    private static HashMap<String, String> mTypeFragmentNameMap = new HashMap<>();

    public static void initialize(Context context) {
        mTypeActivityNameMap.putAll(FileUtil2.simpleProperty2HashMap(context, Constants.BASE_TYPE_ACTIVITY_MAP_PATH));

        mTypeFragmentNameMap.putAll(FileUtil2.simpleProperty2HashMap(context, Constants.BASE_TYPE_FRAGMENT_MAP_PATH));

    }


    public static Fragment initialView(Context mContext, Channel mChannel) {
        if (mContext == null) {
            mContext = AbsApplication.app();
        }
        if (mChannel == null) {
            throw new IllegalArgumentException("initial Channel when create View(activity/fragment)");
        }
        String typeCode = mChannel.getType();
        if (typeCode.startsWith("1")) {   //show activity
            showActivity(mContext, typeCode);
            return null;
        } else if (typeCode.startsWith("2")) {  //create fragment
            return createFragment(mContext, mChannel);
        }
        return null;
    }

    public static void showActivity(Context context, String type) {
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

    public static Fragment createFragment(Context context, Channel channel) {

        String typeCode = channel.getType();
        String name = mTypeFragmentNameMap.get(typeCode);

        if (name != null && name.length() > 0) {
            try {
                Fragment fragment = null;

                // 通过配置初始化一个fragment.
                fragment = Fragment.instantiate(context, name);
                // 给fragment赋值.
                addArguments(fragment, channel);

                return fragment;
            } catch (Exception e) {
                Log.w(TAG, String.format("Instant fragment %s error %s", name, e));
            }
        }

        return null;
    }

    private static void addArguments(Fragment fragment, Channel c) {
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

    public static String getActivityName(String type) {
        return mTypeActivityNameMap.get(type);
    }

    public static String getFragmentName(String type) {
        return mTypeFragmentNameMap.get(type);
    }
}