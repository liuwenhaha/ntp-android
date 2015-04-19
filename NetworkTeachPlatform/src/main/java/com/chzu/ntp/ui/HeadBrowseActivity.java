package com.chzu.ntp.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.chzu.ntp.dao.UserDao;
import com.chzu.ntp.util.BitmapUtil;

/**
 * 头像浏览界面，可以更换头像
 *
 * @author yanxing
 */
public class HeadBrowseActivity extends Activity {
    private UserDao userDao;
    private ImageView head;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_head_browse);
        Bundle bundle = getIntent().getExtras();
        String username = bundle.getString("username");
        head = (ImageView) findViewById(R.id.head);
        Object object = bundle.get("head");
        Log.i("head", object.toString());
        if (object != null) {
            head.setImageBitmap(BitmapFactory.decodeByteArray(bundle.getByteArray("head"), 0,
                    bundle.getByteArray("head").length));
        }
    }
}
