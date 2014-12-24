package com.chzu.ntp.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 课程课件
 */
public class CourseWareFragment extends Fragment {
    private static CourseWareFragment mCourseWareFragment;

    /**
     *创建单例对象
     */
    public static CourseWareFragment getInstance() {
        if (mCourseWareFragment==null){
            mCourseWareFragment=new CourseWareFragment();
        }
        return mCourseWareFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_course_ware, container, false);
    }


}
