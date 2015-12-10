package com.ntp.activity.me;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ntp.activity.R;
import com.ntp.util.PathConstant;
import com.ntp.dao.PreferenceDao;
import com.ntp.dao.UserDao;
import com.ntp.model.User;
import com.ntp.util.BitmapUtil;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.ntp.view.CircleImageView;

import org.apache.http.Header;


/**
 * 我(个人中心)界面，可以查看学生本人选的课程和一些系统设置
 */
public class MeFragment extends Fragment implements View.OnClickListener {

    private TextView myCourse, myDownload, setting;//我的课程、我的下载、设置
    private CircleImageView login;
    private TextView username;
    private static final int REQUEST_CODE = 3;//请求码
    private static final String TAG = "MeFragment";
    private UserDao userDao;
    private AsyncHttpClient asyncHttpClient = new AsyncHttpClient();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate");
    }

    @Override
    public void onResume() {//用户退出，返回此Activity，清除登录状态
        super.onResume();
        if (PreferenceDao.getLoadName(getActivity()).equals("")) {
            username.setText("");
            login.setImageDrawable(getResources().getDrawable(R.drawable.default_head));
        }
        if (null == userDao) {
            userDao = new UserDao(getActivity().getApplicationContext());
        }
        Log.i(TAG, "onResume");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me, container, false);
        login = (CircleImageView) view.findViewById(R.id.login);
        myCourse = (TextView) view.findViewById(R.id.myCourse);
        myDownload = (TextView) view.findViewById(R.id.myDownload);
        username = (TextView) view.findViewById(R.id.username);
        setting = (TextView) view.findViewById(R.id.setting);
        username.setText(PreferenceDao.getLoadName(getActivity()));
        login.setOnClickListener(this);
        myCourse.setOnClickListener(this);
        myDownload.setOnClickListener(this);
        setting.setOnClickListener(this);
        userDao = new UserDao(getActivity().getApplicationContext());
        if (!PreferenceDao.getLoadName(getActivity().getApplicationContext()).equals("")) {//已有用户登陆
            displayHead();
        }
        return view;
    }

    /**
     * 显示本地缓存头像
     */
    private void displayHead() {
        User user = userDao.findByName(PreferenceDao.getLoadName(getActivity().getApplicationContext()).trim());
        if (null==user){
            return;
        }
        if (user.getHead() != null) {//数据库有缓存头像
            Bitmap bitmap = BitmapFactory.decodeByteArray(user.getHead(), 0, user.getHead().length);
            login.setImageBitmap(bitmap);
        } else {//没有显示默认应用默认头像
            Log.d(TAG,"头像为空");
            login.setImageDrawable(getResources().getDrawable(R.drawable.head_default));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login://登录
                String name = PreferenceDao.getLoadName(getActivity().getApplicationContext());
                //检查是否有登录
                if (!name.equals("")) {//已登录
                    Intent intent = new Intent(getActivity(), MeInformationActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("username", name);
                    intent.putExtras(bundle);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivityForResult(intent, REQUEST_CODE);
                }
                break;
            case R.id.myCourse://我的课程
                startActivity(new Intent(getActivity().getApplicationContext(),MyCourseActivity.class));
                break;
            case R.id.myDownload://我的下载
                startActivity(new Intent(getActivity().getApplicationContext(),MyDownloadActivity.class));
                break;
            case R.id.setting://设置
                startActivity(new Intent(getActivity(), SettingActivity.class));
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode == LoginActivity.RESULT_CODE) {
                String name = data.getExtras().getString("username");
                username.setText(name);
                String head = data.getExtras().getString("head");
                if (head.equals("error")) {//用户没有服务器没有头像
                    login.setImageDrawable(getResources().getDrawable(R.drawable.head_default));
                    User user = new User();
                    user.setUsername(name);
                    if (userDao.findByName(name)!=null){
                        userDao.update(user);
                    }else {
                        userDao.save(user);
                    }
                    return;
                }
                login.setImageDrawable(getResources().getDrawable(R.drawable.default_head_loading));
                String imageUri = PathConstant.PATH_IMAGE + head;
                asyncHttpClient.post(imageUri, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            Bitmap bitmap = BitmapFactory.decodeByteArray(responseBody, 0, responseBody.length);
                            Bitmap bitmap1 = BitmapUtil.createBitmapZoop(bitmap, 70, 70);
                            User user = new User();
                            user.setUsername(username.getText().toString());
                            user.setHead(BitmapUtil.getBitmapByte(bitmap1));
                            if (null == userDao) {
                                Log.w(TAG,"userDao is null");
                                userDao = new UserDao(getActivity().getApplicationContext());
                            }
                            Log.e(TAG,user.toString());
                            Log.e(TAG, userDao + "");
                            if (userDao.findByName(user.getUsername())!=null){
                                userDao.update(user);
                            }else {
                                userDao.save(user);
                            }
                            login.setImageBitmap(bitmap1);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers,
                                          byte[] responseBody, Throwable error) {
                        error.printStackTrace();
                    }
                });
            }
        }
    }

    @Override
    public void onDestroy() {
        userDao.close();
        super.onDestroy();
    }
}
