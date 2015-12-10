package com.ntp.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ntp.activity.course.CourseListFragment;
import com.ntp.activity.course.SearchCourseActivity;
import com.ntp.activity.me.MeFragment;
import com.ntp.activity.notice.NoticeFragment;
import com.ntp.adapter.FragAdapter;
import com.ntp.base.BaseActivity;
import com.ntp.dao.PreferenceDao;
import com.ntp.util.AppUtil;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

/**
 * 主程序
 */
@EActivity(R.layout.activity_main)
public class MainActivity extends BaseActivity implements View.OnClickListener {

    @ViewById
    ViewPager viewPager;

    //底部功能导航图片和文字课程,消息（作业），我
    @ViewById
    ImageView allCourse;

    @ViewById
    ImageView homework;

    @ViewById
    ImageView me;

    @ViewById
    TextView courseTxt;

    @ViewById
    TextView homeworkTxt;

    @ViewById
    TextView meTxt;

    //消息红点
    @ViewById
    ImageView noticeRed;

    @ViewById
    TextView tip;

    @ViewById
    LinearLayout search;

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

    /**
     * 设置底部导航
     * 以下代码不能在onCreate写，因为View注入在onCreate之后
     */
    @AfterViews
    void afterInitView() {
        AppUtil.setStatusBarDarkMode(true, this);
        if (PreferenceDao.isNoticeRed(getApplicationContext())){
            noticeRed.setVisibility(View.VISIBLE);
        }
        fragments = new ArrayList<Fragment>();
        fragments.add(new CourseListFragment());
        fragments.add(new NoticeFragment());
        fragments.add(new MeFragment());
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
                noticeRed.setVisibility(View.VISIBLE);
            }
        }
    };


    @Click({R.id.allCourse, R.id.homework, R.id.me, R.id.search})
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
