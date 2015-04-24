package com.ntp.widget;

import android.app.Activity;
import android.os.Bundle;

import com.ntp.activity.R;

/**
 * 自定义悬浮进度对话框
 *
 * @author yanxing
 */
public class MyProgress extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_progress);
    }
}
