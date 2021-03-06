package com.ntp.base;

import android.app.Application;
import android.content.Context;

import com.igexin.sdk.PushManager;
import com.ntp.model.Course;
import com.ntp.util.ImageNameGenerator;
import com.ntp.util.SDCardUtil;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.tencent.bugly.crashreport.CrashReport;

import org.xutils.x;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 全局变量
 *
 * @author yanxing 2015/3/31.
 */
public class MyApplication extends Application {

    public void onCreate() {
        super.onCreate();
        initImageLoader(getApplicationContext());
        PushManager.getInstance().initialize(getApplicationContext());//初始化个推SDK
        x.Ext.init(this);
        x.Ext.setDebug(true); // 是否输出debug日志
        CrashReport.initCrashReport(this, "900014235", false);
    }

    /**
     * 初始化UIL全局配置
     *
     * @param context
     */
    public static void initImageLoader(Context context) {
        File file = SDCardUtil.creatSDDir("ntp");
        File reserveFile = SDCardUtil.createInnerSDDir("ntp");
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCache(new UnlimitedDiscCache(file, reserveFile, new ImageNameGenerator())) // 缓存到SD卡
                .diskCacheSize(50 * 1024 * 1024) // 50 Mb
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .writeDebugLogs()
                .build();
        ImageLoader.getInstance().init(config);
    }
}
