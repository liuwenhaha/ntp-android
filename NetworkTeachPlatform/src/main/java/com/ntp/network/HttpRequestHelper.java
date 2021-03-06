package com.ntp.network;

import com.ntp.network.okhttp.ObjectCallbackHandler;
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
    public void getCourseList(int page,int pageSize,ObjectCallbackHandler callback){
        String url= ConstantValue.PATH_COURSE_LIST;
        String key[]=new String[]{"page","pageSize"};
        String value[]=new String[]{String.valueOf(page),String.valueOf(pageSize)};
        post(url,key,value,callback);
    }

    /**
     * 获取我的课程
     * @param username 用户名
     * @param callback
     */
    public void getMyCourse(String username,ObjectCallbackHandler callback){
        String url= ConstantValue.PATH_MY_COURSE;
        String key[]=new String[]{"username"};
        String value[]=new String[]{username};
        post(url,key,value,callback);
    }

    /**
     * 获取评论消息列表
     * @param username
     * @param currentPage 当前页
     * @param pageSize
     * @param objectCallbackHandler
     */
    public void getCommentNoticeList(String username,String currentPage,String pageSize,ObjectCallbackHandler objectCallbackHandler){
        String url= ConstantValue.PATH_FORUM_COMMENT;
        String key[]=new String[]{"username","page","pageSize"};
        String value[]=new String[]{username,currentPage,pageSize};
        post(url,key,value, objectCallbackHandler);
    }

    /**
     * 获取作业列表
     * @param username
     * @param currentPage 当前页
     * @param pageSize
     * @param objectCallbackHandler
     */
    public void getHomeworkList(String username,String currentPage,String pageSize,ObjectCallbackHandler objectCallbackHandler){
        String url= ConstantValue.PATH_MY_HOMEWORK;
        String key[]=new String[]{"username","page","pageSize"};
        String value[]=new String[]{username,currentPage,pageSize};
        post(url,key,value, objectCallbackHandler);
    }

    /**
     * 获取课程问题列表
     * @param page 获取第几页
     * @param pageSize 每页大小
     * @param code 课程代码
     * @param callback
     */
    public void getForumList(int page,int pageSize,String code,ObjectCallbackHandler callback){
        String url= ConstantValue.PATH_COURSE_FORUM;
        String key[]=new String[]{"page","pageSize","code"};
        String value[]=new String[]{String.valueOf(page),String.valueOf(pageSize),code};
        post(url,key,value,callback);
    }

    /**
     * 获取课程问题列表
     * @param page 获取第几页
     * @param pageSize 每页大小
     * @param forumID 问题ID
     * @param callback
     */
    public void getForumReplyList(int page,int pageSize,String forumID,ObjectCallbackHandler callback){
        String url= ConstantValue.PATH_COURSE_FORUM_ALL;
        String key[]=new String[]{"page","pageSize","forumId"};
        String value[]=new String[]{String.valueOf(page),String.valueOf(pageSize),forumID};
        post(url,key,value,callback);
    }

    /**
     * 回复课程问题
     * @param name
     * @param content
     * @param forumId
     * @param callback
     */
    public void reply(String name,String content,String forumId ,Callback callback){
        String url= ConstantValue.PATH_COURSE_FORUM_REPLY;
        String key[]=new String[]{"name","comment","forumId"};
        String value[]=new String[]{name,content,forumId};
        post(url,key,value,callback);
    }

    /**
     * 获取课程类型
     * @param callback
     */
    public void getCourseTypeList(ObjectCallbackHandler callback){
        String url=ConstantValue.PATH_COURSE_TYPE_LIST;
        post(url,new String[]{},new String[]{},callback);
    }

    /**
     * 获取课程简介
     * @param callback
     */
    public void getCourseOverview(String code,ObjectCallbackHandler callback){
        String url=ConstantValue.PATH_COURSE_DETAIL;
        post(url,new String[]{"code"},new String[]{code},callback);
    }

    /**
     * 获取课程课件
     * @param callback
     */
    public void getCourseware(String code,ObjectCallbackHandler callback){
        String url=ConstantValue.PATH_COURSE_WARE;
        post(url,new String[]{"code"},new String[]{code},callback);
    }

    /**
     * 获取课程视频
     * @param callback
     */
    public void getCourseVideo(String code,ObjectCallbackHandler callback){
        String url=ConstantValue.PATH_COURSE_VIDEO;
        post(url,new String[]{"code"},new String[]{code},callback);
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
