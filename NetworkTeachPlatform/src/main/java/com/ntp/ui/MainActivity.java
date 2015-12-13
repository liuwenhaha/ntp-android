package com.ntp.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ntp.ui.course.CourseListFragment;
import com.ntp.ui.course.SearchCourseActivity;
import com.ntp.ui.me.MeFragment;
import com.ntp.ui.notice.NoticeFragment;
import com.ntp.adapter.FragAdapter;
import com.ntp.base.BaseActivity;
import com.ntp.util.AppConfig;
import com.ntp.util.AppUtil;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * 主程序
 */
@ContentView(R.layout.activity_main)
public class MainActivity extends BaseActivity{

    @ViewInject(R.id.viewPager)
    private ViewPager mViewPager;

    //底部功能导航图片和文字课程,消息（作业），我
    @ViewInject(R.id.allCourse)
    private ImageView mAllCourse;

    @ViewInject(R.id.homework)
    private ImageView mHomework;

    @ViewInject(R.id.me)
    private ImageView mMe;

    @ViewInject(R.id.courseTxt)
    private TextView mCourseTxt;

    @ViewInject(R.id.homeworkTxt)
    private TextView mHomeworkTxt;

    @ViewInject(R.id.meTxt)
    private TextView mMeTxt;

    //消息红点
    @ViewInject(R.id.noticeRed)
    private ImageView mNoticeRed;

    @ViewInject(R.id.tip)
    private TextView mTip;

    @ViewInject(R.id.search)
    private LinearLayout search;

    private FragAdapter fragAdapter;
    //课程、消息、我三个fragment
    private List<Fragment> fragments;

    private static final String COURSE = "课程";
    private static final String NOTICE = "消息";
    private static final String ME = "我";
    /**
     * 应用退出
     */
    public static final String EXIT_ACTION = "com.ntp.exit.action";

    /**
     * 显示消息红点
     */
    public static final String SHOW_NOTICE_ACTION = "com.ntp.notice.action";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    /**
     * 设置底部导航
     */
    void initView() {
        AppUtil.setStatusBarDarkMode(true, this);
        if (AppConfig.isNoticeRed(getApplicationContext())){
            mNoticeRed.setVisibility(View.VISIBLE);
        }
        fragments = new ArrayList<Fragment>();
        fragments.add(new CourseListFragment());
        fragments.add(new NoticeFragment());
        fragments.add(new MeFragment());
        fragAdapter = new FragAdapter(getSupportFragmentManager(), fragments);
        mViewPager.setAdapter(fragAdapter);
        mViewPager.setCurrentItem(0);//设置默认显示CourseListFragment界面
        mViewPager.setOnPageChangeListener(new MyViewPagerChangeListener());
        setSwipeBackEnable(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (AppConfig.isNoticeRed(getApplicationContext())){
            mNoticeRed.setVisibility(View.VISIBLE);
        }else {
            mNoticeRed.setVisibility(View.INVISIBLE);
        }
        IntentFilter filter = new IntentFilter();
        filter.addAction(EXIT_ACTION);
        filter.addAction(SHOW_NOTICE_ACTION);
        registerReceiver(mBroadcastReceiver, filter);
    }

    //广播处理
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(EXIT_ACTION)) {
                finish();
            } else if (intent.getAction().equals(SHOW_NOTICE_ACTION)) {
                mNoticeRed.setVisibility(View.VISIBLE);
            }
        }
    };


    //点击事件，必须私有
    @Event(value = {R.id.allCourse, R.id.homework, R.id.me, R.id.search})
    private void onClick(View v) {
        switch (v.getId()) {
            case R.id.allCourse:
                mViewPager.setCurrentItem(0);
                break;
            case R.id.homework:
                mViewPager.setCurrentItem(1);
                break;
            case R.id.me:
                mViewPager.setCurrentItem(2);
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
                    mTip.setText(COURSE);
                    search.setVisibility(View.VISIBLE);
                    break;
                case 1://NoticeFragment被选择
                    setTextColor(1);
                    mTip.setText(NOTICE);
                    search.setVisibility(View.INVISIBLE);
                    break;
                case 2://MeFragment被选择
                    setTextColor(2);
                    mTip.setText(ME);
                    search.setVisibility(View.INVISIBLE);
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
            mAllCourse.setImageDrawable(getResources().getDrawable(R.drawable.homepage_normal));
            mHomework.setImageDrawable(getResources().getDrawable(R.drawable.homework_normal));
            mMe.setImageDrawable(getResources().getDrawable(R.drawable.me_normal));
            mCourseTxt.setTextColor(getResources().getColor(R.color.menu_text_reserve));
            mHomeworkTxt.setTextColor(getResources().getColor(R.color.menu_text_reserve));
            mMeTxt.setTextColor(getResources().getColor(R.color.menu_text_reserve));
            if (position == 0) {//CourseListFragment被选中
                mAllCourse.setImageDrawable(getResources().getDrawable(R.drawable.homepage_pressed_d));
                mCourseTxt.setTextColor(getResources().getColor(R.color.blue_3));
            } else if (position == 1) {//NoticeFragment被选择
                mHomework.setImageDrawable(getResources().getDrawable(R.drawable.homework_pressed_d));
                mHomeworkTxt.setTextColor(getResources().getColor(R.color.blue_3));
            } else if (position == 2) {//MeFragment被选择
                mMe.setImageDrawable(getResources().getDrawable(R.drawable.me_pressed_d));
                mMeTxt.setTextColor(getResources().getColor(R.color.blue_3));
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mBroadcastReceiver);
    }
}
