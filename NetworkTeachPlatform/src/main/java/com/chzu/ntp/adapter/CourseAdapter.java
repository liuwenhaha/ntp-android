package com.chzu.ntp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chzu.ntp.model.Course;
import com.chzu.ntp.ui.R;

import java.util.List;


/**
 * @author yanxing
 * 课程适配器
 */
public class CourseAdapter extends BaseAdapter {
    private List<Course> mCourseList;
    private Context context;

    public CourseAdapter(List<Course> mCourseList, Context context) {
        this.mCourseList = mCourseList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return mCourseList.size();
    }

    @Override
    public Object getItem(int position) {
        return mCourseList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView code,courseName, courseType, teacher;
//        ImageView imageView;
        convertView = LayoutInflater.from(context).inflate(R.layout.card_layout, null);
//        imageView = (ImageView) convertView.findViewById(R.id.img);
//        imageView.setBackgroundResource(cardViewList.get(position).getId());//拉伸图片，充满ImageView控件
//        imageView.setImageResource(cardViewList.get(position).getId());
        code= (TextView) convertView.findViewById(R.id.code);
        code.setText(mCourseList.get(position).getCode());
        courseName = (TextView) convertView.findViewById(R.id.courseName);
        courseName.setText(mCourseList.get(position).getName());
        courseType = (TextView) convertView.findViewById(R.id.courseType);
        courseType.setText("类型：" + mCourseList.get(position).getType());
        teacher = (TextView) convertView.findViewById(R.id.teacher);
        teacher.setText("老师：" + mCourseList.get(position).getTeacher());
        return convertView;
    }
}
