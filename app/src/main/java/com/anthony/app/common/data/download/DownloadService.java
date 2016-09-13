package com.anthony.app.common.data.download;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import com.anthony.app.R;
import com.anthony.app.common.data.RxBus;
import com.anthony.app.common.data.event.DownloadEvent;
import com.anthony.app.common.data.event.DownloadFinishEvent;
import com.anthony.app.common.utils.AppUtils;
import com.anthony.app.common.utils.ToastUtils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;


public class DownloadService extends Service {
    public static String DOWNLOAD_URL = "DOWNLOAD_URL";
    private static String ACTION = "DOWNLOAD_ACTION";
    private static int ACTION_CANCEL = 1;
    private NotificationManager mNotificationManager;
    private HashMap<String, DownloadTask> mTaskMap = new HashMap<>();
    @Inject
    RxBus bus;
    @Inject
    ToastUtils
            toastUtils;

    @Override
    public void onCreate() {
        super.onCreate();
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        bus
                .toObservable(DownloadEvent.class)
                .onBackpressureBuffer()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<DownloadEvent>() {
                    @Override
                    public void call(DownloadEvent downloadEvent) {
                        if (downloadEvent.total != -1) {
                            float percent = (float) downloadEvent.progress / (float) downloadEvent.total * 100;
                            //整数值未变没就不更新通知栏，否则通知栏更新太频繁会导致卡顿
                            if (downloadEvent.task.current_percent != (int) percent) {
                                downloadEvent.task.current_percent = (int) percent;
                                downloadEvent.task.mNotification.contentView
                                        .setProgressBar(R.id.progressbar_download, 100, (int) percent, false);

                                if (mNotificationManager != null) {
                                    mNotificationManager.notify(downloadEvent.task.id, downloadEvent.task.mNotification);
                                }
                                Log.v("FileDownload", "Process: " + String.valueOf((int) percent));
                            }
                        } else {
                            if (!downloadEvent.task.isUnknownLength) {
                                downloadEvent.task.isUnknownLength = true;
                                downloadEvent.task.mNotification.contentView
                                        .setViewVisibility(R.id.progressbar_download, View.INVISIBLE);
                                downloadEvent.task.mNotification.contentView
                                        .setViewVisibility(R.id.progressbar_download_unknown, View.VISIBLE);
                                if (mNotificationManager != null) {
                                    mNotificationManager.notify(downloadEvent.task.id, downloadEvent.task.mNotification);
                                }
                            }
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });

        bus
                .toObservable(DownloadFinishEvent.class)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<DownloadFinishEvent>() {
                    @Override
                    public void call(DownloadFinishEvent downloadFinishEvent) {
                        if (mNotificationManager != null) {
                            mNotificationManager.cancel(downloadFinishEvent.task.id);
                        }

                        if (!downloadFinishEvent.isSuccess) {
                           toastUtils.showToast("下载失败");
                        }

                        mTaskMap.remove(downloadFinishEvent.task.mUrl);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int action = intent.getIntExtra(ACTION, 0);

        if (action == ACTION_CANCEL) {
            String url = intent.getStringExtra(DOWNLOAD_URL);
            DownloadTask task = mTaskMap.get(url);

            if (mNotificationManager != null) {
                mNotificationManager.cancel(task.id);
            }
            task.cancel();
        } else {
            String url = intent.getStringExtra(DOWNLOAD_URL);

            if (!TextUtils.isEmpty(url)) {
                if (mTaskMap.containsKey(url)) {
                    toastUtils.showToast("正在下载中...");
                } else {
                    DownloadTask task = new DownloadTask(mTaskMap.size(), url);
                    mTaskMap.put(url, task);
                    task.start();
                    createNotification(task);
                }
            }
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        mNotificationManager = null;
        cancelAll();
        super.onDestroy();
    }

    private void cancelAll() {
        Iterator iterator = mTaskMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            DownloadTask task = (DownloadTask) entry.getValue();
            task.cancel();
        }
    }

    private void createNotification(DownloadTask task) {
        RemoteViews remoteViews = new RemoteViews(this.getPackageName(),
                R.layout.lib_layout_download_notification);
        remoteViews.setProgressBar(R.id.progressbar_download, 100, 0, false);
        remoteViews.setTextViewText(R.id.tv_title, "正在下载" + AppUtils.getUrlFileName(task.mUrl));

        Intent cancelIntent = new Intent(this, DownloadService.class);
        cancelIntent.putExtra(ACTION, ACTION_CANCEL);
        cancelIntent.putExtra(DOWNLOAD_URL, task.mUrl);
        PendingIntent intent = PendingIntent.getService(this, task.id, cancelIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.layout_btn_cancel, intent);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setContent(remoteViews)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(false) //将AutoCancel设为true后，当你点击通知栏的notification后，它会自动被取消消失
                .setPriority(NotificationCompat.PRIORITY_MAX) //从Android4.1开始，可以设置notification的优先级，优先级越高的，通知排的越靠前，优先级低的，不会在手机最顶部的状态栏显示图标
                .setOngoing(true) //将Ongoing设为true 那么notification将不能滑动删除
                .setTicker("下载中，请稍等...");

        task.mNotification = builder.build();
        mNotificationManager.notify(task.id, task.mNotification);
    }
}
