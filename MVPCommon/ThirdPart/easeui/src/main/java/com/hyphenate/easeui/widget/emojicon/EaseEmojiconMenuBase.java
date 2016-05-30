package com.hyphenate.easeui.widget.emojicon;

import com.hyphenate.easeui.domain.EaseEmojicon;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class EaseEmojiconMenuBase extends LinearLayout{
    protected EaseEmojiconMenuListener listener;
    
    public EaseEmojiconMenuBase(Context context) {
        super(context);
    }
    
    @SuppressLint("NewApi")
    public EaseEmojiconMenuBase(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    public EaseEmojiconMenuBase(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    
    /**
     * 设置回调监听
     * @param listener
     */
    public void setEmojiconMenuListener(EaseEmojiconMenuListener listener){
        this.listener = listener;
    }
    
    public interface EaseEmojiconMenuListener{
        /**
         * 表情被点击
         * @param emojicon
         */
        void onExpressionClicked(EaseEmojicon emojicon);
        /**
         * 删除按钮被点击
         */
        void onDeleteImageClicked();
    }
}
