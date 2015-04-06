package com.chzu.ntp.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.chzu.ntp.widget.MyTitleView;

/**
 * 个人信息
 *
 * @author yanxing
 */
public class MeInformationActivity extends Activity {

    private MyTitleView myTitleView;
    private TextView username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_me_information);
        myTitleView = (MyTitleView) findViewById(R.id.myTitleView);
        myTitleView.setTitle("基本信息");
        username = (TextView) findViewById(R.id.username);
        String name = getIntent().getExtras().getString("username");
        username.setText(name);

    }

    /**
     * 修改邮箱
     */
    public void modifyEmail(View view) {

    }

    /**
     * 修改性别
     */
    public void modifySex(View view) {

    }

    /**
     * 修改密码
     */
    public void modifyPwd(View view) {

    }


}
