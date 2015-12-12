package com.ntp.ui.course;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.ntp.ui.R;
import com.ntp.adapter.CoursevideoAdapter;
import com.ntp.util.PathConstant;
import com.ntp.dao.PreferenceDao;
import com.ntp.model.Coursevideo;
import com.ntp.util.NetworkStateUtil;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * 课程教学视频
 */
public class CoursevideoFragment extends Fragment implements CoursevideoAdapter.Callback {

    private static CoursevideoFragment mCoursevideoFragment;
    private ListView mCoursevideoList;
    private CoursevideoAdapter mCoursevideoAdapter;
    private LinearLayout load;

    private List<Coursevideo> list;
    private String code;//课程代码

    private static AsyncHttpClient client = new AsyncHttpClient();
    private static final String TAG = "CourseVideoFragment";

    /**
     * 创建对象
     */
    public static CoursevideoFragment getInstance(String code) {
        mCoursevideoFragment = new CoursevideoFragment();
        Bundle bundle = new Bundle();
        bundle.putString("code", code);
        mCoursevideoFragment.setArguments(bundle);
        return mCoursevideoFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_course_video, container, false);
        mCoursevideoList = (ListView) view.findViewById(R.id.coursevideoList);
        load = (LinearLayout) view.findViewById(R.id.load);
        list = new ArrayList<Coursevideo>();
        mCoursevideoAdapter = new CoursevideoAdapter(list, getActivity().getApplicationContext(), this);
        mCoursevideoList.setAdapter(mCoursevideoAdapter);
        code = getArguments().getString("code");
        RequestParams params = new RequestParams();
        params.put("code", code);
        if (NetworkStateUtil.isNetworkConnected(getActivity().getApplicationContext())) {//网络可用
            client.post(PathConstant.PATH_COURSE_VIDEO, params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers,
                                      JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    if (response != null) {
                        try {
                            JSONArray ja = response.getJSONArray("videos");
                            for (int i = 0; i < ja.length(); i++) {
                                JSONObject jb = ja.getJSONObject(i);
                                Coursevideo coursevideo = new Coursevideo(jb.getString("name"), jb.getString("path"), jb.getString("size").equals("null") ? "" : jb.getString("size"));
                                list.add(coursevideo);
                            }
                            load.setVisibility(View.GONE);
                            mCoursevideoAdapter = new CoursevideoAdapter(list, getActivity().getApplicationContext(), CoursevideoFragment.this);
                            mCoursevideoAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(getActivity().getApplicationContext(), "加载失败", Toast.LENGTH_SHORT).show();
                        load.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    Log.i(TAG, throwable.toString());
                    load.setVisibility(View.GONE);
                }
            });
        }
        return view;
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
                if (NetworkStateUtil.isMobileConnected(getActivity().getApplicationContext()) && !PreferenceDao.getConfig(getActivity().getApplicationContext())) {
                    Toast.makeText(getActivity().getApplicationContext(), "你已经禁用移动网络下载课件和观看视频", Toast.LENGTH_LONG).show();
                    break;
                }
                int position=Integer.parseInt(v.getTag().toString());
                String path=list.get(position).getPath();
                Intent intent = new Intent(getActivity(), VideoPlayActivity.class);
                intent.putExtra("path",PathConstant.PATH_DOWNLOAD_COURSE_VIDEO+path);
                startActivity(intent);
                break;
        }
    }
}
