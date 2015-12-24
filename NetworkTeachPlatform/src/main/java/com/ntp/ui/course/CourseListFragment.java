package com.ntp.ui.course;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.ntp.base.BaseFragment;
import com.ntp.model.gson.CoursePageInfoGson;
import com.ntp.model.gson.CourseTypeGson;
import com.ntp.network.HttpRequestHelper;
import com.ntp.network.okhttp.ObjectCallbackHandler;
import com.ntp.ui.R;
import com.ntp.adapter.CourseAdapter;
import com.ntp.dao.CourseDao;
import com.ntp.dao.CourseTypeDao;
import com.ntp.model.Course;
import com.ntp.util.ErrorCodeUtil;
import com.ntp.util.LogUtil;
import com.ntp.util.NetworkStateUtil;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.squareup.okhttp.Request;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 课程界面,供学生浏览课程
 */
@ContentView(R.layout.fragment_course_list)
public class CourseListFragment extends BaseFragment implements PullToRefreshBase.OnRefreshListener2{

    @ViewInject(R.id.pull_to_refresh_listview)
    private PullToRefreshListView pullToRefreshView;

    private CourseAdapter adapter;
    private CourseDao courseDao;
    private CourseTypeDao courseTypeDao;
    private List<Course> courses = new ArrayList<Course>();

    //当前分页页数
    private int currentPage=1;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pullToRefreshView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        pullToRefreshView.setOnRefreshListener(this);
        adapter = new CourseAdapter(courses, getActivity());
        pullToRefreshView.setAdapter(adapter);
        courseDao = new CourseDao(getActivity());
        courseTypeDao = new CourseTypeDao(getActivity());
        List<Course> courseList = courseDao.getAllCourse();
        //如果本地有缓存数据
        if (courseList.size() > 0) {
            adapter.update(courseList);
            pullToRefreshView.setMode(PullToRefreshBase.Mode.BOTH);
        } else {
            pullToRefreshView.setRefreshing(true);
        }
    }

    //item点击事件响应
    @Event(value = R.id.pull_to_refresh_listview,type=AdapterView.OnItemClickListener.class)
    private void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getActivity(), CourseDetailActivity.class);
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

    //下拉刷新
    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        loadData(true);
    }

    //上拉刷新
    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        loadData(false);
    }

    /**
     * 加载课程列表
     * @param pullUpOrDown true 下拉刷新，false上拉刷新
     */
    private void loadData(final boolean pullUpOrDown) {
        if (!NetworkStateUtil.isNetworkConnected(getActivity())){
            showToast(NetworkStateUtil.NO_NETWORK);
            return;
        }
        if (!pullUpOrDown){//上拉刷新
            currentPage++;
        }else {
            currentPage=1;
        }
        HttpRequestHelper.getInstance().getCourseList(currentPage, 10, new ObjectCallbackHandler<CoursePageInfoGson>() {
            @Override
            public void onFailure(Request request, IOException e,int code) {
                LogUtil.e(TAG, request.toString() + e.toString());
                pullToRefreshView.onRefreshComplete();
                showToast(ErrorCodeUtil.SERVER_ERROR);
            }

            @Override
            public void onResponse(CoursePageInfoGson coursePageInfoGson){
                currentPage = coursePageInfoGson.getCurrentPage();

                if (pullUpOrDown) {//下拉刷新
                    if (coursePageInfoGson.getList().isEmpty()) {
                        pullToRefreshView.onRefreshComplete();
                        showToast("没有课程");
                        return;
                    }
                    courses.clear();
                    pullToRefreshView.setMode(PullToRefreshBase.Mode.BOTH);
                    updateCourseTypeData();
                } else {
                    if (coursePageInfoGson.getList().isEmpty()) {
                        pullToRefreshView.onRefreshComplete();
                        showToast("已经翻到底了");
                        return;
                    }

                }
                for (CoursePageInfoGson.ListEntity listEntity : coursePageInfoGson.getList()) {
                    Course course = new Course();
                    course.setName(listEntity.getName());
                    course.setImageUri(listEntity.getImage());
                    course.setCode(listEntity.getCode());
                    course.setTeacher(listEntity.getUser().getName());
                    course.setType(listEntity.getCoursetype().getType());
                    courses.add(course);
                    if (pullUpOrDown) {
                        courseDao.save(course);//上拉缓存到数据库
                    }
                }
                pullToRefreshView.onRefreshComplete();
                adapter.update(courses);
            }
        });
    }

    /**
     * 更新课程类型，保存到数据库
     */
    public void updateCourseTypeData() {
        HttpRequestHelper.getInstance().getCourseTypeList(new ObjectCallbackHandler<CourseTypeGson>() {
            @Override
            public void onFailure(Request request, IOException e) {
                LogUtil.e(TAG, request.toString() + e.toString());
                showToast(ErrorCodeUtil.SERVER_ERROR);
            }

            @Override
            public void onResponse(CourseTypeGson courseTypeGson){
                courseTypeDao.delete();
                for (CourseTypeGson.ListCTypeEntity listCTypeEntity : courseTypeGson.getListCType()) {
                    courseTypeDao.save(listCTypeEntity.getType());
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        courseDao.close();
        courseTypeDao.close();
        super.onDestroy();
    }
}



