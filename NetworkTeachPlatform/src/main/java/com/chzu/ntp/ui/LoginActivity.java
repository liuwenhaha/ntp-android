package com.chzu.ntp.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


/**
 * 登录
 */
public class LoginActivity extends Activity implements View.OnClickListener {

    private ImageView back;//返回
    private Button login;//登录
    private EditText username, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        back = (ImageView) findViewById(R.id.back);
        login = (Button) findViewById(R.id.login);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        back.setOnClickListener(this);
        login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back://返回
                finish();
                break;
            case R.id.login://登录
                String nameString = username.getText().toString().trim();
                String passwordString = password.getText().toString().trim();
                if (nameString.equals("") | passwordString.equals("")) {
                    Toast.makeText(getApplicationContext(), "用户名或密码不能为空！",
                            Toast.LENGTH_SHORT).show();
                } else {
                    //检查网络是否连接

                }
        }
    }
}
