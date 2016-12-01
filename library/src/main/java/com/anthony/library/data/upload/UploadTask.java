package com.anthony.library.data.upload;

import android.app.Notification;
import android.util.Log;

import com.anthony.library.data.HttpHelper;
import com.anthony.library.data.RxBus;
import com.anthony.library.data.event.UploadFinishEvent;
import com.anthony.library.utils.FileTypeUtil;
import com.anthony.library.utils.FileUtil;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;



import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by Anthony on 2016/7/8.
 * Class Note:
 */
public class UploadTask {
    public int id;
    public String mUrl;
    public Subscription mSubscription;
    public Notification mNotification;
    public HashMap<String, File> mFileMap;
    public HashMap<String, String> mParamMap;
    public long total = 0;
    public long progress = 0;
    public int current_percent = 0;

    RxBus bus;

    HttpHelper httpHelper;

    public UploadTask(int id, String mUrl,
                      HashMap<String, File> map, HashMap<String, String> paramMap) {
        this.id = id;
        this.mUrl = mUrl;
        this.mFileMap = map;
        this.mParamMap = paramMap;
    }

    public void start() {
        Map<String, RequestBody> files = new HashMap<>();

        Iterator fileIterator = mFileMap.entrySet().iterator();
        while (fileIterator.hasNext()) {
            Map.Entry entry = (Map.Entry) fileIterator.next();
            String key = (String) entry.getKey();
            File file = (File) entry.getValue();

            RequestBody fileBody = RequestBody.create(MediaType.parse(getContentType(file)), file);
            files.put("" + key + "\"; filename=\"" + FileUtil.getUrlFileName(file.getAbsolutePath()) + "",
                    new UploadRequestBody(fileBody, this));
        }

        Iterator paramIterator = mParamMap.entrySet().iterator();
        while (paramIterator.hasNext()) {
            Map.Entry entry = (Map.Entry) paramIterator.next();
            String key = (String) entry.getKey();
            String val = (String) entry.getValue();

            files.put(key, RequestBody.create(MediaType.parse("text/plain"), val));
        }


        mSubscription = httpHelper.getApi(UploadApi.class)
                .uploadFile(mUrl, files)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ResponseBody>() {
                    @Override
                    public void call(ResponseBody responseBody) {
                        try {
                            Log.v("FileUpload", responseBody.string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                        bus.post(new UploadFinishEvent(UploadTask.this, false));
                    }
                });
    }

    public void cancel() {
        if (mSubscription != null && !mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
    }

    //获取文件的上传类型，图片格式为image/png,image/jpg等。非图片为application/octet-stream
    private String getContentType(File f) {
        String fileType = FileTypeUtil.getFileType(f.getAbsolutePath());

        if (fileType == null || fileType.equals("")) {
            return "application/octet-stream";
        } else {
            return "image/" + fileType;
        }
    }
}
