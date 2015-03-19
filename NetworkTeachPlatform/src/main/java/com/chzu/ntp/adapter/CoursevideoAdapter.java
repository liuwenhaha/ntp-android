package com.chzu.ntp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.chzu.ntp.model.Coursevideo;
import com.chzu.ntp.ui.R;

import java.util.List;


/**
 * @author yanxing
 * 课程课件适配器
 */
public class CoursevideoAdapter extends BaseAdapter {
    private List<Coursevideo> mCoursevideoList;
    private Context context;

    public CoursevideoAdapter(List<Coursevideo> mCoursevideoList, Context context) {
        this.mCoursevideoList = mCoursevideoList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return mCoursevideoList.size();
    }

    @Override
    public Object getItem(int position) {
        return mCoursevideoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView id,name;
        ImageView imageView;
        convertView = LayoutInflater.from(context).inflate(R.layout.listview_item_coursevideo, null);
        imageView = (ImageView) convertView.findViewById(R.id.watch);
//        imageView.setBackgroundResource(cardViewList.get(position).getId());//拉伸图片，充满ImageView控件
//        imageView.setImageResource(cardViewList.get(position).getId());
        id= (TextView) convertView.findViewById(R.id.coursewareId);
        id.setText(mCoursevideoList.get(position).getId());
        name = (TextView) convertView.findViewById(R.id.coursevideoName);
        name.setText(mCoursevideoList.get(position).getName());
        return convertView;
    }
}
