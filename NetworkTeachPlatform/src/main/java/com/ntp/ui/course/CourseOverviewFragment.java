package com.ntp.ui.course;


import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ntp.base.BaseFragment;
import com.ntp.model.gson.CourseOverviewGson;
import com.ntp.network.HttpRequestHelper;
import com.ntp.network.okhttp.ObjectCallbackHandler;
import com.ntp.ui.R;
import com.ntp.util.ErrorCodeUtil;
import com.squareup.okhttp.Request;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.io.IOException;

/**
 * 课程简介
 */
@ContentView(R.layout.fragment_course_overview)
public class CourseOverviewFragment extends BaseFragment {

    @ViewInject(R.id.content)
    private TextView mContent;//课程简介

    @ViewInject(R.id.load)
    private LinearLayout mLoad;

    private String mCode;//课程代码

    @Override
     public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCode=getArguments().getString("code");
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        HttpRequestHelper.getInstance().getCourseOverview(mCode, new ObjectCallbackHandler<CourseOverviewGson>() {
            @Override
            public void onResponse(CourseOverviewGson courseOverviewGson) {
                if (courseOverviewGson != null) {
                    mLoad.setVisibility(View.GONE);
                    mContent.setText(courseOverviewGson.getCourse().getOverview());
                }
            }

            @Override
            public void onFailure(Request request, IOException e, int response) {
                super.onFailure(request, e, response);
                showToast(ErrorCodeUtil.SERVER_ERROR);
                mLoad.setVisibility(View.GONE);
            }
        });
    }
}
