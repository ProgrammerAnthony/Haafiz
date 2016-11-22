package com.anthony.inputlayout.layout;

import android.content.Context;
import android.graphics.drawable.LevelListDrawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.anthony.inputlayout.utils.Utils;
import com.anthony.inputlayout.audio.AudioPlayerManager;
import com.anthony.inputlayout.audio.AudioRecorderManager;
import com.trs.inputlayout.R;

import java.io.File;


public class RecorderKeyboardLayout extends BaseCustomCompositeView implements AudioRecorderManager.Callback, View.OnTouchListener {
    /**
     * 录音的最大时间
     */
    private static final int RECORDER_MAX_TIME = 60;
    private static final int VOICE_LEVEL_COUNT = 9;

    private static final int STATE_NORMAL = 1;
    private static final int STATE_RECORDING = 2;
    private static final int STATE_WANT_CANCEL = 3;

    private int mCurrentState = STATE_NORMAL;
    private boolean mIsRecording;
    /**
     * 是否超时，默认不超时
     */
    private boolean mIsOvertime = false;
    private boolean mHasPermission = false;

    private int mDistanceCancel;

    private AudioRecorderManager mAudioRecorderManager;
    private float mTime;
    private Callback mCallback;
    private TextView mStatusTv;
    private ImageView mAnimIv;

    /**
     * 上一次提示录音时间太短的时间戳
     */
    private long mLastTipTooShortTime;

    private Runnable mGetVoiceLevelRunnable = new Runnable() {
        @Override
        public void run() {
            while (mIsRecording) {
                try {
                    Thread.sleep(100);
                    mTime += 0.1f;
                    if (mTime <= RECORDER_MAX_TIME) {
                        refreshVoiceLevel();
                    } else {
                        mIsOvertime = true;
                        handleActionUp();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    public RecorderKeyboardLayout(Context context) {
        super(context);
    }

    public RecorderKeyboardLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RecorderKeyboardLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_recorder_keyboard;
    }

    @Override
    protected void initView() {
        mStatusTv = getViewById(R.id.tv_recorder_keyboard_status);
        mAnimIv = getViewById(R.id.iv_recorder_keyboard_anim);
    }

    @Override
    protected void setListener() {
        mAnimIv.setOnTouchListener(this);
    }

    @Override
    protected void processLogic() {
        initLevelListDrawable();

        mDistanceCancel = Utils.dip2px(getContext(), 10);
        mAudioRecorderManager = new AudioRecorderManager(getContext(), this);
    }

    private void initLevelListDrawable() {
        LevelListDrawable levelListDrawable = new LevelListDrawable();
        for (int i = 0; i < 9; i++) {
            int resId = getContext().getResources().getIdentifier("voice_level" + (i + 1), "drawable", getContext().getPackageName());
            levelListDrawable.addLevel(i, i + 1, Utils.tintDrawable(getContext(), getResources().getDrawable(resId), R.color.mq_chat_audio_recorder_icon));
        }
        levelListDrawable.addLevel(9, 10, getResources().getDrawable(R.drawable.voice_want_cancel));
        mAnimIv.setImageDrawable(levelListDrawable);
    }

    @Override
    public void wellPrepared() {
        mIsRecording = true;
        new Thread(mGetVoiceLevelRunnable).start();
    }

    @Override
    public void onAudioRecorderNoPermission() {
        endRecorder();
        reset();
    }

    private void refreshVoiceLevel() {
        post(new Runnable() {
            @Override
            public void run() {
                if (mCurrentState == STATE_RECORDING) {
                    mAnimIv.setImageLevel(mAudioRecorderManager.getVoiceLevel(VOICE_LEVEL_COUNT));

                    int remainingTime = Math.round(RECORDER_MAX_TIME - mTime);
                    if (remainingTime <= 10) {
                        mStatusTv.setText(getContext().getString(R.string.mq_recorder_remaining_time, remainingTime));
                    }
                }
            }
        });
    }

    private void changeState(int status) {
        if (mCurrentState != status) {
            mCurrentState = status;
            switch (mCurrentState) {
                case STATE_NORMAL:
                    mStatusTv.setText(R.string.mq_audio_status_normal);
                    mAnimIv.setImageLevel(1);
                    break;
                case STATE_RECORDING:
                    mStatusTv.setText(R.string.mq_audio_status_recording);
                    break;
                case STATE_WANT_CANCEL:
                    mStatusTv.setText(R.string.mq_audio_status_want_cancel);
                    mAnimIv.setImageLevel(10);
                    break;
            }
        }
    }

    private boolean isWantCancel(int x, int y) {
        if (y < -mDistanceCancel) {
            return true;
        }
        return false;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mIsOvertime = false;
                mHasPermission = true;
                changeState(STATE_RECORDING);
                mAudioRecorderManager.prepareAudio();
                break;
            case MotionEvent.ACTION_MOVE:
                if (!mIsOvertime && mIsRecording && mHasPermission) {
                    if (isWantCancel(x, y)) {
                        changeState(STATE_WANT_CANCEL);
                    } else {
                        changeState(STATE_RECORDING);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                handleActionUp();
                break;
            case MotionEvent.ACTION_CANCEL:
                mAudioRecorderManager.cancel();
                reset();
                break;
        }
        return true;
    }

    private void handleActionUp() {
        post(new Runnable() {
            @Override
            public void run() {
                if (!mIsOvertime && mHasPermission) {
                    // 录音没有超时的情况

                    if (!mIsRecording || mTime < 1) {
                        // prepare未完成
                        mAudioRecorderManager.cancel();

                        if (System.currentTimeMillis() - mLastTipTooShortTime > 1000) {
                            mLastTipTooShortTime = System.currentTimeMillis();
                            mCallback.onAudioRecorderTooShort();
                        }
                    } else if (mCurrentState == STATE_RECORDING) {
                        endRecorder();
                    } else if (mCurrentState == STATE_WANT_CANCEL) {
                        mAudioRecorderManager.cancel();
                    }
                } else if (mIsRecording) {
                    // 录音超时，并且正在录音时

                    endRecorder();
                }

                reset();
            }
        });
    }

    /**
     * 结束录音
     */
    private void endRecorder() {
        mAudioRecorderManager.release();
        if (mCallback != null) {
            String currentFilePath = mAudioRecorderManager.getCurrenFilePath();
            if (!TextUtils.isEmpty(currentFilePath)) {
                File currentFile = new File(currentFilePath);
                // 在某些手机上，没有权限录音时，文件大小为6
                if (currentFile.exists() && currentFile.length() > 6) {
                    mCallback.onAudioRecorderFinish(AudioPlayerManager.getDurationByFilePath(getContext(), currentFile.getAbsolutePath()), currentFile.getAbsolutePath());
                } else {
                    mAudioRecorderManager.cancel();
                    mCallback.onAudioRecorderNoPermission();
                }
            }
        }
    }

    /**
     * 恢复到初始状态
     */
    private void reset() {
        mIsRecording = false;
        mHasPermission = false;
        mTime = 0;
        changeState(STATE_NORMAL);
    }

    /**
     * 是否正在录音
     *
     * @return
     */
    public boolean isRecording() {
        return mCurrentState != STATE_NORMAL;
    }

    public void setCallback(Callback callback) {
        mCallback = callback;
    }

    public interface Callback {
        void onAudioRecorderFinish(int time, String filePath);

        void onAudioRecorderTooShort();

        void onAudioRecorderNoPermission();
    }
}