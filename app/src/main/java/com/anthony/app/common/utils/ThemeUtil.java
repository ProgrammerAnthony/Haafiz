package com.anthony.app.common.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;

import com.anthony.app.R;


public class ThemeUtil {
    public static int[][] themeArr = {
            {R.style.AppThemeLight_Red, R.style.AppThemeDark_Red},
            {R.style.AppThemeLight_Pink, R.style.AppThemeDark_Pink},
            {R.style.AppThemeLight_Purple, R.style.AppThemeDark_Purple},
            {R.style.AppThemeLight_DeepPurple, R.style.AppThemeDark_DeepPurple},
            {R.style.AppThemeLight_Indigo, R.style.AppThemeDark_Indigo},
            {R.style.AppThemeLight_Blue, R.style.AppThemeDark_Blue},
            {R.style.AppThemeLight_LightBlue, R.style.AppThemeDark_LightBlue},
            {R.style.AppThemeLight_Cyan, R.style.AppThemeDark_Cyan},
            {R.style.AppThemeLight_Teal, R.style.AppThemeDark_Teal},
            {R.style.AppThemeLight_Green, R.style.AppThemeDark_Green},
            {R.style.AppThemeLight_LightGreen, R.style.AppThemeDark_LightGreen},
            {R.style.AppThemeLight_Lime, R.style.AppThemeDark_Lime},
            {R.style.AppThemeLight_Yellow, R.style.AppThemeDark_Yellow},
            {R.style.AppThemeLight_Amber, R.style.AppThemeDark_Amber},
            {R.style.AppThemeLight_Orange, R.style.AppThemeDark_Orange},
            {R.style.AppThemeLight_DeepOrange, R.style.AppThemeDark_DeepOrange},
            {R.style.AppThemeLight_Brown, R.style.AppThemeDark_Brown},
            {R.style.AppThemeLight_Grey, R.style.AppThemeDark_Grey},
            {R.style.AppThemeLight_BlueGrey, R.style.AppThemeDark_BlueGrey}
    };

    public static int[][] themeColorArr = {
            {R.color.md_red_500, R.color.md_red_700}, {R.color.md_pink_500, R.color.md_pink_700},
            {R.color.md_purple_500, R.color.md_purple_700},
            {R.color.md_deep_purple_500, R.color.md_deep_purple_700},
            {R.color.md_indigo_500, R.color.md_indigo_700},
            {R.color.md_blue_500, R.color.md_blue_700},
            {R.color.md_light_blue_500, R.color.md_light_blue_700},
            {R.color.md_cyan_500, R.color.md_cyan_700}, {R.color.md_teal_500, R.color.md_teal_500},
            {R.color.md_green_500, R.color.md_green_500},
            {R.color.md_light_green_500, R.color.md_light_green_500},
            {R.color.md_lime_500, R.color.md_lime_700},
            {R.color.md_yellow_500, R.color.md_yellow_700},
            {R.color.md_amber_500, R.color.md_amber_700},
            {R.color.md_orange_500, R.color.md_orange_700},
            {R.color.md_deep_orange_500, R.color.md_deep_orange_700},
            {R.color.md_brown_500, R.color.md_brown_700}, {R.color.md_grey_500, R.color.md_grey_700},
            {R.color.md_blue_grey_500, R.color.md_blue_grey_700}
    };

    public static int getTheme(Context mContext) {
        return mContext.getResources()
                .getColor(themeColorArr[SettingUtil.getThemeIndex(mContext)][0]);
    }

    public static int getThemeColor(@NonNull Context context) {
        return getThemeAttrColor(context, R.attr.colorPrimary);
    }

    public static int getThemeAttrColor(@NonNull Context context, @AttrRes int attr) {
        TypedArray a = context.obtainStyledAttributes(null, new int[]{attr});
        try {
            return a.getColor(0, 0);
        } finally {
            a.recycle();
        }
    }
}
