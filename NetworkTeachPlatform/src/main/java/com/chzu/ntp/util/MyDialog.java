package com.chzu.ntp.util;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.chzu.ntp.ui.R;

/**
 * 自定义确定取消对话框控件，通过继承Activity实现，需要设置此MyDialog为透明
 * @author  yanxing
 */
public class MyDialog extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_dialog);
    }
}
