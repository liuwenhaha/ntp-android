package com.chzu.ntp.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chzu.ntp.util.ImageNameGenerator;
import com.chzu.ntp.util.PreferenceUtil;
import com.chzu.ntp.util.SDCardUtil;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.io.File;


/**
 * 我(个人中心)界面，可以查看学生本人选的课程和一些系统设置
 */
public class MeFragment extends Fragment implements View.OnClickListener {

    /**
     * Universal Image Loader加载图片类
     */
    private ImageLoader imageLoader;
    ImageLoaderConfiguration config;
    DisplayImageOptions options;
    private static MeFragment meFragment;
    private TextView myCourse, myDownload, setting;//我的课程、我的下载、设置
    private ImageView login;
    private TextView username;
    private static final int REQUEST_CODE = 3;//请求码


    /**
     * 创建单例对象
     */
    public static MeFragment getInstance() {
        if (meFragment == null) {
            meFragment = new MeFragment();
        }
        return meFragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me, container, false);
        login = (ImageView) view.findViewById(R.id.login);
        myCourse = (TextView) view.findViewById(R.id.myCourse);
        myDownload = (TextView) view.findViewById(R.id.myDownload);
        username = (TextView) view.findViewById(R.id.username);
        setting = (TextView) view.findViewById(R.id.setting);
        username.setText(PreferenceUtil.getLoadName(getActivity()));
        imageLoader = ImageLoader.getInstance();
        login.setOnClickListener(this);
        myCourse.setOnClickListener(this);
        myDownload.setOnClickListener(this);
        setting.setOnClickListener(this);
        if (!PreferenceUtil.getLoadName(getActivity()).equals("")) {//已有用户登陆
            displayHead();
            login.setClickable(false);
        }
        return view;
    }

    /**
     * 显示本地缓存头像
     */
    private void displayHead() {
        String imageUri = "file:///mnt/sdcard/ntp/head.png";//缓存图片路径
        File file = SDCardUtil.creatSDDir("ntp");
        config = new ImageLoaderConfiguration.Builder(getActivity().getApplicationContext())
                .diskCache(new UnlimitedDiscCache(file, null, new ImageNameGenerator("head.png"))) // 缓存到SD卡
                .build();
        imageLoader.init(config);
        //显示图片的配置
        options = new DisplayImageOptions.Builder()
                .cacheOnDisk(true)
                .displayer(new RoundedBitmapDisplayer(60))
                .showImageOnFail(R.drawable.head)//不存在默认显示图片
                .build();
        imageLoader.displayImage(imageUri, login, options);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login://登录
                //检查是否有登录
               /* if (!PreferenceUtil.getLoadName(getActivity()).equals("")) {
                    //login.setText(PreferenceUtil.getLoadName(getActivity()));
                } else {
                    //login.setText(getString(R.string.noLogin));
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivityForResult(intent, REQUEST_CODE);
                }*/
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
                break;
            case R.id.myCourse://我的课程

                break;
            case R.id.myDownload://我的下载

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
                //login.setText("头像");
                //模拟图片
                String imageUri = "http://h.hiphotos.baidu.com/image/w%3D230/sign=1ea5b9ff34d3d539c13d08c00a87e927/2e2eb9389b504fc2022d2904e7dde71190ef6d45.jpg";
                imageLoader.displayImage(imageUri, login, options);
            }
        }
    }
}
