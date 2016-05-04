package com.chanven.commonpulltorefresh;

import android.content.Context;
import android.util.AttributeSet;
import com.chanven.commonpulltorefresh.header.VWHeader;

public class PtrClassicFrameLayout extends PtrFrameLayout {

    private PtrClassicDefaultHeader mPtrClassicHeader;
    public VWHeader vwHeader;

    public PtrClassicFrameLayout(Context context) {
        super(context);
        initViews();
    }

    public PtrClassicFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews();
    }

    public PtrClassicFrameLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initViews();
    }

    private void initViews() {
//        mPtrClassicHeader = new PtrClassicDefaultHeader(getContext());
//        setHeaderView(mPtrClassicHeader);
//        addPtrUIHandler(mPtrClassicHeader);

//        MaterialHeader mMaterialHeader = new MaterialHeader(getContext());
//        setHeaderView(mMaterialHeader);
//        addPtrUIHandler(mMaterialHeader);

        vwHeader = new VWHeader(getContext());
        setHeaderView(vwHeader);
        addPtrUIHandler(vwHeader);
    }

    public PtrClassicDefaultHeader getHeader() {
        return mPtrClassicHeader;
    }

    /**
     * Specify the last update time by this key string
     *
     * @param key
     */
    public void setLastUpdateTimeKey(String key) {
        if (mPtrClassicHeader != null) {
            mPtrClassicHeader.setLastUpdateTimeKey(key);
        }
    }

    /**
     * Using an object to specify the last update time.
     *
     * @param object
     */
    public void setLastUpdateTimeRelateObject(Object object) {
        if (mPtrClassicHeader != null) {
            mPtrClassicHeader.setLastUpdateTimeRelateObject(object);
        }
    }
}
