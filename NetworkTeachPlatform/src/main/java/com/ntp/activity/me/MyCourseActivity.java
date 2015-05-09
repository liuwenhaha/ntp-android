package com.ntp.activity.me;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.ntp.activity.R;
import com.ntp.activity.course.CourseDetailActivity;
import com.ntp.adapter.CourseAdapter;
import com.ntp.activity.PathConstant;
import com.ntp.dao.PreferenceDao;
import com.ntp.model.Course;
import com.ntp.util.HttpUtil;
import com.ntp.util.NetworkStateUtil;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * 我的课程(已选学)
 * @author yanxing
 */
public class MyCourseActivity extends Activity implements AdapterView.OnItemClickListener{

    private LinearLayout load;//提示加载
    private CourseAdapter courseAdapter;
    private TextView tip;

    private AsyncHttpClient client=new AsyncHttpClient();
    private static PullToRefreshListView pullToRefreshView;
    private ImageLoader imageLoader;

    private List<Course> list=new ArrayList<Course>();
    private static final String TAG="MyCourseActivity";
    private String username;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_course);
        pullToRefreshView= (PullToRefreshListView) findViewById(R.id.pull_to_refresh_listview);
        load= (LinearLayout) findViewById(R.id.load);
        tip= (TextView) findViewById(R.id.tip);
        pullToRefreshView.setOnItemClickListener(this);
        pullToRefreshView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                if (NetworkStateUtil.isNetworkConnected(getApplicationContext())) {//网络可用
                    new GetDataTask().execute();
                } else {
                    Toast.makeText(getApplicationContext(), "网络不可用", Toast.LENGTH_SHORT).show();
                }
            }
        });
        imageLoader = ImageLoader.getInstance();
        username= PreferenceDao.getLoadName(getApplicationContext());
        if (!username.equals("")){
            tip.setVisibility(View.GONE);
            loadMyCourse(username);
        }
    }

    /**
     * 加载我选学的课程
     * @param username 用户名
     */
    private void loadMyCourse(String username){
        RequestParams params = new RequestParams();
        params.put("username", username);//键和后台参数接受字段一直
        client.post(PathConstant.PATH_MY_COURSE, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    if (response != null) {
                        JSONArray ja = response.getJSONArray("list");
                        if (ja.length() == 0) {
                            Toast.makeText(getApplicationContext(), "您还没有选学课程", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        for (int i = 0; i < ja.length(); i++) {
                            JSONObject j = ja.getJSONObject(i);
                            Course course = new Course(j.getString("code"), j.getString("name"),j.getString("image").equals("null")?"":j.getString("image"),
                                    j.get("coursetype").equals(null)?"":j.getJSONObject("coursetype").getString("type"), j.get("user").equals(null)?"":j.getJSONObject("user").getString("name"));
                            list.add(course);
                        }
                        for (Course course:list){
                            //有图片加上网址前缀
                            if (!course.getImageUri().equals("")){
                                course.setImageUri(PathConstant.PATH_IMAGE+course.getImageUri());
                            }
                        }
                        courseAdapter = new CourseAdapter(list, getApplicationContext(),imageLoader);
                        load.setVisibility(View.GONE);
                        pullToRefreshView.setAdapter(courseAdapter);
                    } else {//服务器没有开启
                        Toast.makeText(getApplicationContext(), "加载失败,稍后再试", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
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

    /**
     * ----------------------------------------- 下拉刷新线程-------------------------------------------------*
     */
    private class GetDataTask extends AsyncTask<Void, Void, List<Course>> {


        @Override //后台耗时操作
        protected List<Course> doInBackground(Void... params) {
            try {
                JSONObject response = HttpUtil.getDataFromInternet(new URL(PathConstant.PATH_MY_COURSE+"?username=" + username), "GET");
                list.clear();
                if (response != null) {
                    JSONArray ja = response.getJSONArray("list");
                    if (ja.length() == 0) {
                        return list;
                    }
                    for (int i = 0; i < ja.length(); i++) {
                        JSONObject j = ja.getJSONObject(i);
                        Course course = new Course(j.getString("code"), j.getString("name"),j.getString("image").equals("null")?"":j.getString("image"),
                                j.get("coursetype").equals(null)?"":j.getJSONObject("coursetype").getString("type"), j.get("user").equals(null)?"":j.getJSONObject("user").getString("name"));
                        list.add(course);
                    }
                    for (Course course:list){
                        //有图片加上网址前缀
                        if (!course.getImageUri().equals("")){
                            course.setImageUri(PathConstant.PATH_IMAGE+course.getImageUri());
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            return list;
        }

        @Override //操作UI
        protected void onPostExecute(List<Course> list) {
            pullToRefreshView.onRefreshComplete();
            if (list.size()==0){
                Toast.makeText(getApplicationContext(), "您还没有选学课程", Toast.LENGTH_SHORT).show();
            }else {
                courseAdapter = new CourseAdapter(list, getApplicationContext(),imageLoader);
                load.setVisibility(View.GONE);
                pullToRefreshView.setAdapter(courseAdapter);
                Toast.makeText(getApplicationContext(), "更新成功", Toast.LENGTH_SHORT).show();
            }
            super.onPostExecute(list);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
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
