package com.anthony.app.common.widgets.pullrefresh;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.widget.OverScroller;

import com.anthony.app.R;


/**
 * Created by Anthony on 2016/7/18.
 * 实现对子view 的上拉和下拉的监听实现，提供下拉和下拉的视图和接口
 *
 */
public class PullToRefreshView extends ViewGroup{

    private LayoutInflater mInflater;
    private OverScroller mScroller;
    private OnRefreshListener mListener;

    /**
     * 头部View
     */
    private View header;
    private BaseIndicator mHeaderIndicator;
    private String mHeaderIndicatorClassName;

    /**
     * 尾部View
     */
    private View footer;
    private BaseIndicator mFooterIndicator;
    private String mFooterIndicatorClassName;

    /**
     * 内容View
     */
    private View contentView;

    private int mHeaderActionPosition;
    private int mFooterActionPosition;
    private int mHeaderHoldingPosition;
    private int mFooterHoldingPosition;

    private boolean isPullDownEnable = true;
    private boolean isPullUpEnable = true;

    private float mLastX;
    private float mLastY;
    private float deltaX = 0;
    private float deltaY = 0;

    private int IDLE = 0;
    private int PULL_DOWN = 1;
    private int PULL_UP = 2;
    private int AUTO_SCROLL_PULL_DOWN = 3;  //自动刷新时的状态
    private int mStatus = IDLE;

    private boolean isLoading = false;
    private long mStartLoadingTime;

    public PullToRefreshView(Context context) {
        super(context);
    }

    public PullToRefreshView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public PullToRefreshView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mInflater = LayoutInflater.from(context);
        mScroller = new OverScroller(context);

        //获取自定义属性
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.PullToRefresh);
        if (ta.hasValue(R.styleable.PullToRefresh_header_indicator)) {
            mHeaderIndicatorClassName = ta.getString(R.styleable.PullToRefresh_header_indicator);
        }
        if (ta.hasValue(R.styleable.PullToRefresh_footer_indicator)) {
            mFooterIndicatorClassName = ta.getString(R.styleable.PullToRefresh_footer_indicator);
        }
        ta.recycle();
    }

    @Override
    protected void onFinishInflate() {
        if (getChildCount() != 1) {
            throw new RuntimeException("The child of VIPullToRefresh should be only one!!!");
        }

        contentView = getChildAt(0);
        setPadding(0, 0, 0, 0);
        contentView.setPadding(0, 0, 0, 0);

        mHeaderIndicator = getIndicator(mHeaderIndicatorClassName);
        if (mHeaderIndicator == null) {
            mHeaderIndicator = new DefaultHeader();
        }
        header = mHeaderIndicator.createView(mInflater, this);

        mFooterIndicator = getIndicator(mFooterIndicatorClassName);
        if (mFooterIndicator == null) {
            mFooterIndicator = new DefaultFooter();
        }
        footer = mFooterIndicator.createView(mInflater, this);

        contentView.bringToFront();

        super.onFinishInflate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (getChildCount() > 0) {
            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                measureChild(child, widthMeasureSpec, heightMeasureSpec);
            }
        }

        mHeaderActionPosition = header.getMeasuredHeight() / 3 + header.getMeasuredHeight();
        mFooterActionPosition = footer.getMeasuredHeight() / 3 + footer.getMeasuredHeight();
        mHeaderHoldingPosition = header.getMeasuredHeight();
        mFooterHoldingPosition = footer.getMeasuredHeight();

        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (contentView != null) {
            if (header != null) {
                header.layout(0, -header.getMeasuredHeight(), getWidth(), 0);
            }
            if (footer != null) {
                footer.layout(0, getHeight(), getWidth(), getHeight() + footer.getMeasuredHeight());
            }

//            if (header != null) {
//                header.layout(0, 0, getWidth(), header.getMeasuredHeight());
//            }
//            if (footer != null) {
//                footer.layout(0, getHeight() - footer.getMeasuredHeight(), getWidth(), getHeight());
//            }
            contentView.layout(0, 0, contentView.getMeasuredWidth(), contentView.getMeasuredHeight());
        }
    }

    private boolean isInControl = false;
    private boolean isNeedIntercept;

    private boolean isNeedIntercept() {
        if (deltaY > 0 && isContentScrollToTop() || getScrollY() < 0 - 10) {
            mStatus = PULL_DOWN;
            return true;
        }

        if (deltaY < 0 && isContentScrollToBottom() || getScrollY() > 0 + 10) {
            mStatus = PULL_UP;
            return true;
        }

        return false;
    }

    /**
     * dispatchTouchEvent主要用于记录触摸事件的初始状态
     * 因为这里是所有触摸事件的入口函数所有事件都会经过这里
     * <p>
     * 因此如果你需要观测整个事件序列从开始到最后，无论它是否被本ViewGroup拦截，那么请在这个函数中进行
     * <p>
     * 为什么不在onInterceptTouchEvent中观测??
     * 因为在onInterceptTouchEvent中进行记录可能漏掉一些事件
     * 例如：当布局内部控件在onTouchEvent函数中对DOWN返回true，那么后续的MOVE事件和UP事件就可能不会传入onInterceptTouchEvent函数中
     * 再比如：当本ViewGroup自身对第一个MOVE事件进行拦截即onInterceptTouchEvent返回true时后续的MOVE和UP事件都不会传入onInterceptTouchEvent函数中
     * 而是直接进入onTouchEvent中去
     */
    private VelocityTracker mVelocityTracker;
    private float yVelocity;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        dealMultiTouch(ev);
        final int action = MotionEventCompat.getActionMasked(ev);
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (mVelocityTracker == null) {
                    mVelocityTracker = VelocityTracker.obtain();
                } else {
                    mVelocityTracker.clear();
                }
                mVelocityTracker.addMovement(ev);
                break;
            case MotionEvent.ACTION_MOVE:
                mVelocityTracker.addMovement(ev);
                mVelocityTracker.computeCurrentVelocity(500);
                yVelocity = mVelocityTracker.getYVelocity();

                isNeedIntercept = isNeedIntercept();
                if (isNeedIntercept && !isInControl) {
                    //把内部控件的事件转发给本控件处理
                    isInControl = true;
                    ev.setAction(MotionEvent.ACTION_CANCEL);
                    MotionEvent ev2 = MotionEvent.obtain(ev);
                    dispatchTouchEvent(ev);
                    ev2.setAction(MotionEvent.ACTION_DOWN);
                    return dispatchTouchEvent(ev2);
                }
                break;
            case MotionEvent.ACTION_UP:
                /**
                 * 为什么将本ViewGroup自动返回初始位置的触发函数放在dispatchTouchEvent中?
                 * 由于下面onTouchEvent代码中ACTION_MOVE时有一段对本ViewGroup当前事件控制权转移给内部控件的代码
                 * 因此这会使得最后的Up event不会到本ViewGroup中的onTouchEvent中去
                 * 所以只能将autoBackToOriginalPosition()前移到dispatchTouchEvent()中来
                 */
                autoBackToPosition();
                isNeedIntercept = false;
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return isNeedIntercept;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        final int action = MotionEventCompat.getActionMasked(ev);

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                return true;
            case MotionEvent.ACTION_MOVE:
                if (!isPullDownEnable && deltaY > 0 && getScrollY() <= 0) {
                    break;
                }

                if (!isPullUpEnable && deltaY < 0 && getScrollY() >= 0) {
                    break;
                }

                if (isNeedIntercept) {
                    if (mStatus == PULL_DOWN && getScrollY() > 0) {
                        break;
                    }
                    if (mStatus == PULL_UP && getScrollY() < 0) {
                        break;
                    }
                    scrollBy(0, (int) (-getMoveFloat(yVelocity, deltaY)));

                    updateIndicator();
                } else {
                    ev.setAction(MotionEvent.ACTION_DOWN);
                    dispatchTouchEvent(ev);
                    isInControl = false;
                }
                break;
            case MotionEvent.ACTION_UP:
                autoBackToPosition();
                return true;
        }

        return true;
    }

    /**
     * 处理多点触控的情况
     * 记录手指按下和移动时的各种相关数值
     * mActivePointerId为有效手指的ID，后续所有移动数值均来自这个手指
     * 当前active的手指只有一个且为后按下的那个
     */
    private int mActivePointerId = MotionEvent.INVALID_POINTER_ID;

    public void dealMultiTouch(MotionEvent ev) {
        final int action = MotionEventCompat.getActionMasked(ev);
        switch (action) {
            case MotionEvent.ACTION_DOWN: {
                final int pointerIndex = MotionEventCompat.getActionIndex(ev);
                final float x = MotionEventCompat.getX(ev, pointerIndex);
                final float y = MotionEventCompat.getY(ev, pointerIndex);
                mLastX = x;
                mLastY = y;
                mActivePointerId = MotionEventCompat.getPointerId(ev, 0);
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                final int pointerIndex = MotionEventCompat.findPointerIndex(ev, mActivePointerId);
                final float x = MotionEventCompat.getX(ev, pointerIndex);
                final float y = MotionEventCompat.getY(ev, pointerIndex);
                //下拉时deltaY为正，上拉时deltaY为负
                deltaX = x - mLastX;
                deltaY = y - mLastY;
                mLastY = y;
                mLastX = x;
                break;
            }
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mActivePointerId = MotionEvent.INVALID_POINTER_ID;
                break;
            case MotionEvent.ACTION_POINTER_DOWN: {
                final int pointerIndex = MotionEventCompat.getActionIndex(ev);
                final int pointerId = MotionEventCompat.getPointerId(ev, pointerIndex);
                if (pointerId != mActivePointerId) {
                    mLastX = MotionEventCompat.getX(ev, pointerIndex);
                    mLastY = MotionEventCompat.getY(ev, pointerIndex);
                    mActivePointerId = MotionEventCompat.getPointerId(ev, pointerIndex);
                }
                break;
            }
            case MotionEvent.ACTION_POINTER_UP: {
                final int pointerIndex = MotionEventCompat.getActionIndex(ev);
                final int pointerId = MotionEventCompat.getPointerId(ev, pointerIndex);
                if (pointerId == mActivePointerId) {
                    final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
                    mLastX = MotionEventCompat.getX(ev, newPointerIndex);
                    mLastY = MotionEventCompat.getY(ev, newPointerIndex);
                    mActivePointerId = MotionEventCompat.getPointerId(ev, newPointerIndex);
                }
                break;
            }
        }
    }

    @Override
    public void computeScroll() {
        // 先判断mScroller滚动是否完成
        if (mScroller.computeScrollOffset()) {
            // 这里调用View的scrollTo()完成实际的滚动
            scrollTo(0, mScroller.getCurrY());
            // 立即重绘View实现滚动效果
            invalidate();
        }
    }

    /**
     * 速度控制函数
     * 当手指移动速度越接近10000px每500毫秒时获得的控件移动距离越小
     * 1.5f为权重，越大速度控制越明显，表现为用户越不容易拉动本控件，即拉动越吃力
     * <p>
     * 若不进行速度控制，将可能导致一系列问题，其中包括
     * 用户下拉一段距离，突然很快加速上拉控件，这时footer将被拖出，但此时内部控件并未到达它的底部
     * 这样显示不符合上拉加载的逻辑
     */
    private float getMoveFloat(float velocity, float org) {
        return ((10000f - Math.abs(velocity)) / 10000f * org) / 1.5f;
    }

    /**
     * 判断该回到初始状态还是Loading状态
     */
    private void autoBackToPosition() {
        if (mStatus == PULL_DOWN && Math.abs(getScrollY()) < mHeaderActionPosition) {
            autoBackToOriginalPosition();
        } else if (mStatus == PULL_DOWN && Math.abs(getScrollY()) > mHeaderActionPosition) {
            autoBackToLoadingPosition();
        } else if (mStatus == PULL_UP && Math.abs(getScrollY()) < mFooterActionPosition) {
            autoBackToOriginalPosition();
        } else if (mStatus == PULL_UP && Math.abs(getScrollY()) > mFooterActionPosition) {
            autoBackToLoadingPosition();
        }
    }

    /**
     * 回到Loading状态
     */
    private void autoBackToLoadingPosition() {
        mStartLoadingTime = System.currentTimeMillis();
        if (mStatus == PULL_DOWN) {
            mScroller.startScroll(0, getScrollY(), 0, -getScrollY() - mHeaderHoldingPosition, 400);
            invalidate();
            if (!isLoading) {
                isLoading = true;
                mListener.onRefresh();
            }
        }

        if (mStatus == PULL_UP) {
            mScroller.startScroll(0, getScrollY(), 0, mFooterHoldingPosition - getScrollY(), 400);
            invalidate();
            if (!isLoading) {
                isLoading = true;
                mListener.onLoadMore();
            }
        }

        loadingIndicator();
    }

    /**
     * 回到初始状态
     */
    private void autoBackToOriginalPosition() {
        mScroller.startScroll(0, getScrollY(), 0, -getScrollY(), 400);
        invalidate();
        this.postDelayed(new Runnable() {
            @Override
            public void run() {
                restoreIndicator();
                mStatus = IDLE;
            }
        }, 500);
    }

    private boolean isContentScrollToTop() {
        return !ViewCompat.canScrollVertically(contentView, -1);
    }

    private boolean isContentScrollToBottom() {
        return !ViewCompat.canScrollVertically(contentView, 1);
    }

    /**
     * 在拖动过程中调用Indicator(Header或Footer)的接口函数完成相应的指示性变化
     * 例如：下拉刷新、放开刷新的变化
     */
    private void updateIndicator() {
        if (mStatus == PULL_DOWN && deltaY > 0) {
            if (Math.abs(getScrollY()) > mHeaderActionPosition) {
                mHeaderIndicator.onAction();
            }
        } else if (mStatus == PULL_DOWN && deltaY < 0) {
            if (Math.abs(getScrollY()) < mHeaderActionPosition) {
                mHeaderIndicator.onUnaction();
            }
        } else if (mStatus == PULL_UP && deltaY < 0) {
            if (Math.abs(getScrollY()) > mFooterActionPosition) {
                mFooterIndicator.onAction();
            }
        } else if (mStatus == PULL_UP && deltaY > 0) {
            if (Math.abs(getScrollY()) < mFooterActionPosition) {
                mFooterIndicator.onUnaction();
            }
        }
    }

    /**
     * 本控件自动返回初始位置后恢复Indicator到初始状态
     */
    private void restoreIndicator() {
        mHeaderIndicator.onRestore();
        mFooterIndicator.onRestore();
    }

    /**
     * 本控件自动返回Loading位置后设置Indicator为Loading状态
     */
    private void loadingIndicator() {
        if (mStatus == PULL_DOWN) {
            mHeaderIndicator.onLoading();
        }

        if (mStatus == PULL_UP) {
            mFooterIndicator.onLoading();
        }
    }

    public void onFinishLoading() {
        long delta = System.currentTimeMillis() - mStartLoadingTime;
        if (delta > 2000) {
            isLoading = false;
            autoBackToOriginalPosition();
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    isLoading = false;
                    autoBackToOriginalPosition();
                }
            }, 1000);
        }
    }

    public void onAutoRefresh() {
        mStartLoadingTime = System.currentTimeMillis();
        mStatus = PULL_DOWN;
        mScroller.startScroll(0, getScrollY(), 0, -mHeaderHoldingPosition, 400);
        invalidate();
        if (!isLoading) {
            isLoading = true;
            mListener.onRefresh();
        }
        loadingIndicator();
    }

    /**
     * Interface
     */
    public interface OnRefreshListener {
        void onRefresh();
        void onLoadMore();
    }

    private BaseIndicator getIndicator(String className) {
        if (!TextUtils.isEmpty(className)) {
            try {
                Class clazz = Class.forName(className);
                return (BaseIndicator) clazz.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    /**
     * Getter and Setter
     */

    public void setListener(OnRefreshListener mListener) {
        this.mListener = mListener;
    }

    public boolean isPullDownEnable() {
        return isPullDownEnable;
    }

    public void setPullDownEnable(boolean pullDownEnable) {
        isPullDownEnable = pullDownEnable;
    }

    public boolean isPullUpEnable() {
        return isPullUpEnable;
    }

    public void setPullUpEnable(boolean pullUpEnable) {
        isPullUpEnable = pullUpEnable;
    }
}
