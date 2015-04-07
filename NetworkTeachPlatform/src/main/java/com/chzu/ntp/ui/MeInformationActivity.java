package com.chzu.ntp.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.chzu.ntp.dao.UserDao;
import com.chzu.ntp.model.User;
import com.chzu.ntp.util.NetworkState;
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
    /**
     * 修改邮箱
     */
    private static final String PATH_EMAIL="http://192.168.1.102/ntp/phone/user-info";
    /**
     * 修改密码
     */
    private static final String PATH_PWD="http://192.168.1.102/ntp/phone/user-info";
    private MyTitleView myTitleView;
    private TextView username;
    private AsyncHttpClient asyncHttpClient=new AsyncHttpClient();
    private static final String TAG="MeInformationActivity";
    private TextView sex,email;
    public  static final String MODIFY_TYPE="修改类型";
    public  static final String MODIFY_EMAIL="修改邮箱";
    public  static final String MODIFY_PWD="修改密码";
    private UserDao userDao;
    /**
     * 修改邮箱请求码
     */
    private static final int REQUEST_MODIFY_EMAIL=4;
    /**
     * 修改密码请求码
     */
    private static final int REQUEST_MODIFY_PWD=5;

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
        userDao = new UserDao(getApplicationContext());
        User user = userDao.findByName(name);
        if (user.getUsername() != null) {
            Log.i(TAG, "本地有缓存信息");
            sex.setText(user.getSex());
            email.setText(user.getEmail());
        } else {
            getUser();
        }

    }

    /**
     * 从服务器获取用户详细信息
     */
    private void getUser(){
        RequestParams params=new RequestParams();
        params.put("username", username.getText());
        if (!NetworkState.isNetworkConnected(getApplicationContext())){
            Toast.makeText(getApplicationContext(),"请连接网络再试",Toast.LENGTH_SHORT).show();
            return;
        }
        asyncHttpClient.post(PATH, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.i(TAG, response.toString());
                if (response != null) {
                    try {
                        JSONObject jb = response.getJSONObject("user");
                        String sexStr = jb.getString("sex");
                        String emailStr = jb.getString("email");
                        sex.setText(sexStr);
                        email.setText(emailStr);
                        //缓存到本地
                        User user = new User();
                        user.setUsername(username.getText().toString());
                        user.setEmail(emailStr);
                        user.setSex(sexStr);
                        userDao.save(user);
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
            case R.id.emailLayout:
                Intent intent=new Intent(getApplicationContext(),ModifyUserInfoActivity.class);
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
        if (view.getId() == R.id.pwdLayout) {
            Intent intent=new Intent(getApplicationContext(),ModifyUserInfoActivity.class);
            Bundle bundle=new Bundle();
            bundle.putString(MODIFY_TYPE, MODIFY_PWD);
            intent.putExtras(bundle);
            startActivityForResult(intent,REQUEST_MODIFY_PWD);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            if (requestCode==REQUEST_MODIFY_EMAIL){//邮箱修改
                final String emailStr=data.getExtras().getString("email");
                RequestParams params=new RequestParams();
                params.put("username",username.getText());
                params.put("email", emailStr);
                if (!NetworkState.isNetworkConnected(getApplicationContext())){
                    Toast.makeText(getApplicationContext(),"请连接网络再试",Toast.LENGTH_SHORT).show();
                    return;
                }
                asyncHttpClient.post(PATH_EMAIL, params, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        if (response != null) {
                            try {
                                String result = response.getString("result");
                                if (result.equals("success")){
                                    email.setText(emailStr);
                                }else {
                                    Toast.makeText(getApplicationContext(),"修改失败",Toast.LENGTH_SHORT).show();
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

            }else if (requestCode==REQUEST_MODIFY_PWD){//密码修改
                String password=data.getExtras().getString("pwd");
                RequestParams params=new RequestParams();
                params.put("password",password);
                if (!NetworkState.isNetworkConnected(getApplicationContext())){
                    Toast.makeText(getApplicationContext(),"请连接网络再试",Toast.LENGTH_SHORT).show();
                    return;
                }
                asyncHttpClient.post(PATH_PWD,params,new JsonHttpResponseHandler(){
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        if (response != null) {
                            try {
                                String result = response.getString("result");
                                if (result.equals("success")){
                                    Toast.makeText(getApplicationContext(),"修改成功",Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(getApplicationContext(),"修改失败",Toast.LENGTH_SHORT).show();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        userDao.close();
    }
}
