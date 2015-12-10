package com.ntp.activity;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.igexin.sdk.PushManager;
import com.ntp.activity.course.CourseListFragment;
import com.ntp.activity.course.SearchCourseActivity;
import com.ntp.activity.me.MeFragment;
import com.ntp.activity.notice.NoticeFragment;
import com.ntp.adapter.FragAdapter;
import com.ntp.base.BaseActivity;
import com.ntp.dao.PreferenceDao;
import com.ntp.util.AppUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 主程序
 */
public class MainActivity extends BaseActivity implements View.OnClickListener {

    private static final int ACTION_DESTORY = 1;
    private static final String TAG = "mActivity";
    private ViewPager viewPager;
    private FragAdapter fragAdapter;//ViewPager适配器
    private List<Fragment> fragments;
    private Fragment loadCourse;//加载课程Fragemnt

    //底部功能导航图片和文字课程,消息（作业），我
    private ImageView allCourse, homework, me;
    private TextView courseTxt, homeworkTxt, meTxt;
    public static ImageView noticeRed;//消息红点
    //所有课程及其背景
    private TextView courseType, tip;
    private LinearLayout navigateMore, search;
    private static final String COURSE = "课程";
    private static final String NOTICE = "消息";
    private static final String ME = "我";
    /**
     * 应用退出
     */
    public static final String EXIT_ACTION = "com.ntp.exit.action";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppUtil.setStatusBarDarkMode(true, this);
        initView();
    }

    /**
     * 初始化控件,设置控件监听
     */
    private void initView() {
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        noticeRed= (ImageView) findViewById(R.id.noticeRed);
        if (PreferenceDao.isNoticeRed(getApplicationContext())){
            noticeRed.setVisibility(View.VISIBLE);
        }
        allCourse = (ImageView) findViewById(R.id.allCourse);
        homework = (ImageView) findViewById(R.id.homework);
        me = (ImageView) findViewById(R.id.me);
        courseTxt = (TextView) findViewById(R.id.courseTxt);
        homeworkTxt = (TextView) findViewById(R.id.homeworkTxt);
        meTxt = (TextView) findViewById(R.id.meTxt);
//        navigateMore = (LinearLayout) findViewById(R.id.navigateMore);
        courseType = (TextView) findViewById(R.id.courseType);
        search = (LinearLayout) findViewById(R.id.search);
        tip = (TextView) findViewById(R.id.tip);
        allCourse.setOnClickListener(this);
        homework.setOnClickListener(this);
        me.setOnClickListener(this);
        search.setOnClickListener(this);
//        navigateMore.setOnClickListener(this);
        fragments = new ArrayList<Fragment>();
        fragments.add(CourseListFragment.getInstance());
        fragments.add(NoticeFragment.getInstance());
        fragments.add(MeFragment.getInstance());
        fragAdapter = new FragAdapter(getSupportFragmentManager(), fragments);
        viewPager.setAdapter(fragAdapter);
        viewPager.setCurrentItem(0);//设置默认显示CourseListFragment界面
        viewPager.setOnPageChangeListener(new MyViewPagerChangeListener());
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (PreferenceDao.isNoticeRed(getApplicationContext())){
            noticeRed.setVisibility(View.VISIBLE);
        }else {
            noticeRed.setVisibility(View.INVISIBLE);
        }
        IntentFilter filter = new IntentFilter();
        filter.addAction(EXIT_ACTION);
        registerReceiver(mBroadcastReceiver, filter);
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(EXIT_ACTION)) {
                finish();
            }
        }
    };


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
//            case R.id.navigateMore:
//                Intent intent = new Intent(this, CoursetypeSelectActivity.class);
//                startActivity(intent);
//                break;
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
                    tip.setText(COURSE);
                    search.setVisibility(View.VISIBLE);
                    break;
                case 1://NoticeFragment被选择
                    setTextColor(1);
                    tip.setText(NOTICE);
                    search.setVisibility(View.INVISIBLE);
                    break;
                case 2://MeFragment被选择
                    setTextColor(2);
                    tip.setText(ME);
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
            allCourse.setImageDrawable(getResources().getDrawable(R.drawable.homepage_normal));
            homework.setImageDrawable(getResources().getDrawable(R.drawable.homework_normal));
            me.setImageDrawable(getResources().getDrawable(R.drawable.me_normal));
            courseTxt.setTextColor(getResources().getColor(R.color.menu_text_reserve));
            homeworkTxt.setTextColor(getResources().getColor(R.color.menu_text_reserve));
            meTxt.setTextColor(getResources().getColor(R.color.menu_text_reserve));
            if (position == 0) {//CourseListFragment被选中
                allCourse.setImageDrawable(getResources().getDrawable(R.drawable.homepage_pressed_d));
                courseTxt.setTextColor(getResources().getColor(R.color.blue_3));
            } else if (position == 1) {//NoticeFragment被选择
                homework.setImageDrawable(getResources().getDrawable(R.drawable.homework_pressed_d));
                homeworkTxt.setTextColor(getResources().getColor(R.color.blue_3));
            } else if (position == 2) {//MeFragment被选择
                me.setImageDrawable(getResources().getDrawable(R.drawable.me_pressed_d));
                meTxt.setTextColor(getResources().getColor(R.color.blue_3));
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mBroadcastReceiver);
    }
}
