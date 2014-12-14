package com.chzu.ntp.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 当首次加载课程时，提示用户课程加载中
 */
public class LoadCourseFragment extends Fragment {

    private static LoadCourseFragment loadCourseFragment;

    /**
     * 创建单例LoadCourseFragment对象
     */
    public static LoadCourseFragment getInstance() {
        if (loadCourseFragment == null) {
            loadCourseFragment = new LoadCourseFragment();
        }
        return loadCourseFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_load_course, container, false);
    }

}
