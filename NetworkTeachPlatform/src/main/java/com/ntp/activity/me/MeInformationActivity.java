package com.ntp.activity.me;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ntp.activity.R;
import com.ntp.activity.PathConstant;
import com.ntp.dao.UserDao;
import com.ntp.model.User;
import com.ntp.util.BitmapUtil;
import com.ntp.util.MD5Util;
import com.ntp.util.NetworkStateUtil;
import com.ntp.widget.MySelectDialog;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 个人信息
 *
 * @author yanxing
 */
public class MeInformationActivity extends Activity {

    /**
     * 修改邮箱请求码
     */
    private static final int REQUEST_MODIFY_EMAIL=4;
    /**
     * 修改密码请求码
     */
    private static final int REQUEST_MODIFY_PWD=5;
    /**
     * 修改性别请求码
     */
    private static final int REQUEST_MODIFY_SEX=6;
    /**
     * 浏览头像请求码
     */
    private static final int REQUEST_HEAD_BROWER = 7;
    public  static final String MAIL="男";
    public  static final String FEMAIL="女";
    private TextView username;
    private AsyncHttpClient asyncHttpClient=new AsyncHttpClient();
    public static final String TAG="MeInformationActivity";
    private TextView sex,email;
    public  static final String MODIFY_TYPE="修改类型";
    public  static final String MODIFY_EMAIL="修改邮箱";
    public  static final String MODIFY_PWD="修改密码";
    private UserDao userDao;
    private CircleImageView head;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_me_information);
        sex= (TextView) findViewById(R.id.sex);
        email= (TextView) findViewById(R.id.email);
        username = (TextView) findViewById(R.id.username);
        head = (CircleImageView) findViewById(R.id.head);
        String name = getIntent().getExtras().getString("username");
        username.setText(name);
        userDao = new UserDao(getApplicationContext());
        User user = userDao.findByName(name);
        head.setClickable(false);//暂时不能换头像
        if (user.getUsername() != null) {
            Log.i(TAG, "本地有缓存信息");
            sex.setText((user.getSex().equals("null")?"":user.getSex()));
            email.setText((user.getEmail().equals("null")?"":user.getEmail()));
            if (user.getHead() != null) {
                head.setImageBitmap(BitmapFactory.decodeByteArray(user.getHead(), 0, user.getHead().length));
            } else {
                head.setImageDrawable(getResources().getDrawable(R.drawable.head_default));
            }

        } else {
            getUser();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (null==userDao){
            userDao = new UserDao(getApplicationContext());
        }
    }

    /**
     * 从服务器获取用户详细信息
     */
    private void getUser(){
        RequestParams params=new RequestParams();
        params.put("username", username.getText());
        if (!NetworkStateUtil.isNetworkConnected(getApplicationContext())){
            Toast.makeText(getApplicationContext(),"请连接网络再试",Toast.LENGTH_SHORT).show();
            return;
        }
        asyncHttpClient.post(PathConstant.PATH_USER_INFO, params, new JsonHttpResponseHandler() {
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
                        Log.e(TAG,userDao.toString());
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
        if (view.getId()==R.id.sexLayout){
            Intent intent=new Intent(getApplicationContext(), MySelectDialog.class);
            Bundle bundle=new Bundle();
            bundle.putString("firstOption",MAIL);
            bundle.putString("secondOption",FEMAIL);
            intent.putExtras(bundle);
            startActivityForResult(intent,REQUEST_MODIFY_SEX);
        }

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
            //邮箱修改
            if (requestCode == REQUEST_MODIFY_EMAIL) {
                final String emailStr=data.getExtras().getString("email");
                RequestParams params=new RequestParams();
                params.put("username",username.getText().toString());
                params.put("email", emailStr);
                if (!NetworkStateUtil.isNetworkConnected(getApplicationContext())){
                    Toast.makeText(getApplicationContext(),"请连接网络再试",Toast.LENGTH_SHORT).show();
                    return;
                }
                asyncHttpClient.post(PathConstant.PATH_EMAIL, params, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        if (response != null) {
                            try {
                                String result = response.getString("result");
                                if (result.equals("success")){
                                    email.setText(emailStr);
                                    User user=new User();
                                    user.setUsername(username.getText().toString());
                                    user.setEmail(emailStr);
                                    userDao.update(user);//修改本地邮箱缓存
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
                params.put("password", MD5Util.generatePassword(password));
                params.put("username",username.getText().toString());
                if (!NetworkStateUtil.isNetworkConnected(getApplicationContext())){
                    Toast.makeText(getApplicationContext(),"请连接网络再试",Toast.LENGTH_SHORT).show();
                    return;
                }
                asyncHttpClient.post(PathConstant.PATH_PWD, params, new JsonHttpResponseHandler() {
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
        } else if (requestCode == REQUEST_MODIFY_SEX) {
            switch (resultCode) {
                case MySelectDialog.RESULT_ITEM1:
                    if (sex.getText().toString().trim().equals(MAIL)) {//如果选择项和原来的相等，不作改变
                        break;
                    } else {//改变性别
                        RequestParams requestParams = new RequestParams();
                        requestParams.put("sex", MAIL);
                        requestParams.put("username", username.getText().toString());
                        if (!NetworkStateUtil.isNetworkConnected(getApplicationContext())) {
                            Toast.makeText(getApplicationContext(), "请连接网络再试", Toast.LENGTH_SHORT).show();
                            break;
                        }
                        asyncHttpClient.post(PathConstant.PATH_SEX, requestParams, new JsonHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                super.onSuccess(statusCode, headers, response);
                                if (response != null) {
                                    try {
                                        String result = response.getString("result");
                                        if (result.equals("success")) {
//                                            Toast.makeText(getApplicationContext(),"修改成功",Toast.LENGTH_SHORT).show();
                                            sex.setText(MAIL);
                                            User user = new User();
                                            user.setUsername(username.getText().toString());
                                            user.setSex(MAIL);
                                            userDao.update(user);//修改本地性别缓存
                                        } else {
                                            Toast.makeText(getApplicationContext(), "修改失败", Toast.LENGTH_SHORT).show();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        });
                    }
                    break;
                case MySelectDialog.RESULT_ITEM2:
                    if (sex.getText().toString().trim().equals(FEMAIL)) {//如果选择项和原来的相等，不作改变
                        break;
                    } else {//修改性别，无论用户选择哪个item，都先视作修改性别
                        RequestParams requestParams = new RequestParams();
                        requestParams.put("sex", FEMAIL);
                        requestParams.put("username", username.getText().toString());
                        if (!NetworkStateUtil.isNetworkConnected(getApplicationContext())) {
                            Toast.makeText(getApplicationContext(), "请连接网络再试", Toast.LENGTH_SHORT).show();
                            break;
                        }
                        asyncHttpClient.post(PathConstant.PATH_SEX, requestParams, new JsonHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                super.onSuccess(statusCode, headers, response);
                                if (response != null) {
                                    try {
                                        String result = response.getString("result");
                                        if (result.equals("success")) {
//                                            Toast.makeText(getApplicationContext(),"修改成功",Toast.LENGTH_SHORT).show();
                                            sex.setText(FEMAIL);
                                            User user = new User();
                                            user.setUsername(username.getText().toString());
                                            user.setSex(FEMAIL);
                                            userDao.update(user);//修改本地性别缓存
                                        } else {
                                            Toast.makeText(getApplicationContext(), "修改失败", Toast.LENGTH_SHORT).show();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        });
                    }
                    break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        userDao.close();
    }

    //点击头像事件
    public void watchHead(View view) {
        if (view.getId() == R.id.head) {
            Intent intent = new Intent(getApplicationContext(), HeadBrowseActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("username", username.getText().toString());
            bundle.putByteArray("head", BitmapUtil.getBitmapByte(((BitmapDrawable) head.getDrawable()).getBitmap()));
            intent.putExtras(bundle);
            startActivityForResult(intent, REQUEST_HEAD_BROWER);
        }
    }
}
