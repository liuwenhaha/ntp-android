package com.ntp.activity.course;


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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.ntp.activity.R;
import com.ntp.util.PathConstant;
import com.ntp.dao.PreferenceDao;
import com.ntp.util.HttpUtil;
import com.ntp.util.NetworkStateUtil;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 课程讨论
 */
public class CourseForumFragment extends Fragment implements AdapterView.OnItemClickListener,View.OnClickListener{

    private static PullToRefreshListView pullToRefreshView;
    private static AsyncHttpClient asyncHttpClient=new AsyncHttpClient();

    private static CourseForumFragment mCourseForumFragment;
    private SimpleAdapter adapter;
    private ImageView reply;

    private String code;
    private int currentPage=1;//默认加载第一页问题
    private List<Map<String, String>> list = new ArrayList<Map<String, String>>();
    private static final String TAG="CourseForumFragment";
    private static final int REQUEST =207;
    private static final int REQUEST_COMMENT =208;

    /**
     * 创建对象
     */
    public static CourseForumFragment getInstance(String code) {
        mCourseForumFragment = new CourseForumFragment();
        Bundle bundle = new Bundle();
        bundle.putString("code", code);
        mCourseForumFragment.setArguments(bundle);
        return mCourseForumFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        code=getArguments().getString("code");
        currentPage=1;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_course_forum, container, false);
        reply= (ImageView) view.findViewById(R.id.reply);
        reply.setOnClickListener(this);
        pullToRefreshView = (PullToRefreshListView) view.findViewById(R.id.pull_to_refresh_listview);
        pullToRefreshView.setOnItemClickListener(this);
        adapter = new SimpleAdapter(getActivity().getApplicationContext(), list,
                R.layout.listview_item_courseforum, new String[]{"id","content", "name", "time", "reply"},
                new int[]{R.id.id,R.id.content, R.id.name, R.id.time, R.id.reply});
        pullToRefreshView.setAdapter(adapter);
        loadForumData();
        pullToRefreshView.setMode(PullToRefreshBase.Mode.BOTH);//同时可以下拉和上拉刷新
        pullToRefreshView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override //下拉刷新
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                String label = DateUtils.formatDateTime(getActivity().getApplicationContext(), System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
                refreshView.getLoadingLayoutProxy(true, false).setLastUpdatedLabel("更新于:" + label);
                if (NetworkStateUtil.isNetworkConnected(getActivity().getApplicationContext())) {//网络可用
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
                if (NetworkStateUtil.isNetworkConnected(getActivity().getApplicationContext())) {//网络可用
                    new PullUpTask().execute();
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "网络不可用", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }

    /**
     * 加载课程讨论问题
     */

    private void loadForumData() {
        RequestParams requestParams=new RequestParams();
        requestParams.put("code", code);
        asyncHttpClient.post(PathConstant.PATH_COURSE_FORUM, requestParams, new JsonHttpResponseHandler() {
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (response != null) {
                    try {
                        JSONArray ja = response.getJSONArray("forums");
                        currentPage = response.getInt("currentPage");
                        Log.i(TAG, currentPage + "");
                        for (int i = 0; i < ja.length(); i++) {
                            JSONObject jb = ja.getJSONObject(i);
                            Map<String, String> map = new HashMap<String, String>();
                            map.put("id", jb.getString("id"));
                            map.put("content", jb.getString("content"));
                            map.put("name", jb.get("user").equals(null) ? "" : jb.getJSONObject("user").getString("name"));
                            String time = jb.getString("time");
                            map.put("time", time.substring(0, time.lastIndexOf("T")));
                            map.put("reply", jb.get("replyNumber").equals(null) ? "0人回复" : jb.getString("replyNumber") + "人回复");
                            list.add(map);
                        }
                        adapter.notifyDataSetChanged();//更新数据
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Log.i(TAG, throwable.toString());
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.reply://发帖
                //检查有没有登录
                if(PreferenceDao.getLoadName(getActivity().getApplicationContext()).equals("")){
                    Toast.makeText(getActivity().getApplicationContext(),"你尚未登录，不能评论",Toast.LENGTH_SHORT).show();
                    break;
                }
                Intent intent=new Intent(getActivity().getApplicationContext(),CourseForumCommentActivity.class);
                intent.putExtra("name",PreferenceDao.getLoadName(getActivity().getApplicationContext()));
                intent.putExtra("code",code);
                startActivityForResult(intent,REQUEST_COMMENT);
                break;
        }
    }

    /**
     * ----------------------------------------- 下拉刷新线程-------------------------------------------------*
     */
    private class GetDataTask extends AsyncTask<Void, Void, List<Map<String, String>>> {


        @Override //后台耗时操作
        protected List<Map<String, String>> doInBackground(Void... params) {
            try {
                JSONObject response = HttpUtil.getDataFromInternet(new URL(PathConstant.PATH_COURSE_FORUM + "?code=" + code), "GET");
                if (response != null) {
                    list.clear();//清空数据
                    JSONArray ja = response.getJSONArray("forums");
                    currentPage = response.getInt("currentPage");
                    Log.i(TAG, currentPage + "");
                    for (int i = 0; i < ja.length(); i++) {
                        JSONObject jb = ja.getJSONObject(i);
                        Map<String, String> map = new HashMap<String, String>();
                        map.put("id", jb.getString("id"));
                        map.put("content", jb.getString("content"));
                        map.put("name", jb.get("user").equals(null) ? "" : jb.getJSONObject("user").getString("name"));
                        String time = jb.getString("time");
                        map.put("time", time.substring(0, time.lastIndexOf("T")));
                        map.put("reply", jb.get("replyNumber").equals(null) ? "0人回复" : jb.getString("replyNumber") + "人回复");
                        list.add(map);
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
        protected void onPostExecute(List<Map<String, String>> list) {
            pullToRefreshView.onRefreshComplete();
            if (list.size()!=0){
                adapter.notifyDataSetChanged();//更新数据
                Toast.makeText(getActivity().getApplicationContext(), "更新成功", Toast.LENGTH_SHORT).show();
            }
            super.onPostExecute(list);
        }
    }


    /**
     * ---------------------------------------------- 上拉刷新线程-----------------------------------------------*
     */
    private class PullUpTask extends AsyncTask<Void, Void, List<Map<String, String>>> {

        private boolean loadComplete;

        //后台耗时操作
        @Override
        protected List<Map<String, String>> doInBackground(Void... params) {
            try {
                JSONObject response = HttpUtil.getDataFromInternet(new URL(PathConstant.PATH_COURSE_FORUM + "?code=" + code+"&page="+(currentPage+1)), "GET");
                if (response != null) {
                    JSONArray ja = response.getJSONArray("forums");
                    if(ja.length()!=0){//获取到了数据，则增加页数
                        currentPage = response.getInt("currentPage");
                    }else {
                        loadComplete=true;//分页加载完服务器端数据
                    }
                    Log.i(TAG, currentPage + "");
                    for (int i = 0; i < ja.length(); i++) {
                        JSONObject jb = ja.getJSONObject(i);
                        Map<String, String> map = new HashMap<String, String>();
                        map.put("id", jb.getString("id"));
                        map.put("content", jb.getString("content"));
                        map.put("name", jb.get("user").equals(null) ? "" : jb.getJSONObject("user").getString("name"));
                        String time = jb.getString("time");
                        map.put("time", time.substring(0, time.lastIndexOf("T")));
                        map.put("reply", jb.get("replyNumber").equals(null) ? "0人回复" : jb.getString("replyNumber") + "人回复");
                        list.add(map);//追加数据
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
        protected void onPostExecute(List<Map<String, String>> list) {
            super.onPostExecute(list);
            pullToRefreshView.onRefreshComplete();
            if (loadComplete){
                Toast.makeText(getActivity().getApplicationContext(), "服务器端数据已经加载完", Toast.LENGTH_SHORT).show();
            }else {
                adapter.notifyDataSetChanged();
            }
        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        //提问用户
        String name=((TextView)view.findViewById(R.id.name)).getText().toString();
        //问题id
        String forumId=((TextView)view.findViewById(R.id.id)).getText().toString();
        //内容
        String content=((TextView)view.findViewById(R.id.content)).getText().toString();
        //时间
        String time=((TextView)view.findViewById(R.id.time)).getText().toString();
        //数量
        String reply=((TextView)view.findViewById(R.id.reply)).getText().toString();
        Intent intent=new Intent(getActivity().getApplicationContext(), CourseForumReplyActivity.class);
        intent.putExtra("name",name);
        intent.putExtra("forumId", forumId);
        intent.putExtra("content",content);
        intent.putExtra("time",time);
        intent.putExtra("reply",reply);
        startActivityForResult(intent, REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //针对帖子评论
        if (requestCode==REQUEST&&resultCode==CourseForumReplyActivity.RESULT_OK){
            pullToRefreshView.setRefreshing(true);
        }
        //参加课程讨论，发帖
        else if (requestCode==REQUEST_COMMENT&&resultCode==CourseForumCommentActivity.RESULT_OK){
            pullToRefreshView.setRefreshing();
        }

    }
}
