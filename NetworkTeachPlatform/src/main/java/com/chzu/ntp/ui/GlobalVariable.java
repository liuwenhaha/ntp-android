package com.chzu.ntp.ui;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.StrictMode;
import android.widget.Toast;

import com.chzu.ntp.model.Course;
import com.chzu.ntp.util.ImageNameGenerator;
import com.chzu.ntp.util.SDCardUtil;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 全局变量list<course>,主界面上拉刷新时，追加新数据，新的数据不缓存到数据库
 * 只在本次有效
 * 初始化Universal-Image-Loader全局配置
 * @author yanxing 2015/3/31.
 */
public class GlobalVariable extends Application{

    private List<Course> list=new ArrayList<Course>();

    public List<Course> getList() {
        return list;
    }

    public void setList(List<Course> list) {
        this.list = list;
    }


    public void onCreate() {
        super.onCreate();
        initImageLoader(getApplicationContext());
    }

    /**
     * 初始化UIL全局配置
     * @param context
     */
    public static void initImageLoader(Context context) {
        File file = SDCardUtil.creatSDDir("ntp");
        File reserveFile=SDCardUtil.createInnerSDDir("ntp");
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCache(new UnlimitedDiscCache(file,reserveFile, new ImageNameGenerator())) // 缓存到SD卡
                .diskCacheSize(50 * 1024 * 1024) // 50 Mb
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .writeDebugLogs()
                .build();
        ImageLoader.getInstance().init(config);
    }
}
