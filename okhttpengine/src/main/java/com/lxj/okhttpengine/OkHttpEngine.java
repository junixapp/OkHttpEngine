package com.lxj.okhttpengine;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.lxj.okhttpengine.callback.OkHttpCallback;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by lxj on 2016/8/6.
 */
public class OkHttpEngine {
    private String TAG = OkHttpEngine.class.getSimpleName();
    private OkHttpClient okhttp;
    private OkHttpClient.Builder builder;
    private HashMap<String, String> headerMap = new HashMap<>();
    private Handler handler = new Handler(Looper.getMainLooper());

    private static OkHttpEngine mInstance = new OkHttpEngine();

    private OkHttpEngine() {
        builder = new OkHttpClient.Builder();
    }

    public static OkHttpEngine create() {
        return mInstance;
    }

    public OkHttpEngine connectTimeout(int timeout, TimeUnit unit) {
        builder.connectTimeout(timeout, unit);
        return mInstance;
    }

    /**
     * 自定义HttpClient
     *
     * @param client
     */
    public void setOkhttpClient(OkHttpClient client) {
        okhttp = client;
    }

    /**
     * 创建okHttpClint
     */
    private void createOkhttpClient() {
        if (okhttp == null) {
            //custom cache policy

            okhttp = builder.build();
        }
    }

    public OkHttpEngine readTimeout(int timeout, TimeUnit unit) {
        builder.readTimeout(timeout, unit);
        return mInstance;
    }

    public OkHttpEngine writeTimeout(int timeout, TimeUnit unit) {
        builder.writeTimeout(timeout, unit);
        return mInstance;
    }

    /**
     * 添加请求头参数
     *
     * @param key
     * @param value
     * @return
     */
    public OkHttpEngine addHeader(String key, String value) {
        headerMap.put(key, value);
        return mInstance;
    }

    public OkHttpEngine setHeaders(HashMap<String, String> headers) {
        if (headers != null) {
            headerMap.putAll(headers);
        }
        return mInstance;
    }

    /**
     * 缓存配置
     *
     * @param cacheDir
     * @param cacheSize
     * @return
     */
    public OkHttpEngine cache(File cacheDir, long cacheSize) {
        builder.cache(new Cache(cacheDir, cacheSize));
        return mInstance;
    }


    /**
     * get请求
     *
     * @param url
     * @param callback
     * @param
     */
    public Call get(String url, OkHttpCallback callback) {
        createOkhttpClient();
        //1.创建请求
        Request.Builder builder = createRequestBuilder(url);
        Request request = builder.build();
        //2.发送请求
        return executeCall(callback, request);
    }

    /**
     * post请求，不需要提交请求体参数
     *
     * @param url
     * @param callback
     * @param
     * @return
     */
    public Call post(String url, OkHttpCallback callback) {
        return post(url, null, callback);
    }

    /**
     * post提交字符串内容，此时需要可以指定字符串的contentType,如果不指定。默认是text/plain
     *
     * @param url
     * @param postContent
     * @param contentType
     * @param callback
     * @param
     */
    public Call post(String url, String postContent, String contentType, OkHttpCallback callback) {
        createOkhttpClient();
        //1.创建请求
        Request.Builder builder = createRequestBuilder(url);
        builder.post(RequestBody.create(MediaType.parse(TextUtils.isEmpty(contentType)
                ? "text/plain" : contentType), postContent));
        Request request = builder.build();
        //2.发送请求
        return executeCall(callback, request);
    }

    /**
     * 进行post请求，支持提交key/value,multipart,多文件上传
     *
     * @param url
     * @param params   将要提交的参数封装到params中
     * @param callback
     * @param
     */
    public Call post(String url, PostParams params, OkHttpCallback callback) {
        createOkhttpClient();
        //1.创建请求
        Request.Builder builder = createRequestBuilder(url);
        if (params != null) {
            RequestBody requestBody = createFormBody(params);
            builder.post(requestBody);
        }
        Request request = builder.build();
        //2.发送请求
        return executeCall(callback, request);
    }

    private Request.Builder createRequestBuilder(String url) {
        Request.Builder builder = new Request.Builder();
        //1.addHeader
        Iterator<Map.Entry<String, String>> iterator = headerMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> entry = iterator.next();
            builder.addHeader(entry.getKey(), entry.getValue());
        }

        builder.url(url);
        return builder;
    }

    /**
     * 执行请求
     *
     * @param callback
     * @param request
     * @param
     */
    private Call executeCall(final OkHttpCallback callback, Request request) {
        Call call = okhttp.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(final Call call, final IOException e) {
                if (callback != null) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onFail(e);
                        }
                    });
                }
            }

            @Override
            public void onResponse(final Call call, final Response response) throws IOException {
                if (callback != null) {
                    //post data to UI Thread
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onSuccess(callback.parseResponse(response));
                        }
                    });
                }
            }
        });
        return call;
    }

    /**
     * 创建PostBody
     *
     * @param params
     * @return
     */
    private RequestBody createFormBody(PostParams params) {
        RequestBody requestBody = null;
        Iterator<Map.Entry<String, Object>> iterator = params.getParams().entrySet().iterator();
        if (params.isMultipart()) {
            MultipartBody.Builder builder = new MultipartBody.Builder();
            while (iterator.hasNext()) {
                Map.Entry<String, Object> entry = iterator.next();
                if (entry.getValue() instanceof String) {
                    builder.addFormDataPart(entry.getKey(), (String) entry.getValue());
                } else if (entry.getValue() instanceof File) {
                    File file = (File) entry.getValue();
                    builder.addFormDataPart(entry.getKey(), file.getName(),
                            RequestBody.create(CommonMediaType.getMediaType(file), file));
                }
            }
            requestBody = builder.build();
        } else {
            FormBody.Builder builder = new FormBody.Builder();
            while (iterator.hasNext()) {
                Map.Entry<String, Object> entry = iterator.next();
                builder.add(entry.getKey(), (String) entry.getValue());
            }
            requestBody = builder.build();
        }
        return requestBody;
    }

}
