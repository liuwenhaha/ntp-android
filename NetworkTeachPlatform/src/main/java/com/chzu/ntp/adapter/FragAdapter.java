package com.chzu.ntp.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.view.ViewGroup;

import java.util.List;

/**
 * @author yanxing
 *         ViewPager适配器,把Fragment视图装载到ViewPager中
 */
public class FragAdapter extends FragmentStatePagerAdapter {

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

    public FragAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

}

