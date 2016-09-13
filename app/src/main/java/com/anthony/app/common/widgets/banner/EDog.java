package com.anthony.app.common.widgets.banner;

import android.os.Handler;


public class EDog {
    public static final String TAG = "EDog";

    private class WaitingThread extends Thread {
        public void run(){
            while(!cancelled){
                boolean shouldBark = System.currentTimeMillis() - startTick > duration;

                if(shouldBark){
                    bark();
                    break;
                }

                try {
                    Thread.sleep(100);
                } catch (Exception e) {
//					Log.w(TAG, "Exception while waiting: " + e);
                    e.printStackTrace();
                }
            }
        }
    }

    private Handler handler = new Handler();
    private int duration;
    private long startTick;
    private boolean cancelled;
    private Runnable r;
    private WaitingThread waitingThread;

    public void feed(Runnable r, int duration){
//		Log.i(TAG, "Feed, duration: " + duration);
        this.r = r;
        this.duration = duration;
        this.startTick = System.currentTimeMillis();
        this.cancelled = false;

        if(waitingThread == null || !waitingThread.isAlive()){
//			Log.i(TAG, "thread not running, start new");
            waitingThread = new WaitingThread();
            waitingThread.start();
        }
    }

    private void bark(){
//		Log.i(TAG, "bark");
        handler.post(r);
    }

    public void cancel(){
//		Log.i(TAG, "cancel");
        cancelled = true;
    }
}
