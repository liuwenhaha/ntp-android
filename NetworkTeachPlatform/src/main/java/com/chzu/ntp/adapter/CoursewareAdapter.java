package com.chzu.ntp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.chzu.ntp.model.Courseware;
import com.chzu.ntp.ui.R;

import java.util.List;


/**
 * @author yanxing
 * 课程课件适配器
 */
public class CoursewareAdapter extends BaseAdapter {
    private List<Courseware> mCoursewareList;
    private Context context;

    public CoursewareAdapter(List<Courseware> mCoursewareList, Context context) {
        this.mCoursewareList = mCoursewareList;
        this.context = context;
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
        TextView id,name, size;
        ImageView imageView;
        convertView = LayoutInflater.from(context).inflate(R.layout.listview_item_layout, null);
        imageView = (ImageView) convertView.findViewById(R.id.myDownload);
//        imageView.setBackgroundResource(cardViewList.get(position).getId());//拉伸图片，充满ImageView控件
//        imageView.setImageResource(cardViewList.get(position).getId());
        id= (TextView) convertView.findViewById(R.id.coursewareId);
        id.setText(mCoursewareList.get(position).getId());
        name = (TextView) convertView.findViewById(R.id.coursewareName);
        name.setText(mCoursewareList.get(position).getName());
        size = (TextView) convertView.findViewById(R.id.size);
        size.setText(mCoursewareList.get(position).getSize());
        return convertView;
    }
}
