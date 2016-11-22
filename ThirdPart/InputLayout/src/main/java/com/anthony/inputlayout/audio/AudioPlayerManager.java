package com.anthony.inputlayout.audio;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;

import java.io.IOException;


public class AudioPlayerManager {
    private static MediaPlayer sMediaPlayer;
    private static boolean sIsPause;

    public static void playSound(String path, final Callback callback) {
        try {
            if (sMediaPlayer == null) {
                sMediaPlayer = new MediaPlayer();
            } else {
                sMediaPlayer.reset();
            }
            sMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            sMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    if (callback != null) {
                        callback.onCompletion();
                    }
                }
            });
            sMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    sMediaPlayer.reset();
                    if (callback != null) {
                        callback.onError();
                    }
                    return false;
                }
            });
            sMediaPlayer.setDataSource(path);
            sMediaPlayer.prepare();
            sMediaPlayer.start();
        } catch (IOException e) {
            if (callback != null) {
                callback.onError();
            }
        }
    }

    public static void resume() {
        if (sMediaPlayer != null && sIsPause) {
            sMediaPlayer.start();
            sIsPause = false;
        }
    }

    public static void pause() {
        if (isPlaying()) {
            sMediaPlayer.pause();
            sIsPause = true;
        }
    }

    public static void stop() {
        if (isPlaying()) {
            sMediaPlayer.stop();
        }
    }

    public static boolean isPlaying() {
        return sMediaPlayer != null && sMediaPlayer.isPlaying();
    }

    public static void release() {
        stop();
        if (sMediaPlayer != null) {
            sMediaPlayer.release();
            sMediaPlayer = null;
        }
    }

    /**
     * 获取当前正在播放音频的时间长度
     *
     * @return
     */
    public static int getCurrentPosition() {
        if (isPlaying()) {
            int position = sMediaPlayer.getCurrentPosition();
            return position == 0 ? 1 : position;
        }
        return 0;
    }

    /**
     * 获取当前正在播放音频的时间长度
     *
     * @return
     */
    public static int getCurrentDuration() {
        if (isPlaying()) {
            int duration = sMediaPlayer.getDuration();
            return duration == 0 ? 1 : duration;
        }
        return 0;
    }

    /**
     * 根据音频文件路径获取时间长度
     *
     * @param context
     * @param audioFilePath
     * @return
     */
    public static int getDurationByFilePath(Context context, String audioFilePath) {
        try {
            MediaPlayer mp = MediaPlayer.create(context, Uri.parse(audioFilePath));
            int duration = mp.getDuration() / 1000;
            return duration == 0 ? 1 : duration;
        } catch (Exception e) {
            return 0;
        }
    }

    public interface Callback {
        void onError();

        void onCompletion();
    }
}