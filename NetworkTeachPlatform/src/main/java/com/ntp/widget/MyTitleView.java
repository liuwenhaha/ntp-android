package com.ntp.widget;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.ntp.activity.R;

/**
 * 标题栏控件：返回图片、标题
 * @author  yanxing
 */
public class MyTitleView extends FrameLayout {
    private ImageView back;
    private TextView title;

    public MyTitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.my_title_view, this);
        back = (ImageView) findViewById(R.id.back);
        title = (TextView) findViewById(R.id.title);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MyTitleView);
        CharSequence text = a.getText(0);
        title.setText(text);
        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Activity) getContext()).finish();
            }
        });
    }

    /**
     * 设置图片
     *
     * @param id 资源文件id
     */
    public void setBack(int id) {
        back.setImageDrawable(getResources().getDrawable(id));
    }

    /**
     * 设置标题
     */
    public void setTitle(String text) {
        title.setText(text);
    }

}
