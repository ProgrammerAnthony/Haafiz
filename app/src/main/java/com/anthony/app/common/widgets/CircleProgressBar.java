package com.anthony.app.common.widgets;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.util.AttributeSet;
import android.view.View;

import com.anthony.app.R;


public class CircleProgressBar extends View {
    private Path mPath;
    private Paint mPaint;
    private PathMeasure mPathMeasure;
    private float mAnimatorValue;
    private Path mDst;
    private float mLength;

    private ValueAnimator mValueAnimator;

    private static final int DEFAULT_BORDER_WIDTH = 10;
    private static final int DEFAULT_BORDER_COLOR = Color.BLACK;
    private static final int DEFAULT_DURATION = 2;
    private int mBorderColor = DEFAULT_BORDER_COLOR;
    private int mBorderWidth = DEFAULT_BORDER_WIDTH;
    private int mDuration = DEFAULT_DURATION;

    private static final int DEFAULT_FILL_COLOR = Color.argb(32, 0, 0, 0);
    private Paint mBgPaint;
    private int mFillColor = DEFAULT_FILL_COLOR;


    public CircleProgressBar(Context context) {
        super(context);
    }


    public CircleProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CircleProgressBar, defStyleAttr, 0);
        mBorderWidth = a.getDimensionPixelSize(R.styleable.CircleProgressBar_border_width, DEFAULT_BORDER_WIDTH);
        mBorderColor = a.getColor(R.styleable.CircleProgressBar_border_color, DEFAULT_BORDER_COLOR);
        mDuration = a.getInt(R.styleable.CircleProgressBar_duration_second, DEFAULT_DURATION);
        mFillColor = a.getColor(R.styleable.CirclePageIndicator_fillColor, DEFAULT_FILL_COLOR);
        a.recycle();

        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (getHeight() != 0 && getWidth() != 0) {
            mPath.addCircle(getWidth() / 2, getHeight() / 2, Math.min(getWidth(), getHeight()) / 2 - mBorderWidth, Path.Direction.CW);
            mPathMeasure.setPath(mPath, true);
            mLength = mPathMeasure.getLength();
        }
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mBorderWidth);
        mPaint.setColor(mBorderColor);
        mPaint.setStrokeCap(Paint.Cap.ROUND);

        mPath = new Path();
        mPathMeasure = new PathMeasure();
        mDst = new Path();

        mValueAnimator = ValueAnimator.ofFloat(0, 1);
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mAnimatorValue = (float) valueAnimator.getAnimatedValue();
                invalidate();
            }
        });
        mValueAnimator.setDuration(mDuration * 1000);
//        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mValueAnimator.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mBgPaint == null) {
            mBgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mBgPaint.setStyle(Paint.Style.FILL);
            mBgPaint.setColor(mFillColor);
        }
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, Math.min(getWidth(), getHeight()) / 2 - mBorderWidth / 1.8f, mBgPaint);

        mDst.reset();
        // 硬件加速的BUG
        mDst.lineTo(0, 0);
        float stop = mLength * mAnimatorValue;
        mPathMeasure.getSegment(0, stop, mDst, true);
        canvas.drawPath(mDst, mPaint);
    }

    public void stop() {
        mValueAnimator.cancel();
    }

//    public interface onCircleProgressBarClick {
//        void onClick();
//    }
//
//    public void setCircleProgressBarOnClickListener(onCircleProgressBarClick listener) {
//        mClickListener = listener;
//    }
}
