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
import android.widget.Toast;

import com.chzu.ntp.adapter.CardView;
import com.chzu.ntp.adapter.CardViewAdapter;
import com.chzu.ntp.dao.CourseDao;
import com.chzu.ntp.dao.CourseTypeDao;
import com.chzu.ntp.model.Course;
import com.chzu.ntp.util.HttpUtil;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * 课程界面,供学生浏览课程
 */
public class CourseListFragment extends Fragment implements AdapterView.OnItemClickListener {

    /**
     * Android-PullToRefresh中的ListView控件，具有下拉刷新特征
     */
    PullToRefreshListView pullToRefreshView;
    private static CourseListFragment courseListFragment;
    private static CardViewAdapter adapter;//课程适配器
    private CourseDao courseDao;
    private CourseTypeDao courseTypeDao;
    private LinearLayout load;
    /**
     * 请求课程网络地址
     */
    public  static final String PATH = "http://10.0.2.2/ntp/phone/courseList";
    public  static final String TAG = "json";


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
        courseDao = new CourseDao(getActivity().getApplicationContext());
        courseTypeDao=new CourseTypeDao(getActivity().getApplicationContext());
        List<Course> courseList = courseDao.getAllCourse();
        if (courseList.size() > 0) {//如果本地有缓存,隐藏"提示正在加载课程中"视图
            Log.i(TAG, "本地有缓存。。。");
            load.setVisibility(View.GONE);
            adapter = new CardViewAdapter(getItems(courseList), getActivity());
            pullToRefreshView.setAdapter(adapter);
        } else {//本地没有缓存，请求网络数据
            adapter = new CardViewAdapter(getItems(getData()), getActivity());
            load.setVisibility(View.GONE);
            pullToRefreshView.setAdapter(adapter);
        }
        pullToRefreshView.setOnItemClickListener(this);
        return view;
    }

    /**
     * 获取网络数据并缓存到数据库
     */
    private List<Course> getData() {
        JSONObject jb = HttpUtil.getDataFromInternet(PATH);
        List<Course> list = new ArrayList<Course>();
        if (jb != null) {//请求成功
            try {
                JSONArray ja = jb.getJSONArray("list");
                for (int i = 0; i < ja.length(); i++) {
                    JSONObject j = ja.getJSONObject(i);
                    Course course = new Course(j.getString("code"), j.getString("name"), j.getJSONObject("coursetype").getString("type"), j.getJSONObject("user").getString("name"));
                    list.add(course);
                    courseDao.save(course);//缓存到数据库
                    Log.i(TAG, course.toString());
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Log.i(TAG, "没有获取到后台数据");
        }
        return list;
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
        List<Course> list = new ArrayList<Course>();

        @Override //后台耗时操作
        protected String[] doInBackground(Void... params) {
            try {
                //Thread.sleep(4000);
                JSONObject jb = HttpUtil.getDataFromInternet(new URL(PATH));
                if (jb != null) {
                    JSONArray ja = jb.getJSONArray("list");
                    courseDao.delete();//先清空缓存
                    for (int i = 0; i < ja.length(); i++) {
                        JSONObject j = ja.getJSONObject(i);
                        Course course = new Course(j.getString("code"), j.getString("name"), j.getJSONObject("coursetype").getString("type"), j.getJSONObject("user").getString("name"));
                        list.add(course);
                        courseDao.save(course);//缓存到数据库
                        Log.i(TAG, course.toString());
                    }
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "没有更新到数据，请检查网络，稍后再试", Toast.LENGTH_SHORT).show();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            updateData();//下拉更新首页时同时更新所有的课程类型，保存到数据库
            return null;
        }

        @Override //操作UI
        protected void onPostExecute(String[] result) {
            pullToRefreshView.onRefreshComplete();
            if (list.size() > 0) {//获取到数据，更新UI
                load.setVisibility(View.GONE);
                adapter = new CardViewAdapter(getItems(list), getActivity());
                pullToRefreshView.setAdapter(adapter);
                Toast.makeText(getActivity().getApplicationContext(), "更新成功", Toast.LENGTH_SHORT).show();
            }
            super.onPostExecute(result);
        }

        /**
         * 更新课程类型，保存到数据库
         */
        public void updateData() {
            try {
                JSONObject jb = HttpUtil.getDataFromInternet(new URL(CourseTypeSelectActivity.PATH));
                if (jb != null) {
                    courseTypeDao.delete();//先清空
                    JSONArray ja = jb.getJSONArray("listCType");
                    for (int i = 0; i < ja.length(); i++) {
                        JSONObject j = ja.getJSONObject(i);
                        courseTypeDao.save(j.getString("type"));
                    }
                } else {
                    Log.i(TAG, "没有取到后台数据");
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
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

    @Override
    public void onDestroy() {
        courseDao.close();
        super.onDestroy();
    }
}



