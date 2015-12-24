package com.ntp.ui.course;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.ntp.base.BaseFragment;
import com.ntp.model.gson.CourseVideoGson;
import com.ntp.network.HttpRequestHelper;
import com.ntp.network.okhttp.ObjectCallbackHandler;
import com.ntp.ui.R;
import com.ntp.adapter.CoursevideoAdapter;
import com.ntp.util.ConstantValue;
import com.ntp.util.AppConfig;
import com.ntp.model.Coursevideo;
import com.ntp.util.ErrorCodeUtil;
import com.ntp.util.NetworkStateUtil;
import com.squareup.okhttp.Request;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * 课程教学视频
 */
@ContentView(R.layout.fragment_course_video)
public class CoursevideoFragment extends BaseFragment implements CoursevideoAdapter.Callback {

    @ViewInject(R.id.coursevideoList)
    private ListView mCoursevideoList;

    @ViewInject(R.id.load)
    private LinearLayout load;

    private CoursevideoAdapter mCoursevideoAdapter;
    private List<Coursevideo> list=new ArrayList<Coursevideo>();
    private String code;//课程代码


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        code = getArguments().getString("code");
        HttpRequestHelper.getInstance().getCourseVideo(code,new ObjectCallbackHandler<CourseVideoGson>(){
            @Override
            public void onFailure(Request request, IOException e, int response) {
                super.onFailure(request, e, response);
                load.setVisibility(View.GONE);
                showToast(ErrorCodeUtil.SERVER_ERROR);
            }

            @Override
            public void onResponse(CourseVideoGson courseVideoGson) {
                super.onResponse(courseVideoGson);
                if (courseVideoGson!=null){
                    for (CourseVideoGson.VideosEntity videosEntity:courseVideoGson.getVideos()){
                        Coursevideo coursevideo = new Coursevideo(videosEntity.getName(),videosEntity.getPath(),videosEntity.getSize());
                        list.add(coursevideo);
                    }
                    load.setVisibility(View.GONE);
                    mCoursevideoAdapter = new CoursevideoAdapter(list, getActivity(), CoursevideoFragment.this);
                    mCoursevideoList.setAdapter(mCoursevideoAdapter);
                }else {
                    load.setVisibility(View.GONE);
                }
            }
        });

    }

    /**
     * 回调接口方法
     */
    @Override
    public void click(View v) {
        switch (v.getId()) {
            case R.id.watch://观看视频
                //检测网络是否可用
                if (!NetworkStateUtil.isNetworkConnected(getActivity().getApplicationContext())) {
                    Toast.makeText(getActivity().getApplicationContext(), "当前网络不可用", Toast.LENGTH_LONG).show();
                    break;
                }
                //检查当前是否禁用了移动网络下载课件和播放视频
                if (NetworkStateUtil.isMobileConnected(getActivity().getApplicationContext()) && !AppConfig.getConfig(getActivity().getApplicationContext())) {
                    Toast.makeText(getActivity().getApplicationContext(), "你已经禁用移动网络下载课件和观看视频", Toast.LENGTH_LONG).show();
                    break;
                }
                int position=Integer.parseInt(v.getTag().toString());
                String path=list.get(position).getPath();
                Intent intent = new Intent(getActivity(), VideoPlayActivity.class);
                intent.putExtra("path", ConstantValue.PATH_DOWNLOAD_COURSE_VIDEO+path);
                startActivity(intent);
                break;
        }
    }
}
