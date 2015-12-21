package com.ntp.ui.notice;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.ntp.base.BaseActivity;
import com.ntp.model.gson.HomeworkNoticeGson;
import com.ntp.network.HttpRequestHelper;
import com.ntp.network.okhttp.CallbackHandler;
import com.ntp.network.okhttp.GsonOkHttpResponse;
import com.ntp.ui.R;
import com.ntp.adapter.HomeworkNoticeAdapter;
import com.ntp.util.AppConfig;
import com.squareup.okhttp.Request;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 作业消息
 * @author yanxing
 */
@ContentView(R.layout.activity_homework_notice)
public class HomeworkNoticeActivity extends BaseActivity implements PullToRefreshBase.OnRefreshListener2{

    @ViewInject(R.id.pull_to_refresh_listview)
    private PullToRefreshListView mPullToRefreshView;

    @ViewInject(R.id.tip)
    private TextView tip;

    private List<HomeworkNoticeGson.ScoresEntity> mHomeworkNoticeList=new ArrayList<HomeworkNoticeGson.ScoresEntity>();
    private HomeworkNoticeAdapter mHomeworkNoticeAdapter;
    private String mName;
    private int mCurrentPage=1;//默认加载第一页问题

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mName= AppConfig.getLoadName(getApplicationContext());
        if (mName.equals("")){
            tip.setVisibility(View.VISIBLE);
            return;
        }
        mHomeworkNoticeAdapter =new HomeworkNoticeAdapter(this, mHomeworkNoticeList);
        mPullToRefreshView.setAdapter(mHomeworkNoticeAdapter);
        mPullToRefreshView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        mPullToRefreshView.setOnRefreshListener(this);
        mPullToRefreshView.setRefreshing(true);//自动刷新
    }

    @Event(value = R.id.pull_to_refresh_listview,type = AdapterView.OnItemClickListener.class)
    private void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String  scoreId= ((TextView)view.findViewById(R.id.id)).getText().toString();
        Intent intent=new Intent(getApplicationContext(), HomeworkDetailActivity.class);
        intent.putExtra("scoreId", scoreId);
        startActivity(intent);
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
        GsonOkHttpResponse gsonOkHttpResponse=new GsonOkHttpResponse(HomeworkNoticeGson.class);
        HttpRequestHelper.getInstance().getHomeworkList(mName, String.valueOf(mCurrentPage), String.valueOf(10), new CallbackHandler<HomeworkNoticeGson>(gsonOkHttpResponse) {
            @Override
            public void onFailure(Request request, IOException e, int response) {
                super.onFailure(request, e, response);
                mPullToRefreshView.onRefreshComplete();
                showToast("加载失败");
            }

            @Override
            public void onResponse(HomeworkNoticeGson homeworkNoticeGson) {
                super.onResponse(homeworkNoticeGson);
                if (homeworkNoticeGson != null) {
                    if (refreshDownOrUp) {
                        mPullToRefreshView.setMode(PullToRefreshBase.Mode.BOTH);
                        mCurrentPage = homeworkNoticeGson.getCurrentPage();
                        mPullToRefreshView.onRefreshComplete();
                        mHomeworkNoticeAdapter.updateHomeworkNotice(homeworkNoticeGson.getScores());

                    } else {
                        for (HomeworkNoticeGson.ScoresEntity entity : homeworkNoticeGson.getScores()) {
                            mHomeworkNoticeList.add(entity);
                        }
                        mHomeworkNoticeAdapter.updateHomeworkNotice(mHomeworkNoticeList);
                        mPullToRefreshView.onRefreshComplete();
                    }
                }
            }
        });
    }
}

