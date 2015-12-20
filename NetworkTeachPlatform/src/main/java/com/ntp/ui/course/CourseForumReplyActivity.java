package com.ntp.ui.course;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.ntp.base.BaseActivity;
import com.ntp.model.gson.ForumReplyGson;
import com.ntp.network.HttpRequestHelper;
import com.ntp.network.okhttp.CallbackHandler;
import com.ntp.network.okhttp.GsonOkHttpResponse;
import com.ntp.ui.R;
import com.ntp.util.AppConfig;
import com.ntp.util.AppUtil;
import com.ntp.util.NetworkStateUtil;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 帖子详情界面，可以评论
 * @author yanxing
 */
@ContentView(R.layout.activity_course_forum_reply)
public class CourseForumReplyActivity extends BaseActivity implements PullToRefreshBase.OnRefreshListener2 {

    private TextView forum;//问题内容
    private TextView name;//提问人
    private TextView time;
    private TextView commentReplyNumber;//评论数量

    @ViewInject(R.id.replyContent)
    private EditText replyContent;

    @ViewInject(R.id.pull_to_refresh_listview)
    private PullToRefreshListView pullToRefreshView;

    private SimpleAdapter adapter;

    private List<Map<String,String>> list=new ArrayList<Map<String, String>>();
    private String replyNumber;
    private int currentPage=1;
    private int forumId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view=LayoutInflater.from(this).inflate(R.layout.activity_course_forum_reply_head,null);
        forum= (TextView) view.findViewById(R.id.forum);
        name= (TextView) view.findViewById(R.id.name);
        time= (TextView) view.findViewById(R.id.time);
        commentReplyNumber= (TextView) view.findViewById(R.id.comment__reply_number);
        pullToRefreshView.getRefreshableView().addHeaderView(view);
        pullToRefreshView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        pullToRefreshView.setOnRefreshListener(this);
        AppUtil.setStatusBarDarkMode(true, this);
        setUI(getIntent());
        adapter = new SimpleAdapter(getApplicationContext(), list,
                R.layout.listview_item_courseforum_reply, new String[]{"content", "name", "time"},
                new int[]{R.id.content, R.id.username, R.id.time});
        pullToRefreshView.setAdapter(adapter);
        pullToRefreshView.setRefreshing(true);
    }

    /**
     * 设置UI数据
     * @param intent
     */
    private void setUI(Intent intent) {
        forum.setText(intent.getStringExtra("content"));
        name.setText(intent.getStringExtra("name") + " ");
        time.setText(intent.getStringExtra("time"));
        forumId=Integer.parseInt(intent.getStringExtra("forumId"));
        replyNumber=intent.getStringExtra("reply");
        commentReplyNumber.setText("评论 " + replyNumber.substring(0, replyNumber.lastIndexOf("人")));
    }

    @Event(value = R.id.reply)
    private void onClick(View view){
        switch (view.getId()){
            case R.id.reply:
                reply();
                break;
        }
    }

    /**
     * 回复
     */
    private void reply() {
        //检查有没有登录
        if(AppConfig.getLoadName(getApplicationContext()).equals("")){
            showToast("你尚未登录，不能评论");
            return;
        }
        String commentContent=replyContent.getText().toString();
        if (commentContent.trim().equals("")){
            showToast("不能为空");
        }else{
            HttpRequestHelper.getInstance().reply(AppConfig.getLoadName(this), commentContent, String.valueOf(forumId), new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            showToast("评论失败");
                        }
                    });
                }

                @Override
                public void onResponse(Response response) throws IOException {
                    String content=response.body().string();
                    try {
                        String str=new JSONObject(content).getString("result");
                        if (str.equals("success")){
                            loadData(true);
                        }else {
                            new Handler().post(new Runnable() {
                                @Override
                                public void run() {
                                    showToast("评论失败");
                                }
                            });
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    //下拉刷新
    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        loadData(true);
    }

    //上拉刷新
    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        loadData(false);
    }

    /**
     * 加载数据
     * @param pullDownOrUp true 下拉，false上拉
     */
    private void loadData(final boolean pullDownOrUp){
        if (!NetworkStateUtil.isNetworkConnected(this)){
            showToast(NetworkStateUtil.NO_NETWORK);
            return;
        }
        if (!pullDownOrUp){
            currentPage++;
        }else {
            currentPage=1;
        }
        GsonOkHttpResponse gsonOkHttpResponse=new GsonOkHttpResponse(ForumReplyGson.class);
        HttpRequestHelper.getInstance().getForumReplyList(currentPage, 10, String.valueOf(forumId), new CallbackHandler<ForumReplyGson>(gsonOkHttpResponse) {
            @Override
            public void onFailure(Request request, IOException e, int response) {
                super.onFailure(request, e, response);
                pullToRefreshView.onRefreshComplete();
                showToast("加载失败");
            }

            @Override
            public void onResponse(ForumReplyGson forumReplyGson) {
                super.onResponse(forumReplyGson);
                if (forumReplyGson != null) {
                    if (pullDownOrUp) {//下拉刷新
                        list.clear();
                        currentPage = 1;
                        updateUI(forumReplyGson);
                    } else {
                        if (forumReplyGson.getForumUsers().size()==0){
                            showToast("已经翻到最低了");
                            pullToRefreshView.onRefreshComplete();
                            return;
                        }
                        currentPage = forumReplyGson.getCurrentPage();
                        updateUI(forumReplyGson);
                    }
                }
            }

            /**
             * 更新UI
             */
            private void updateUI(ForumReplyGson forumReplyGson) {
                for (ForumReplyGson.ForumUsersEntity entity:forumReplyGson.getForumUsers()){
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("content", (list.size() + 1) + "#  " + entity.getContent());
                    map.put("name", entity.getUser().getName());
                    String time =entity.getTime();
                    map.put("time", time.substring(0, time.lastIndexOf("T")));
                    list.add(map);
                }
                setUI(getIntent());
                pullToRefreshView.setMode(PullToRefreshBase.Mode.BOTH);
                pullToRefreshView.onRefreshComplete();
                adapter.notifyDataSetChanged();
            }
        });
    }
}
