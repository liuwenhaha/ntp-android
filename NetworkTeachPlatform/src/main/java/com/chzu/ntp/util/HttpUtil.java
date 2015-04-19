package com.chzu.ntp.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
 * 服务器返回的是json数据
 *
 * @author yanxing
 */
public class HttpUtil {

    /**
     * 获取json数据，使用HttpURLConnection
     *
     * @param requestMethod 提交方法。为空时，默认post提交
     */
    public static JSONObject getDataFromInternet(URL url, Object requestMethod) {
        JSONObject jb = null;
        try {
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            if (requestMethod == null) {
                conn.setRequestMethod("POST");
            } else {
                conn.setRequestMethod((String) requestMethod);
            }
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

    /**
     * 获取网络图片
     *
     * @param path 图片路径
     * @return
     */
    public static Bitmap getImage(String path) throws Exception {
        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(6000);
        conn.setRequestMethod("GET");
        if (conn.getResponseCode() == 200) {
            InputStream inStream = conn.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(inStream);
            return bitmap;
        }
        return null;
    }
}
