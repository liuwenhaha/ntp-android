package com.ntp.network.okhttp;

import com.google.gson.Gson;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * 用Gson解析json,返回对象
 * Created by lishuangxiang on 2015/12/13.
 */
public class GsonOkHttpResponse<T> implements ResponseInterface<T> {
    private Class<T> mClass = null;

    public GsonOkHttpResponse(Class<T> mClass) {
        if (mClass == null) {
            throw new IllegalArgumentException(mClass + " Class can't be null");
        }
        this.mClass = mClass;
    }

    @Override
    public T parse(Response response) {
        try {
            Gson gson = new Gson();
            String content = response.body().string();
            T t = gson.fromJson(content,mClass);
            return t;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
