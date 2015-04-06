package com.chzu.ntp.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.TextView;

import com.chzu.ntp.adapter.CoursevideoAdapter;
import com.chzu.ntp.dao.SearchHistoryDao;
import com.chzu.ntp.model.Course;
import com.chzu.ntp.model.Coursevideo;
import com.chzu.ntp.widget.MyProgress;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 课程搜索历史显示，悬浮activity
 */
public class SearchHistoryActivity extends Activity implements CoursevideoAdapter.Callback {
    /**
     * 搜索课程路径
     */
    private static final String PATH = "http://10.0.2.2/ntp/phone/course-search";
    private ListView searchHistoryList;
    private CoursevideoAdapter coursevideoAdapter;//和视频列表同用一个适配器,数据展示结构一样
    private SearchHistoryDao searchHistoryDao;
    private TextView name;
    List<Coursevideo> list;
    private static AsyncHttpClient client = new AsyncHttpClient();
    List<Course> courseList = new ArrayList<Course>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_history);
        searchHistoryList = (ListView) findViewById(R.id.searchHistoryList);
        searchHistoryDao = new SearchHistoryDao(getApplicationContext());
        List<String> strList = searchHistoryDao.findAll();
        list = new ArrayList<Coursevideo>();
        for (int i = 0; i < strList.size(); i++) {
            list.add(new Coursevideo(0 + "", strList.get(i), R.drawable.delete));
        }
        coursevideoAdapter = new CoursevideoAdapter(list, getApplicationContext(), this);
        searchHistoryList.setAdapter(coursevideoAdapter);

    }


    /**
     * 回调接口方法
     */
    @Override
    public void click(View v) {
        switch (v.getId()) {
            case R.id.watch://删除这条搜索历史
                int position = Integer.parseInt(v.getTag().toString());
                Log.i("TAG", position + "");
                String name = list.get(position).getName();
                list.remove(position);
                searchHistoryDao.deleteByName(name);
                coursevideoAdapter = new CoursevideoAdapter(list, getApplicationContext(), this);
                searchHistoryList.setAdapter(coursevideoAdapter);
                coursevideoAdapter.notifyDataSetChanged();
                break;
            case R.id.coursevideoName://根据这条搜索记录进行重新搜索
                Intent intent = new Intent(getApplicationContext(), MyProgress.class);
                startActivity(intent);
                int posi = Integer.parseInt(v.getTag().toString());
                String nameStr = list.get(posi).getName();
                RequestParams params = new RequestParams();
                params.put("name", nameStr);//键和后台参数接受字段一直
                client.post(PATH, params, new JsonHttpResponseHandler() {
                    public void onSuccess(int statusCode, Header[] headers,
                                          JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        try {
                            if (response != null) {
                                JSONArray ja = response.getJSONArray("list");
                                for (int i = 0; i < ja.length(); i++) {
                                    JSONObject j = ja.getJSONObject(i);
                                    Course course = new Course(null, j.getString("code"), j.getString("name"), j.getJSONObject("coursetype").getString("type"), j.getJSONObject("user").getString("name"));
                                    courseList.add(course);
                                }
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("list", (java.io.Serializable) list);
                                setResult(RESULT_OK, new Intent().putExtras(bundle));
                            } else {
                                setResult(RESULT_CANCELED);
                                finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        searchHistoryDao.close();
    }

    /**
     * 点击屏幕其他地方，对话框消失
     */
    public boolean onTouchEvent(MotionEvent event) {
        finish();
        return true;
    }

    /**
     * 重绘使本activity宽度占据全部手机屏幕
     */
    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        //获取手机屏幕宽度
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        //改变activity尺寸
        View view = getWindow().getDecorView();
        WindowManager.LayoutParams lp = (WindowManager.LayoutParams) view.getLayoutParams();
        lp.gravity = Gravity.TOP;
        lp.width = dm.widthPixels;
        getWindowManager().updateViewLayout(view, lp);
    }

}
