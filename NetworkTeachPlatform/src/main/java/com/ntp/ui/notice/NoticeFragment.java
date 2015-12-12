package com.ntp.ui.notice;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ntp.ui.R;
import com.ntp.dao.PreferenceDao;


/**
 * 通知界面，学生在此界面可以查看作业和回帖消息
 */
public class NoticeFragment extends Fragment implements View.OnClickListener{

    private LinearLayout homeworkNotice,replyNotice;//作业消息、回帖消息
    public static ImageView homeworkRed,commentRed;

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
        homeworkRed= (ImageView) view.findViewById(R.id.homeworkRed);
        commentRed= (ImageView) view.findViewById(R.id.commentRed);
        //如果有作业消息
        if (PreferenceDao.isHomeworkRed(getActivity().getApplicationContext())){
          homeworkRed.setVisibility(View.VISIBLE);
        }
        //如果有回帖消息
        if (PreferenceDao.isCommentRed(getActivity().getApplicationContext())){
            commentRed.setVisibility(View.VISIBLE);
        }
        homeworkNotice.setOnClickListener(this);
        replyNotice.setOnClickListener(this);
        return view;
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //查看作业消息
            case R.id.homeworkNotice:
                startActivity(new Intent(getActivity().getApplicationContext(), HomeworkNoticeActivity.class));
                PreferenceDao.setHomeworkRed(getActivity().getApplicationContext(),false);
                //如果没有回帖消息，消去消息图标红点
                if (!PreferenceDao.isCommentRed(getActivity().getApplicationContext())){
                    PreferenceDao.setNoticeRed(getActivity().getApplicationContext(),false);
                }
                break;
            //查看回帖消息
            case R.id.replyNotice:
                startActivity(new Intent(getActivity().getApplicationContext(),CommentNoticeActivity.class));
                PreferenceDao.setCommentRed(getActivity().getApplicationContext(),false);
                //如果没有回帖消息，消去消息图标红点
                if (!PreferenceDao.isHomeworkRed(getActivity().getApplicationContext())){
                    PreferenceDao.setNoticeRed(getActivity().getApplicationContext(),false);
                }
                break;

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (PreferenceDao.isHomeworkRed(getActivity().getApplicationContext())){
            homeworkRed.setVisibility(View.VISIBLE);
        }else {
            homeworkRed.setVisibility(View.INVISIBLE);
        }
        if (PreferenceDao.isCommentRed(getActivity().getApplicationContext())){
            commentRed.setVisibility(View.VISIBLE);
        }else {
            commentRed.setVisibility(View.INVISIBLE);
        }
    }
}
