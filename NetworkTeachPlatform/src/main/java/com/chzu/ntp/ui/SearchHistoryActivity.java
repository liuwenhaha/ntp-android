package com.chzu.ntp.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.chzu.ntp.adapter.CourseAdapter;
import com.chzu.ntp.adapter.CoursevideoAdapter;
import com.chzu.ntp.dao.SearchHistoryDao;
import com.chzu.ntp.model.Course;
import com.chzu.ntp.model.Coursevideo;
import com.chzu.ntp.util.HttpUtil;
import com.chzu.ntp.widget.MyProgress;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 课程搜索历史显示，悬浮activity
 */
public class SearchHistoryActivity extends Activity implements CoursevideoAdapter.Callback{
    private ListView searchHistoryList;
    private CoursevideoAdapter coursevideoAdapter;//和视频列表同用一个适配器,数据展示结构一样
    private SearchHistoryDao searchHistoryDao;
    private TextView name;
    List<Coursevideo> list;
    /**
     * 搜索课程路径
     */
    private static  final String PATH="http://10.0.2.2/ntp/phone/course-search";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_history);
        searchHistoryList= (ListView) findViewById(R.id.searchHistoryList);
        searchHistoryDao=new SearchHistoryDao(getApplicationContext());
        List<String> strList=searchHistoryDao.findAll();
        list=new ArrayList<Coursevideo>();
        for (int i=0;i<strList.size();i++){
            list.add(new Coursevideo(0+"",strList.get(i),R.drawable.delete));
        }
        coursevideoAdapter=new CoursevideoAdapter(list,getApplicationContext(),this);
        searchHistoryList.setAdapter(coursevideoAdapter);

    }


    /**
     * 回调接口方法
     */
    @Override
    public void click(View v) {
        switch (v.getId()){
            case R.id.watch://删除这条搜索历史
                int position=Integer.parseInt(v.getTag().toString());
                Log.i("TAG",position+"");
                String name=list.get(position).getName();
                list.remove(position);
                searchHistoryDao.deleteByName(name);
                coursevideoAdapter=new CoursevideoAdapter(list,getApplicationContext(),this);
                coursevideoAdapter.notifyDataSetChanged();
                break;
            case R.id.coursevideoName://根据这条搜索记录进行重新搜索
                Intent intent = new Intent(getApplicationContext(), MyProgress.class);
                startActivity(intent);
                int posi=Integer.parseInt(v.getTag().toString());
                String nameStr=list.get(posi).getName();
                Map<String, String> map = new HashMap<String, String>();
                map.put("name", nameStr);//键和后台参数接受字段一直
                JSONObject jb = HttpUtil.getDataFromInternet(PATH, map);
                List<Course> list = new ArrayList<Course>();
                try {
                    if (jb != null) {
                        JSONArray ja = jb.getJSONArray("list");
                        for (int i = 0; i < ja.length(); i++) {
                            JSONObject j = ja.getJSONObject(i);
                            Course course = new Course(null, j.getString("code"), j.getString("name"), j.getJSONObject("coursetype").getString("type"), j.getJSONObject("user").getString("name"));
                            list.add(course);
                        }
                        Bundle bundle=new Bundle();
                        bundle.putSerializable("list", (java.io.Serializable) list);
                        setResult(RESULT_OK, new Intent().putExtras(bundle));
                        MyProgress.instance.finish();
                    }else {
                        setResult(RESULT_CANCELED);
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        searchHistoryDao.close();
    }

}
