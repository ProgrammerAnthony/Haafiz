package com.hyphenate.easeui.widget;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;

public abstract class EaseChatPrimaryMenuBase extends RelativeLayout{
    protected EaseChatPrimaryMenuListener listener;
    protected Activity activity;
    protected InputMethodManager inputManager;

    public EaseChatPrimaryMenuBase(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public EaseChatPrimaryMenuBase(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public EaseChatPrimaryMenuBase(Context context) {
        super(context);
        init(context);
    }
    
    private void init(Context context){
        this.activity = (Activity) context;
        inputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
    }
    
    /**
     * 设置主按钮栏相关listener
     * @param listener
     */
    public void setChatPrimaryMenuListener(EaseChatPrimaryMenuListener listener){
        this.listener = listener;
    }
    
    /**
     * 表情输入
     * @param emojiContent
     */
    public abstract void onEmojiconInputEvent(CharSequence emojiContent);

    /**
     * 表情删除
     */
    public abstract void onEmojiconDeleteEvent();
    
    /**
     * 整个扩展按钮栏(包括表情栏)隐藏
     */
    public abstract void onExtendMenuContainerHide();
    
    /**
     * 隐藏软键盘
     */
    public void hideKeyboard() {
        if (activity.getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (activity.getCurrentFocus() != null)
                inputManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
    
    public interface EaseChatPrimaryMenuListener{
        /**
         * 发送按钮点击事件
         * @param content 发送内容
         */
        void onSendBtnClicked(String content);
        
        /**
         * 长按说话按钮ontouch事件
         * @return
         */
        boolean onPressToSpeakBtnTouch(View v, MotionEvent event);
        
        /**
         * 长按说话按钮隐藏或显示事件
         */
        void onToggleVoiceBtnClicked();
        
        /**
         * 隐藏或显示扩展menu按钮点击点击事件
         */
        void onToggleExtendClicked();
        
        /**
         * 隐藏或显示表情栏按钮点击事件
         */
        void onToggleEmojiconClicked();
        
        /**
         * 文字输入框点击事件
         */
        void onEditTextClicked();
        
    }

}
