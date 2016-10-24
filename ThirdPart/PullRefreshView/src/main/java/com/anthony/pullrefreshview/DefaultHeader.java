package com.anthony.pullrefreshview;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.pnikosis.materialishprogress.ProgressWheel;


/**
 * Created by Anthony on 2016/7/18.
 */
public class DefaultHeader extends BaseIndicator {
    private TextView mStringIndicator;
    private ProgressWheel progress_wheel;
    private int default_rim_color;

    @Override
    public View createView(LayoutInflater inflater, ViewGroup parent) {
        ViewGroup v = (ViewGroup) inflater.inflate(R.layout.prj_ptr_header_default, parent, true);
        View child = v.getChildAt(v.getChildCount() - 1);
        mStringIndicator = (TextView) child.findViewById(R.id.tv_header);
        progress_wheel = (ProgressWheel) v.findViewById(R.id.progress_wheel);
        default_rim_color = progress_wheel.getRimColor();
        return child;
    }

    @Override
    public void onAction() {
        mStringIndicator.setText("放开以刷新");
    }

    @Override
    public void onUnaction() {
        mStringIndicator.setText("下拉以刷新");
    }

    @Override
    public void onRestore() {
        mStringIndicator.setText("下拉以刷新");
        progress_wheel.setRimColor(default_rim_color);
        progress_wheel.stopSpinning();
    }

    @Override
    public void onLoading() {
        mStringIndicator.setText("加载中...");
        progress_wheel.setRimColor(Color.parseColor("#00000000"));
        progress_wheel.spin();
    }
}
