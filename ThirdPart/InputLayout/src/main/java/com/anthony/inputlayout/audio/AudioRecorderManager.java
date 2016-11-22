package com.anthony.inputlayout.audio;

import android.content.Context;
import android.media.MediaRecorder;
import android.support.annotation.Nullable;

import java.io.File;
import java.util.UUID;


public class AudioRecorderManager {
    private MediaRecorder mMediaRecorder;
    private File mCurrentFile;
    private Callback mCallback;

    private boolean mIsPrepared;
    private Context mContext;

    public AudioRecorderManager(Context context, Callback callback) {
        mContext = context.getApplicationContext();
        mCallback = callback;
    }

    public void prepareAudio() {
        try {
            mCurrentFile = new File(getVoiceCacheDir(mContext), UUID.randomUUID().toString());
            mMediaRecorder = new MediaRecorder();
            mMediaRecorder.setOutputFile(mCurrentFile.getAbsolutePath());
            // 设置音频源为麦克风
            mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            // 设置音频的格式
            mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
            // 设置音频的编码为amr
            mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mMediaRecorder.prepare();
            mMediaRecorder.start();

            mIsPrepared = true;

            if (mCallback != null) {
                mCallback.wellPrepared();
            }
        } catch (Exception e) {
            if (mCallback != null) {
                mCallback.onAudioRecorderNoPermission();
            }
        }
    }

    public int getVoiceLevel(int maxLevel) {
        if (mIsPrepared) {
            try {
                return Math.max(Math.min((int) (25 * Math.log10(mMediaRecorder.getMaxAmplitude() / 500)) / 4, maxLevel), 1);

                // 没有设置音频源之前获取声音振幅会报IllegalStateException，直接返回1
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return 1;
    }

    public void release() {
        try {
            if (mMediaRecorder != null) {
                mMediaRecorder.stop();
                mMediaRecorder.release();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mMediaRecorder = null;
        }
    }

    public void cancel() {
        release();
        if (mCurrentFile != null) {
            mCurrentFile.delete();
            mCurrentFile = null;
        }
    }

    @Nullable
    public String getCurrenFilePath() {
        return mCurrentFile == null ? null : mCurrentFile.getAbsolutePath();
    }

    public interface Callback {
        void wellPrepared();

        void onAudioRecorderNoPermission();
    }

    /**
     * 获取录音文件缓存目录
     *
     * @param context
     * @return
     */
    public static File getVoiceCacheDir(Context context) {
        File voiceCacheDir = new File(context.getExternalCacheDir(), "voice");
        if (!voiceCacheDir.exists()) {
            voiceCacheDir.mkdirs();
        }
        return voiceCacheDir;
    }

    /**
     * 根据文件服务器上的key值，获取本地缓存文件
     *
     * @param context
     * @param url
     * @return
     */
    public static File getCachedVoiceFileByUrl(Context context, String url) {
        String key = url.substring(url.lastIndexOf("/") + 1);
        return new File(getVoiceCacheDir(context), key);
    }

    /**
     * 重命名录音文件
     *
     * @param context 应用程序上下文
     * @param path    原录音文件绝对路径
     * @param key     文件服务器上文件对应的key值
     * @return 新的录音文件的绝对路径
     */
    public static String renameVoiceFilename(Context context, String path, String key) {
        File oldFile = new File(path);
        // 注意：替换掉文件服务器返回的key前面的audio/
        File newFile = new File(getVoiceCacheDir(context), key.replace("audio/", ""));
        oldFile.renameTo(newFile);
        return newFile.getAbsolutePath();
    }
}