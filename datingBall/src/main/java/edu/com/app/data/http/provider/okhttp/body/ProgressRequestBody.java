package edu.com.app.data.http.provider.okhttp.body;

import android.os.Handler;


import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;

import java.io.IOException;

import edu.com.app.data.http.callback.FileUploadHttpCallback;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;

/**
 * Created by Anthony on 2016/3/2.
 * Class Note:
 * encapsulation of the request body,use to upload file
 * 请求体封装，用于上传文件
 */
public class ProgressRequestBody extends RequestBody {
    //实际的待包装请求体
    private final RequestBody requestBody;
    //进度回调接口
    private final FileUploadHttpCallback progressListener;

    //包装完成的BufferedSink
    private BufferedSink bufferedSink;
    private Handler delivery;

    /**
     *constructor ,pass params
     * @param requestBody request body need to be encapsulation
     * @param progressListener callback interface
     * @param delivery
     */
    public ProgressRequestBody(RequestBody requestBody, FileUploadHttpCallback progressListener, Handler delivery) {
        this.requestBody = requestBody;
        this.progressListener = progressListener;
        this.delivery = delivery;
    }

    @Override
    public MediaType contentType() {
        return requestBody.contentType();
    }

    @Override
    public long contentLength() throws IOException {
        return requestBody.contentLength();
    }

    /**
     * override to write
     *
     * @param sink BufferedSink
     * @throws IOException
     */
    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        if (bufferedSink == null) {
            //包装
            bufferedSink = Okio.buffer(sink(sink));
        }
        //写入
        requestBody.writeTo(bufferedSink);
        //必须调用flush，否则最后一部分数据可能不会被写入
        bufferedSink.flush();

    }


    /**
     * 写入，回调进度接口
     *write ,callback sthe process interface
     * @param sink Sink
     * @return Sink
     */
    private Sink sink(Sink sink) {
        return new ForwardingSink(sink) {
            //当前写入字节数
            long bytesWritten = 0L;
            //总字节长度，避免多次调用contentLength()方法
            long contentLength = 0L;

            @Override
            public void write(Buffer source, long byteCount) throws IOException {
                super.write(source, byteCount);
                if (contentLength == 0) {
                    //获得contentLength的值，后续不再调用
                    contentLength = contentLength();
                }
                //增加当前写入的字节数
                bytesWritten += byteCount;
                //回调
                if (progressListener != null) {
                    delivery.post(new Runnable() {
                        @Override
                        public void run() {
                            progressListener.onProgress(bytesWritten, contentLength, bytesWritten == contentLength);
                        }
                    });
                }
            }
        };
    }
}
