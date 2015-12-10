package com.ntp.activity.notice;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.ntp.activity.R;
import com.ntp.util.PathConstant;
import com.ntp.dao.PreferenceDao;
import com.ntp.util.HttpUtil;
import com.ntp.util.NetworkStateUtil;

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
 * 回帖消息
 * @author yanxing
 */
public class CommentNoticeActivity extends Activity{

    private PullToRefreshListView pullToRefreshView;
    private SimpleAdapter simpleAdapter;
    private TextView tip;

    private static final String TAG ="CommentNoticeActivity";
    private List<Map<String, String>> list = new ArrayList<Map<String, String>>();
    private String name;
    private int currentPage=1;//默认加载第一页问题

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_notice);
        tip= (TextView) findViewById(R.id.tip);
        pullToRefreshView = (PullToRefreshListView)findViewById(R.id.pull_to_refresh_listview);
        name=PreferenceDao.getLoadName(getApplicationContext());
        simpleAdapter = new SimpleAdapter(getApplicationContext(), list,
                R.layout.listview_item_comment_notice, new String[]{"commentName","time", "forumUsernameRe", "commentContent", "forumUsername","forumContent"},
                new int[]{R.id.commentName,R.id.time, R.id.forumUsernameRe, R.id.commentContent, R.id.forumUsername,R.id.forumContent});
        pullToRefreshView.setMode(PullToRefreshBase.Mode.BOTH);//同时可以下拉和上拉刷新
        pullToRefreshView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override //下拉刷新
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                String label = DateUtils.formatDateTime(getApplicationContext(), System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
                refreshView.getLoadingLayoutProxy(true, false).setLastUpdatedLabel("更新于:" + label);
                if (NetworkStateUtil.isNetworkConnected(getApplicationContext())) {//网络可用
                    if (!name.equals("")){//没有登录
                        new GetDataTask().execute();
                    }else {
                        pullToRefreshView.setVisibility(View.GONE);
                        tip.setVisibility(View.VISIBLE);
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "网络不可用", Toast.LENGTH_SHORT).show();
                }

            }

            @Override  //下拉刷新
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                String label = "正在加载...";
                refreshView.getLoadingLayoutProxy(false, true).setPullLabel(label);
                refreshView.getLoadingLayoutProxy(false, true).setReleaseLabel(label);
                refreshView.getLoadingLayoutProxy(false, true).setRefreshingLabel(label);
                if (NetworkStateUtil.isNetworkConnected(getApplicationContext())) {//网络可用
                    if (!name.equals("")){
                        new PullUpTask().execute();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "网络不可用", Toast.LENGTH_SHORT).show();
                }
            }
        });
        pullToRefreshView.setAdapter(simpleAdapter);
        pullToRefreshView.setRefreshing(true);//自动刷新
    }

    /**----------------------------------------- 下拉刷新线程-------------------------------------------------**/
    private class GetDataTask extends AsyncTask<Void, Void, List<Map<String, String>>> {


        @Override //后台耗时操作
        protected List<Map<String, String>> doInBackground(Void... params) {
            try {
                JSONObject response = HttpUtil.getDataFromInternet(new URL(PathConstant.PATH_FORUM_COMMENT + "?username=" +name), "GET");
                list.clear();
                if (response != null) {
                    Log.d(TAG,response.toString());
                    JSONArray ja = response.getJSONArray("forumUsers");
                    if (ja.length() == 0) {
                        return list;
                    }
                    for (int i = 0; i < ja.length(); i++) {
                        JSONObject j = ja.getJSONObject(i);
                        Map<String,String> map=new HashMap<String, String>();
                        String time="";
                        //解析回复人用户名
                        if (!j.get("user").equals(null)){
                            if (!j.getJSONObject("user").getString("name").equals("null")){
                                map.put("commentName",j.getJSONObject("user").getString("name"));
                            }else {
                                map.put("commentName","");
                            }
                        }

                        //解析时间，并格式化
                        if (!j.getString("time").equals("null")){
                            time=j.getString("time");
                            time=time.substring(0, time.lastIndexOf("T"));
                            map.put("time",time);
                        }else {
                            map.put("time","");
                        }

                        //发帖人
                        map.put("forumUsernameRe","        "+name);
                        String format=name;
                        String blank="";
                        for (int t=0;t<format.length();t++){
                            blank=blank+"  ";
                        }
                        //格式化回复内容
                        map.put("commentContent","        "+blank+":"+(j.getString("content").equals("null")?"":j.getString("content")));

                        //发帖人
                        map.put("forumUsername",name);

                        //解析发帖人发帖内容
                        if (!j.get("forum").equals(null)){
                            if (!j.getJSONObject("forum").getString("content").equals("null")){
                                map.put("forumContent",blank+":"+j.getJSONObject("forum").getString("content"));
                            }else {
                                map.put("forumContent","");
                            }
                        }
                        list.add(map);
                    }//for循环结束
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
            if (list.size()==0){
                Toast.makeText(getApplicationContext(), "你还没有回帖信息", Toast.LENGTH_SHORT).show();
            }else {
                simpleAdapter = new SimpleAdapter(getApplicationContext(), list,
                        R.layout.listview_item_comment_notice, new String[]{"commentName","time", "forumUsernameRe", "commentContent", "forumUsername","forumContent"},
                        new int[]{R.id.commentName,R.id.time, R.id.forumUsernameRe, R.id.commentContent, R.id.forumUsername,R.id.forumContent});
                pullToRefreshView.setAdapter(simpleAdapter);
            }
            super.onPostExecute(list);
        }
    }


    /**---------------------------------------------- 上拉刷新线程-----------------------------------------------**/
    private class PullUpTask extends AsyncTask<Void, Void, List<Map<String, String>>> {

        private boolean loadComplete;

        //后台耗时操作
        @Override
        protected List<Map<String, String>> doInBackground(Void... params) {

            try {
                JSONObject response = HttpUtil.getDataFromInternet(new URL(PathConstant.PATH_FORUM_COMMENT + "?username=" +name+"&page="+(currentPage+1)), "GET");
                if (response != null) {
                    JSONArray ja = response.getJSONArray("forumUsers");
                    if(ja.length()!=0){//获取到了数据，则增加页数
                        currentPage = response.getInt("currentPage");
                    }else {
                        loadComplete=true;//分页加载完服务器端数据
                    }
                    for (int i = 0; i < ja.length(); i++) {
                        JSONObject j = ja.getJSONObject(i);
                        Map<String,String> map=new HashMap<String, String>();
                        String time="";
                        //解析回复人用户名
                        if (!j.get("user").equals(null)){
                            if (!j.getJSONObject("user").getString("name").equals("null")){
                                map.put("commentName",j.getJSONObject("user").getString("name"));
                            }else {
                                map.put("commentName","");
                            }
                        }

                        //解析时间，并格式化
                        if (!j.getString("time").equals("null")){
                            time=j.getString("time");
                            time=time.substring(0, time.lastIndexOf("T"));
                            map.put("time",time);
                        }else {
                            map.put("time","");
                        }

                        //发帖人
                        map.put("forumUsernameRe","        "+name);
                        String format=name;
                        String blank="";
                        for (int t=0;t<format.length();t++){
                            blank=blank+"  ";
                        }
                        //格式化回复内容
                        map.put("commentContent","        "+blank+":"+(j.getString("content").equals("null")?"":j.getString("content")));

                        //发帖人
                        map.put("forumUsername",name);

                        //解析发帖人发帖内容
                        if (!j.get("forum").equals(null)){
                            if (!j.getJSONObject("forum").getString("content").equals("null")){
                                map.put("forumContent",blank+":"+j.getJSONObject("forum").getString("content"));
                            }else {
                                map.put("forumContent","");
                            }
                        }
                        list.add(map);
                    }//for循环结束
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
                Toast.makeText(getApplicationContext(), "已经加载完", Toast.LENGTH_SHORT).show();
            }else {
                simpleAdapter.notifyDataSetChanged();
            }

        }
    }

}

