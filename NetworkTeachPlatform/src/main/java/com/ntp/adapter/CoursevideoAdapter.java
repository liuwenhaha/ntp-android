package com.ntp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ntp.model.Coursevideo;
import com.ntp.ui.R;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;


/**
 * @author yanxing
 * 课程视频适配器
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
        ViewHolder viewHolder;
        if (convertView==null){
            convertView = LayoutInflater.from(context).inflate(R.layout.listview_item_coursevideo, null);
            viewHolder=new ViewHolder();
            x.view().inject(viewHolder,convertView);
            convertView.setTag(viewHolder);
        }else {
            viewHolder= (ViewHolder) convertView.getTag();
        }
        viewHolder.watch.setImageResource(mCoursevideoList.get(position).getImageId());
        viewHolder.watch.setTag(position);//设置标记
        viewHolder.coursevideoName.setText(mCoursevideoList.get(position).getName());
        viewHolder.coursevideoName.setTag(position);
        viewHolder.path.setText(mCoursevideoList.get(position).getPath());
        viewHolder.coursevideoName.setOnClickListener(this);
        viewHolder.watch.setOnClickListener(this);
        return convertView;
    }

    @Override
    public void onClick(View v) {
           callback.click(v);
    }

    private class ViewHolder{
        @ViewInject(R.id.watch)
        private ImageView watch;

        @ViewInject(R.id.coursevideoName)
        private TextView coursevideoName;

        @ViewInject(R.id.path)
        private TextView path;
    }

}
