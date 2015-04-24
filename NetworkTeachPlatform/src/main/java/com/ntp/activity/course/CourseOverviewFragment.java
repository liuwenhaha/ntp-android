package com.ntp.activity.course;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ntp.activity.R;
import com.ntp.dao.PathConstant;
import com.ntp.util.NetworkState;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 课程简介
 */
public class CourseOverviewFragment extends Fragment {

    private static CourseOverviewFragment mCourseOverviewFragment;
    private String code;//课程代码
    private static final String TAG = "CourseOverviewFragment";
    private static AsyncHttpClient client = new AsyncHttpClient();


    /**
     * 创建单例对象
     * @param code 需要向Fragment传入的课程代码
     */
    public static CourseOverviewFragment getInstance(String code) {
        if (mCourseOverviewFragment == null) {
            mCourseOverviewFragment = new CourseOverviewFragment();
            Bundle bundle=new Bundle();
            bundle.putString("code",code);
            mCourseOverviewFragment.setArguments(bundle);
        }
        return mCourseOverviewFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        code=getArguments().getString("code");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_course_overview, container, false);
        RequestParams params=new RequestParams();
        params.put("code",code);
        if(NetworkState.isNetworkConnected(getActivity().getApplicationContext())) {//网络可用
            client.post(PathConstant.PATH_COURSE_DETAIL, params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers,
                                      JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    try {
                        String overview = response.getJSONObject("course").getString("overview");
                        Log.i(TAG, overview);
                    } catch (JSONException e) {
                        Log.i(TAG, e.toString());
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    Log.i(TAG, responseString);
                    Log.i(TAG, throwable.toString());
                }
            });
        }
        return view;
    }


}
