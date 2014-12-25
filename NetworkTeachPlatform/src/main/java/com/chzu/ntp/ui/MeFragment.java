package com.chzu.ntp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chzu.ntp.util.PreferenceUtil;


/**
 * 我(个人中心)界面，可以查看学生本人选的课程和一些系统设置
 */
public class MeFragment extends Fragment implements View.OnClickListener {

    private static MeFragment meFragment;
    private TextView login, myCourse, myDownload, setting;//登录、我的课程、我的下载、设置

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
        login = (TextView) view.findViewById(R.id.login);
        myCourse = (TextView) view.findViewById(R.id.myCourse);
        myDownload = (TextView) view.findViewById(R.id.myDownload);
        setting = (TextView) view.findViewById(R.id.setting);
        login.setOnClickListener(this);
        myCourse.setOnClickListener(this);
        myDownload.setOnClickListener(this);
        setting.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login://登录
                //检查是否有登录
                if (!PreferenceUtil.getLoadName(getActivity()).equals("")) {
                    login.setText(PreferenceUtil.getLoadName(getActivity()));
                } else {
                    login.setText(getString(R.string.noLogin));
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                }
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

}
