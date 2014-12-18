package com.chzu.ntp.dao;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * @author yanxing
 *         保存用户配置操作类，登录状态、是否允许使用2G3G4G网络播放视频或下载课件（其他的联网操作默认可以使用移动网络）
 */
public class MySharedPreferences extends Activity {
    private static SharedPreferences preferences;
    //登录状态配置
    private static final String LOAD = "load";//登录信息保存
    private static final int MODE_LOAD = Context.MODE_PRIVATE;//保存登录信息文件为私有文件
    private static final String NAME = "name";//登录信息键
    //应用配置
    private static final String CONFIG = "config";//配置文件
    private static final int MODE_CONFIG = Context.MODE_APPEND;
    private static final String USE_MOBILE_DATA = "use_mobile_data";

    /**
     * 以私有操作方式保存用户登录信息，此种方式只能保存一个用户信息
     *
     * @param name 登录成功名
     */
    public  void saveLoad(String name) {
        preferences = getSharedPreferences(LOAD, MODE_LOAD);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(NAME, name);
        editor.commit();
    }

    /**
     * 登录信息文件中是否存在用户
     *
     * @return
     */
    public boolean isExistLoad() {
        preferences = getSharedPreferences(LOAD, MODE_LOAD);
        return preferences.contains(NAME);
    }

    /**
     * 保存是否可以用2G3G4G网络播放视频和下载课件
     *
     * @param useMobileData
     */
    public void saveConfig(boolean useMobileData) {
        preferences = getSharedPreferences(CONFIG, MODE_CONFIG);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(USE_MOBILE_DATA, useMobileData);
        editor.commit();
    }

    /**
     * 检查用户配置，是否可以用2G3G4G网络播放视频或下载课件
     *
     * @return true 可以使用，false 不可使用
     */
    public Boolean getConfig() {
        preferences = getSharedPreferences(CONFIG, MODE_CONFIG);
        return preferences.getBoolean(USE_MOBILE_DATA, true);
    }


}
