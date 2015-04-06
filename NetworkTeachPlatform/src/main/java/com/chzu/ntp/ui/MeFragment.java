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
import android.widget.Toast;

import com.chzu.ntp.util.ImageNameGenerator;
import com.chzu.ntp.util.PreferenceUtil;
import com.chzu.ntp.util.SDCardUtil;
import com.chzu.ntp.widget.MyExitDialog;
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
    private static ImageLoaderConfiguration config;
    private static DisplayImageOptions options;
    private static MeFragment meFragment;
    private TextView myCourse, myDownload, setting;//我的课程、我的下载、设置
    private ImageView login;
    private TextView username;
    private static final int REQUEST_CODE = 3;//请求码
    private static final String TAG = "MeFragment";


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
        Log.i(TAG, "onCreate");
    }

    @Override
    public void onResume() {//用户退出，返回此Activity，清除登录状态
        super.onResume();
        if (PreferenceUtil.getLoadName(getActivity()).equals("")) {
            username.setText("");
            login.setImageBitmap(null);
        }
        Log.i(TAG, "onResume");
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
                .displayer(new RoundedBitmapDisplayer(60))//设置圆角，弧度60
                .showImageOnFail(R.drawable.head)//不存在默认显示图片
                .build();
        imageLoader.displayImage(imageUri, login, options);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login://登录
                String name = PreferenceUtil.getLoadName(getActivity());
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
                if (PreferenceUtil.getLoadName(getActivity()).equals("")) {
                    Toast.makeText(getActivity(), "请先登录", Toast.LENGTH_SHORT).show();
                }
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
                /** 下面的Universal Image Loader配置、选项和onCreate重复，因为跳到登录界面登录成功返回，
                 * Activity生命周期从OnResume开始，没有对UIL配置、选项初始化，导致显示图片无效。这些配置、
                 * 选项代码可以放到OnResume方法中**/
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
                String imageUri = "file:///mnt/sdcard/ntp/head.png";
                imageLoader.displayImage(imageUri, login, options);
            }
        }
    }
}
