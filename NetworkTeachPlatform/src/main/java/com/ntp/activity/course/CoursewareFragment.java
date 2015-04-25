package com.ntp.activity.course;


import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.ntp.activity.R;
import com.ntp.activity.me.LoginActivity;
import com.ntp.adapter.CoursewareAdapter;
import com.ntp.dao.PathConstant;
import com.ntp.dao.PreferenceDao;
import com.ntp.model.Courseware;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 课程课件
 */
public class CoursewareFragment extends Fragment implements CoursewareAdapter.Callback{
    private static CoursewareFragment mCoursewareFragment;
    private ListView mCourseWareList;
    private List<Courseware> list;
    private CoursewareAdapter mCoursewareAdapter;
    private static AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
    private static final String TAG="CoursewareFragment";
    private String code;//课程代码

    /**
     * @param code 课程代码
     */
    public static CoursewareFragment getInstance(String code) {
        mCoursewareFragment = new CoursewareFragment();
        Bundle bundle = new Bundle();
        bundle.putString("code", code);
        mCoursewareFragment.setArguments(bundle);
        return mCoursewareFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            code = getArguments().getString("code");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_course_ware, container, false);
        mCourseWareList= (ListView) view.findViewById(R.id.courseWareList);
        RequestParams params=new RequestParams();
        params.put("code",code);
        Log.i(TAG,code);
        list=new ArrayList<Courseware>();
        asyncHttpClient.post(PathConstant.PATH_COURSE_WARE,params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (response!=null){
                    try {
                        JSONArray ja=response.getJSONArray("coursewares");
                        for (int i=0;i<ja.length();i++){
                            JSONObject jb=ja.getJSONObject(i);
                            Courseware courseware=new Courseware(null,jb.getString("name"),jb.getString("path"),jb.getString("size"));
                            list.add(courseware);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        mCoursewareAdapter=new CoursewareAdapter(list,getActivity().getApplicationContext(),this);
        mCourseWareList.setAdapter(mCoursewareAdapter);
        return view;
    }

    /**
     * 回调接口
     */
    @Override
    public void click(View v,String name,String path) {
        switch (v.getId()){
            case  R.id.myDownload:
                if (PreferenceDao.getLoadName(getActivity().getApplicationContext()).equals("")){
                    Toast.makeText(getActivity().getApplicationContext(),"该操作需要先登录",Toast.LENGTH_LONG).show();
                    return;
                }
                Log.i(TAG,name+"    "+path);
                Toast.makeText(getActivity().getApplicationContext(),"你点击图片了",Toast.LENGTH_LONG).show();
                break;
        }
    }

}
