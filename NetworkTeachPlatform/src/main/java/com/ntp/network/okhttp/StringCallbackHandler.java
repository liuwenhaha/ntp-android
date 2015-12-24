package com.ntp.network.okhttp;

import android.os.Handler;
import android.os.Looper;

import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * 封装Callback,返回字符串，可在onResponse中更新UI
 * Created by lishuangxiang on 2015/12/24.
 */
public class StringCallbackHandler implements com.squareup.okhttp.Callback  {

    private Handler mHandler;

    public StringCallbackHandler(){
        mHandler=new Handler(Looper.getMainLooper());
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
    public void onResponse(Response response) {
        String content;
        try {
            content=response.body().string();
            final String finalContent = content;
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    onResponse(finalContent);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onResponse(String response){
    }

    public void onFailure(Request request,IOException e,int response){
    }
}
