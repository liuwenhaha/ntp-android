package com.ntp.activity.notice;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.ntp.activity.R;
import com.ntp.adapter.NoticeAdapter;
import com.ntp.model.Notice;
import com.ntp.util.NetworkStateUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 作业消息
 * @author yanxing
 */
public class HomeworkNoticeActivity extends Activity{

    private PullToRefreshListView pullToRefreshView;
    private NoticeAdapter noticeAdapter;
    private List<Notice> noticeList;
    private LinearLayout load;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homework_notice);
        noticeList=new ArrayList<Notice>();
        noticeAdapter=new NoticeAdapter(getApplicationContext(),noticeList);
        load= (LinearLayout) findViewById(R.id.load);
        pullToRefreshView = (PullToRefreshListView)findViewById(R.id.pull_to_refresh_listview);
        pullToRefreshView.setMode(PullToRefreshBase.Mode.BOTH);//同时可以下拉和上拉刷新
        pullToRefreshView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override //下拉刷新
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                String label = DateUtils.formatDateTime(getApplicationContext(), System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
                refreshView.getLoadingLayoutProxy(true, false).setLastUpdatedLabel("更新于:" + label);
                if (NetworkStateUtil.isNetworkConnected(getApplicationContext())) {//网络可用
                    new GetDataTask().execute();
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
    }

    /**----------------------------------------- 下拉刷新线程-------------------------------------------------**/
    private class GetDataTask extends AsyncTask<Void, Void, List<Notice>> {


        @Override //后台耗时操作
        protected List<Notice> doInBackground(Void... params) {
            List<Notice> list = new ArrayList<Notice>();//下拉刷新更新数据，最多显示10条，不显示上次上拉刷新更新的数据
            return list;
        }

        @Override //操作UI
        protected void onPostExecute(List<Notice> list) {
            pullToRefreshView.onRefreshComplete();
            super.onPostExecute(list);
        }
    }


    /**---------------------------------------------- 上拉刷新线程-----------------------------------------------**/
    private class PullUpTask extends AsyncTask<Void, Void, List<Notice>> {

        JSONObject jb;
        JSONArray ja;

        //后台耗时操作
        @Override
        protected List<Notice> doInBackground(Void... params) {
            List<Notice> list=null;
            return list;
        }

        @Override //操作UI
        protected void onPostExecute(List<Notice> list) {
            super.onPostExecute(list);
            pullToRefreshView.onRefreshComplete();
        }
    }

}

