package com.ntp.network.okhttp;

import android.os.Handler;
import android.os.Looper;

import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * 封装Callback,在onResponse更新UI
 * Created by lishuangxiang on 2015/12/13.
 */
public class CallbackHandler<T> implements com.squareup.okhttp.Callback {

    private ResponseInterface<T> mResponseInterface;
    private Handler mHandler;

    public CallbackHandler(ResponseInterface<T> mResponseInterface) {

        if (mResponseInterface == null) {
            throw new IllegalArgumentException(mResponseInterface + " can't be null");
        }
        mHandler=new Handler(Looper.getMainLooper());
        this.mResponseInterface = mResponseInterface;
    }

    @Override
    public void onFailure(final Request request, final IOException e) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
              onFailure(request,e,100);
            }
        });
    }

    @Override
    public void onResponse(Response response) throws IOException {
        if (response.isSuccessful()){
            final T parseResult = mResponseInterface.parse(response);
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    onResponse(parseResult);
                }
            });
        }
    }

    public void onResponse(T t){
    }

    public void onFailure(Request request,IOException e,int response){
    }
}
