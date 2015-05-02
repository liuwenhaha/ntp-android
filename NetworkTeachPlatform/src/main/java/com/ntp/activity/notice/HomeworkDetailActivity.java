package com.ntp.activity.notice;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.ntp.activity.R;

/**
 * 作业消息详情
 * @author yanxing
 */
public class HomeworkDetailActivity extends Activity {

    private TextView homeworkTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homework_detail);
        homeworkTitle= (TextView) findViewById(R.id.homeworkTitle);
        homeworkTitle.setText("第一章作业");
    }
}
