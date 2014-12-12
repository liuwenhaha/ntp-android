package com.chzu.ntp.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chzu.ntp.util.FragAdapter;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends FragmentActivity implements View.OnClickListener {

    private ViewPager viewPager;
    private FragAdapter fragAdapter;//ViewPager适配器
    private List<Fragment> fragments;
    private Fragment allCourseFragment, homeworkFragment, meFragment;//三个主界面

    //顶部导航菜单名
    private ImageView allCourse, homework, me;//课程,作业，我
    private TextView courseIndicator, homeworkIndicator, meIndicator;//课程、作业、我指示下划线
    private TextView courseType, courseTypeBg;//所有课程及其背景

    private LinearLayout courseTypeChoice;//所有课程

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        courseIndicator = (TextView) findViewById(R.id.courseIndicator);
        homeworkIndicator = (TextView) findViewById(R.id.homeworkIndicator);
        meIndicator = (TextView) findViewById(R.id.meIndicator);
        courseTypeChoice = (LinearLayout) findViewById(R.id.courseTypeChoice);
        courseType = (TextView) findViewById(R.id.courseType);
        courseTypeBg = (TextView) findViewById(R.id.courseTypeBg);
        allCourse.setOnClickListener(this);
        homework.setOnClickListener(this);
        me.setOnClickListener(this);
        courseTypeChoice.setOnClickListener(this);
        fragments = new ArrayList<Fragment>();
        allCourseFragment = new AllCourseFragment();
        homeworkFragment = new HomeworkFragment();
        meFragment = new MeFragment();
        fragments.add(allCourseFragment);
        fragments.add(homeworkFragment);
        fragments.add(meFragment);
        fragAdapter = new FragAdapter(getSupportFragmentManager(), fragments);
        viewPager.setAdapter(fragAdapter);
        viewPager.setCurrentItem(0);//设置默认显示AllCourseFragment界面
        viewPager.setOnPageChangeListener(new MyViewPagerChangeListener());
    }

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
            case R.id.courseTypeChoice:
                Intent intent = new Intent(this, CoursetypeSelectActivity.class);
                startActivity(intent);
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
                case 0://AllCourseFragment被选中
                    setTextColor(0);
                    break;
                case 1://HomeworkFragment被选择
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
         * 设置Fragment被选中时菜单图片、指示下划线、所有课程区域LinearLayout的文字和背景设置
         *
         * @param position Fragment索引值
         */
        public void setTextColor(int position) {
            allCourse.setImageDrawable(getResources().getDrawable(R.drawable.homepage_normal));
            homework.setImageDrawable(getResources().getDrawable(R.drawable.homework_normal));
            me.setImageDrawable(getResources().getDrawable(R.drawable.me_normal));
            courseIndicator.setVisibility(View.INVISIBLE);
            homeworkIndicator.setVisibility(View.INVISIBLE);
            meIndicator.setVisibility(View.INVISIBLE);
            if (position == 0) {//AllCourseFragment被选中
                allCourse.setImageDrawable(getResources().getDrawable(R.drawable.homepage_pressed));
                courseIndicator.setVisibility(View.VISIBLE);
                courseTypeChoice.setClickable(true);//设置所有课程区域LinearLayout点击
                courseType.setText(getString(R.string.allCourse));//显示所有课程
                courseTypeBg.setVisibility(View.VISIBLE);//显示所有课程背景
            } else if (position == 1) {//HomeworkFragment被选择
                homework.setImageDrawable(getResources().getDrawable(R.drawable.homework_pressed));
                homeworkIndicator.setVisibility(View.VISIBLE);
                courseTypeChoice.setClickable(false);//设置所有课程区域LinearLayout不可点击
                courseType.setText(getString(R.string.homework));//显示我的作业
                courseTypeBg.setVisibility(View.GONE);//隐藏所有课程背景
            } else if (position == 2) {//MeFragment被选择
                me.setImageDrawable(getResources().getDrawable(R.drawable.me_pressed));
                meIndicator.setVisibility(View.VISIBLE);
                courseTypeChoice.setClickable(false);//设置所有课程区域LinearLayout不可点击
                courseType.setText(getString(R.string.me));//显示个人中心
                courseTypeBg.setVisibility(View.GONE);//隐藏所有课程背景
            }
        }
    }
}
