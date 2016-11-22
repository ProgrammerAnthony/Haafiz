package com.anthony.inputlayout.layout;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.IdRes;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;


public abstract class BaseCustomCompositeView extends RelativeLayout implements View.OnClickListener {

    public BaseCustomCompositeView(Context context) {
        this(context, null);
    }

    public BaseCustomCompositeView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseCustomCompositeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View.inflate(context, getLayoutId(), this);
        initView();
        setListener();
        initAttrs(context, attrs);
        processLogic();
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, getAttrs());
        final int N = typedArray.getIndexCount();
        for (int i = 0; i < N; i++) {
            initAttr(typedArray.getIndex(i), typedArray);
        }
        typedArray.recycle();
    }

    protected abstract int getLayoutId();

    protected abstract void initView();

    protected abstract void setListener();

    protected int[] getAttrs() {
        return new int[0];
    }

    protected void initAttr(int attr, TypedArray typedArray) {
    }

    protected abstract void processLogic();

    @Override
    public void onClick(View view) {
    }

    /**
     * 查找View
     *
     * @param id   控件的id
     * @param <VT> View类型
     * @return
     */
    protected <VT extends View> VT getViewById(@IdRes int id) {
        return (VT) findViewById(id);
    }
}