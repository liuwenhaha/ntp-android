package com.chzu.ntp.ui;

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

import com.chzu.ntp.adapter.CourseAdapter;
import com.chzu.ntp.dao.SearchHistoryDao;
import com.chzu.ntp.model.Course;
import com.chzu.ntp.util.HttpUtil;
import com.chzu.ntp.util.NetworkState;
import com.chzu.ntp.widget.MyProgress;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 搜索课程
 */
public class SearchCourseActivity extends Activity implements View.OnClickListener,View.OnKeyListener,AdapterView.OnItemClickListener {

    /**
     * 搜索课程路径
     */
//    private static  final String PATH="http://10.0.2.2/ntp/phone/course-search";
    private static final String PATH = "http://192.168.1.105/ntp/phone/course-search";
    private ImageView back;//返回
    private EditText search;
    private SearchHistoryDao searchHistoryDao;
    private CourseAdapter courseAdapter;
    private ListView listView;
    private TextView tip;
    List<Course> list;
    private static final int REQUEST=1;//请求码
    private static final int REQUEST_PROGRESS=2;//请求码
    private static AsyncHttpClient client = new AsyncHttpClient();
    private static final String TAG = "SearchCourseActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_course);
        back = (ImageView) findViewById(R.id.back);
        search = (EditText) findViewById(R.id.search);
        back.setOnClickListener(this);
        search.setOnKeyListener(this);
        listView= (ListView) findViewById(R.id.history);
        tip= (TextView) findViewById(R.id.no_search_tip);
        list = new ArrayList<Course>();
        searchHistoryDao=new SearchHistoryDao(getApplicationContext());
        courseAdapter=new CourseAdapter(list,getApplicationContext());
        listView.setAdapter(courseAdapter);


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
            if (!NetworkState.isNetworkConnected(getApplicationContext())) {
                Toast.makeText(getApplicationContext(), "当前网络不可用", Toast.LENGTH_SHORT).show();
                return false;
            }
            Intent intent = new Intent(getApplicationContext(), MyProgress.class);
            startActivityForResult(intent, REQUEST_PROGRESS);
            Map<String, String> map = new HashMap<String, String>();
            RequestParams params = new RequestParams();
            params.put("name", search.getText().toString());//键和后台参数接受字段一直
            client.post(PATH, params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers,
                                      JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    try {
                        if (response != null) {
                            list.clear();
                            JSONArray ja = response.getJSONArray("list");
                            for (int i = 0; i < ja.length(); i++) {
                                JSONObject j = ja.getJSONObject(i);
                                Course course = new Course(null, j.getString("code"), j.getString("name"), j.getJSONObject("coursetype").getString("type"), j.getJSONObject("user").getString("name"));
                                list.add(course);
                            }
                            searchHistoryDao.save(search.getText().toString());//保存搜索历史
                            courseAdapter = new CourseAdapter(list, getApplicationContext());
                            listView.setVisibility(View.VISIBLE);//设置listView可见
                            tip.setVisibility(View.GONE);
                            courseAdapter.notifyDataSetChanged();
                            finishActivity(REQUEST_PROGRESS);
                        } else {
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
        if (requestCode==REQUEST){
            if (resultCode==RESULT_OK){//如果用户利用搜索历史搜索课程，如果搜索成功
                list= (List<Course>) data.getExtras().getSerializable("list");
                courseAdapter = new CourseAdapter(list, getApplicationContext());
                listView.setAdapter(courseAdapter);
            }else if(resultCode==RESULT_CANCELED){
                Toast.makeText(getApplicationContext(),"没有搜索到相关课程",Toast.LENGTH_SHORT).show();
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
