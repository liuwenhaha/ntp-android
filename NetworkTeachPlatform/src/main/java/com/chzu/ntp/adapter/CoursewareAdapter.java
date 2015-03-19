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
public class CoursewareAdapter extends BaseAdapter implements View.OnClickListener{
    private List<Courseware> mCoursewareList;
    private Context context;
    private Callback callback;

    /**
     * 回调接口，内部控件事件响应不在适配器内部响应，使用者来响应
     */
    public interface Callback {
        public void click(View v);
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
        TextView id,name, size;
        ImageView imageView;
        convertView = LayoutInflater.from(context).inflate(R.layout.listview_item_courseware, null);
        imageView = (ImageView) convertView.findViewById(R.id.myDownload);
//        imageView.setBackgroundResource(cardViewList.get(position).getId());//拉伸图片，充满ImageView控件
//        imageView.setImageResource(cardViewList.get(position).getId());
        id= (TextView) convertView.findViewById(R.id.coursewareId);
        id.setText(mCoursewareList.get(position).getId());
        name = (TextView) convertView.findViewById(R.id.coursewareName);
        name.setText(mCoursewareList.get(position).getName());
        size = (TextView) convertView.findViewById(R.id.size);
        size.setText(mCoursewareList.get(position).getSize());
        imageView.setOnClickListener(this);
        return convertView;
    }

    @Override
    public void onClick(View v) {
        callback.click(v);
    }
}
