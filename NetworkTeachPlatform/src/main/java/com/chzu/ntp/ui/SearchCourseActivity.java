package com.chzu.ntp.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.chzu.ntp.widget.MyProgress;

/**
 * 搜索课程
 */
public class SearchCourseActivity extends Activity implements View.OnClickListener {

    private ImageView back;//返回
    private EditText search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_course);
        back = (ImageView) findViewById(R.id.back);
        search = (EditText) findViewById(R.id.search);
        back.setOnClickListener(this);
        search.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    Intent intent = new Intent(getApplicationContext(), MyProgress.class);
                    startActivity(intent);
                }
                return false;
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back://返回
                finish();
                break;
        }
    }


}
