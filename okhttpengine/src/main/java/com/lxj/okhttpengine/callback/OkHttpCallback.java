package com.lxj.okhttpengine.callback;

import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by lxj on 2016/9/30.
 */

public abstract class OkHttpCallback<T> {
    protected Class<T> entityClass;

    public OkHttpCallback() {
        Type genType = getClass().getGenericSuperclass();
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        entityClass = (Class) params[0];
    }

    public T parseResponse(Response response) {
        try {
            ResponseBody body = response.body();
            switch (entityClass.getSimpleName()) {
                case "String":
                    return (T) body.string();
                default:
                    return new Gson().fromJson(body.string(),entityClass);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    public abstract void onSuccess(T result);
    public abstract void onFail(IOException e);


}