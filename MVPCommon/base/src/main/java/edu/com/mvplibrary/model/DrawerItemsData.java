package edu.com.mvplibrary.model;

import android.content.Context;
import android.content.res.TypedArray;

import java.util.ArrayList;

import edu.com.mvplibrary.R;
import edu.com.mvplibrary.contract.DrawerMainContract;

/**
 * Created by Anthony on 2016/5/3.
 * Class Note:
 * drawer list item data.
 *
 */
public class DrawerItemsData {
    private Context mContext;
    public ArrayList<DrawerItem> mDrawerItems;
    private String[] mMenuTitles;
    private TypedArray mMenuIconsTypeArray;
    private TypedArray mMenuIconTintTypeArray;
    private DrawerMainContract.onGetDrawerListListener mListener;


    public DrawerItemsData(Context context, DrawerMainContract.onGetDrawerListListener listener) {
        this.mContext = context;
        this.mListener =listener;
        mDrawerItems=new ArrayList<>();
    }

    public void initItemsData() {
        mMenuTitles = mContext.getResources().getStringArray(R.array.nav_drawer_items);
        // nav drawer icons from resources
        mMenuIconsTypeArray = mContext.getResources()
                .obtainTypedArray(R.array.nav_drawer_icons);
        mMenuIconTintTypeArray = mContext.getResources()
                .obtainTypedArray(R.array.nav_drawer_tint);

        for (int i = 0; i < mMenuTitles.length; i++) {
            mDrawerItems.add(new DrawerItem(mMenuTitles[i], mMenuIconsTypeArray
                    .getResourceId(i, -1), mMenuIconTintTypeArray.getResourceId(i, -1)));
        }
        mMenuIconsTypeArray.recycle();

        if(mDrawerItems.size()==mMenuTitles.length){
            mListener.onSuccess();
        }else{
            mListener.onError();
        }
    }

    public class DrawerItem {

        private String title;
        private int icon;
        //图片颜色
        private int tint;
        private String count = "0";
        // boolean to set visiblity of the counter
        private boolean isCounterVisible = false;

        public DrawerItem(){}

        public DrawerItem(String title, int icon, int tint){
            this.title = title;
            this.icon = icon;
            this.tint = tint;
        }

        public DrawerItem(String title, int icon, boolean isCounterVisible, String count){
            this.title = title;
            this.icon = icon;
            this.isCounterVisible = isCounterVisible;
            this.count = count;
        }

        public String getTitle(){
            return this.title;
        }

        public int getIcon(){
            return this.icon;
        }

        public String getCount(){
            return this.count;
        }

        public boolean getCounterVisibility(){
            return this.isCounterVisible;
        }

        public void setTitle(String title){
            this.title = title;
        }

        public void setIcon(int icon){
            this.icon = icon;
        }

        public void setCount(String count){
            this.count = count;
        }

        public int getTint() {
            return tint;
        }

        public void setTint(int tint) {
            this.tint = tint;
        }

        public void setCounterVisibility(boolean isCounterVisible){
            this.isCounterVisible = isCounterVisible;
        }
    }
}
