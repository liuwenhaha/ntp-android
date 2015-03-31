package com.chzu.ntp.util;

import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/**
 * 工具类，网络访问
 * 访问服务器类，客户端从后台获取数据都通过这个类
 * 服务器返回的是json数据
 *
 * @author yanxing
 */
public class HttpUtil {

    private static JSONObject msg;
    private static AsyncHttpClient client = new AsyncHttpClient();
    private static final String TAG = "TAG";

    /**
     * 异步访问服务器端，获取json对象
     *
     * @param path 访问的网络路径
     * @param maps 网络请求参数，是个Map对象，其中的键值对代表：键为后台接收的成员变量名，值为该成员变量的值
     * @return
     */
    public static JSONObject getDataFromInternet(String path, Map<String, String> maps) {

        RequestParams params = new RequestParams();
        for (Map.Entry<String, String> entry : maps.entrySet()) {
            params.put(entry.getKey(), entry.getValue());
        }
        client.post(path, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  JSONObject response) {
                msg = response;
                super.onSuccess(statusCode, headers, response);

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Log.w(TAG, responseString);
                Log.w(TAG, throwable.toString());
            }
        });
        return msg;
    }

    /**
     * 异步访问服务器端，获取json对象
     *
     * @param path 访问的网络路径
     * @return
     */
    public static JSONObject getDataFromInternet(String path) {

        client.post(path, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  JSONObject response) {
                msg = response;
                super.onSuccess(statusCode, headers, response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Log.w(TAG, responseString);
                Log.w(TAG, throwable.toString());
            }
        });
        return msg;
    }

    /**
     * 获取json数据，使用HttpURLConnection，内部没有使用线程，
     */
    public static JSONObject getDataFromInternet(URL url,String requestMethod) {
        JSONObject jb = null;
        try {
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(requestMethod);
            conn.setConnectTimeout(6 * 1000);//设置超时时间为6s
            conn.setRequestProperty("Charset", "UTF-8");
            if (conn.getResponseCode() == 200) {
                InputStream json = conn.getInputStream();
                byte[] data = read(json);
                jb = new JSONObject(new String(data));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jb;
    }

    /**
     * 把InputStream转化成字节数组
     */
    public static byte[] read(InputStream inStream) throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, len);
        }
        inStream.close();
        return outputStream.toByteArray();
    }
}
