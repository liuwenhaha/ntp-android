package com.chzu.ntp.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 课程跟帖
 */
public class CourseForumFragment extends Fragment {
    private static CourseForumFragment mCourseForumFragment;

    /**
     * 创建单例对象
     */
    public static CourseForumFragment getInstance() {
        if (mCourseForumFragment == null) {
            mCourseForumFragment = new CourseForumFragment();
        }
        return mCourseForumFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_course_forum, container, false);
    }


}
