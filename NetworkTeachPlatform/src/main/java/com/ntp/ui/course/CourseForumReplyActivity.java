package com.ntp.ui.course;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.ntp.ui.R;
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
 * 帖子详情界面，可以评论
 * @author yanxing
 */
public class CourseForumReplyActivity extends Activity {

    private TextView forum,name,time,comment,replyContent;
    private SimpleAdapter adapter;

    private AsyncHttpClient asyncHttpClient=new AsyncHttpClient();
    private static PullToRefreshListView pullToRefreshView;

    private static final String TAG="CourseForumReplyActivit";
    private List<Map<String,String>> list=new ArrayList<Map<String, String>>();
    private String replyNumber;
    private int currentPage=1;
    private int forumId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_course_forum_reply);
        Intent intent=getIntent();
        forum= (TextView) findViewById(R.id.forum);
        name= (TextView) findViewById(R.id.name);
        time= (TextView) findViewById(R.id.time);
        comment= (TextView) findViewById(R.id.comment);
        replyContent= (TextView) findViewById(R.id.replyContent);
        forum.setText(intent.getStringExtra("content"));
        name.setText(intent.getStringExtra("name")+" ");
        time.setText(intent.getStringExtra("time"));
        forumId=Integer.parseInt(intent.getStringExtra("forumId"));
        replyNumber=intent.getStringExtra("reply");
        comment.setText("评论 "+replyNumber.substring(0, replyNumber.lastIndexOf("人")));
        pullToRefreshView= (PullToRefreshListView) findViewById(R.id.pull_to_refresh_listview);
        pullToRefreshView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);//只开启上拉刷新
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
        loadForumReply(forumId);
        adapter = new SimpleAdapter(getApplicationContext(), list,
                R.layout.listview_item_courseforum_reply, new String[]{"content", "name", "time"},
                new int[]{R.id.content, R.id.username, R.id.time});
        pullToRefreshView.setAdapter(adapter);

    }

    /**
     * 获取回复
     * @param forumId
     */
    private void loadForumReply(int forumId) {
        RequestParams params=new RequestParams();
        params.put("forumId",forumId);//帖子id
        asyncHttpClient.post(PathConstant.PATH_COURSE_FORUM_ALL,params,new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (response != null) {
                    try {
                        JSONArray ja = response.getJSONArray("forumUsers");
                        currentPage = response.getInt("currentPage");
                        Log.i(TAG, currentPage + "");
                        for (int i = 0; i < ja.length(); i++) {
                            JSONObject jb = ja.getJSONObject(i);
                            Map<String, String> map = new HashMap<String,String>();
                            map.put("content", (i+1)+"#  "+jb.getString("content"));
                            map.put("name", jb.get("user").equals(null) ? "" : jb.getJSONObject("user").getString("name"));
                            String time = jb.getString("time");
                            map.put("time", time.substring(0, time.lastIndexOf("T")));
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

    /**
     * 刷新评论
     * @param view
     */
    public void refreshReply(View view) {
        currentPage=0;
        list.clear();
        pullToRefreshView.setRefreshing(true);
    }

    /**
     * ----------------------------------------- 上拉刷新线程-------------------------------------------------*
     */
    private class GetDataTask extends AsyncTask<Void, Void, List<Map<String,String>>> {

        private boolean loadComplete;

        @Override //后台耗时操作
        protected List<Map<String,String>> doInBackground(Void... params) {

                try {
                    JSONObject response = HttpUtil.getDataFromInternet(new URL(PathConstant.PATH_COURSE_FORUM_ALL + "?forumId=" + forumId+"&page="+(currentPage+1)), "GET");
                    if (response != null) {
                        JSONArray ja = response.getJSONArray("forumUsers");
                        if(ja.length()!=0){//获取到了数据，则增加页数
                            currentPage = response.getInt("currentPage");
                        }else {
                            loadComplete=true;//分页加载完服务器端数据
                        }
                        Log.i(TAG, currentPage + "");
                        for (int i = 0; i < ja.length(); i++) {
                            JSONObject jb = ja.getJSONObject(i);
                            Map<String, String> map = new HashMap<String, String>();
                            map.put("content", (list.size() + 1) + "#  " + jb.getString("content"));
                            map.put("name", jb.get("user").equals(null) ? "" : jb.getJSONObject("user").getString("name"));
                            String time = jb.getString("time");
                            map.put("time", time.substring(0, time.lastIndexOf("T")));
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
        protected void onPostExecute(List<Map<String,String>> list) {
            pullToRefreshView.onRefreshComplete();
            if (loadComplete){
                Toast.makeText(getApplicationContext(), "服务器端数据已经加载完", Toast.LENGTH_SHORT).show();
            }else {
                adapter.notifyDataSetChanged();
            }
            super.onPostExecute(list);
        }
    }


    /**
     * 评论
     */
    public void reply(View view) {
        //检查有没有登录
        if(PreferenceDao.getLoadName(getApplicationContext()).equals("")){
            Toast.makeText(getApplicationContext(),"你尚未登录，不能评论",Toast.LENGTH_SHORT).show();
            return;
        }
        String commentContent=replyContent.getText().toString();
        if (commentContent.trim().equals("")){
            Toast.makeText(getApplicationContext(),"不能为空",Toast.LENGTH_SHORT).show();
        }else{
            RequestParams params=new RequestParams();
            params.put("forumId",forumId);//帖子
            params.put("name",PreferenceDao.getLoadName(getApplicationContext()));//回帖人用户名
            params.put("comment",commentContent);//评论内容
            asyncHttpClient.post(PathConstant.PATH_COURSE_FORUM_REPLY,params,new JsonHttpResponseHandler(){
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    if (response!=null){
                        try {
                            String result = response.getString("result");
                            //评论成功
                            if (result.equals("success")){
                                currentPage=0;//获取第一页的内容
                                list.clear();
                                pullToRefreshView.setRefreshing(true);
                                replyContent.setText("");//清空评论
                                //更改评论数
                                String replyNum=replyNumber.substring(0, replyNumber.lastIndexOf("人"));
                                comment.setText("评论 " + (Integer.parseInt(replyNum) + 1));
                                setResult(RESULT_OK);
                                Toast.makeText(getApplicationContext(),"评论成功",Toast.LENGTH_SHORT).show();

                            }else{
                                Toast.makeText(getApplicationContext(),"服务器异常，请稍后再试",Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });


        }

    }

}
