package com.ntp.activity.notice;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.ntp.activity.R;
import com.ntp.adapter.HomeworkNoticeAdapter;
import com.ntp.activity.PathConstant;
import com.ntp.dao.PreferenceDao;
import com.ntp.model.HomeworkNotice;
import com.ntp.util.HttpUtil;
import com.ntp.util.NetworkStateUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * 作业消息
 * @author yanxing
 */
public class HomeworkNoticeActivity extends Activity implements AdapterView.OnItemClickListener{

    private PullToRefreshListView pullToRefreshView;
    private HomeworkNoticeAdapter homeworkNoticeAdapter;
    private TextView tip;

    private static final String TAG ="HomeworkNoticeActivity";
    private List<HomeworkNotice> homeworkNoticeList;
    private String name;
    private int currentPage=1;//默认加载第一页问题

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homework_notice);
        tip= (TextView) findViewById(R.id.tip);
        homeworkNoticeList =new ArrayList<HomeworkNotice>();
        homeworkNoticeAdapter =new HomeworkNoticeAdapter(getApplicationContext(), homeworkNoticeList);
        pullToRefreshView = (PullToRefreshListView)findViewById(R.id.pull_to_refresh_listview);
        name=PreferenceDao.getLoadName(getApplicationContext());
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
                    new PullUpTask().execute();
                } else {
                    Toast.makeText(getApplicationContext(), "网络不可用", Toast.LENGTH_SHORT).show();
                }
            }
        });
        homeworkNoticeAdapter =new HomeworkNoticeAdapter(getApplicationContext(), homeworkNoticeList);
        pullToRefreshView.setOnItemClickListener(this);
        pullToRefreshView.setAdapter(homeworkNoticeAdapter);
        pullToRefreshView.setRefreshing(true);//自动刷新
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String  scoreId= ((TextView)view.findViewById(R.id.id)).getText().toString();
        Intent intent=new Intent(getApplicationContext(), HomeworkDetailActivity.class);
        intent.putExtra("scoreId",scoreId);
        startActivity(intent);
    }

    /**----------------------------------------- 下拉刷新线程-------------------------------------------------**/
    private class GetDataTask extends AsyncTask<Void, Void, List<HomeworkNotice>> {


        @Override //后台耗时操作
        protected List<HomeworkNotice> doInBackground(Void... params) {
            try {
                JSONObject response = HttpUtil.getDataFromInternet(new URL(PathConstant.PATH_MY_HOMEWORK + "?username=" +name), "GET");
                homeworkNoticeList.clear();
                if (response != null) {
                    Log.d(TAG,response.toString());
                    JSONArray ja = response.getJSONArray("scores");
                    if (ja.length() == 0) {
                        return homeworkNoticeList;
                    }
                    for (int i = 0; i < ja.length(); i++) {
                        JSONObject j = ja.getJSONObject(i);
                        String time="",courseName="";
                        if (!j.get("exercise").equals(null)){
                            if (!j.getJSONObject("exercise").getString("time").equals("null")){
                                time=j.getJSONObject("exercise").getString("time");
                                time=time.substring(0, time.lastIndexOf("T"));
                            }
                        }
                        if (!j.get("exercise").equals(null)){
                            courseName=j.getJSONObject("exercise").get("course").equals(null)?"":j.getJSONObject("exercise").getJSONObject("course").getString("name");
                        }
                        HomeworkNotice homeworkNotice = new HomeworkNotice(j.getString("id"), j.get("exercise").equals(null)?"":j.getJSONObject("exercise").getString("name"),
                                courseName,time);
                        homeworkNoticeList.add(homeworkNotice);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            return homeworkNoticeList;
        }

        @Override //操作UI
        protected void onPostExecute(List<HomeworkNotice> list) {
            pullToRefreshView.onRefreshComplete();
            if (list.size()==0){
                Toast.makeText(getApplicationContext(), "您还没有作业", Toast.LENGTH_SHORT).show();
            }else {
                homeworkNoticeAdapter = new HomeworkNoticeAdapter(getApplicationContext(),list);
                pullToRefreshView.setAdapter(homeworkNoticeAdapter);
            }
            super.onPostExecute(list);
        }
    }


    /**---------------------------------------------- 上拉刷新线程-----------------------------------------------**/
    private class PullUpTask extends AsyncTask<Void, Void, List<HomeworkNotice>> {

        private boolean loadComplete;

        //后台耗时操作
        @Override
        protected List<HomeworkNotice> doInBackground(Void... params) {

            try {
                JSONObject response = HttpUtil.getDataFromInternet(new URL(PathConstant.PATH_MY_HOMEWORK + "?username=" +name+"&page="+(currentPage+1)), "GET");
                if (response != null) {
                    JSONArray ja = response.getJSONArray("scores");
                    if(ja.length()!=0){//获取到了数据，则增加页数
                        currentPage = response.getInt("currentPage");
                    }else {
                        loadComplete=true;//分页加载完服务器端数据
                    }
                    for (int i = 0; i < ja.length(); i++) {
                        JSONObject j = ja.getJSONObject(i);
                        String time="",courseName="";
                        if (!j.get("exercise").equals(null)){
                            if (!j.getJSONObject("exercise").getString("time").equals("null")){
                                time=j.getJSONObject("exercise").getString("time");
                                time=time.substring(0, time.lastIndexOf("T"));
                            }
                        }
                        if (!j.get("exercise").equals(null)){
                            courseName=j.getJSONObject("exercise").get("course").equals(null)?"":j.getJSONObject("exercise").getJSONObject("course").getString("name");
                        }
                        HomeworkNotice homeworkNotice = new HomeworkNotice(j.getString("id"), j.get("exercise").equals(null)?"":j.getJSONObject("exercise").getString("name"),
                                courseName,time);
                        homeworkNoticeList.add(homeworkNotice);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            return homeworkNoticeList;
        }

        @Override //操作UI
        protected void onPostExecute(List<HomeworkNotice> list) {
            super.onPostExecute(list);
            pullToRefreshView.onRefreshComplete();
            if (loadComplete){
                Toast.makeText(getApplicationContext(), "服务器端数据已经加载完", Toast.LENGTH_SHORT).show();
            }else {
                homeworkNoticeAdapter.notifyDataSetChanged();
            }

        }
    }

}

