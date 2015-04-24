package com.ntp.activity.course;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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

import com.ntp.activity.GlobalVariable;
import com.ntp.activity.R;
import com.ntp.adapter.CourseAdapter;
import com.ntp.dao.PathConstant;
import com.ntp.dao.CourseDao;
import com.ntp.dao.CourseTypeDao;
import com.ntp.dao.PreferenceDao;
import com.ntp.model.Course;
import com.ntp.util.HttpUtil;
import com.ntp.util.NetworkState;
import com.ntp.util.SDCardUtil;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.nostra13.universalimageloader.core.ImageLoader;

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

    private ImageLoader imageLoader;//Universal Image Loader加载图片类
    private PullToRefreshListView pullToRefreshView;//Android-PullToRefresh中的ListView控件，具有下拉刷新、上拉刷新特征

    private static CourseListFragment courseListFragment;
    private static CourseAdapter adapter;//课程适配器
    private CourseDao courseDao;
    private CourseTypeDao courseTypeDao;
    private LinearLayout load;

    private static final int RESULT = 1;//发送消息成功标识
    private static final String IMAGE_URI = "file:///mnt/sdcard/ntp/";//缓存图片文件夹
    public static final String TAG = "down_json";//下拉日志标识
    public static final String TAG1 = "up_json";//下拉日志标识


    //创建对象
    public static CourseListFragment getInstance() {
        if (courseListFragment == null) {
            courseListFragment = new CourseListFragment();
        }
        return courseListFragment;
    }

    //加载课程成功后更新界面
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == RESULT) {
                ArrayList<Course> list = (ArrayList<Course>) msg.getData().getSerializable("list");
                adapter = new CourseAdapter(list, getActivity(), imageLoader);
                //图片显示不了，需要此句
                pullToRefreshView.setAdapter(adapter);
                load.setVisibility(View.GONE);
                Toast.makeText(getActivity().getApplicationContext(), "更新成功", Toast.LENGTH_SHORT).show();
            }
        }
    };

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
        PreferenceDao.saveCurrentPage(getActivity().getApplicationContext(), 1);//重置当前页数
        pullToRefreshView.setMode(PullToRefreshBase.Mode.BOTH);//同时可以下拉和上拉刷新
        pullToRefreshView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override //下拉刷新
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                String label = DateUtils.formatDateTime(getActivity().getApplicationContext(), System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
                refreshView.getLoadingLayoutProxy(true, false).setLastUpdatedLabel("更新于:" + label);
                if (NetworkState.isNetworkConnected(getActivity().getApplicationContext())) {//网络可用
                    new GetDataTask().execute();
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "网络不可用", Toast.LENGTH_SHORT).show();
                }
            }

            @Override  //下拉刷新
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                String label = "正在加载...";
                refreshView.getLoadingLayoutProxy(false, true).setPullLabel(label);
                refreshView.getLoadingLayoutProxy(false, true).setReleaseLabel(label);
                refreshView.getLoadingLayoutProxy(false, true).setRefreshingLabel(label);
                if (NetworkState.isNetworkConnected(getActivity().getApplicationContext())) {//网络可用
                    new PullUpTask().execute();
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "网络不可用", Toast.LENGTH_SHORT).show();
                }
            }
        });
        courseDao = new CourseDao(getActivity().getApplicationContext());
        courseTypeDao = new CourseTypeDao(getActivity().getApplicationContext());
        imageLoader = ImageLoader.getInstance();
        List<Course> courseList = courseDao.getAllCourse();
        if (courseList.size() > 0) {//如果本地有缓存,隐藏"提示正在加载课程中"视图
            loadCourseCache(courseList);

        } else {//本地没有缓存，请求网络数据
            if (NetworkState.isNetworkConnected(getActivity().getApplicationContext())) {//网络可用
                new LoadCourseThread().start();
            } else {
                Toast.makeText(getActivity().getApplicationContext(), "网络不可用", Toast.LENGTH_SHORT).show();
                load.setVisibility(View.GONE);
            }
        }
        pullToRefreshView.setOnItemClickListener(this);
        return view;
    }

    //加载本地课程缓存信息
    private void loadCourseCache(List<Course> courseList) {
        Log.i(TAG, "本地有缓存.......");
        load.setVisibility(View.GONE);
        if (!SDCardUtil.checkSDCard()) {
            Toast.makeText(getActivity().getApplicationContext(), "请插入SD卡", Toast.LENGTH_SHORT).show();
        }
        for (Course course : courseList) {//追加图片路径前缀
            course.setImageUri(IMAGE_URI + course.getImageUri());
        }
        GlobalVariable globalVariable = (GlobalVariable) getActivity().getApplication();
        globalVariable.setList(courseList);
        adapter = new CourseAdapter(courseList, getActivity(), imageLoader);
        pullToRefreshView.setAdapter(adapter);
    }

    //ListView点击事件响应
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
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

    /**
     * 加载课程线程
     */
    private class LoadCourseThread extends Thread {
        Message msg = new Message();
        List<Course> list = new ArrayList<Course>();

        @Override
        public void run() {
            try {
                JSONObject jb = null;
                while (jb == null) {//直到获取数据
                    jb = HttpUtil.getDataFromInternet(new URL(PathConstant.PATH_COURSE_LIST), null);
                }
                int currentPage = jb.getInt("currentPage");
                PreferenceDao.saveCurrentPage(getActivity().getApplicationContext(), currentPage);//保存当前页数
                JSONArray ja = jb.getJSONArray("list");
                GlobalVariable globalVariable = (GlobalVariable) getActivity().getApplication();
                for (int i = 0; i < ja.length(); i++) {
                    JSONObject j = ja.getJSONObject(i);
                    Object courseTypeJS = j.get("coursetype");//先视为对象，防止空指针
                    Object userJS = j.get("user");
                    String coursetypeStr;
                    String userStr;
                    if (courseTypeJS.equals(null)) {
                        coursetypeStr = "";
                    } else {
                        coursetypeStr = j.getJSONObject("coursetype").getString("type");
                    }
                    if (userJS.equals(null)) {
                        userStr = "";
                    } else {
                        userStr = j.getJSONObject("user").getString("name");
                    }
                    Course course = new Course(j.getString("code"), j.getString("name"), j.getString("image").equals("null")?"":j.getString("image"), coursetypeStr, userStr);
                    list.add(course);
                    courseDao.save(course);//缓存到数据库
                    Log.i(TAG, course.toString());
                }
                for (Course course : list) {
                    course.setImageUri(PathConstant.PATH_IMAGE + course.getImageUri());
                }
                msg.what = RESULT;
                Bundle bundle = new Bundle();
                bundle.putSerializable("list", (java.io.Serializable) list);
                msg.setData(bundle);
                globalVariable.setList(list);
            } catch (MalformedURLException e) {
                Log.i(TAG, e.toString());
                e.printStackTrace();
            } catch (JSONException e) {
                Log.i(TAG, e.toString());
                e.printStackTrace();
            } finally {
                handler.sendMessage(msg);
            }

        }
    }

/**----------------------------------------- 下拉刷新线程-------------------------------------------------**/
    /**
     * 下拉刷新线程
     */
    private class GetDataTask extends AsyncTask<Void, Void, String[]> {
        List<Course> list = new ArrayList<Course>();//下拉刷新更新数据，最多显示20条，不显示上次上拉刷新更新的数据

        @Override //后台耗时操作
        protected String[] doInBackground(Void... params) {
            try {

                JSONObject jb = HttpUtil.getDataFromInternet(new URL(PathConstant.PATH_COURSE_LIST), null);
                if (jb != null) {
                    int currentPage = jb.getInt("currentPage");
                    PreferenceDao.saveCurrentPage(getActivity().getApplicationContext(), currentPage);//保存当前页数
                    JSONArray ja = jb.getJSONArray("list");
                    GlobalVariable globalVariable = (GlobalVariable) getActivity().getApplication();
                    courseDao.delete();//先清空缓存
                    for (int i = 0; i < ja.length(); i++) {
                        JSONObject j = ja.getJSONObject(i);
                        Object courseTypeJS = j.get("coursetype");//先视为对象，防止空指针
                        Object userJS = j.get("user");
                        String coursetypeStr;
                        String userStr;
                        if (courseTypeJS.equals(null)) {
                            coursetypeStr = "";
                        } else {
                            coursetypeStr = j.getJSONObject("coursetype").getString("type");
                        }
                        if (userJS.equals(null)) {
                            userStr = "";
                        } else {
                            userStr = j.getJSONObject("user").getString("name");
                        }
                        Course course = new Course(j.getString("code"), j.getString("name"), j.getString("image").equals("null")?"":j.getString("image"), coursetypeStr, userStr);
                        list.add(course);
                        courseDao.save(course);//缓存到数据库
                        Log.i(TAG1, course.toString());
                    }
                    globalVariable.setList(list);
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
                adapter = new CourseAdapter(list, getActivity(), imageLoader);
                adapter.notifyDataSetChanged();
                pullToRefreshView.setAdapter(adapter);
                Toast.makeText(getActivity().getApplicationContext(), "更新成功", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity().getApplicationContext(), "没有更新到数据，请检查网络，稍后再试", Toast.LENGTH_LONG).show();
            }
            super.onPostExecute(result);
        }

        /**
         * 更新课程类型，保存到数据库
         */
        public void updateData() {
            try {
                JSONObject jb = HttpUtil.getDataFromInternet(new URL(PathConstant.PATH_COURSE_TYPE_LIST), null);
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


/**---------------------------------------------- 上拉刷新线程-----------------------------------------------**/
    /**
     * 上拉刷新线程
     */
    private class PullUpTask extends AsyncTask<Void, Void, String[]> {

        List<Course> list;
        JSONObject jb;
        JSONArray ja;

        //后台耗时操作
        @Override
        protected String[] doInBackground(Void... params) {
            try {
                int currentPage = PreferenceDao.getCurrentPage(getActivity().getApplicationContext());
                jb = HttpUtil.getDataFromInternet(new URL(PathConstant.PATH_COURSE_LIST + "?page=" + (currentPage + 1)), "GET");
                if (jb != null) {//下拉刷新的数据不缓存到数据库
                    ja = jb.getJSONArray("list");
                    if (ja.length() != 0) {//如果list集合有数据，可能数据查完了，但是currentPage会返回数据
                        int page = jb.getInt("currentPage");
                        PreferenceDao.saveCurrentPage(getActivity().getApplicationContext(), page);//保存当前页数
                    }
                    GlobalVariable globalVariable = (GlobalVariable) getActivity().getApplication();
                    list = globalVariable.getList();
                    Log.d(TAG1 + " list size=", list.size() + "");
                    for (int i = 0; i < ja.length(); i++) {
                        JSONObject j = ja.getJSONObject(i);
                        Object courseTypeJS = j.get("coursetype");//先视为对象，防止空指针
                        Object userJS = j.get("user");
                        String coursetypeStr;
                        String userStr;
                        if (courseTypeJS.equals(null)) {
                            coursetypeStr = "";
                        } else {
                            coursetypeStr = j.getJSONObject("coursetype").getString("type");
                        }
                        if (userJS.equals(null)) {
                            userStr = "";
                        } else {
                            userStr = j.getJSONObject("user").getString("name");
                        }
                        Course course = new Course(j.getString("code"), j.getString("name"), j.getString("image").equals("null")?"":j.getString("image"), coursetypeStr, userStr);
                        list.add(course);
                        Log.i(TAG, course.toString());
                    }
                    globalVariable.setList(list);
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return new String[0];
        }

        @Override //操作UI
        protected void onPostExecute(String[] strings) {
            super.onPostExecute(strings);
            pullToRefreshView.onRefreshComplete();
            if (jb != null && ja.length() != 0) {//获取到数据，更新UI
                load.setVisibility(View.GONE);
                adapter = new CourseAdapter(list, getActivity(), imageLoader);
                adapter.notifyDataSetChanged();
//                pullToRefreshView.setAdapter(adapter);
                Toast.makeText(getActivity().getApplicationContext(), "更新成功", Toast.LENGTH_SHORT).show();
            } else if (jb == null) {
                Toast.makeText(getActivity().getApplicationContext(), "没有更新到数据，请检查网络，稍后再试", Toast.LENGTH_LONG).show();
            } else if (jb != null && ja.length() == 0) {//数据加载完
                Toast.makeText(getActivity().getApplicationContext(), "课程已加载完", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onDestroy() {
        courseDao.close();
        courseTypeDao.close();
        super.onDestroy();
    }

}



