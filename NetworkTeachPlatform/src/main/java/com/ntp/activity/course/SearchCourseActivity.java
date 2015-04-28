package com.ntp.activity.course;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ntp.activity.R;
import com.ntp.adapter.CourseAdapter;
import com.ntp.dao.PathConstant;
import com.ntp.dao.SearchHistoryDao;
import com.ntp.model.Course;
import com.ntp.util.NetworkStateUtil;
import com.ntp.widget.MyProgress;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 搜索课程
 */
public class SearchCourseActivity extends Activity implements View.OnClickListener, View.OnKeyListener, AdapterView.OnItemClickListener {

    private ImageView back;//返回
    private EditText search;
    private SearchHistoryDao searchHistoryDao;
    private CourseAdapter courseAdapter;
    private ListView listView;
    private TextView tip;
    List<Course> list;
    private static final int REQUEST = 1;//请求码
    private static final int REQUEST_PROGRESS = 2;//请求码
    private static AsyncHttpClient client = new AsyncHttpClient();
    private ImageLoader imageLoader;//Universal Image Loader加载图片类
    private static final String TAG = "SearchCourseActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_course);
        back = (ImageView) findViewById(R.id.back);
        search = (EditText) findViewById(R.id.search);
        back.setOnClickListener(this);
        search.setOnKeyListener(this);
        listView = (ListView) findViewById(R.id.history);
        tip = (TextView) findViewById(R.id.no_search_tip);
        imageLoader = ImageLoader.getInstance();
        list = new ArrayList<Course>();
        searchHistoryDao = new SearchHistoryDao(getApplicationContext());
        courseAdapter = new CourseAdapter(list, getApplicationContext(),imageLoader);
        listView.setAdapter(courseAdapter);
        listView.setOnItemClickListener(this);
//        Intent intent=new Intent(getApplicationContext(),SearchHistoryActivity.class);
//        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back://返回
                finish();
                break;
        }
    }


    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER) {
            if (search.getText().toString().equals("")) {//没有输入
                Toast.makeText(getApplicationContext(), "请输入课程名称", Toast.LENGTH_SHORT).show();
                return false;
            }
            if (!NetworkStateUtil.isNetworkConnected(getApplicationContext())) {
                Toast.makeText(getApplicationContext(), "当前网络不可用", Toast.LENGTH_SHORT).show();
                return false;
            }
            Intent intent = new Intent(getApplicationContext(), MyProgress.class);
            startActivityForResult(intent, REQUEST_PROGRESS);
            RequestParams params = new RequestParams();
            params.put("name", search.getText().toString());//键和后台参数接受字段一直
            client.post(PathConstant.PATH_COURSE_SEARCH, params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers,
                                      JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    try {
                        if (response != null) {
                            list.clear();
                            JSONArray ja = response.getJSONArray("list");
                            if (ja.length() == 0) {
                                Toast.makeText(getApplicationContext(), "没有搜索到相关课程", Toast.LENGTH_SHORT).show();
                                finishActivity(REQUEST_PROGRESS);
                                return;
                            }
                            for (int i = 0; i < ja.length(); i++) {
                                JSONObject j = ja.getJSONObject(i);
                                Course course = new Course(j.getString("code"), j.getString("name"),j.getString("image").equals("null")?"":j.getString("image"),j.getJSONObject("coursetype").getString("type"), j.getJSONObject("user").getString("name"));
                                list.add(course);
                            }
                            searchHistoryDao.save(search.getText().toString());//保存搜索历史
                            for (Course course:list){
                                //有图片加上网址前缀
                                if (!course.getImageUri().equals("")){
                                    course.setImageUri(PathConstant.PATH_IMAGE+course.getImageUri());
                                }
                            }
                            courseAdapter = new CourseAdapter(list, getApplicationContext(),imageLoader);
                            listView.setVisibility(View.VISIBLE);//设置listView可见
                            tip.setVisibility(View.GONE);
                            courseAdapter.notifyDataSetChanged();
                            finishActivity(REQUEST_PROGRESS);
                        } else {//服务器没有开启
                            Toast.makeText(getApplicationContext(), "没有搜索到相关课程", Toast.LENGTH_SHORT).show();
                            finishActivity(REQUEST_PROGRESS);
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
        return false;
    }

    //启动的子activity结果处理
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST) {
            if (resultCode == RESULT_OK) {//如果用户利用搜索历史搜索课程，如果搜索成功
                list = (List<Course>) data.getExtras().getSerializable("list");
                courseAdapter = new CourseAdapter(list, getApplicationContext(),imageLoader);
                listView.setAdapter(courseAdapter);
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(getApplicationContext(), "没有搜索到相关课程", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        searchHistoryDao.close();
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
