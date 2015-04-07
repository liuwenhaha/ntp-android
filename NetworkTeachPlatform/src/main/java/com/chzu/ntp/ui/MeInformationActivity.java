package com.chzu.ntp.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.chzu.ntp.widget.MyTitleView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 个人信息
 *
 * @author yanxing
 */
public class MeInformationActivity extends Activity {

    /**
     * 获取用户详细地址
     */
    private static final String PATH="http://192.168.1.102/ntp/phone/user-info";
    private MyTitleView myTitleView;
    private TextView username;
    private AsyncHttpClient asyncHttpClient=new AsyncHttpClient();
    private static final String TAG="MeInformationActivity";
    private TextView sex,email;
    public  static final String MODIFY_TYPE="修改类型";
    public  static final String MODIFY_EMAIL="修改邮箱";
    public  static final String MODIFY_PWD="修改密码";
    /**
     * 修改邮箱请求码
     */
    private static final int REQUEST_MODIFY_EMAIL=4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_me_information);
        myTitleView = (MyTitleView) findViewById(R.id.myTitleView);
        myTitleView.setTitle("基本信息");
        sex= (TextView) findViewById(R.id.sex);
        email= (TextView) findViewById(R.id.email);
        username = (TextView) findViewById(R.id.username);
        String name = getIntent().getExtras().getString("username");
        username.setText(name);
    }

    /**
     * 从服务器获取用户详细信息
     */
    private void getUser(){
        RequestParams params=new RequestParams();
        params.put("username", username.getText());
        asyncHttpClient.post(PATH, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (response != null) {
                    try {
                        String sexStr = response.getString("sex");
                        String emailStr = response.getString("email");
                        sex.setText(sexStr);
                        email.setText(emailStr);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Log.e(TAG, throwable.toString());
            }
        });
    }

    /**
     * 修改邮箱
     */
    public void modifyEmail(View view) {
        switch (view.getId()){
            case R.id.email:
                Intent intent=new Intent();
                Bundle bundle=new Bundle();
                bundle.putString(MODIFY_TYPE,MODIFY_EMAIL);
                bundle.putString("email",email.getText().toString());
                intent.putExtras(bundle);
                startActivityForResult(intent,REQUEST_MODIFY_EMAIL);
        }

    }

    /**
     * 修改性别
     */
    public void modifySex(View view) {

    }

    /**
     * 修改密码
     */
    public void modifyPwd(View view) {

    }


}
