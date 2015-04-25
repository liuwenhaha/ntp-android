package com.ntp.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ntp.model.Courseware;
import com.ntp.activity.R;

import java.util.List;


/**
 * @author yanxing
 * 课程课件适配器
 */
public class CoursewareAdapter extends BaseAdapter{
    private List<Courseware> mCoursewareList;
    private Context context;
    private Callback callback;

    /**
     * 回调接口，内部控件事件响应不在适配器内部响应，使用者来响应
     * 返回点击的课件的名称和路径
     */
    public interface Callback {
        public void click(View v,String name,String path);
    }

    public CoursewareAdapter(List<Courseware> mCoursewareList, Context context,Callback callback) {
        this.mCoursewareList = mCoursewareList;
        this.context = context;
        this.callback=callback;
    }

    @Override
    public int getCount() {
        return mCoursewareList.size();
    }

    @Override
    public Object getItem(int position) {
        return mCoursewareList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final TextView name,path, size;
        ImageView imageView;
        convertView = LayoutInflater.from(context).inflate(R.layout.listview_item_courseware, null);
        imageView = (ImageView) convertView.findViewById(R.id.myDownload);
        path= (TextView) convertView.findViewById(R.id.coursewarePath);
        path.setText(mCoursewareList.get(position).getId());
        name = (TextView) convertView.findViewById(R.id.coursewareName);
        name.setText(mCoursewareList.get(position).getName());
        size = (TextView) convertView.findViewById(R.id.size);
        size.setText(mCoursewareList.get(position).getSize());
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.click(v,name.getText().toString(),path.getText().toString());
            }
        });
        return convertView;
    }
}
