package com.lxj.okhttpengine;

import java.io.IOException;

/**
 * Created by lxj on 2016/9/30.
 */

public interface OkHttpCallback{
    void onSuccess(String result);
    void onFail(IOException e);
}