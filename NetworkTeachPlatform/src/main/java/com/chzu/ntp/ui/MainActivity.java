package com.chzu.ntp.ui;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chzu.ntp.adapter.FragAdapter;
import com.chzu.ntp.util.ExitListApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * 主程序
 */
public class MainActivity extends FragmentActivity implements View.OnClickListener {

    private static final int ACTION_DESTORY = 1;
    private static final String TAG = "mActivity";
    private ViewPager viewPager;
    private FragAdapter fragAdapter;//ViewPager适配器
    private List<Fragment> fragments;
    private Fragment loadCourse;//加载课程Fragemnt

    //顶部功能导航图片和文字课程,消息（作业），我
    private ImageView allCourse, homework, me;
    private TextView courseTxt, homeworkTxt, meTxt;
    //所有课程及其背景
    private TextView courseType;
    private LinearLayout navigateMore, search;

 /*   //加载课程成功后更新界面
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == ACTION_DESTORY) {
                try {
                    Thread.sleep(1000);
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.loadCourse, CourseListFragment.getInstance());//替换提示加载课程LoadCourseFragment
                    fragmentTransaction.commitAllowingStateLoss();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ExitListApplication.getInstance().addActivity(this);
        initView();
    }

    /**
     * 初始化控件,设置控件监听
     */
    private void initView() {
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        allCourse = (ImageView) findViewById(R.id.allCourse);
        homework = (ImageView) findViewById(R.id.homework);
        me = (ImageView) findViewById(R.id.me);
        courseTxt = (TextView) findViewById(R.id.courseTxt);
        homeworkTxt = (TextView) findViewById(R.id.homeworkTxt);
        meTxt = (TextView) findViewById(R.id.meTxt);
        navigateMore = (LinearLayout) findViewById(R.id.navigateMore);
        courseType = (TextView) findViewById(R.id.courseType);
        search = (LinearLayout) findViewById(R.id.search);
        allCourse.setOnClickListener(this);
        homework.setOnClickListener(this);
        me.setOnClickListener(this);
        search.setOnClickListener(this);
        navigateMore.setOnClickListener(this);
        fragments = new ArrayList<Fragment>();
        fragments.add(CourseListFragment.getInstance());
        fragments.add(NoticeFragment.getInstance());
        fragments.add(MeFragment.getInstance());
        fragAdapter = new FragAdapter(getSupportFragmentManager(), fragments);
        viewPager.setAdapter(fragAdapter);
        viewPager.setCurrentItem(0);//设置默认显示AllCourseFragment界面
        viewPager.setOnPageChangeListener(new MyViewPagerChangeListener());
        //new LoadCourseThread().start();
    }

   /* *//**
     * 加载课程线程，在此模拟3s加载成功
     *//*
    private class LoadCourseThread extends Thread {
        @Override
        public void run() {
            Message msg = new Message();
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            msg.what = ACTION_DESTORY;
            handler.sendMessage(msg);
        }
    }*/

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.allCourse:
                viewPager.setCurrentItem(0);
                break;
            case R.id.homework:
                viewPager.setCurrentItem(1);
                break;
            case R.id.me:
                viewPager.setCurrentItem(2);
                break;
            case R.id.navigateMore:
                Intent intent = new Intent(this, CourseTypeSelectActivity.class);
                startActivity(intent);
                break;
            case R.id.search:
                Intent searchIntent = new Intent(this, SearchCourseActivity.class);
                startActivity(searchIntent);
                break;
        }
    }

    /**
     * ViewPager切换监听
     */
    private class MyViewPagerChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int i, float v, int i2) {

        }

        @Override
        public void onPageSelected(int position) {
            switch (position) {
                case 0://CourseListFragment被选中
                    setTextColor(0);
                    break;
                case 1://NoticeFragment被选择
                    setTextColor(1);
                    break;
                case 2://MeFragment被选择
                    setTextColor(2);
            }
        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }

        /**
         * 设置Fragment被选中时菜单图片、文字颜色
         *
         * @param position Fragment索引值
         */
        public void setTextColor(int position) {
            allCourse.setImageDrawable(getResources().getDrawable(R.drawable.homepage_normal));
            homework.setImageDrawable(getResources().getDrawable(R.drawable.homework_normal));
            me.setImageDrawable(getResources().getDrawable(R.drawable.me_normal));
            courseTxt.setTextColor(getResources().getColor(R.color.menu_text_reserve));
            homeworkTxt.setTextColor(getResources().getColor(R.color.menu_text_reserve));
            meTxt.setTextColor(getResources().getColor(R.color.menu_text_reserve));
            if (position == 0) {//CourseListFragment被选中
                allCourse.setImageDrawable(getResources().getDrawable(R.drawable.homepage_pressed));
                courseTxt.setTextColor(getResources().getColor(R.color.menu_text_pressed));
            } else if (position == 1) {//NoticeFragment被选择
                homework.setImageDrawable(getResources().getDrawable(R.drawable.homework_pressed));
                homeworkTxt.setTextColor(getResources().getColor(R.color.menu_text_pressed));
            } else if (position == 2) {//MeFragment被选择
                me.setImageDrawable(getResources().getDrawable(R.drawable.me_pressed));
                meTxt.setTextColor(getResources().getColor(R.color.menu_text_pressed));
            }
        }
    }

}
