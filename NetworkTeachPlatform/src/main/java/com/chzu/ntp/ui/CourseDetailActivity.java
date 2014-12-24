package com.chzu.ntp.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.chzu.ntp.adapter.FragAdapter;

import java.util.ArrayList;
import java.util.List;


/**
 * 课程详细
 */
public class CourseDetailActivity extends FragmentActivity implements View.OnClickListener {

    private ImageView back;//返回
    private TextView courseName;
    private ViewPager viewPager;
    private FragAdapter fragAdapter;//ViewPager适配器
    private List<Fragment> fragments = new ArrayList<Fragment>();
    //tab选项卡文字
    private TextView mOverview, mCourseWare, mCourseVideo, mCourseForum;
    //tab选项卡下划线
    private ImageView mOverviewIndicator, mCourseWareIndicator, mCourseVideoIndicator, mCourseForumIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);
        initView();
    }

    //初始化界面
    private void initView() {
        courseName = (TextView) findViewById(R.id.courseName);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        mOverview = (TextView) findViewById(R.id.overview);
        mCourseWare = (TextView) findViewById(R.id.courseWare);
        mCourseVideo = (TextView) findViewById(R.id.courseVideo);
        mCourseForum = (TextView) findViewById(R.id.courseForum);
        mOverviewIndicator = (ImageView) findViewById(R.id.overviewIndicator);
        mCourseWareIndicator = (ImageView) findViewById(R.id.courseWareIndicator);
        mCourseVideoIndicator = (ImageView) findViewById(R.id.courseVideoIndicator);
        mCourseForumIndicator = (ImageView) findViewById(R.id.courseForumIndicator);
        String name = getIntent().getExtras().getString("name");
        courseName.setText(name);//设置课程名称
        mOverview.setOnClickListener(this);
        mCourseWare.setOnClickListener(this);
        mCourseVideo.setOnClickListener(this);
        mCourseForum.setOnClickListener(this);
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(this);
        fragments.add(CourseOverviewFragment.getInstance());
        fragments.add(CourseWareFragment.getInstance());
        fragments.add(CourseVideoFragment.getInstance());
        fragments.add(CourseForumFragment.getInstance());
        fragAdapter = new FragAdapter(getSupportFragmentManager(), fragments);
        viewPager.setAdapter(fragAdapter);
        viewPager.setCurrentItem(0);
        viewPager.setOnPageChangeListener(new MyViewPagerChangeListener());
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
                case 0://简介Fragment被选择
                    setTextColor(0);
                    break;
                case 1://课件Fragent被选择
                    setTextColor(1);
                    break;
                case 2://课程视频被选择
                    setTextColor(2);
                    break;
                case 3://论坛被选择
                    setTextColor(3);
                    break;
            }
        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    }

    /**
     * 设置Fragment被选中时菜单图片、指示下划线
     *
     * @param position Fragment索引值
     */
    public void setTextColor(int position) {
        mOverview.setTextColor(getResources().getColor(R.color.menu_text_reserve));
        mCourseWare.setTextColor(getResources().getColor(R.color.menu_text_reserve));
        mCourseVideo.setTextColor(getResources().getColor(R.color.menu_text_reserve));
        mCourseForum.setTextColor(getResources().getColor(R.color.menu_text_reserve));
        mOverviewIndicator.setVisibility(View.INVISIBLE);
        mCourseWareIndicator.setVisibility(View.INVISIBLE);
        mCourseVideoIndicator.setVisibility(View.INVISIBLE);
        mCourseForumIndicator.setVisibility(View.INVISIBLE);
        if (position == 0) {
            mOverviewIndicator.setVisibility(View.VISIBLE);
            mOverview.setTextColor(getResources().getColor(R.color.course_name));
        } else if (position == 1) {
            mCourseWareIndicator.setVisibility(View.VISIBLE);
            mCourseWare.setTextColor(getResources().getColor(R.color.course_name));
        } else if (position == 2) {
            mCourseVideoIndicator.setVisibility(View.VISIBLE);
            mCourseVideo.setTextColor(getResources().getColor(R.color.course_name));
        } else if (position == 3) {
            mCourseForumIndicator.setVisibility(View.VISIBLE);
            mCourseForum.setTextColor(getResources().getColor(R.color.course_name));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.overview:
                viewPager.setCurrentItem(0);
                break;
            case R.id.courseWare:
                viewPager.setCurrentItem(1);
                break;
            case R.id.courseVideo:
                viewPager.setCurrentItem(2);
                break;
            case R.id.courseForum:
                viewPager.setCurrentItem(3);
                break;
        }
    }
}
