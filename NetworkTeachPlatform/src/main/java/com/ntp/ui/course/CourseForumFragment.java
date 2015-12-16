package com.ntp.ui.course;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.ntp.adapter.CourseForumAdapter;
import com.ntp.base.BaseFragment;
import com.ntp.model.gson.CourseForumGson;
import com.ntp.network.HttpRequestHelper;
import com.ntp.network.okhttp.CallbackHandler;
import com.ntp.network.okhttp.GsonOkHttpResponse;
import com.ntp.ui.R;
import com.ntp.util.AppConfig;
import com.ntp.util.NetworkStateUtil;
import com.squareup.okhttp.Request;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 课程讨论
 */
@ContentView(R.layout.fragment_course_forum)
public class CourseForumFragment extends BaseFragment implements PullToRefreshBase.OnRefreshListener2<ListView>{

    @ViewInject(R.id.pull_to_refresh_listview)
    private  PullToRefreshListView pullToRefreshView;

    private CourseForumAdapter courseForumAdapter;
    private List<CourseForumGson.ForumsEntity> courseForumGsonList=new ArrayList<CourseForumGson.ForumsEntity>();
    private String code;
    private int currentPage=1;//默认加载第一页问题
    private static final int REQUEST =207;
    private static final int REQUEST_COMMENT =208;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        code=getArguments().getString("code");
        courseForumAdapter=new CourseForumAdapter(courseForumGsonList);
        pullToRefreshView.setAdapter(courseForumAdapter);
        pullToRefreshView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);//下拉刷新
        pullToRefreshView.setOnRefreshListener(this);
        pullToRefreshView.setRefreshing(true);
    }

    //下拉刷新
    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
        loadData(true);

    }

    /**
     * 上拉刷新
     * @param refreshView
     */
    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
        loadData(false);

    }

    /**
     * 加载数据
     * @param pullDownOrUp true 下拉，false上拉
     */
    private void loadData(final boolean pullDownOrUp){
        if (!NetworkStateUtil.isNetworkConnected(getActivity())){
            showToast(NetworkStateUtil.NO_NETWORK);
            return;
        }
        if (!pullDownOrUp){
            currentPage++;
        }else {
            currentPage=1;
        }
        GsonOkHttpResponse gsonOkHttpResponse=new GsonOkHttpResponse(CourseForumGson.class);
        HttpRequestHelper.getInstance().getForumList(currentPage, 10, code,new CallbackHandler<CourseForumGson>(gsonOkHttpResponse) {
            @Override
            public void onFailure(Request request, IOException e, int response) {
                super.onFailure(request, e, response);
                pullToRefreshView.onRefreshComplete();
                showToast("加载失败");
            }

            @Override
            public void onResponse(CourseForumGson courseForumGson) {
                super.onResponse(courseForumGson);
                if (courseForumGson != null) {
                    if (pullDownOrUp) {//下拉刷新
                        currentPage = 1;
                        courseForumAdapter.update(courseForumGson.getForums());
                        pullToRefreshView.setMode(PullToRefreshBase.Mode.BOTH);
                        pullToRefreshView.onRefreshComplete();
                    } else {
                        currentPage = courseForumGson.getCurrentPage();
                        for (CourseForumGson.ForumsEntity entity : courseForumGson.getForums()) {
                            courseForumGsonList.add(entity);
                        }
                        courseForumAdapter.update(courseForumGsonList);
                        pullToRefreshView.onRefreshComplete();
                    }
                }
            }
        });
    }


    @Event(value = R.id.reply)
    private void onClick(View v) {
        switch (v.getId()){
            case R.id.reply://发帖
                //检查有没有登录
                if(AppConfig.getLoadName(getActivity()).equals("")){
                    showToast("你尚未登录，不能评论");
                    break;
                }
                Intent intent=new Intent(getActivity(),CourseForumCommentActivity.class);
                intent.putExtra("name", AppConfig.getLoadName(getActivity()));
                intent.putExtra("code",code);
                startActivityForResult(intent,REQUEST_COMMENT);
                break;
        }
    }


    @Event(value = R.id.pull_to_refresh_listview,type = AdapterView.OnItemClickListener.class)
    private void onItemClick(AdapterView<?> parent, View view, int position, long id) {

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
        Intent intent=new Intent(getActivity(), CourseForumReplyActivity.class);
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
