package com.ntp.ui.course;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ntp.base.BaseFragment;
import com.ntp.model.gson.CoursewareGson;
import com.ntp.network.HttpRequestHelper;
import com.ntp.network.okhttp.CallbackHandler;
import com.ntp.network.okhttp.GsonOkHttpResponse;
import com.ntp.ui.R;
import com.ntp.adapter.CoursewareAdapter;
import com.ntp.util.ConstantValue;
import com.ntp.dao.DownloadHistoryDao;
import com.ntp.util.AppConfig;
import com.ntp.model.Courseware;
import com.ntp.service.DownloadService;
import com.ntp.util.NetworkStateUtil;
import com.ntp.util.SDCardUtil;
import com.squareup.okhttp.Request;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 课程课件、课件进度表
 */
@ContentView(R.layout.fragment_course_ware)
public class CoursewareFragment extends BaseFragment implements CoursewareAdapter.Callback {

    @ViewInject(R.id.courseWareList)
    private ListView mCourseWareList;

    @ViewInject(R.id.load)
    private LinearLayout load;

    @ViewInject(R.id.progressBar)
    private ProgressBar progressBar;

    @ViewInject(R.id.tip)
    private TextView tip;

    private Button download;
    private DownloadService downloadService;//文件下载服务
    private DownloadHistoryDao downloadHistoryDao;
    private List<Courseware> list;
    private CoursewareAdapter mCoursewareAdapter;
    private String code;//课程代码
    private static String name;//文件名称
    private static String path;//文件路径

    private static int flag = 0;

    private static final String TAG = "CoursewareFragment";
    //接受更新进度表intent
    public static final String ACTION_UPDATE = "com.ntp.service.DownloadService.UPDATE";
    //接受下载完成intent
    public static final String ACTION_FINISH = "com.ntp.service.DownloadService.FINISH";
    public static final String DOWNLOAD = "下载";
    public static final String DOWNLOAD_CANCEL = "取消";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            code = getArguments().getString("code");
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        downloadService = new DownloadService();
        list = new ArrayList<Courseware>();
        GsonOkHttpResponse gsonOkHttpResponse = new GsonOkHttpResponse(CoursewareGson.class);
        HttpRequestHelper.getInstance().getCourseware(code, new CallbackHandler<CoursewareGson>(gsonOkHttpResponse) {
            @Override
            public void onFailure(Request request, IOException e, int response) {
                super.onFailure(request, e, response);
                showToast("加载失败");
                load.setVisibility(View.GONE);
            }

            @Override
            public void onResponse(CoursewareGson coursewareGson) {
                super.onResponse(coursewareGson);
                if (coursewareGson != null) {
                    for (CoursewareGson.CoursewaresEntity coursewaresEntity : coursewareGson.getCoursewares()) {
                        Courseware courseware = new Courseware(null, coursewaresEntity.getName(), coursewaresEntity.getPath(), coursewaresEntity.getSize());
                        list.add(courseware);
                    }
                    load.setVisibility(View.GONE);
                    mCoursewareAdapter = new CoursewareAdapter(list, getActivity(), CoursewareFragment.this);
                    mCourseWareList.setAdapter(mCoursewareAdapter);
                } else {
                    showToast("加载失败");
                    load.setVisibility(View.GONE);
                }
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_UPDATE);
        filter.addAction(ACTION_FINISH);
        //注册广播
        getActivity().getApplicationContext().registerReceiver(myReceiver, filter);
    }

    /**
     * 回调接口
     */
    @Override
    public void click(View view, String name, String path, ProgressBar progressBar, TextView tip) {
        switch (view.getId()) {
            case R.id.myDownload:
                download = (Button) view;
                //取消下载
                if (download.getText().equals(DOWNLOAD_CANCEL)) {
                    downloadService.setDownloadPause(true);
                    download.setText(DOWNLOAD);//设置按钮为下载状态
                    this.progressBar.setVisibility(View.GONE);
                    this.tip.setVisibility(View.GONE);
                    File file = new File(ConstantValue.SAVE_PATH + name);//删除下载文件
                    file.delete();
                    break;
                }
                //检测是否有SD卡
                if (!SDCardUtil.checkSDCard()) {
                    Toast.makeText(getActivity().getApplicationContext(), "请插入SD卡", Toast.LENGTH_LONG).show();
                    break;
                }
                //检测网络是否可用
                if (!NetworkStateUtil.isNetworkConnected(getActivity().getApplicationContext())) {
                    Toast.makeText(getActivity().getApplicationContext(), "当前网络不可用", Toast.LENGTH_LONG).show();
                    break;
                }
                //如果用户没有登录，不可下载课件
                if (AppConfig.getLoadName(getActivity().getApplicationContext()).equals("")) {
                    Toast.makeText(getActivity().getApplicationContext(), "该操作需要先登录", Toast.LENGTH_LONG).show();
                    break;
                }
                //检查当前是否禁用了移动网络下载课件和播放视频
                if (NetworkStateUtil.isMobileConnected(getActivity().getApplicationContext()) && !AppConfig.getConfig(getActivity().getApplicationContext())) {
                    Toast.makeText(getActivity().getApplicationContext(), "你已经禁用移动网络下载课件和观看视频", Toast.LENGTH_LONG).show();
                    break;
                }
                this.progressBar = progressBar;
                this.tip = tip;
                downloadService.startActionDownload(getActivity().getApplicationContext(), ConstantValue.PATH_DOWNLOAD_COURSE_WARE + path, name, ConstantValue.SAVE_PATH);
                CoursewareFragment.name = name;
                CoursewareFragment.path = path;
                progressBar.setVisibility(View.VISIBLE);
                //点击下载按钮后，按钮设置为取消下载状态
                download.setText(DOWNLOAD_CANCEL);
                break;
        }
    }

    //使用广播，对收到的下载进度进行处理
    private BroadcastReceiver myReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(ACTION_UPDATE)) {
                int fileSize = intent.getIntExtra("fileSize", 0);
                int downloadLength = intent.getIntExtra("downloadLength", 0);
                if (flag == 0) {//没有设置最大进度
                    progressBar.setMax(fileSize);
                    flag = -1;
                }
                progressBar.setProgress(downloadLength);
            } else if (intent.getAction().equals(ACTION_FINISH)) {
                boolean isSuccess = intent.getBooleanExtra("success", false);
                if (isSuccess) {//如果下载成功
                    tip.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    progressBar.setProgress(0);//重置进度
                    flag = 0;//下载完成，下载另一个文件，重置设置最大进度标志
                    download.setText(DOWNLOAD);
                    String fileName = intent.getStringExtra("fileName");
                    downloadHistoryDao = new DownloadHistoryDao(context);
                    //在数据库中添加下载记录
                    downloadHistoryDao.save(fileName);
                    downloadHistoryDao.close();
                }
            }
        }
    };

    @Override
    public void onPause() {
        // 移除广播接收器
        if (myReceiver != null) {
            getActivity().getApplicationContext().unregisterReceiver(myReceiver);
        }
        super.onPause();
    }
}
