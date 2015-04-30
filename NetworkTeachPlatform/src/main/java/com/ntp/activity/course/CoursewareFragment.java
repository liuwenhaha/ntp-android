package com.ntp.activity.course;


import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.ntp.activity.R;
import com.ntp.adapter.CoursewareAdapter;
import com.ntp.dao.PathConstant;
import com.ntp.dao.PreferenceDao;
import com.ntp.model.Courseware;
import com.ntp.service.DownloadService;
import com.ntp.util.NetworkStateUtil;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 课程课件
 */
public class CoursewareFragment extends Fragment implements CoursewareAdapter.Callback {

    private static CoursewareFragment mCoursewareFragment;
    private static AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
    private DownloadService downloadService;//文件下载服务
    private ListView mCourseWareList;
    private List<Courseware> list;
    private CoursewareAdapter mCoursewareAdapter;
    private String code;//课程代码
    private LinearLayout load;
    private ProgressBar progressBar;
    private Button download;
    private static String name;//文件名称
    private static String path;//文件路径
    private TextView tip;
    private static int flag = 0;
    private static int notificationId = 0;//通知id

    private static final String TAG = "CoursewareFragment";
    //接受更新进度表intent
    public static final String ACTION_UPDATE = "com.ntp.service.DownloadService.UPDATE";
    //接受下载完成intent
    public static final String ACTION_FINISH = "com.ntp.service.DownloadService.FINISH";
    public static final String DOWNLOAD = "下载";
    public static final String DOWNLOAD_CANCEL = "取消";


    /**
     * @param code 课程代码
     */
    public static CoursewareFragment getInstance(String code) {
        mCoursewareFragment = new CoursewareFragment();
        Bundle bundle = new Bundle();
        bundle.putString("code", code);
        mCoursewareFragment.setArguments(bundle);
        return mCoursewareFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            code = getArguments().getString("code");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_course_ware, container, false);
        mCourseWareList = (ListView) view.findViewById(R.id.courseWareList);
        load = (LinearLayout) view.findViewById(R.id.load);
        downloadService = new DownloadService();
        RequestParams params = new RequestParams();
        params.put("code", code);
        Log.i(TAG, code);
        list = new ArrayList<Courseware>();
        asyncHttpClient.post(PathConstant.PATH_COURSE_WARE, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (response != null) {
                    try {
                        JSONArray ja = response.getJSONArray("coursewares");
                        for (int i = 0; i < ja.length(); i++) {
                            JSONObject jb = ja.getJSONObject(i);
                            Courseware courseware = new Courseware(null, jb.getString("name"), jb.getString("path"), jb.getString("size").equals("null") ? "" : jb.getString("size"));
                            list.add(courseware);
                            load.setVisibility(View.GONE);
                        }
                        mCoursewareAdapter = new CoursewareAdapter(list, getActivity().getApplicationContext(),CoursewareFragment.this);
                        mCoursewareAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else{
                        Toast.makeText(getActivity().getApplicationContext(), "加载失败", Toast.LENGTH_SHORT).show();
                        load.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Log.i(TAG, throwable.toString());
                load.setVisibility(View.GONE);
            }
        });
        mCoursewareAdapter = new CoursewareAdapter(list, getActivity().getApplicationContext(), this);
        mCourseWareList.setAdapter(mCoursewareAdapter);
        return view;
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
                    NotificationManager manager = (NotificationManager) getActivity().getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
                    Log.i(TAG,"取消通知id="+notificationId+"");
                    downloadService.stopSelf();
                    //没有取消掉？？？？？？
                    manager.cancel(notificationId);
                    download.setText(DOWNLOAD);//设置按钮为下载状态
                    this.progressBar.setVisibility(View.GONE);
                    this.tip.setVisibility(View.GONE);
                    File file=new File(PathConstant.SAVE_PATH+name);//删除下载文件
                    file.delete();
                    return;
                }
                //检测网络是否可用
                if (!NetworkStateUtil.isNetworkConnected(getActivity().getApplicationContext())) {
                    Toast.makeText(getActivity().getApplicationContext(), "当前网络不可用", Toast.LENGTH_LONG).show();
                    return;
                }
                //如果用户没有登录，不可下载课件
                if (PreferenceDao.getLoadName(getActivity().getApplicationContext()).equals("")) {
                    Toast.makeText(getActivity().getApplicationContext(), "该操作需要先登录", Toast.LENGTH_LONG).show();
                    return;
                }
                //检查当前是否禁用了移动网络下载课件和播放视频
                if (NetworkStateUtil.isMobileConnected(getActivity().getApplicationContext()) && !PreferenceDao.getConfig(getActivity().getApplicationContext())) {
                    Toast.makeText(getActivity().getApplicationContext(), "你已经禁用移动网络下载课件和观看视频", Toast.LENGTH_LONG).show();
                }
                this.progressBar = progressBar;
                this.tip = tip;
                downloadService.startActionDownload(getActivity().getApplicationContext(), PathConstant.PATH_DOWNLOAD_COURSE_WARE + path, name, PathConstant.SAVE_PATH);
                CoursewareFragment.name =name;
                CoursewareFragment.path =path;
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
                CoursewareFragment.notificationId = intent.getIntExtra("notificationId", 0);
                if (flag == 0) {//没有设置最大进度
                    progressBar.setMax(fileSize);
                    flag = -1;
                }
                progressBar.setProgress(downloadLength);
            } else if (intent.getAction().equals(ACTION_FINISH)) {
                boolean isSuccess = intent.getBooleanExtra("success", false);
                if (isSuccess == true) {
                    tip.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    progressBar.setProgress(0);//重置进度
                    flag=0;//下载完成，下载另一个文件，重置设置最大进度标志
                    download.setText(DOWNLOAD);
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
