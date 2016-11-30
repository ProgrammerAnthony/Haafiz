package com.anthony.app.common.data.download;

import android.app.Notification;
import android.content.Context;

import com.anthony.app.common.Constants;
import com.anthony.app.common.data.HttpHelper;
import com.anthony.app.common.data.RxBus;
import com.anthony.app.common.utils.FileUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;

import okhttp3.ResponseBody;
import rx.Subscription;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by Anthony on 2016/6/12.
 * Class Note:
 *
 */
public class DownloadTask implements Serializable {
    private Context mContext;
    public int id;
    public String mUrl;
    public Subscription mSubscription;
    public Notification mNotification;
    public int current_percent = 0;
    public boolean isUnknownLength = false;

    HttpHelper httpHelper;


    public DownloadTask(int id, String mUrl, Context context) {
        this.id = id;
        this.mUrl = mUrl;
        this.mContext = context;
    }

    public void start() {
        httpHelper = new HttpHelper(mContext);
        mSubscription = httpHelper.getApi(DownloadApi.class)
                .downloadFile(mUrl)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new Action1<ResponseBody>() {
                    @Override
                    public void call(ResponseBody responseBody) {
                        boolean result = writeResponseBodyToDisk(responseBody, FileUtil.getUrlFileName(mUrl));
                        RxBus.getDefault().post(new DownloadFinishEvent(DownloadTask.this, result));
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                        RxBus.getDefault().post(new DownloadFinishEvent(DownloadTask.this, false));
                    }
                });
    }

    public void cancel() {
        if (mSubscription != null && !mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
    }

    private boolean writeResponseBodyToDisk(ResponseBody body, String fileName) {
        try {
            String store_path = Constants.DOWNLOAD_STORE_FOLDER;
            File futureStudioIconFile = new File(store_path + fileName);

            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[4096];

                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(futureStudioIconFile);

                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }

                    outputStream.write(fileReader, 0, read);

                    fileSizeDownloaded += read;

                    RxBus.getDefault().post(new DownloadEvent(mUrl, fileSize, fileSizeDownloaded, this));
//                    Log.d("FileDownload", "file download: " + fileSizeDownloaded + " of " + fileSize);
                }

                outputStream.flush();
                return true;
            } catch (IOException e) {
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return false;
        }
    }
}
