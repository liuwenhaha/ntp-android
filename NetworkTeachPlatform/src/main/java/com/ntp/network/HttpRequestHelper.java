package com.ntp.network;

import com.ntp.util.ConstantValue;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;


/**
 * 网络请求
 * Created by lishuangxiang on 2015/12/13.
 */
public class HttpRequestHelper {

    private OkHttpClient mOkHttpClient;
    private static HttpRequestHelper mHttpRequestHelper;

    private HttpRequestHelper(){
        mOkHttpClient = new OkHttpClient();
    }

    public static HttpRequestHelper getInstance(){
        if (mHttpRequestHelper==null){
            synchronized(HttpRequestHelper.class){
                if (mHttpRequestHelper == null)
                {
                    mHttpRequestHelper = new HttpRequestHelper();
                }
            }
        }
        return mHttpRequestHelper;
    }

    /**
     * 获取课程列表
     * @param page 获取第几页
     * @param pageSize 每页大小
     * @param callback
     */
    public void getCourseList(int page,int pageSize,Callback callback){
        String url= ConstantValue.PATH_COURSE_LIST;
        String key[]=new String[]{"page","pageSize"};
        String value[]=new String[]{String.valueOf(page),String.valueOf(pageSize)};
        post(url,key,value,callback);
    }

    /**
     * post异步网络请求
     * @param url
     * @param key
     * @param value
     * @param callback
     */
    private void post(String url,String key[],String value[],Callback callback){
        FormEncodingBuilder builder = new FormEncodingBuilder();
        for (int i=0;i<key.length;i++){
            builder.add(key[i],value[i]);
        }
        Request request = new Request.Builder()
                .url(url)
                .post(builder.build())
                .build();
        mOkHttpClient.newCall(request).enqueue(callback);
    }
}
