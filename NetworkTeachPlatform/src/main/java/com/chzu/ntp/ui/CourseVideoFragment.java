package com.chzu.ntp.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * 课程教学视频
 */
public class CoursevideoFragment extends Fragment {

    private static CoursevideoFragment mCoursevideoFragment;

    /**
     * 创建单例对象
     */
    public static CoursevideoFragment getInstance() {
        if (mCoursevideoFragment == null) {
            mCoursevideoFragment = new CoursevideoFragment();
        }
        return mCoursevideoFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_course_video, container, false);
    }


}
