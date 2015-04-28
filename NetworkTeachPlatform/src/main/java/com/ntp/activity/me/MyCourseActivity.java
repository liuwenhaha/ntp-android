package com.ntp.activity.me;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.ntp.activity.R;
import com.ntp.activity.course.CourseDetailActivity;
import com.ntp.adapter.CourseAdapter;
import com.ntp.dao.PathConstant;
import com.ntp.dao.PreferenceDao;
import com.ntp.model.Course;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的课程(已选学)
 * @author yanxing
 */
public class MyCourseActivity extends Activity implements AdapterView.OnItemClickListener{

    private ListView myCourse;
    private LinearLayout load;//提示加载
    private CourseAdapter courseAdapter;
    private TextView tip;

    private AsyncHttpClient client=new AsyncHttpClient();
    private ImageLoader imageLoader;

    private List<Course> list=new ArrayList<Course>();
    private static final String TAG="MyCourseActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_course);
        myCourse= (ListView) findViewById(R.id.myCourse);
        load= (LinearLayout) findViewById(R.id.load);
        tip= (TextView) findViewById(R.id.tip);
        myCourse.setOnItemClickListener(this);
        imageLoader = ImageLoader.getInstance();
        String username= PreferenceDao.getLoadName(getApplicationContext());
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
                            Toast.makeText(getApplicationContext(), "没有搜索到相关课程", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        for (int i = 0; i < ja.length(); i++) {
                            JSONObject j = ja.getJSONObject(i);
                            Course course = new Course(j.getString("code"), j.getString("name"),j.getString("image").equals("null")?"":j.getString("image"),j.getJSONObject("coursetype").getString("type"), j.getJSONObject("user").getString("name"));
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
                        myCourse.setAdapter(courseAdapter);
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
