package com.anthony.app.common.data;

import android.content.Context;

import com.anthony.app.common.injection.scope.ApplicationContext;
import com.anthony.app.common.utils.AppUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

/**
 * Created by Anthony on 2016/7/8.
 * Class Note:
 * entrance class to access network with {@link Retrofit}
 * used only by{@link DataManager} is recommended
 *
 * 使用retrofit进行网络访问的入口类，推荐只在{@link DataManager}中使用
 */
public class HttpHelper {
    private static final int DEFAULT_TIMEOUT = 30;
    private HashMap<String, Object> mServiceMap;
    private Context mContext;


    @Inject
    public HttpHelper(@ApplicationContext Context context) {
        //Map used to store RetrofitService
        mServiceMap = new HashMap<>();
        this.mContext = context;
    }


    @SuppressWarnings("unchecked")
    public <S> S getService(Class<S> serviceClass) {
        if (mServiceMap.containsKey(serviceClass.getName())) {
            return (S) mServiceMap.get(serviceClass.getName());
        } else {
            Object obj = createService(serviceClass);
            mServiceMap.put(serviceClass.getName(), obj);
            return (S) obj;
        }
    }

    @SuppressWarnings("unchecked")
    public <S> S getService(Class<S> serviceClass, OkHttpClient client) {
        if (mServiceMap.containsKey(serviceClass.getName())) {
            return (S) mServiceMap.get(serviceClass.getName());
        } else {
            Object obj = createService(serviceClass, client);
            mServiceMap.put(serviceClass.getName(), obj);
            return (S) obj;
        }
    }

    private <S> S createService(Class<S> serviceClass) {
        //custom OkHttp
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        //time our
        httpClient.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        httpClient.writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        httpClient.readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        //cache
        File httpCacheDirectory = new File(mContext.getCacheDir(), "OkHttpCache");
        httpClient.cache(new Cache(httpCacheDirectory, 10 * 1024 * 1024));
        //Interceptor
        httpClient.addNetworkInterceptor(new LogInterceptor());
        httpClient.addInterceptor(new CacheControlInterceptor());

        return createService(serviceClass, httpClient.build());
    }

    private <S> S createService(Class<S> serviceClass, OkHttpClient client) {
        String end_point = "";
        try {
            Field field1 = serviceClass.getField("end_point");
            end_point = (String) field1.get(serviceClass);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.getMessage();
            e.printStackTrace();
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(end_point)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(client)
                .build();

        return retrofit.create(serviceClass);
    }

    private class LogInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();

            long t1 = System.nanoTime();
            Timber.d("HttpHelper"+String.format("Sending request %s on %s%n%s",
                    request.url(), chain.connection(), request.headers()));

            Response response = chain.proceed(request);
            long t2 = System.nanoTime();

            Timber.d("HttpHelper"+String.format("Received response for %s in %.1fms%n%s",
                    response.request().url(), (t2 - t1) / 1e6d, response.headers()));
            return response;

            // log Response Body
//            if(BuildConfig.DEBUG) {
//                String responseBody = response.body().string();
//                Log.v("HttpHelper", String.format("Received response for %s in %.1fms%n%s%n%s",
//                        response.request().url(), (t2 - t1) / 1e6d, response.headers(), responseBody));
//                return response.newBuilder()
//                        .body(ResponseBody.create(response.body().contentType(), responseBody))
//                        .build();
//            } else {
//                Log.v("HttpHelper", String.format("Received response for %s in %.1fms%n%s",
//                        response.request().url(), (t2 - t1) / 1e6d, response.headers()));
//                return response;
//            }
        }
    }

    private class CacheControlInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            if (!AppUtils.isNetworkConnected(mContext)) {
                request = request.newBuilder()
                        .cacheControl(CacheControl.FORCE_CACHE)
                        .build();
            }

            Response response = chain.proceed(request);

            if (AppUtils.isNetworkConnected(mContext)) {
                int maxAge = 60 * 60; // read from cache for 1 minute
                response.newBuilder()
                        .removeHeader("Pragma")
                        .header("Cache-Control", "public, max-age=" + maxAge)
                        .build();
            } else {
                int maxStale = 60 * 60 * 24 * 28; // tolerate 4-weeks stale
                response.newBuilder()
                        .removeHeader("Pragma")
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                        .build();
            }
            return response;
        }
    }
}
