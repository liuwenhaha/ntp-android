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

    //底部导航菜单名
    private TextView allCourse, homework, me;//课程,作业，我
    private LinearLayout search;//搜索


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
        allCourse = (TextView) findViewById(R.id.allCourse);
        homework = (TextView) findViewById(R.id.homework);
        me = (TextView) findViewById(R.id.me);
        search = (LinearLayout) findViewById(R.id.search);
        allCourse.setOnClickListener(this);
        homework.setOnClickListener(this);
        me.setOnClickListener(this);
        search.setOnClickListener(this);
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
            case R.id.search://搜索课程
                Intent intent = new Intent(getApplicationContext(), SearchCourseActivity.class);
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
         * 设置Fragment被选中时菜单字体颜色
         *
         * @param position Fragment索引值
         */
        public void setTextColor(int position) {
            allCourse.setTextColor(getResources().getColor(R.color.menu_text_normal));
            homework.setTextColor(getResources().getColor(R.color.menu_text_normal));
            me.setTextColor(getResources().getColor(R.color.menu_text_normal));
            if (position == 0) {
                allCourse.setTextColor(getResources().getColor(R.color.menu_text_pressed));
            } else if (position == 1) {
                homework.setTextColor(getResources().getColor(R.color.menu_text_pressed));
            } else if (position == 2) {
                me.setTextColor(getResources().getColor(R.color.menu_text_pressed));
            }
        }
    }
}
