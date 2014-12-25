package com.chzu.ntp.util;

import android.app.Activity;
import android.app.Application;

import java.util.LinkedList;
import java.util.List;

/**
 * 销毁该应用所有的Activity，即完全退出应用
 */
public class ExitListApplication extends Application {
    private List<Activity> mList = new LinkedList<Activity>();
    private static ExitListApplication instance;

    public synchronized static ExitListApplication getInstance() {
        if (null == instance) {
            instance = new ExitListApplication();
        }
        return instance;
    }

    //添加需要销毁的Activity到list中
    public void addActivity(Activity activity) {
        mList.add(activity);
    }

    //销毁mlist中的Activity
    public void exit() {
        try {
            for (Activity activity : mList) {
                if (activity != null)
                    activity.finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.exit(0);
        }
    }

    public void onLowMemory() {
        super.onLowMemory();
        System.gc();
    }
}

