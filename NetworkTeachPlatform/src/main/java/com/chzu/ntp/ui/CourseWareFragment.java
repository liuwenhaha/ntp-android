package com.chzu.ntp.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.chzu.ntp.adapter.CoursevideoAdapter;
import com.chzu.ntp.adapter.CoursewareAdapter;
import com.chzu.ntp.model.Courseware;

import java.util.ArrayList;
import java.util.List;

/**
 * 课程课件
 */
public class CoursewareFragment extends Fragment implements CoursewareAdapter.Callback{
    private static CoursewareFragment mCoursewareFragment;
    private ListView mCourseWareList;
    private CoursewareAdapter mCoursewareAdapter;

    /**
     * 创建单例对象
     */
    public static CoursewareFragment getInstance() {
        if (mCoursewareFragment == null) {
            mCoursewareFragment = new CoursewareFragment();
        }
        return mCoursewareFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_course_ware, container, false);
        mCourseWareList= (ListView) view.findViewById(R.id.courseWareList);
        mCoursewareAdapter=new CoursewareAdapter(getData(),getActivity().getApplicationContext(),this);
        mCourseWareList.setAdapter(mCoursewareAdapter);
        return view;
    }


    /**
     * 模拟数据
     */
    public List<Courseware> getData(){
        List<Courseware> list=new ArrayList<Courseware>();
        for (int i = 0; i < 5; i++) {
            Courseware courseware=new Courseware(""+i,"java面向对象",i+"M");
            list.add(courseware);
        }
        return list;
    }


    /**
     * 回调接口
     */
    @Override
    public void click(View v) {
        switch (v.getId()){
            case  R.id.myDownload:
                Toast.makeText(getActivity().getApplicationContext(),"你点击图片了",Toast.LENGTH_LONG).show();
                break;
        }
    }
}
