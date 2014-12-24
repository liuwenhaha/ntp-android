package com.chzu.ntp.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chzu.ntp.ui.R;
import com.chzu.ntp.util.ExitListApplication;

/**
 * 软件设置
 */
public class SettingActivity extends Activity implements View.OnClickListener {

    private ImageView back;//退出
    private ImageView switchImg;//用2G3G4G网络播放视频和下载课件开关图片
    private TextView about,exit;//关于、退出

    //应用配置
    private  SharedPreferences preferences;
    private static final String CONFIG = "config";//配置文件
    private static final int MODE_CONFIG = Context.MODE_APPEND;
    private static final String USE_MOBILE_DATA = "use_mobile_data";

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
        if (getConfig()){
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
                if (getConfig()){
                    switchImg.setImageDrawable(getResources().getDrawable(R.drawable.switch_off));
                    saveConfig(false);
                }else{
                    switchImg.setImageDrawable(getResources().getDrawable(R.drawable.switch_on));
                    saveConfig(true);
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

    /**
     * 保存是否可以用2G3G4G网络播放视频和下载课件
     * <br>其他的联网操作默认可以使用移动网络
     *
     * @param useMobileData true 可以使用，false不可使用
     */
    public void saveConfig(boolean useMobileData) {
        preferences =getSharedPreferences(CONFIG, MODE_CONFIG);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(USE_MOBILE_DATA, useMobileData);
        editor.commit();
    }

    /**
     * 检查用户配置，是否可以用2G3G4G网络播放视频或下载课件。
     * <br>默认不可使用
     * @return true 可以使用，false 不可使用
     */
    public Boolean getConfig() {
        preferences =getSharedPreferences(CONFIG, MODE_CONFIG);
        return preferences.getBoolean(USE_MOBILE_DATA, false);
    }

}
