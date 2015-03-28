package com.chzu.ntp.ui;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.chzu.ntp.adapter.CoursevideoAdapter;
import com.chzu.ntp.model.Coursevideo;

import java.util.ArrayList;
import java.util.List;



/**
 * 课程教学视频
 */
public class CoursevideoFragment extends Fragment implements CoursevideoAdapter.Callback{

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
        mCoursevideoList= (ListView) view.findViewById(R.id.coursevideoList);
        mCoursevideoAdapter=new CoursevideoAdapter(getData(),getActivity().getApplicationContext(),this);
        mCoursevideoList.setAdapter(mCoursevideoAdapter);
        return view;
    }

    /**
     * 模拟数据
     */
    public List<Coursevideo> getData(){
        List<Coursevideo> list=new ArrayList<Coursevideo>();
        for (int i = 0; i < 5; i++) {
            Coursevideo coursevideo=new Coursevideo(""+i,"第四讲：AsyncTask运行问题的解决");
            list.add(coursevideo);
        }
        return list;
    }


    /**
     * 回调接口方法
     */
    @Override
    public void click(View v) {
          switch (v.getId()){
              case R.id.watch://观看视频
                  Toast.makeText(getActivity().getApplicationContext(),"视频加载中",Toast.LENGTH_SHORT).show();
                  Intent intent=new Intent(getActivity(),VideoPlayActivity.class);
                  startActivity(intent);
                  break;
          }
    }
}
