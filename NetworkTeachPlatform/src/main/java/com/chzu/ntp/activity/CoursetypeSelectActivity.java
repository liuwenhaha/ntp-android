package com.chzu.ntp.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 点击标题栏所有课程，弹出所有课程类型对话框，包含所有课程类型、搜索，所有课程类型从后台获取
 *
 * @author yanxing
 */
public class CoursetypeSelectActivity extends Activity implements View.OnClickListener {
    private TextView coursetypeTitle;//课程类型标题
    private ImageView search;
    private GridView courseTypeName;//课程类型名称

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coursetype_select);
        coursetypeTitle = (TextView) findViewById(R.id.coursetypeTitle);
        courseTypeName = (GridView) findViewById(R.id.courseTypeName);
        search = (ImageView) findViewById(R.id.search);
        search.setOnClickListener(this);
        //模拟数据
        String name[] = new String[]{"c语言", "java", "c++", "c#", "硬件", "软件", "网络", "物联网", "c语言", "java", "c++", "c#", "硬件", "软件", "网络", "物联网"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.activity_coursetype_select_gridview, R.id.type, name);
        courseTypeName.setAdapter(adapter);
    }

    /**
     * 点击屏幕其他地方，对话框消失
     */
    public boolean onTouchEvent(MotionEvent event) {
        finish();
        return true;
    }

    /**
     * 重绘使本activity宽度占据全部手机屏幕
     */
    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        //获取手机屏幕宽度
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        //改变activity尺寸
        View view = getWindow().getDecorView();
        WindowManager.LayoutParams lp = (WindowManager.LayoutParams) view.getLayoutParams();
        lp.gravity = Gravity.TOP;
        lp.width = dm.widthPixels;
        getWindowManager().updateViewLayout(view, lp);
    }


    /**
     * 返回用户的选择，课程类型
     *
     * @param type 课程类型
     */
    public void resultBack(String type) {
        Intent resultIntent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("result", type);
        resultIntent.putExtras(bundle);
        this.setResult(RESULT_OK, resultIntent);
        finish();
    }

    @Override
    protected void onPause() {
        finish();
        super.onPause();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search://搜索课程
                Intent intent = new Intent(this, SearchCourseActivity.class);
                startActivity(intent);
        }
    }
}
