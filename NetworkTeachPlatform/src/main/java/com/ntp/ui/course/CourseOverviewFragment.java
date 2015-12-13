package com.ntp.ui.course;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ntp.ui.R;
import com.ntp.util.ConstantValue;
import com.ntp.util.NetworkStateUtil;
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
    private TextView content;//课程简介
    private LinearLayout load;//加载提示


    /**
     * @param code 需要向Fragment传入的课程代码
     */
    public static CourseOverviewFragment getInstance(String code) {
        mCourseOverviewFragment = new CourseOverviewFragment();
        Bundle bundle = new Bundle();
        bundle.putString("code", code);
        mCourseOverviewFragment.setArguments(bundle);
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
        content= (TextView) view.findViewById(R.id.content);
        load= (LinearLayout) view.findViewById(R.id.load);
        RequestParams params=new RequestParams();
        params.put("code", code);
        if(NetworkStateUtil.isNetworkConnected(getActivity().getApplicationContext())) {//网络可用
            client.post(ConstantValue.PATH_COURSE_DETAIL, params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers,
                                      JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    if (response != null) {
                        try {
                            String overview = response.getJSONObject("course").getString("overview");
                            content.setText(overview.equals("null") ? "无" : overview);
                            load.setVisibility(View.GONE);
                            Log.i(TAG, overview);
                        } catch (JSONException e) {
                            Log.i(TAG, e.toString());
                            e.printStackTrace();
                        }
                    } else {
                        if(getActivity()!=null){
                            Toast.makeText(getActivity().getApplicationContext(), "加载失败", Toast.LENGTH_SHORT).show();
                        }
                        load.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    Log.i(TAG, throwable.toString());
                    if(getActivity()!=null){
                        Toast.makeText(getActivity().getApplicationContext(), "加载失败", Toast.LENGTH_SHORT).show();
                    }
                    load.setVisibility(View.GONE);
                }
            });
        }
        return view;
    }

}
