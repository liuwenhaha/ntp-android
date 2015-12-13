package com.ntp.network.okhttp;

import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * 返回字符串
 * Created by lishuangxiang on 2015/12/13.
 */
public class StringOkHttpResponse implements ResponseInterface<String> {
    @Override
    public String parse(Response response) {
        String content=null;
        try {
            content=response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }
}
