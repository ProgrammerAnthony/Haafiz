package com.anthony.app.common.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.anthony.app.R;


/**
 * Created by Anthony on 2016/8/5.
 * Class Note:
 * a vertical text effect using {@link TextView}to represent one char in
 * {@link LinearLayout} whose orientation is {@link #VERTICAL}
 */
public class VerticalTextView extends LinearLayout {
    private String mText;
    private Context mContext;

    public VerticalTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }


    public VerticalTextView(Context context) {
        this(context, null);
    }

    public VerticalTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(VERTICAL);
        this.mContext = context;
    }


    public void setText(String text) {
        this.mText = text;
        addText();
    }

    private void addText() {
        removeAllViews();
        System.out.println("------>" + mText);
        if (mText != null) {
            char[] chara = mText.toCharArray();
            for (int i = 0; i < chara.length; i++) {
                TextView oneText = new TextView(mContext);

                oneText.setText(mText.substring(i, i + 1));
                oneText.setTextColor(getResources().getColor(R.color.gz_dark_blue));
                oneText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                oneText.setGravity(Gravity.CENTER_HORIZONTAL);

                addView(oneText, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            }

        }
    }

}