package com.ntp.view;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ntp.ui.R;
import com.ntp.util.AppConfig;

/**
 * 退出对话框,退出应用，退出账号
 *
 * @author yanxing
 */
public class MyExitDialog extends Activity {

    /**
     * 退出登录结果码
     */
    public static final int RESULT_EXIT_LOGIN = 1;
    /**
     * 退出应用结果码
     */
    public static final int RESULT_EXIT_APP = 2;
    private TextView exitLogin;
    private ImageView line;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_exit_dialog);
        exitLogin = (TextView) findViewById(R.id.exit_login);
        line = (ImageView) findViewById(R.id.h_line);
        if (AppConfig.getLoadName(getApplicationContext()).equals("")) {
            exitLogin.setVisibility(View.GONE);
            line.setVisibility(View.GONE);
        }
    }

    /**
     * 退出登录
     */
    public void exitLogin(View view) {
        if (view.getId() == R.id.exit_login) {
            setResult(RESULT_EXIT_LOGIN);
            finish();
        }

    }

    /**
     * 退出应用
     */
    public void exitApp(View view) {
        if (view.getId() == R.id.exit_app) {
            setResult(RESULT_EXIT_APP);
            finish();
        }
    }

    /**
     * 点击屏幕其他地方，对话框消失
     */
    public boolean onTouchEvent(MotionEvent event) {
        finish();
        return true;
    }
}
