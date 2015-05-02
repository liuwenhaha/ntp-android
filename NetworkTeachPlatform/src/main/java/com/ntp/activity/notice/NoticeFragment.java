package com.ntp.activity.notice;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.ntp.activity.GlobalVariable;
import com.ntp.activity.R;
import com.ntp.adapter.CourseAdapter;
import com.ntp.adapter.NoticeAdapter;
import com.ntp.dao.PathConstant;
import com.ntp.dao.PreferenceDao;
import com.ntp.model.Course;
import com.ntp.model.Notice;
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
 * 通知界面，学生在此界面查看需要完成的作业和回帖提醒
 */
public class NoticeFragment extends Fragment implements View.OnClickListener{


    private static NoticeFragment noticeFragment;
    private LinearLayout homeworkNotice,replyNotice;//作业消息、回帖消息

    //创建对象
    public static NoticeFragment getInstance() {
        if (noticeFragment == null) {
            noticeFragment = new NoticeFragment();
        }
        return noticeFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_notice, container, false);
        homeworkNotice= (LinearLayout) view.findViewById(R.id.homeworkNotice);
        replyNotice= (LinearLayout) view.findViewById(R.id.replyNotice);
        homeworkNotice.setOnClickListener(this);
        replyNotice.setOnClickListener(this);
        return view;
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //查看作业消息
            case R.id.homeworkNotice:
                startActivity(new Intent(getActivity().getApplicationContext(),HomeworkNoticeActivity.class));
                break;
            //查看回帖消息
            case R.id.replyNotice:
                //模拟
                startActivity(new Intent(getActivity().getApplicationContext(),HomeworkNoticeActivity.class));
                break;

        }
    }
}
