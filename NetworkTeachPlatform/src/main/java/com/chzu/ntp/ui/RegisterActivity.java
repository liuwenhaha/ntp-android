package com.chzu.ntp.ui;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.chzu.ntp.util.MD5Util;
import com.chzu.ntp.util.NetworkState;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 注册
 * @author yanxing
 *
 */
public class RegisterActivity extends Activity{

    /**
     * 验证用户名是否存在
     */
    private static final String PATH = "http://192.168.1.113/ntp/phone/user-exist";
    /**
     * 注册地址
     */
    private static final String PATH_REGISTER= "http://192.168.1.113/ntp/phone/register";
    private EditText username,password;
    private Button register;
    private AsyncHttpClient asyncHttpClient=new AsyncHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        username= (EditText) findViewById(R.id.username);
        password= (EditText) findViewById(R.id.pwd);
        register= (Button) findViewById(R.id.register);
        username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                RequestParams params=new RequestParams();
                params.put("username",s.toString().trim());
                if (!NetworkState.isNetworkConnected(getApplicationContext())){
                    Toast.makeText(getApplicationContext(),"请连接网络再试",Toast.LENGTH_SHORT).show();
                    return;
                }
                asyncHttpClient.post(PATH, params, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        if (response != null) {
                            try {
                                String result = response.getString("result");
                                if (result.equals("exist")){
                                    Toast.makeText(getApplicationContext(),"该用户名已存在",Toast.LENGTH_SHORT).show();
                                    register.setClickable(false);
                                }else {
                                    register.setClickable(true);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
            }
        });
    }


    /**
     * 注册事件
     */
    public void register(View view){
        if (view.getId()==R.id.register){
           if (username.getText().toString().trim().equals("")){
                Toast.makeText(getApplicationContext(),"用户名不能为空",Toast.LENGTH_SHORT).show();
                return;
            }
            else if (password.getText().toString().trim().equals("")){
                Toast.makeText(getApplicationContext(),"请输入密码",Toast.LENGTH_SHORT).show();
                return;
            }else{
                RequestParams params=new RequestParams();
                params.put("username", username.getText().toString().trim());
                params.put("password", MD5Util.generatePassword(password.getText().toString().trim()));
                if (!NetworkState.isNetworkConnected(getApplicationContext())){
                    Toast.makeText(getApplicationContext(),"请连接网络再试",Toast.LENGTH_SHORT).show();
                    return;
                }
                asyncHttpClient.post(PATH_REGISTER, params, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        if (response != null) {
                            try {
                                String result = response.getString("result");
                                if (result.equals("success")){
                                    Toast.makeText(getApplicationContext(),"注册成功",Toast.LENGTH_SHORT).show();
                                    finish();
                                }else {
                                    Toast.makeText(getApplicationContext(),"注册失败，稍后再试",Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
            }
        }
    }
}
