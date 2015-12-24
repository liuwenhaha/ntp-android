package com.ntp.network.okhttp;

import android.os.Handler;
import android.os.Looper;

import com.google.gson.Gson;
import com.google.gson.internal.$Gson$Types;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 封装Callback,返回对象，可在onResponse中更新UI
 * Created by lishuangxiang on 2015/12/13.
 */
public class ObjectCallbackHandler<T> implements com.squareup.okhttp.Callback {

    private Handler mHandler;
    private Type mType;

    public ObjectCallbackHandler() {
        mType = getSuperclassTypeParameter(getClass());
        mHandler=new Handler(Looper.getMainLooper());
    }

    static Type getSuperclassTypeParameter(Class<?> subclass)
    {
        Type superclass = subclass.getGenericSuperclass();
        ParameterizedType parameterized = (ParameterizedType) superclass;
        return $Gson$Types.canonicalize(parameterized.getActualTypeArguments()[0]);
    }

    @Override
    public void onFailure(final Request request, final IOException e) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
              onFailure(request,e,0);
            }
        });
    }

    @Override
    public void onResponse(Response response){
        if (response.isSuccessful()){
            String content;
            final T t;
            try {
                content = response.body().string();
                Gson gson = new Gson();
                t = gson.fromJson(content,mType);
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        onResponse(t);
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void onResponse(T t){
    }

    public void onFailure(Request request,IOException e,int response){
    }
}
