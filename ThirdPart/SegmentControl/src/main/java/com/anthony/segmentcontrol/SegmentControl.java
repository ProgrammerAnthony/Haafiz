package com.anthony.segmentcontrol;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import com.edu.anthony.segmentcontrol.R;

/**
 * Created by 7heaven on 15/4/22.
 */
public class SegmentControl extends View {

    private String[] mTexts;
    private Rect[] mCacheBounds;
    private Rect[] mTextBounds;

    private RadiusDrawable mBackgroundDrawable;
    private RadiusDrawable mSelectedDrawable;

    private int mCurrentIndex;

    private int mTouchSlop;
    private boolean inTapRegion;
    private float mStartX;
    private float mStartY;
    private float mCurrentX;
    private float mCurrentY;

    private int mHorizonGap;
    private int mVerticalGap;

    /** 外边框的width */
    private int mBoundWidth = 4;
    /** 内边框的width */
    private int mSeparatorWidth = mBoundWidth / 2;

    private int mSingleChildWidth;
    private int mSingleChildHeight;

    private Paint mPaint;

    private int mTextSize;
    private ColorStateList mBackgroundColors;
    private ColorStateList mTextColors;
    private int mCornerRadius;

    private int DEFAULT_SELECTED_COLOR = 0xFF32ADFF;
    private int DEFAULT_NORMAL_COLOR = 0xFFFFFFFF;

    private Paint.FontMetrics mCachedFM;

    public enum Direction {
        HORIZONTAL(0), VERTICAL(1);

        int value;

        Direction(int v) {
            value = v;
        }
    }

    private Direction mDirection;

    public SegmentControl(Context context) {
        this(context, null);
    }

    public SegmentControl(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SegmentControl(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.SegmentControl);

        String textArray = ta.getString(R.styleable.SegmentControl_texts);
        if (textArray != null) {
            mTexts = textArray.split("\\|");
        }

        mTextSize = ta.getDimensionPixelSize(R.styleable.SegmentControl_android_textSize, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 14, context.getResources().getDisplayMetrics()));
        mCornerRadius = ta.getDimensionPixelSize(R.styleable.SegmentControl_cornerRadius, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, context.getResources().getDisplayMetrics()));
        mDirection = Direction.values()[ta.getInt(R.styleable.SegmentControl_block_direction, 0)];

        mHorizonGap = ta.getDimensionPixelSize(R.styleable.SegmentControl_horizonGap, 0);
        mVerticalGap = ta.getDimensionPixelSize(R.styleable.SegmentControl_verticalGap, 0);

        int gap = ta.getDimensionPixelSize(R.styleable.SegmentControl_gaps, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, context.getResources().getDisplayMetrics()));

        if(mHorizonGap == 0) {
            mHorizonGap = gap;
        }
        if(mVerticalGap == 0) {
            mVerticalGap = gap;
        }

        mBackgroundDrawable = new RadiusDrawable(mCornerRadius, true);
        mBackgroundDrawable.setStrokeWidth(2);

        DEFAULT_NORMAL_COLOR = ta.getColor(R.styleable.SegmentControl_normalColor, DEFAULT_NORMAL_COLOR);
        DEFAULT_SELECTED_COLOR = ta.getColor(R.styleable.SegmentControl_sc_selectedColor, DEFAULT_SELECTED_COLOR);

        mBackgroundColors = ta.getColorStateList(R.styleable.SegmentControl_backgroundColors);
        mTextColors = ta.getColorStateList(R.styleable.SegmentControl_textColors);
        if (mBackgroundColors == null) {
            mBackgroundColors = new ColorStateList(new int[][]{{android.R.attr.state_selected}, {-android.R.attr.state_selected}}, new int[]{DEFAULT_SELECTED_COLOR, DEFAULT_NORMAL_COLOR});
        }

        if(mTextColors == null){
            mTextColors = new ColorStateList(new int[][]{{android.R.attr.state_selected}, {-android.R.attr.state_selected}}, new int[]{DEFAULT_NORMAL_COLOR, DEFAULT_SELECTED_COLOR});
        }

        mBoundWidth = ta.getDimensionPixelSize(R.styleable.SegmentControl_boundWidth, mBoundWidth);
        mSeparatorWidth = ta.getDimensionPixelSize(R.styleable.SegmentControl_separatorWidth, mSeparatorWidth);

        ta.recycle();

        mBackgroundDrawable = new RadiusDrawable(mCornerRadius, true);
        mBackgroundDrawable.setStrokeWidth(mBoundWidth);
        mBackgroundDrawable.setStrokeColor(getSelectedBGColor());
        mBackgroundDrawable.setFillColor(getNormalBGColor());

        if (Build.VERSION.SDK_INT < 16) {
            setBackgroundDrawable(mBackgroundDrawable);
        } else {
            setBackground(mBackgroundDrawable);
        }

        mSelectedDrawable = new RadiusDrawable(false);
        mSelectedDrawable.setFillColor(getSelectedBGColor());

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setTextSize(mTextSize);
        mCachedFM = mPaint.getFontMetrics();

        int touchSlop = 0;
        if (context == null) {
            touchSlop = ViewConfiguration.getTouchSlop();
        } else {
            final ViewConfiguration config = ViewConfiguration.get(context);
            touchSlop = config.getScaledTouchSlop();
        }
        mTouchSlop = touchSlop * touchSlop;
        inTapRegion = false;
    }

    public void setText(String... texts) {
        mTexts = texts;
        if (mTexts != null) {
            requestLayout();
        }
    }

    /**
     * 设置文字颜色
     *
     * @param color 需要设置的颜色
     */
    public void setSelectedTextColors(ColorStateList color) {
        mTextColors = color;
        invalidate();
    }

    /**
     * 设置背景颜色
     *
     * @param colors 颜色
     */
    public void setColors(ColorStateList colors) {
        mBackgroundColors = colors;

        if (mBackgroundDrawable != null) {
            mBackgroundDrawable.setStrokeColor(getSelectedBGColor());
            mBackgroundDrawable.setFillColor(getNormalBGColor());
        }

        if (mSelectedDrawable != null) {
            mSelectedDrawable.setFillColor(getSelectedBGColor());
        }

        invalidate();
    }

    public void setCornerRadius(int cornerRadius) {
        mCornerRadius = cornerRadius;

        if (mBackgroundDrawable != null) {
            mBackgroundDrawable.setRadius(cornerRadius);
        }

        invalidate();
    }

    public void setDirection(Direction direction) {
        Direction tDirection = mDirection;

        mDirection = direction;

        if (tDirection != direction) {
            requestLayout();
            invalidate();
        }
    }

    public void setTextSize(int textSize_sp) {
        setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize_sp);
    }

    public void setTextSize(int unit, int textSize) {
        mPaint.setTextSize((int) (TypedValue.applyDimension(unit, textSize, getContext().getResources().getDisplayMetrics())));

        if (textSize != mTextSize) {
            mTextSize = textSize;
            mCachedFM = mPaint.getFontMetrics();

            requestLayout();
        }
    }

    public void setSelectedIndex(int index) {
        mCurrentIndex = index;

        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width = 0;
        int height = 0;

        if (mTexts != null && mTexts.length > 0) {

            mSingleChildHeight = 0;
            mSingleChildWidth = 0;

            if (mCacheBounds == null || mCacheBounds.length != mTexts.length) {
                mCacheBounds = new Rect[mTexts.length];
            }

            if (mTextBounds == null || mTextBounds.length != mTexts.length) {
                mTextBounds = new Rect[mTexts.length];
            }

            for (int i = 0; i < mTexts.length; i++) {
                String text = mTexts[i];

                if(text != null){

                    if(mTextBounds[i] == null) {
                        mTextBounds[i] = new Rect();
                    }

                    mPaint.getTextBounds(text, 0, text.length(), mTextBounds[i]);

                    if(mSingleChildWidth < mTextBounds[i].width() + mHorizonGap * 2) {
                        mSingleChildWidth = mTextBounds[i].width() + mHorizonGap * 2;
                    }
                    if(mSingleChildHeight < mTextBounds[i].height() + mVerticalGap * 2) {
                        mSingleChildHeight = mTextBounds[i].height() + mVerticalGap * 2;
                    }
                }
            }

            switch (widthMode) {
                case MeasureSpec.AT_MOST:
                    if (mDirection == Direction.HORIZONTAL) {
                        if (widthSize <= mSingleChildWidth * mTexts.length) {
                            mSingleChildWidth = widthSize / mTexts.length;
                            width = widthSize;
                        } else {
                            width = mSingleChildWidth * mTexts.length;
                        }
                    } else {
                        width = widthSize <= mSingleChildWidth ? widthSize : mSingleChildWidth;
                    }
                    break;
                case MeasureSpec.EXACTLY:
                    width = widthSize;
                    break;
                case MeasureSpec.UNSPECIFIED:
                default:
                    if (mDirection == Direction.HORIZONTAL) {
                        width = mSingleChildWidth * mTexts.length;
                    } else {
                        width = mSingleChildWidth;
                    }
                    break;
            }

            switch (heightMode) {
                case MeasureSpec.AT_MOST:
                    if (mDirection == Direction.VERTICAL) {
                        if (heightSize <= mSingleChildHeight * mTexts.length) {
                            mSingleChildHeight = heightSize / mTexts.length;
                            height = heightSize;
                        } else {
                            height = mSingleChildHeight * mTexts.length;
                        }
                    } else {
                        height = heightSize <= mSingleChildHeight ? heightSize : mSingleChildHeight;
                    }
                    break;
                case MeasureSpec.EXACTLY:
                    height = heightSize;
                    break;
                case MeasureSpec.UNSPECIFIED:
                default:
                    if (mDirection == Direction.VERTICAL) {
                        height = mSingleChildHeight * mTexts.length;
                    } else {
                        height = mSingleChildHeight;
                    }

                    break;
            }

            switch (mDirection) {
                case HORIZONTAL:
                    if (mSingleChildWidth != width / mTexts.length) {
                        mSingleChildWidth = width / mTexts.length;
                    }
                    mSingleChildHeight = height;
                    break;
                case VERTICAL:
                    if(mSingleChildHeight != height / mTexts.length) {
                        mSingleChildHeight = height / mTexts.length;
                    }
                    mSingleChildWidth = width;
                    break;
                default:
                    break;
            }

            for (int i = 0; i < mTexts.length; i++) {

                if (mCacheBounds[i] == null) {
                    mCacheBounds[i] = new Rect();
                }

                if (mDirection == Direction.HORIZONTAL) {
                    mCacheBounds[i].left = i * mSingleChildWidth;
                    mCacheBounds[i].top = 0;
                } else {
                    mCacheBounds[i].left = 0;
                    mCacheBounds[i].top = i * mSingleChildHeight;
                }

                mCacheBounds[i].right = mCacheBounds[i].left + mSingleChildWidth;
                mCacheBounds[i].bottom = mCacheBounds[i].top + mSingleChildHeight;
            }
        } else {
            width = widthMode == MeasureSpec.UNSPECIFIED ? 0 : widthSize;
            height = heightMode == MeasureSpec.UNSPECIFIED ? 0 : heightSize;
        }

        setMeasuredDimension(width, height);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                inTapRegion = true;

                mStartX = event.getX();
                mStartY = event.getY();
                break;

            case MotionEvent.ACTION_MOVE:
                mCurrentX = event.getX();
                mCurrentY = event.getY();

                int dx = (int) (mCurrentX - mStartX);
                int dy = (int) (mCurrentY - mStartY);

                int distance = dx * dx + dy * dy;

                if (distance > mTouchSlop) {
                    inTapRegion = false;
                }
                break;

            case MotionEvent.ACTION_UP:
                if (inTapRegion) {
                    int index = 0;
                    if (mDirection == Direction.HORIZONTAL) {
                        index = (int) (mStartX / mSingleChildWidth);
                    } else {
                        index = (int) (mStartY / mSingleChildHeight);
                    }

                    if(mOnSegmentControlClickListener != null) {
                        mOnSegmentControlClickListener.onSegmentControlClick(index);
                    }

                    mCurrentIndex = index;

                    invalidate();
                }
                break;
            default:
                break;
        }
        return true;
    }

    private int getSelectedTextColor(){
        return mTextColors.getColorForState(new int[]{android.R.attr.state_selected}, DEFAULT_NORMAL_COLOR);
    }

    private int getNormalTextColor(){
        return mTextColors.getColorForState(new int[]{-android.R.attr.state_selected}, DEFAULT_SELECTED_COLOR);
    }

    private int getSelectedBGColor(){
        return mBackgroundColors.getColorForState(new int[]{android.R.attr.state_selected}, DEFAULT_SELECTED_COLOR);
    }

    private int getNormalBGColor(){
        return mBackgroundColors.getColorForState(new int[]{-android.R.attr.state_selected}, DEFAULT_NORMAL_COLOR);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mTexts != null && mTexts.length > 0) {

            for (int i = 0; i < mTexts.length; i++) {

                //draw separate lines
                if (i < mTexts.length - 1) {
                    mPaint.setColor(getSelectedBGColor());
                    mPaint.setStrokeWidth(mSeparatorWidth);
                    if (mDirection == Direction.HORIZONTAL) {
                        canvas.drawLine(mCacheBounds[i].right, 0, mCacheBounds[i].right, getHeight(), mPaint);
                    } else {
                        canvas.drawLine(mCacheBounds[i].left, mSingleChildHeight * (i + 1), mCacheBounds[i].right, mSingleChildHeight * (i + 1), mPaint);
                    }
                }

                //draw selected drawable
                if (i == mCurrentIndex && mSelectedDrawable != null) {
                    int topLeftRadius = 0;
                    int topRightRadius = 0;
                    int bottomLeftRadius = 0;
                    int bottomRightRadius = 0;

                    if (mDirection == Direction.HORIZONTAL) {
                        if (i == 0) {
                            topLeftRadius = mCornerRadius;
                            bottomLeftRadius = mCornerRadius;
                        } else if (i == mTexts.length - 1) {
                            topRightRadius = mCornerRadius;
                            bottomRightRadius = mCornerRadius;
                        }
                    } else {
                        if (i == 0) {
                            topLeftRadius = mCornerRadius;
                            topRightRadius = mCornerRadius;
                        } else if (i == mTexts.length - 1) {
                            bottomLeftRadius = mCornerRadius;
                            bottomRightRadius = mCornerRadius;
                        }
                    }

                    mSelectedDrawable.setRadius(topLeftRadius, topRightRadius, bottomLeftRadius, bottomRightRadius);
                    mSelectedDrawable.setBounds(mCacheBounds[i]);
                    mSelectedDrawable.draw(canvas);

                    mPaint.setColor(getSelectedTextColor());
                } else {
                    mPaint.setColor(getNormalTextColor());
                }

                //draw texts

                float baseline = mCacheBounds[i].top + ((mSingleChildHeight - mCachedFM.ascent + mCachedFM.descent) / 2) - mCachedFM.descent;
                canvas.drawText(mTexts[i], mCacheBounds[i].left + (mSingleChildWidth - mTextBounds[i].width()) / 2, baseline, mPaint);

            }
        }
    }

    // =========================================================
    // OnSegmentControlClickListener
    // =========================================================
    private OnSegmentControlClickListener mOnSegmentControlClickListener;

    public void setOnSegmentControlClickListener(OnSegmentControlClickListener listener) {
        mOnSegmentControlClickListener = listener;
    }

    public OnSegmentControlClickListener getOnSegmentControlClicklistener() {
        return mOnSegmentControlClickListener;
    }

    public interface OnSegmentControlClickListener {
        void onSegmentControlClick(int index);
    }
}
