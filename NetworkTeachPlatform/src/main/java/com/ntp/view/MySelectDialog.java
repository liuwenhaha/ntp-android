package com.ntp.view;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ntp.activity.R;

/**
 * 选择对话框，当收到的intent参数itemNumber为true时，2个item
 *
 * @author yanxing
 */
public class MySelectDialog extends Activity {

    /**
     * 第一个item结果码
     */
    public static final int RESULT_ITEM1 = 1;
    /**
     * 第二个item结果码
     */
    public static final int RESULT_ITEM2 = 2;
    private TextView firstOption, secondOption;
    private ImageView line;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_select_dialog);
        firstOption = (TextView) findViewById(R.id.item1);
        secondOption = (TextView) findViewById(R.id.item2);
        line = (ImageView) findViewById(R.id.h_line);
        Bundle bundle = getIntent().getExtras();
        //两个item
        if (getIntent().getBooleanExtra("itemNumber", true)) {
            String first = bundle.getString("firstOption");
            String second = bundle.getString("secondOption");
            firstOption.setText(first);
            secondOption.setText(second);
        }
        //itemNumber为false，显示一个item
        else if (!getIntent().getBooleanExtra("itemNumber", true)) {
            String first = bundle.getString("firstOption");
            firstOption.setText(first);
            secondOption.setVisibility(View.GONE);
            line.setVisibility(View.GONE);
        }

    }

    /**
     * 选择第一项
     */
    public void firstOption(View view) {
        if (view.getId() == R.id.item1) {
            setResult(RESULT_ITEM1);
            finish();
        }

    }

    /**
     * 选择第二项
     */
    public void secondOption(View view) {
        if (view.getId() == R.id.item2) {
            setResult(RESULT_ITEM2);
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
