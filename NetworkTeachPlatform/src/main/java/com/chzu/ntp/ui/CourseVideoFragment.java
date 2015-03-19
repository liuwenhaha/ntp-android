package com.chzu.ntp.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.chzu.ntp.adapter.CoursevideoAdapter;
import com.chzu.ntp.model.Coursevideo;
import com.chzu.ntp.model.Courseware;

import java.util.ArrayList;
import java.util.List;


/**
 * 课程教学视频
 */
public class CoursevideoFragment extends Fragment {

    private static CoursevideoFragment mCoursevideoFragment;
    private ListView mCoursevideoList;
    private CoursevideoAdapter mCoursevideoAdapter;

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
        View view=inflater.inflate(R.layout.fragment_course_video, container, false);
        mCoursevideoList= (ListView) view.findViewById(R.id.coursevideList);
        mCoursevideoAdapter=new CoursevideoAdapter(getData(),getActivity().getApplicationContext());
        mCoursevideoList.setAdapter(mCoursevideoAdapter);
        return view;
    }

    /**
     * 模拟数据
     */
    public List<Coursevideo> getData(){
        List<Coursevideo> list=new ArrayList<Coursevideo>();
        for (int i = 0; i < 5; i++) {
            Coursevideo coursevideo=new Coursevideo(""+i,"第一集");
            list.add(coursevideo);
        }
        return list;
    }


}
