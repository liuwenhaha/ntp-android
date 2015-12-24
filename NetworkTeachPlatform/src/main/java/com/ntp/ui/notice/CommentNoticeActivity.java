package com.ntp.ui.notice;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.ntp.adapter.CommentNoticeAdapter;
import com.ntp.base.BaseActivity;
import com.ntp.model.gson.CommentNoticeGson;
import com.ntp.network.HttpRequestHelper;
import com.ntp.network.okhttp.ObjectCallbackHandler;
import com.ntp.ui.R;
import com.ntp.util.AppConfig;
import com.ntp.util.ErrorCodeUtil;
import com.squareup.okhttp.Request;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 回帖消息
 * @author yanxing
 */
@ContentView(R.layout.activity_comment_notice)
public class CommentNoticeActivity extends BaseActivity implements PullToRefreshBase.OnRefreshListener2{

    @ViewInject(R.id.pull_to_refresh_listview)
    private PullToRefreshListView mPullToRefreshView;

    @ViewInject(R.id.tip)
    private TextView mTip;

    private CommentNoticeAdapter mCommentNoticeAdapter;
    private List<CommentNoticeGson.ForumUsersEntity> mForumUsersEntityList=new ArrayList<CommentNoticeGson.ForumUsersEntity>();
    private String mName;//当前登录用户名
    private int mCurrentPage =1;//默认加载第一页问题

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTip = (TextView) findViewById(R.id.tip);
        mPullToRefreshView = (PullToRefreshListView)findViewById(R.id.pull_to_refresh_listview);
        mName = AppConfig.getLoadName(getApplicationContext());
        if (mName.equals("")){
            mTip.setVisibility(View.VISIBLE);
            return;
        }
        mCommentNoticeAdapter =new CommentNoticeAdapter(mForumUsersEntityList, mName);
        mPullToRefreshView.setAdapter(mCommentNoticeAdapter);
        mPullToRefreshView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        mPullToRefreshView.setOnRefreshListener(this);
        mPullToRefreshView.setRefreshing(true);//自动刷新
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
     * @param refreshDownOrUp true下拉刷新，false上拉刷新
     */
    public void loadData(final boolean refreshDownOrUp){
        if (!refreshDownOrUp){//上拉刷新
            mCurrentPage++;
        }
        HttpRequestHelper.getInstance().getCommentNoticeList(mName,String.valueOf(mCurrentPage),String.valueOf(10),new ObjectCallbackHandler<CommentNoticeGson>(){
            @Override
            public void onFailure(Request request, IOException e, int response) {
                super.onFailure(request, e, response);
                mPullToRefreshView.onRefreshComplete();
                showToast(ErrorCodeUtil.SERVER_ERROR);
            }

            @Override
            public void onResponse(CommentNoticeGson commentNoticeGson) {
                super.onResponse(commentNoticeGson);
                if (commentNoticeGson!=null){
                    if (refreshDownOrUp){
                        mPullToRefreshView.setMode(PullToRefreshBase.Mode.BOTH);
                        mCurrentPage =commentNoticeGson.getCurrentPage();
                        mCommentNoticeAdapter.updateCommentNotice(commentNoticeGson.getForumUsers());
                        mPullToRefreshView.onRefreshComplete();
                    }else {
                        for (CommentNoticeGson.ForumUsersEntity entity:commentNoticeGson.getForumUsers()){
                            mForumUsersEntityList.add(entity);
                        }
                        mCommentNoticeAdapter.updateCommentNotice(mForumUsersEntityList);
                        mPullToRefreshView.onRefreshComplete();
                    }
                }
            }
        });
    }
}

