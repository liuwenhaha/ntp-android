package com.chzu.ntp.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.chzu.ntp.adapter.CourseAdapter;
import com.chzu.ntp.dao.CourseDao;
import com.chzu.ntp.dao.CourseTypeDao;
import com.chzu.ntp.model.Course;
import com.chzu.ntp.util.BitmapZoomHttp;
import com.chzu.ntp.util.HttpUtil;
import com.chzu.ntp.util.ImageNameGenerator;
import com.chzu.ntp.util.NetworkState;
import com.chzu.ntp.util.PreferenceUtil;
import com.chzu.ntp.util.SDCardUtil;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.FileNameGenerator;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.utils.StorageUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * 课程界面,供学生浏览课程
 */
public class CourseListFragment extends Fragment implements AdapterView.OnItemClickListener {

    /**
     * 请求课程网络地址
     */
    /*public static final String PATH = "http://10.0.2.2/ntp/phone/courseList";*/
    public static final String PATH = "http://192.168.1.112/ntp/phone/courseList";
    public static final String TAG = "json";
    /**
     * 发送消息成功标识
     */
    private static final int RESULT = 1;
    /**
     * Android-PullToRefresh中的ListView控件，具有下拉刷新、上拉刷新特征
     */
    PullToRefreshListView pullToRefreshView;
    /**
     * Universal Image Loader加载图片类
     */
    ImageLoader imageLoader;
    private static CourseListFragment courseListFragment;
    private static CourseAdapter adapter;//课程适配器
    private CourseDao courseDao;
    private CourseTypeDao courseTypeDao;
    private LinearLayout load;
    List<Course> list = new ArrayList<Course>();//全局list对象，方便listView分页显示课程

    /**
     * 创建单例对象
     */
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
                adapter = new CourseAdapter(list, getActivity());
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
                    new pullUpTask().execute();
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
            loadImage(courseList);

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
    private void loadImage(final List<Course> courseList) {
        Log.i(TAG, "本地有缓存。。。");
        load.setVisibility(View.GONE);
        if (!SDCardUtil.checkSDCard()) {
            Toast.makeText(getActivity().getApplicationContext(), "请插入SD卡", Toast.LENGTH_SHORT).show();
        }

        String imageUri = "file:///mnt/sdcard/ntp/1.png";//缓存图片路径
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getActivity().getApplicationContext())
                .build();
        imageLoader.init(config);
        //显示图片的配置
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheOnDisk(true)
                .showImageOnFail(R.drawable.course_default)//不存在默认显示图片
                .build();
        if (SDCardUtil.isExistSDFile("ntp/1.png")) {//如果文件存在
            Bitmap bitmap = imageLoader.loadImageSync(imageUri, options);
            for (int i = 0; i < courseList.size(); i++) {
                courseList.get(i).setBitmap(BitmapZoomHttp.createBitmapZoop(bitmap, 120, 76));
            }
        }
        adapter = new CourseAdapter(courseList, getActivity());
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
                    jb = HttpUtil.getDataFromInternet(new URL(PATH), "POST");
                }
                int currentPage = jb.getInt("currentPage");
                PreferenceUtil.saveCurrentPage(getActivity().getApplicationContext(), currentPage);//保存当前页数
                JSONArray ja = jb.getJSONArray("list");
                for (int i = 0; i < ja.length(); i++) {
                    JSONObject j = ja.getJSONObject(i);
                    Course course = new Course(null, j.getString("code"), j.getString("name"), j.getJSONObject("coursetype").getString("type"), j.getJSONObject("user").getString("name"));
                    list.add(course);
                    courseDao.save(course);//缓存到数据库
                    Log.i(TAG, course.toString());
                }
                loadImage(list);
                msg.what = RESULT;
                Bundle bundle = new Bundle();
                bundle.putSerializable("list", (java.io.Serializable) list);
                msg.setData(bundle);
            } catch (MalformedURLException e) {
                Log.i(TAG, e.toString());
                e.printStackTrace();
            } catch (JSONException e) {
                Log.i(TAG, e.toString());
                e.printStackTrace();
            }
            handler.sendMessage(msg);
        }

        /**
         * 加载课程图片
         */
        private void loadImage(List<Course> list) {
            //模拟图片
            String imageUri = "http://h.hiphotos.baidu.com/image/w%3D230/sign=1ea5b9ff34d3d539c13d08c00a87e927/2e2eb9389b504fc2022d2904e7dde71190ef6d45.jpg";
            File file = SDCardUtil.creatSDDir("ntp");
            ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getActivity().getApplicationContext())
                    .diskCache(new UnlimitedDiscCache(file, null, new ImageNameGenerator("1.png"))) // 缓存到SD卡
                    .build();
            imageLoader.init(config);
            //显示图片的配置
            DisplayImageOptions options = new DisplayImageOptions.Builder()
                    .cacheOnDisk(true)
                    .build();
            Bitmap bitmap = imageLoader.loadImageSync(imageUri, options);
            for (int i = 0; i < list.size(); i++) {
                list.get(i).setBitmap(BitmapZoomHttp.createBitmapZoop(bitmap, 120, 76));
            }
        }
    }

    /**
     * 下拉刷新线程
     */
    private class GetDataTask extends AsyncTask<Void, Void, String[]> {

        @Override //后台耗时操作
        protected String[] doInBackground(Void... params) {
            try {

                JSONObject jb = HttpUtil.getDataFromInternet(new URL(PATH), "POST");
                if (jb != null) {
                    int currentPage = jb.getInt("currentPage");
                    PreferenceUtil.saveCurrentPage(getActivity().getApplicationContext(), currentPage);//保存当前页数
                    JSONArray ja = jb.getJSONArray("list");
                    courseDao.delete();//先清空缓存
                    for (int i = 0; i < ja.length(); i++) {
                        JSONObject j = ja.getJSONObject(i);
                        Course course = new Course(j.getString("code"), j.getString("name"), j.getJSONObject("coursetype").getString("type"), j.getJSONObject("user").getString("name"));
                        list.add(course);
                        courseDao.save(course);//缓存到数据库
                        Log.i(TAG, course.toString());
                    }
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
                adapter = new CourseAdapter(list, getActivity());
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
                JSONObject jb = HttpUtil.getDataFromInternet(new URL(CoursetypeSelectActivity.PATH), "POST");
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
     * 上拉刷新线程
     */
    private class pullUpTask extends AsyncTask<Void, Void, String[]> {

        //后台耗时操作
        @Override
        protected String[] doInBackground(Void... params) {
            try {
                int currentPage = PreferenceUtil.getCurrentPage(getActivity().getApplicationContext());
                JSONObject jb = HttpUtil.getDataFromInternet(new URL(PATH + "?page=" + currentPage), "POST");
                if (jb != null) {
                    int page = jb.getInt("currentPage");
                    PreferenceUtil.saveCurrentPage(getActivity().getApplicationContext(), page);//保存当前页数
                    JSONArray ja = jb.getJSONArray("list");
                    courseDao.delete();//先清空缓存
                    for (int i = 0; i < ja.length(); i++) {
                        JSONObject j = ja.getJSONObject(i);
                        Course course = new Course(j.getString("code"), j.getString("name"), j.getJSONObject("coursetype").getString("type"), j.getJSONObject("user").getString("name"));
                        list.add(course);
                        courseDao.save(course);//缓存到数据库
                        Log.i(TAG, course.toString());
                    }
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
            if (list.size() > 0) {//获取到数据，更新UI
                load.setVisibility(View.GONE);
                adapter = new CourseAdapter(list, getActivity());
                pullToRefreshView.setAdapter(adapter);
                Toast.makeText(getActivity().getApplicationContext(), "更新成功", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity().getApplicationContext(), "没有更新到数据，请检查网络，稍后再试", Toast.LENGTH_LONG).show();
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



