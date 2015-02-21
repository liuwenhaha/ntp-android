package com.chzu.ntp.ui;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.chzu.ntp.adapter.CardView;
import com.chzu.ntp.adapter.CardViewAdapter;
import com.chzu.ntp.dao.CourseDao;
import com.chzu.ntp.model.Course;
import com.chzu.ntp.util.HttpUtil;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 课程界面,供学生浏览课程
 */
public class CourseListFragment extends Fragment implements AdapterView.OnItemClickListener {
    //Android-PullToRefresh中的ListView控件，具有下拉刷新特征
    PullToRefreshListView pullToRefreshView;
    private static CourseListFragment courseListFragment;
    private static CardViewAdapter adapter;//课程适配器
    private CourseDao courseDao;
    private LinearLayout load;
    /**
     * 标识请求网络数据成功
     */
    private static final int SUCCESS = 1;
    private static final String path = "http://10.0.2.2/ntp/phone/courseList";
    private static final String TAG = "json";


    /**
     * 创建单例对象
     */
    public static CourseListFragment getInstance() {
        if (courseListFragment == null) {
            courseListFragment = new CourseListFragment();
        }
        return courseListFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_course_list, container, false);
        load = (LinearLayout) view.findViewById(R.id.load);
        pullToRefreshView = (PullToRefreshListView) view.findViewById(R.id.pull_to_refresh_listview);
        pullToRefreshView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                String label = DateUtils.formatDateTime(getActivity().getApplicationContext(), System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel("更新于:" + label);
                new GetDataTask().execute();
            }

        });
        courseDao = new CourseDao(getActivity().getApplication());
        List<Course> courseList = courseDao.getAllCourse();
        if (courseList.size() > 0) {//如果本地有缓存,隐藏"提示正在加载课程中"视图
            Log.i(TAG, "本地有缓存。。。");
            load.setVisibility(View.GONE);
            adapter = new CardViewAdapter(getItems(courseList), getActivity());
            pullToRefreshView.setAdapter(adapter);
        } else {//本地没有缓存，请求网络数据
            JSONObject jb = HttpUtil.getDataFromInternet(path);
            if (jb != null) {//请求成功
                try {
                    JSONArray ja = jb.getJSONArray("list");
                    for (int i = 0; i < ja.length(); i++) {
                        JSONObject j = ja.getJSONObject(i);
                        Course course = new Course(j.getString("code"), j.getString("name"), j.getJSONObject("coursetype").getString("type"), j.getJSONObject("user").getString("name"));
                        List<Course> list = new ArrayList<Course>();
                        list.add(course);
                        load.setVisibility(View.GONE);
                        adapter = new CardViewAdapter(getItems(list), getActivity());
                        pullToRefreshView.setAdapter(adapter);
                        courseDao.save(course);//缓存到数据库
                        Log.i(TAG, course.toString());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.i(TAG, "没有获取到后台数据");
            }
        }
        pullToRefreshView.setOnItemClickListener(this);
        return view;
    }

    //ListView点击事件响应
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getActivity(), CourseDetailActivity.class);
        TextView text = (TextView) view.findViewById(R.id.courseName);
        String name = (String) text.getText();
        Bundle bundle = new Bundle();
        bundle.putString("name", name);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    //下拉刷新线程
    private class GetDataTask extends AsyncTask<Void, Void, String[]> {
        @Override
        protected String[] doInBackground(Void... params) {
            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
            }
            return null;
        }

        @Override
        protected void onPostExecute(String[] result) {
            // Call onRefreshComplete when the list has been refreshed.
            pullToRefreshView.onRefreshComplete();
            super.onPostExecute(result);
        }
    }

    /**
     * listView item数据
     */
    private List<CardView> getItems(List<Course> list) {
        List<CardView> cards = new ArrayList<CardView>();
        for (Course course : list) {
            CardView card = new CardView(course.getCode(), course.getName(), course.getType(), course.getUsername());
            cards.add(card);
        }
        return cards;
    }
}



