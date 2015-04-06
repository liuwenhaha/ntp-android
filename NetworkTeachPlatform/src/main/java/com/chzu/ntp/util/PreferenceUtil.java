package com.chzu.ntp.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * @author yanxing
 *         保存用户配置操作类，登录状态、是否允许使用2G3G4G网络播放视频或下载课件（其他的联网操作默认可以使用移动网络）
 *         ListView当前显示的页数（分页显示数据）
 */
public class PreferenceUtil {
    //登录状态配置
    private static SharedPreferences preferences;
    private static final String LOAD = "load";//登录信息保存
    private static final String NAME = "name";//登录信息键

    //应用配置
    private static final String CONFIG = "config";//配置文件
    private static final String USE_MOBILE_DATA = "use_mobile_data";

    //ListView当前显示页数
    private static final String CURRENT_PAGE = "current_page";
    private static final String PAGE = "page";

    /**
     * 以私有操作方式保存用户登录信息，此种方式只能保存一个用户信息
     *
     * @param name 登录成功名
     */
    public static void saveLoadName(Context context, String name) {
        preferences = context.getSharedPreferences(LOAD, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(NAME, name);
        editor.commit();
    }

    /**
     * 登录信息文件中用户的姓名，没有返回空字符
     *
     * @return
     */
    public static String getLoadName(Context context) {
        preferences = context.getSharedPreferences(LOAD, Context.MODE_PRIVATE);//保存登录信息文件为私有文件
        return preferences.getString(NAME, "");
    }


    /**
     * 保存是否可以用2G3G4G网络播放视频和下载课件
     * <br>其他的联网操作默认可以使用移动网络
     *
     * @param useMobileData true 可以使用，false不可使用
     */
    public static void saveConfig(Context context, boolean useMobileData) {
        preferences = context.getSharedPreferences(CONFIG, Context.MODE_APPEND);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(USE_MOBILE_DATA, useMobileData);
        editor.commit();
    }

    /**
     * 检查用户配置，是否可以用2G3G4G网络播放视频或下载课件。
     * <br>默认可以使用
     *
     * @return true 可以使用，false 不可使用
     */
    public static Boolean getConfig(Context context) {
        preferences = context.getSharedPreferences(CONFIG, Context.MODE_APPEND);
        return preferences.getBoolean(USE_MOBILE_DATA, true);
    }

    /**
     * 保存listView显示的当前页数
     *
     * @param context
     * @param currentPage 当前页数
     */
    public static void saveCurrentPage(Context context, int currentPage) {
        preferences = context.getSharedPreferences(CURRENT_PAGE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(PAGE, currentPage);
        editor.commit();
    }

    /**
     * 获取ListView显示的当前页数
     *
     * @param context
     * @return 当前页数，默认1
     */
    public static int getCurrentPage(Context context) {
        preferences = context.getSharedPreferences(CURRENT_PAGE, Context.MODE_PRIVATE);
        return preferences.getInt(PAGE, 1);
    }


}
