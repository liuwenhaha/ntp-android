package com.ntp.activity.notice;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.ntp.activity.R;


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
