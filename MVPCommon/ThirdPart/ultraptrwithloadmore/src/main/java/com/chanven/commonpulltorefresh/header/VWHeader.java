package com.chanven.commonpulltorefresh.header;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.chanven.commonpulltorefresh.PtrFrameLayout;
import com.chanven.commonpulltorefresh.PtrUIHandler;
import com.chanven.commonpulltorefresh.indicator.PtrIndicator;
import com.chanven.commonpulltorefreshview.R;
import com.pnikosis.materialishprogress.ProgressWheel;
import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Vincent Woo
 * Date: 2016/1/20
 * Time: 16:16
 */
public class VWHeader extends FrameLayout implements PtrUIHandler {
    private View mHeaderView;
    private TextView mTxtIndicator;
    private TextView mTxtUpdateTime;
    private ProgressWheel progress_wheel;
    private int default_rim_color;

    public VWHeader(Context context) {
        super(context);
        initViews();
    }

    public VWHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews();
    }

    public VWHeader(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initViews();
    }

    protected void initViews() {
        mHeaderView = LayoutInflater.from(getContext()).inflate(R.layout.vw_header, this);
        mTxtIndicator = (TextView) mHeaderView.findViewById(R.id.txt_refresh_indicator);
        mTxtUpdateTime = (TextView) mHeaderView.findViewById(R.id.txt_update_time);
        progress_wheel = (ProgressWheel) mHeaderView.findViewById(R.id.progress_wheel);
        default_rim_color = progress_wheel.getRimColor();
    }

    @Override
    public void onUIReset(PtrFrameLayout frame) {

    }

    @Override
    public void onUIRefreshPrepare(PtrFrameLayout frame) {
        mTxtIndicator.setVisibility(VISIBLE);
        mTxtUpdateTime.setVisibility(VISIBLE);
        String time = getLastUpdateTime();
        if(!TextUtils.isEmpty(time)) {
            mTxtUpdateTime.setText(time);
        }

        if (frame.isPullToRefresh()) {
            mTxtIndicator.setText(R.string.cube_ptr_pull_down_to_refresh);
        } else {
            mTxtIndicator.setText(R.string.cube_ptr_pull_down);
        }
    }

    @Override
    public void onUIRefreshBegin(PtrFrameLayout frame) {
        mTxtIndicator.setVisibility(VISIBLE);
        mTxtIndicator.setText(R.string.cube_ptr_refreshing);
        progress_wheel.setRimColor(Color.parseColor("#00000000"));
        progress_wheel.spin();
    }

    @Override
    public void onUIRefreshComplete(PtrFrameLayout frame) {
        mTxtIndicator.setVisibility(VISIBLE);
        mTxtIndicator.setText(R.string.cube_ptr_refresh_complete);
        progress_wheel.setRimColor(default_rim_color);
        progress_wheel.stopSpinning();

        SharedPreferences sharedPreferences = getContext().getSharedPreferences(KEY_SharedPreferences, 0);
        if (!TextUtils.isEmpty(mLastUpdateTimeKey)) {
            mLastUpdateTime = new Date().getTime();
            sharedPreferences.edit().putLong(mLastUpdateTimeKey, mLastUpdateTime).commit();
        }
    }

    @Override
    public void onUIPositionChange(PtrFrameLayout frame, boolean isUnderTouch, byte status, PtrIndicator ptrIndicator) {
        final int mOffsetToRefresh = frame.getOffsetToRefresh();
        final int currentPos = ptrIndicator.getCurrentPosY();
        final int lastPos = ptrIndicator.getLastPosY();

        if (currentPos < mOffsetToRefresh && lastPos >= mOffsetToRefresh) {
            if (isUnderTouch && status == PtrFrameLayout.PTR_STATUS_PREPARE) {
                crossRotateLineFromBottomUnderTouch(frame);
            }
        } else if (currentPos > mOffsetToRefresh && lastPos <= mOffsetToRefresh) {
            if (isUnderTouch && status == PtrFrameLayout.PTR_STATUS_PREPARE) {
                crossRotateLineFromTopUnderTouch(frame);
            }
        }
    }

    private void crossRotateLineFromTopUnderTouch(PtrFrameLayout frame) {
        if (!frame.isPullToRefresh()) {
            mTxtIndicator.setVisibility(VISIBLE);
            mTxtIndicator.setText(R.string.cube_ptr_release_to_refresh);
        }
    }

    private void crossRotateLineFromBottomUnderTouch(PtrFrameLayout frame) {
        mTxtIndicator.setVisibility(VISIBLE);
        if (frame.isPullToRefresh()) {
            mTxtIndicator.setText(R.string.cube_ptr_pull_down_to_refresh);
        } else {
            mTxtIndicator.setText(R.string.cube_ptr_pull_down);
        }
    }

    public View getHeaderView() {
        return mHeaderView;
    }

    public TextView getTxtIndicator() {
        return mTxtIndicator;
    }

    public TextView getTxtUpdateTime() {
        return mTxtUpdateTime;
    }

    private long mLastUpdateTime = -1;
    private String mLastUpdateTimeKey = "UpdateTime";
    private final static String KEY_SharedPreferences = "cube_ptr_classic_last_update";
    private static SimpleDateFormat sDataFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private String getLastUpdateTime() {
        if (mLastUpdateTime == -1 && !TextUtils.isEmpty(mLastUpdateTimeKey)) {
            mLastUpdateTime = getContext().getSharedPreferences(KEY_SharedPreferences, 0).getLong(mLastUpdateTimeKey, -1);
        }
        if (mLastUpdateTime == -1) {
            return null;
        }
        long diffTime = new Date().getTime() - mLastUpdateTime;
        int seconds = (int) (diffTime / 1000);
        if (diffTime < 0) {
            return null;
        }
        if (seconds <= 0) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
//        sb.append(getContext().getString(R.string.cube_ptr_last_update));

        if (seconds < 60) {
//            sb.append(seconds + getContext().getString(R.string.cube_ptr_seconds_ago));
            sb.append(getContext().getString(R.string.cube_ptr_just_now));
        } else {
            int minutes = (seconds / 60);
            if (minutes > 60) {
                int hours = minutes / 60;
                if (hours > 24) {
                    Date date = new Date(mLastUpdateTime);
                    sb.append(sDataFormat.format(date));
                } else {
                    sb.append(hours + getContext().getString(R.string.cube_ptr_hours_ago));
                }

            } else {
                sb.append(minutes + getContext().getString(R.string.cube_ptr_minutes_ago));
            }
        }
        sb.append(getContext().getString(R.string.cube_ptr_last_update));
        return sb.toString();
    }
}
