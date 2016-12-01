package com.anthony.library.data.upload;


import com.anthony.library.data.RxBus;
import com.anthony.library.data.event.UploadEventInternal;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;

/**
 * Created by Anthony on 2016/7/8.
 * Class Note:
 *
 */
public class UploadRequestBody extends RequestBody {
    //实际的待包装请求体
    private final RequestBody requestBody;
    //包装完成的BufferedSink
    private BufferedSink bufferedSink;
    private UploadTask task;

    RxBus bus;

    /**
     * 构造函数，赋值
     *
     * @param requestBody 待包装的请求体
     * @param task        上传任务
     */
    public UploadRequestBody(RequestBody requestBody, UploadTask task) {
        this.requestBody = requestBody;
        this.task = task;
    }

    /**
     * 重写调用实际的响应体的contentType
     *
     * @return MediaType
     */
    @Override
    public MediaType contentType() {
        return requestBody.contentType();
    }

    /**
     * 重写调用实际的响应体的contentLength
     *
     * @return contentLength
     * @throws IOException 异常
     */
    @Override
    public long contentLength() throws IOException {
        return requestBody.contentLength();
    }

    /**
     * 重写进行写入
     *
     * @param sink BufferedSink
     * @throws IOException 异常
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
     *
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

               bus.post(new UploadEventInternal(byteCount, task));
//                Log.v("FileUpload", "contentLength: " + contentLength + "\n"+ "bytesWritten: " + bytesWritten);
            }
        };
    }
}