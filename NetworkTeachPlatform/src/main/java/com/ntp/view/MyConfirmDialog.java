package com.ntp.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.ntp.ui.R;

/**
 * 确定取消对话框，通过继承Activity实现，需要设置此MyDialog为透明
 *
 * @author yanxing
 */
public class MyConfirmDialog extends Activity implements View.OnClickListener {
    private TextView tip, cancel, confirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_my_dialog);
        setContentView(R.layout.my_dialog_view);
        Intent intent = getIntent();
        String tipStr = intent.getExtras().getString("tip");
        tip = (TextView) findViewById(R.id.tip);
        tip.setText(tipStr);
        cancel = (TextView) findViewById(R.id.cancel);
        confirm = (TextView) findViewById(R.id.confirm);
        cancel.setOnClickListener(this);
        confirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel://取消
                finish();
                break;
            case R.id.confirm://确定
                setResult(RESULT_OK);
                finish();
                break;
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
