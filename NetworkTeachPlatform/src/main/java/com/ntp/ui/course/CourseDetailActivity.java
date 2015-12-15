package com.ntp.ui.course;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ntp.ui.R;
import com.ntp.adapter.FragAdapter;
import com.ntp.base.BaseActivity;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * 课程详细:简介、课件、视频、讨论
 */
@ContentView(R.layout.activity_course_detail)
public class CourseDetailActivity extends BaseActivity{

    @ViewInject(R.id.courseName)
    private TextView mCourseName;

    @ViewInject(R.id.viewPager)
    private ViewPager mViewPager;

    //tab选项卡文字
    @ViewInject(R.id.overview)
    private TextView mOverview;

    @ViewInject(R.id.courseWare)
    private TextView mCourseWare;

    @ViewInject(R.id.courseVideo)
    private TextView mCourseVideo;

    @ViewInject(R.id.courseForum)
    private TextView mCourseForum;
    //tab选项卡下划线
    @ViewInject(R.id.overviewIndicator)
    private ImageView mOverviewIndicator;

    @ViewInject(R.id.courseWareIndicator)
    private ImageView mCourseWareIndicator;

    @ViewInject(R.id.courseVideoIndicator)
    private ImageView mCourseVideoIndicator;

    @ViewInject(R.id.courseForumIndicator)
    private ImageView mCourseForumIndicator;

    private String mCode;//课程代码
    private FragAdapter mFragAdapter;//ViewPager适配器
    private List<Fragment> mFragments = new ArrayList<Fragment>();
    private Fragment mCourseOverviewFragment;
    private Fragment mCoursewareFragment;
    private Fragment mCoursevideoFragment;
    private Fragment mCourseForumFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
    }

    //初始化界面
    private void initData() {
        String name = getIntent().getExtras().getString("name");
        mCourseName.setText(name);//设置课程名称
        mCode=getIntent().getExtras().getString("code");
        Bundle bundle = new Bundle();
        bundle.putString("code", mCode);
        mCourseOverviewFragment=new CourseOverviewFragment();
        mCoursewareFragment=new CoursewareFragment();
        mCoursevideoFragment=new CoursevideoFragment();
        mCourseForumFragment=new CourseForumFragment();
        mCourseOverviewFragment.setArguments(bundle);
        mCoursewareFragment.setArguments(bundle);
        mCoursevideoFragment.setArguments(bundle);
        mCourseForumFragment.setArguments(bundle);
        mFragments.add(mCourseOverviewFragment);
        mFragments.add(mCoursewareFragment);
        mFragments.add(mCoursevideoFragment);
        mFragments.add(mCourseForumFragment);
        mFragAdapter = new FragAdapter(getSupportFragmentManager(), mFragments);
        mViewPager.setAdapter(mFragAdapter);
        mViewPager.setOffscreenPageLimit(2);//缓存相邻两个页面
        mViewPager.setCurrentItem(0);
        mViewPager.setOnPageChangeListener(new MyViewPagerChangeListener());
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
                case 1://课件Fragment被选择
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

    @Event(value = {R.id.back,R.id.overview,R.id.courseWare,R.id.courseVideo,R.id.courseForum})
    private void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.overview:
                mViewPager.setCurrentItem(0);
                break;
            case R.id.courseWare:
                mViewPager.setCurrentItem(1);
                break;
            case R.id.courseVideo:
                mViewPager.setCurrentItem(2);
                break;
            case R.id.courseForum:
                mViewPager.setCurrentItem(3);
                break;
        }
    }
}
