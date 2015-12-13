package com.ntp.network.okhttp;

import com.squareup.okhttp.Response;

/**
 * 对okHttp返回response解析
 * Created by lishuangxiang on 2015/12/13.
 */
public interface ResponseInterface<T> {

    T parse(Response response);
}
