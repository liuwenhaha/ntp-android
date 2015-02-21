package com.chzu.ntp.util;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
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

    /**
     * 异步访问服务器端，获取json对象
     *
     * @param path 访问的网络路径
     * @param maps 网络请求参数，是个Map对象，其中的键值对代表：键为后台接收的成员变量名，值为该成员变量的值
     * @return
     */
    public static JSONObject getDataFromInternet(String path, Map<String, String> maps) {

        AsyncHttpClient client = new AsyncHttpClient();
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

        AsyncHttpClient client = new AsyncHttpClient();
        client.post(path, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  JSONObject response) {
                msg = response;
                super.onSuccess(statusCode, headers, response);

            }
        });
        return msg;
    }

    /**
     * 获取json数据，使用HttpURLConnection，内部没有使用线程，
     */
    public static JSONObject getDataFromInternet(URL url) {
        JSONObject jb = null;
        try {
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setConnectTimeout(8 * 1000);//设置超时时间为8s
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
