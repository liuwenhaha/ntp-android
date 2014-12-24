package com.chzu.ntp.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chzu.ntp.ui.R;

/**
 * 课程教学视频
 */
public class CourseVideoFragment extends Fragment {

    private static CourseVideoFragment mCourseVideoFragment;

    /**
     *创建单例对象
     */
    public static CourseVideoFragment getInstance() {
        if (mCourseVideoFragment==null){
            mCourseVideoFragment=new CourseVideoFragment();
        }
        return mCourseVideoFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_course_video, container, false);
    }


}
