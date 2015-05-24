package com.ntp.activity.me;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ntp.activity.R;
import com.ntp.activity.PathConstant;
import com.ntp.dao.PreferenceDao;
import com.ntp.util.MD5Util;
import com.ntp.widget.MyProgress;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * 登录
 */
public class LoginActivity extends Activity implements View.OnClickListener {

    private ImageView back;//返回
    private Button login;//登录
    private EditText username, password;
    private TextView register;
    private static final int REQUEST_CODE = 2;//对话框进度请求码
    /**
     * 登陆成功返回结果码
     */
    public static final int RESULT_CODE = 3;
    private static AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        back = (ImageView) findViewById(R.id.back);
        login = (Button) findViewById(R.id.login);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        register = (TextView) findViewById(R.id.register);
        register.setOnClickListener(this);
        back.setOnClickListener(this);
        login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back://返回
                finish();
                break;
            case R.id.login://登录
                final String nameString = username.getText().toString().trim();
                String passwordString = password.getText().toString().trim();
                if (nameString.equals("") | passwordString.equals("")) {
                    Toast.makeText(getApplicationContext(), "用户名或密码不能为空！",
                            Toast.LENGTH_SHORT).show();
                } else {
                    //检查网络是否连接
                    startActivityForResult(new Intent(getApplicationContext(), MyProgress.class), REQUEST_CODE);
                    RequestParams requestParams = new RequestParams();
                    requestParams.put("username", nameString);
                    requestParams.put("password", MD5Util.generatePassword(passwordString));
                    asyncHttpClient.post(PathConstant.PATH_LOGIN, requestParams, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            super.onSuccess(statusCode, headers, response);
                            if (response != null) {
                                try {
                                    String result = response.getString("result");
                                    Log.i(TAG, result);
                                    if (result.equals("success")) {//登陆成功
                                        //如果服务端没有头像，返回的head是error字符串，如果有则是头像名称
                                        String head = response.getString("head");
                                        Log.i(TAG, head);
                                        PreferenceDao.saveLoadName(getApplicationContext(), nameString);
                                        finishActivity(REQUEST_CODE);
                                        Intent intent = new Intent();
                                        Bundle bundle = new Bundle();
                                        bundle.putString("username", nameString);
                                        bundle.putString("head", head);
                                        intent.putExtras(bundle);
                                        setResult(RESULT_CODE, intent);
                                        finish();
                                    } else if (result.equals("error")) {
                                        finishActivity(REQUEST_CODE);
                                        Toast.makeText(getApplicationContext(), "用户名或密码不正确", Toast.LENGTH_SHORT).show();
                                    }
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
                break;
            case R.id.register://注册
                Intent intent = new Intent(this, RegisterActivity.class);
                startActivity(intent);
                break;

        }
    }
}
