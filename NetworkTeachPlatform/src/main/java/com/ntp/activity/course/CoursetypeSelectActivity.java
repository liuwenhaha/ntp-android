package com.ntp.activity.course;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.ntp.activity.R;
import com.ntp.dao.PathConstant;
import com.ntp.dao.CourseTypeDao;
import com.ntp.util.HttpUtil;
import com.ntp.util.NetworkStateUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * 点击标题栏所有课程，弹出所有课程类型对话框，包含所有课程类型
 *
 * @author yanxing
 */
public class CoursetypeSelectActivity extends Activity implements View.OnClickListener {
    private TextView coursetypeTitle;//课程类型标题
    private GridView courseTypeName;//课程类型名称
    private CourseTypeDao courseTypeDao;
    public static final String TAG = "CoursetypeSelectActivit";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coursetype_select);
        coursetypeTitle = (TextView) findViewById(R.id.coursetypeTitle);
        courseTypeName = (GridView) findViewById(R.id.courseTypeName);
        courseTypeDao = new CourseTypeDao(getApplicationContext());
        String type[] = courseTypeDao.getAllCourseType();
        if (type.length > 0) {//本地有缓存
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.activity_coursetype_select_gridview, R.id.type, type);
            courseTypeName.setAdapter(adapter);
        } else {//没有缓存
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.activity_coursetype_select_gridview, R.id.type, getData());
            courseTypeName.setAdapter(adapter);
        }
    }

    /**
     * 获取网络数据并缓存到数据库
     */
    public String[] getData() {
        if (NetworkStateUtil.isNetworkConnected(getApplicationContext())) {//网络可用
            try {
                JSONObject jb = HttpUtil.getDataFromInternet(new URL(PathConstant.PATH_COURSE_TYPE_LIST), null);
                if (jb != null) {
                    JSONArray ja = jb.getJSONArray("listCType");
                    String name[] = new String[ja.length()];
                    for (int i = 0; i < ja.length(); i++) {
                        JSONObject j = ja.getJSONObject(i);
                        name[i] = j.getString("type");
                        courseTypeDao.save(name[i]);
                    }
                    return name;
                } else {
                    Log.i(TAG, "没有取到后台数据");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        return new String[]{};
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


        }
    }
}
