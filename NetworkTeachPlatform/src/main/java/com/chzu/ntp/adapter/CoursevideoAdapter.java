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
public class CoursevideoAdapter extends BaseAdapter implements View.OnClickListener {
    private List<Coursevideo> mCoursevideoList;
    private Context context;
    private Callback callback;

    /**
     * 回调接口，内部控件事件响应不在适配器内部响应，使用者来响应
     */
    public interface Callback {
        public void click(View v);
    }

    public CoursevideoAdapter(List<Coursevideo> mCoursevideoList, Context context,Callback callback) {
        this.mCoursevideoList = mCoursevideoList;
        this.context = context;
        this.callback=callback;
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
        id= (TextView) convertView.findViewById(R.id.coursevideoId);
        id.setText(mCoursevideoList.get(position).getId());
        name = (TextView) convertView.findViewById(R.id.coursevideoName);
        name.setText(mCoursevideoList.get(position).getName());
        imageView.setOnClickListener(this);
        return convertView;
    }

    @Override
    public void onClick(View v) {
           callback.click(v);
    }

}
