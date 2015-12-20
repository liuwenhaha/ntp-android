package com.ntp.ui.me;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.ntp.base.BaseActivity;
import com.ntp.model.gson.CoursePageInfoGson;
import com.ntp.network.HttpRequestHelper;
import com.ntp.network.okhttp.CallbackHandler;
import com.ntp.network.okhttp.GsonOkHttpResponse;
import com.ntp.ui.R;
import com.ntp.ui.course.CourseDetailActivity;
import com.ntp.adapter.CourseAdapter;
import com.ntp.util.AppConfig;
import com.ntp.model.Course;
import com.ntp.util.AppUtil;
import com.ntp.util.LogUtil;
import com.ntp.util.NetworkStateUtil;
import com.squareup.okhttp.Request;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 我的课程(已选学)
 * @author yanxing
 */
@ContentView(R.layout.activity_my_course)
public class MyCourseActivity extends BaseActivity implements PullToRefreshBase.OnRefreshListener{

    @ViewInject(R.id.pull_to_refresh_listview)
    private PullToRefreshListView pullToRefreshView;

    @ViewInject(R.id.tip)
    private TextView tip;

    private CourseAdapter courseAdapter;
    private List<Course> courses = new ArrayList<Course>();
    private String username;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppUtil.setStatusBarDarkMode(true,this);
        pullToRefreshView.setOnRefreshListener(this);
        pullToRefreshView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        courseAdapter=new CourseAdapter(courses,this);
        pullToRefreshView.setAdapter(courseAdapter);
        username= AppConfig.getLoadName(getApplicationContext());
        if (!username.equals("")){
            tip.setVisibility(View.GONE);
            pullToRefreshView.setRefreshing(true);
        }
    }

    @Override
    public void onRefresh(PullToRefreshBase refreshView) {
        loadData();
    }

    /**
     * 加载课程列表
     */
    private void loadData() {
        if (!NetworkStateUtil.isNetworkConnected(this)){
            showToast(NetworkStateUtil.NO_NETWORK);
            return;
        }
        GsonOkHttpResponse gsonOkHttpResponse=new GsonOkHttpResponse(CoursePageInfoGson.class);
        HttpRequestHelper.getInstance().getMyCourse(username, new CallbackHandler<CoursePageInfoGson>(gsonOkHttpResponse) {
            @Override
            public void onFailure(Request request, IOException e, int code) {
                LogUtil.e(TAG, request.toString() + e.toString());
                pullToRefreshView.onRefreshComplete();
                showToast("连接异常，请稍后再试");
            }

            @Override
            public void onResponse(CoursePageInfoGson coursePageInfoGson) {
                if (coursePageInfoGson.getList().isEmpty()) {
                    pullToRefreshView.onRefreshComplete();
                    showToast("你还没有选择课程");
                    return;
                }
                courses.clear();
                for (CoursePageInfoGson.ListEntity listEntity : coursePageInfoGson.getList()) {
                    Course course = new Course();
                    course.setName(listEntity.getName());
                    course.setImageUri(listEntity.getImage());
                    course.setCode(listEntity.getCode());
                    course.setTeacher(listEntity.getUser().getName());
                    course.setType(listEntity.getCoursetype().getType());
                    courses.add(course);
                }
                pullToRefreshView.onRefreshComplete();
                courseAdapter.update(courses);
            }
        });
    }

    @Event(value = R.id.pull_to_refresh_listview,type =AdapterView.OnItemClickListener.class)
    private void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getApplicationContext(), CourseDetailActivity.class);
        TextView text = (TextView) view.findViewById(R.id.code);
        TextView textView = (TextView) view.findViewById(R.id.courseName);
        String code = (String) text.getText();
        String name = (String) textView.getText();
        Bundle bundle = new Bundle();
        bundle.putString("code", code);
        bundle.putString("name", name);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
