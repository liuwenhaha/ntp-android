package com.chzu.ntp.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chzu.ntp.dao.MySharedPreferences;
import com.chzu.ntp.ui.R;
import com.chzu.ntp.util.ExitListApplication;

/**
 * 软件设置
 */
public class SettingActivity extends Activity implements View.OnClickListener {

    private ImageView back;//退出
    private ImageView switchImg;//用2G3G4G网络播放视频和下载课件开关图片
    private TextView about,exit;//关于、退出

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        back = (ImageView) findViewById(R.id.back);
        switchImg = (ImageView) findViewById(R.id.switchImg);
        about= (TextView) findViewById(R.id.about);
        exit= (TextView) findViewById(R.id.exit);
        back.setOnClickListener(this);
        switchImg.setOnClickListener(this);
        about.setOnClickListener(this);
        exit.setOnClickListener(this);
        MySharedPreferences preferences=new MySharedPreferences(this);
        if (preferences.getConfig()){
            switchImg.setImageDrawable(getResources().getDrawable(R.drawable.switch_on));
        }else{
            switchImg.setImageDrawable(getResources().getDrawable(R.drawable.switch_off));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back://退出
                finish();
                break;
            case R.id.switchImg://设置用2G3G4G网络播放视频和下载课件开关
                MySharedPreferences preferences=new MySharedPreferences(this);
                if (preferences.getConfig()){
                    switchImg.setImageDrawable(getResources().getDrawable(R.drawable.switch_off));
                    preferences.saveConfig(false);
                }else{
                    switchImg.setImageDrawable(getResources().getDrawable(R.drawable.switch_on));
                    preferences.saveConfig(true);
                }
                break;
            case R.id.about://关于
                Intent intent=new Intent(this,AboutActivity.class);
                startActivity(intent);
                break;
            case R.id.exit://退出
                ExitListApplication.getInstance().exit();
                break;
        }
    }

}
