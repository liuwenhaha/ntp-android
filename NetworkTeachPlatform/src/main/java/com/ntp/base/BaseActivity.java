package com.ntp.base;

import android.os.Build;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Toast;

import com.ntp.util.AppUtil;

import org.xutils.x;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

/**
 * 基类Activity
 * Created by lishuangxiang on 2015/12/10.
 */
public class BaseActivity extends SwipeBackActivity {

    /**
     * 使用子类包名+类名打印日志,如果只用类名getClass().getSimpleName()
     */
    protected String TAG = getClass().getName();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        initImmersionStatus();
        AppUtil.setStatusBarDarkMode(true,this);
    }

    /**
     * 使用沉浸式状态栏
     */
    public void initImmersionStatus() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    /**
     * 提示
     */
    protected void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }
}
