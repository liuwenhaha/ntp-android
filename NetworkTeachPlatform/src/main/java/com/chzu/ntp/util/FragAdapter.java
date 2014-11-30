package com.chzu.ntp.util;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * @author yanxing
 *         ViewPager适配器,把Fragment视图装载到ViewPager中
 */
public class FragAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragments;//存放Fragments界面


    /**
     * 构造方法，初始化Fragment管理器和Fragment集合
     *
     * @param fm        Fragment管理器
     * @param fragments Fragment集合
     */
    public FragAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int i) {
        return fragments.get(i);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
