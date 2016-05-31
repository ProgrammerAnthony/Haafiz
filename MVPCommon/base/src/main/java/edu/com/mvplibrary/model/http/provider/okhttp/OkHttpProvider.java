package edu.com.mvplibrary.model.http.provider.okhttp;

import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;


import com.squareup.okhttp.Cache;
import com.squareup.okhttp.CacheControl;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import edu.com.mvplibrary.AbsApplication;
import edu.com.mvplibrary.model.config.Constants;
import edu.com.mvplibrary.model.http.HttpUtil;
import edu.com.mvplibrary.model.http.callback.BaseHttpCallback;
import edu.com.mvplibrary.model.http.callback.FileDownloadHttpCallback;
import edu.com.mvplibrary.model.http.callback.FileUploadHttpCallback;
import edu.com.mvplibrary.model.http.callback.StringHttpCallback;
import edu.com.mvplibrary.model.http.provider.BaseHttpProvider;
import edu.com.mvplibrary.model.http.provider.okhttp.body.ProgressRequestBody;
import edu.com.mvplibrary.model.http.provider.okhttp.body.ProgressResponseBody;
import edu.com.mvplibrary.model.http.request.HttpRequest;
import edu.com.mvplibrary.util.FileUtil;

/**
 * Created by Anthony on 2016/3/2.
 * Class Note:provide common operation using OkHttp
 */
public class OkHttpProvider extends BaseHttpProvider {
    private OkHttpClient mOkHttpClient;
    private Handler mDelivery;
    private HashMap<String, OkHttpClient> mDownloadMap;

    public OkHttpProvider() {
        mOkHttpClient = new OkHttpClient();
        mDelivery = new Handler(Looper.getMainLooper());
        File cacheDir = AbsApplication.app().getCacheDir();
        int cacheSize = 20 * 1024 * 1024;
        mOkHttpClient.setCache(new Cache(cacheDir.getAbsoluteFile(), cacheSize));

        //        mOkHttpClient.setConnectTimeout(10, TimeUnit.SECONDS);
//        mOkHttpClient.setWriteTimeout(10, TimeUnit.SECONDS);
//        mOkHttpClient.setReadTimeout(30, TimeUnit.SECONDS);
    }

    /**
     * load String use HttpRequest ,call back is StringHttpCallback
     *
     * @param customRequest http request wrapped by ourselves
     * @param callback      callback of String Http
     */
    @Override
    public void loadString(HttpRequest customRequest, final StringHttpCallback callback) {
        //get params wrapped in the custom HttpRequest
        int method = customRequest.getMethod();
        final boolean isNeedCache = customRequest.getIsNeedCache();
        HashMap<String, String> headers = customRequest.getHeaders();
        HashMap<String, String> params = customRequest.getParams();
        String url = customRequest.getUrl();
        String tag = customRequest.getTag();

//        if (TextUtils.isEmpty(url)) {
//            return;
//        }
        //if not start with http,load local String
        if (!url.startsWith(Constants.HTTP_PREFIX) && !url.startsWith(Constants.HTTPS_PREFIX)) {
            loadLocalString(url, callback);
            return;
        }

        final Request.Builder builder = new Request.Builder();
        builder.cacheControl(CacheControl.FORCE_NETWORK);
        if (!TextUtils.isEmpty(tag)) {
            builder.tag(tag);
        }
        //add Http Header to the builder of OkHttp
        if (headers != null && headers.size() > 0) {
            Iterator iterator = headers.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry entry = (Map.Entry) iterator.next();
                String key = (String) entry.getKey();
                String val = (String) entry.getValue();
                builder.addHeader(key, val);
            }
        }
        //add POST or GET Params to OkHttp from our custom Request
        if (params != null && params.size() > 0) {
            if (method == HttpUtil.POST) {
                builder.post(getRequestBody(params));
            } else {
                url = getEncodedUrl(url, params);
            }
        }
        builder.url(url);
        //new OkHttp Request and enqueue to load network
        Request request = builder.build();
        onStart(callback);

        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                final String result;
                result = e.getMessage();
                //failure at first time ,then request again;
                if ((e instanceof SocketTimeoutException || e instanceof UnknownHostException)
                        && isNeedCache) {
                    builder.cacheControl(CacheControl.FORCE_CACHE);
                    mOkHttpClient.newCall(builder.build()).enqueue(new Callback() {
                        @Override
                        public void onFailure(Request request, IOException e) {
                        }

                        @Override
                        public void onResponse(Response response) throws IOException {
                            String result;
                            result = response.body().string();
                            if (response.isSuccessful()) {
                                onSuccessDelivery(callback, result);
                            } else if (response.code() == 504) {
                                //No Cache yet
                                onSuccessDelivery(callback, "");
                            } else {
                                onFailDelivery(callback, result);
                            }
                        }
                    });
                } else {
                    onFailDelivery(callback, result);
                }
            }

            @Override
            public void onResponse(Response response) throws IOException {
                final String result;
                if (response.isSuccessful()) {
                    result = response.body().string();
                    onSuccessDelivery(callback, result);
                } else {
                    result = response.code() + ": " + response.message();
                    onFailDelivery(callback, result);
                }
            }
        });
    }

    /**
     * upload file with HttpRequest and the callback is FileUploadHttpCallback
     *
     * @param customRequest HttpRequest
     * @param callback      FileUploadHttpCallback
     */
    @Override
    public void uploadFile(HttpRequest customRequest, final FileUploadHttpCallback callback) {
        HashMap<String, String> headers = customRequest.getHeaders();
        HashMap<String, String> params = customRequest.getParams();
        HashMap<String, File> files = customRequest.getFiles();
        String url = customRequest.getUrl();
        String tag = customRequest.getTag();

        Request.Builder builder = new Request.Builder();
        builder.url(url);
        builder.cacheControl(CacheControl.FORCE_NETWORK);
        if (!TextUtils.isEmpty(tag)) {
            builder.tag(tag);
        }

        //Add Http Header
        if (headers != null && headers.size() > 0) {
            Iterator iterator = headers.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry entry = (Map.Entry) iterator.next();
                String key = (String) entry.getKey();
                String val = (String) entry.getValue();
                builder.addHeader(key, val);
            }
        }

        //Add Post Params
        if ((params != null && params.size() > 0) || (files != null && files.size() > 0)) {
            builder.post(new ProgressRequestBody(getUploadRequestBody(params, files),
                    callback, mDelivery));
        }

        Request request = builder.build();
        onStart(callback);

        OkHttpClient client = mOkHttpClient.clone();
        client.setConnectTimeout(30, TimeUnit.SECONDS);
        client.setWriteTimeout(30, TimeUnit.SECONDS);
        client.setReadTimeout(30, TimeUnit.SECONDS);

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                final String result;
                result = e.getMessage();
                onFailDelivery(callback, result);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                final String result;
                if (response.isSuccessful()) {
                    result = response.body().string();
                    onUploadSuccessDelivery(callback, result);
                } else {
                    result = response.code() + ": " + response.message();
                    onFailDelivery(callback, result);
                }
            }
        });
    }

    /**
     * download file with HttpRequest,callback is FileDownloadHttpCallback
     *
     * @param customRequest HttpRequest
     * @param callback      FileDownloadHttpCallback
     */
    @Override
    public void downloadFile(HttpRequest customRequest, final FileDownloadHttpCallback callback) {
        HashMap<String, String> headers = customRequest.getHeaders();
        HashMap<String, String> params = customRequest.getParams();
        final String url = customRequest.getUrl();
        String tag = customRequest.getTag();

        Request.Builder builder = new Request.Builder();
        builder.url(url);
        builder.cacheControl(CacheControl.FORCE_NETWORK);
        if (!TextUtils.isEmpty(tag)) {
            builder.tag(tag);
        }

        //Add Http Header
        if (headers != null && headers.size() > 0) {
            Iterator iterator = headers.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry entry = (Map.Entry) iterator.next();
                String key = (String) entry.getKey();
                String val = (String) entry.getValue();
                builder.addHeader(key, val);
            }
        }

        //Add Post Params
        if (params != null && params.size() > 0) {
            builder.post(getRequestBody(params));
        }

        Request request = builder.build();
        onStart(callback);

        OkHttpClient client = addDownloadInterceptor(mOkHttpClient, callback);
        client.setConnectTimeout(30, TimeUnit.SECONDS);
        client.setWriteTimeout(30, TimeUnit.SECONDS);
        client.setReadTimeout(30, TimeUnit.SECONDS);

        if (!TextUtils.isEmpty(tag)) {
            mDownloadMap.put(tag, client);
        }

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                final String result;
                result = e.getMessage();
                onFailDelivery(callback, result);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                final String result;
                if (response.isSuccessful()) {
                    try {
                        File file = FileUtil.writeFile(response.body().byteStream(),
                                Environment.getExternalStorageDirectory().getAbsolutePath()
                                        + "/Download/"
                                        + url.substring(url.lastIndexOf("/") + 1), false);
                        onDownloadSuccessDelivery(callback, file);
                    } catch (Exception e) {
                        e.printStackTrace();
                        onFailDelivery(callback, "File Write Failed!");
                    }
                } else {
                    result = response.code() + ": " + response.message();
                    onFailDelivery(callback, result);
                }
            }
        });
    }

    private OkHttpClient addDownloadInterceptor(OkHttpClient client, final FileDownloadHttpCallback progressListener) {
        OkHttpClient clone = client.clone();
        //增加拦截器
        clone.networkInterceptors().add(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                //拦截
                Response originalResponse = chain.proceed(chain.request());
                //包装响应体并返回
                return originalResponse.newBuilder()
                        .body(new ProgressResponseBody(originalResponse.body(), progressListener, mDelivery))
                        .build();
            }
        });
        return clone;
    }

    private RequestBody getUploadRequestBody(HashMap<String, String> params, HashMap<String, File> files) {
        MultipartBuilder builder = new MultipartBuilder().type(MultipartBuilder.FORM);

        Iterator iteratorParams = params.entrySet().iterator();
        while (iteratorParams.hasNext()) {
            Map.Entry entry = (Map.Entry) iteratorParams.next();
            String key = (String) entry.getKey();
            String val = (String) entry.getValue();
            builder.addFormDataPart(key, val);
        }

        Iterator iteratorFiles = files.entrySet().iterator();
        while (iteratorFiles.hasNext()) {
            Map.Entry entry = (Map.Entry) iteratorFiles.next();
            String key = (String) entry.getKey();
            File val = (File) entry.getValue();
            builder.addFormDataPart(key, val.getName(), RequestBody.create(MediaType.parse(getContentType(val)), val));
        }

        return builder.build();
    }


    private void onFailDelivery(final BaseHttpCallback callback, final String result) {
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                callback.onEnd();
                callback.onError(result);
            }
        });
    }

    private void onSuccessDelivery(final StringHttpCallback callback, final String result) {
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                callback.onEnd();
                callback.onResponse(result);
            }
        });
    }

    private void onStart(final BaseHttpCallback callback) {
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                callback.onStart();
            }
        });
    }


    private String getEncodedUrl(String url, HashMap<String, String> params) {
        StringBuilder builder = new StringBuilder();
        builder.append(url).append("?");
        Iterator iterator = params.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            String key = (String) entry.getKey();
            String val = (String) entry.getValue();

            try {
                key = URLEncoder.encode(key, "UTF-8");
                val = URLEncoder.encode(val, "UTF-8");
                builder.append(key).append("=").append(val).append("&");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        String result = builder.toString();
        int len = result.length();

        return result.substring(0, len - 1);
    }

    private RequestBody getRequestBody(HashMap<String, String> params) {
        FormEncodingBuilder builder = new FormEncodingBuilder();
        Iterator iterator = params.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            String key = (String) entry.getKey();
            String val = (String) entry.getValue();
            builder.add(key, val);
        }
        return builder.build();
    }


    /**
     * get upload type of file,Image type is image/png,image/jpg and so on ,
     * application.octet-stream is not image.
     */
    private String getContentType(File f) {
        String fileType = FileUtil.getFileType(f.getAbsolutePath());

        if (fileType == null || fileType.equals("")) {
            return "application/octet-stream";
        } else {
            return "image/" + fileType;
        }
    }

    private void onUploadSuccessDelivery(final FileUploadHttpCallback callback, final String result) {
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                callback.onEnd();
                callback.onResponse(result);
            }
        });
    }

    private void onDownloadSuccessDelivery(final FileDownloadHttpCallback callback, final File file) {
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                callback.onEnd();
                callback.onResponse(file);
            }
        });
    }

    public Handler getDelivery() {
        return mDelivery;
    }

    public OkHttpClient getClient() {
        return mOkHttpClient;
    }
}
