package com.ntp.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.ntp.activity.course.CoursewareFragment;
import com.ntp.model.Courseware;
import com.ntp.activity.R;
import com.ntp.util.SDCardUtil;

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
     */
    public interface Callback {

        /**
         * @param v
         * @param name 文件名
         * @param path 文件路径
         * @param progressBar 进度条
         * @param tip 提示
         */
        public void click(View v,String name,String path,ProgressBar progressBar,TextView tip);
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
        final TextView name,path, size,tip;
        final Button download;
        final ProgressBar progressBar;
        convertView = LayoutInflater.from(context).inflate(R.layout.listview_item_courseware, null);
        download = (Button) convertView.findViewById(R.id.myDownload);
        path= (TextView) convertView.findViewById(R.id.coursewarePath);
        path.setText(mCoursewareList.get(position).getPath());
        name = (TextView) convertView.findViewById(R.id.coursewareName);
        name.setText(mCoursewareList.get(position).getName());
        size = (TextView) convertView.findViewById(R.id.size);
        if (mCoursewareList.get(position).getSize().equals("")){
            size.setVisibility(View.INVISIBLE);
        }else {
            size.setText(mCoursewareList.get(position).getSize());
        }
        progressBar= (ProgressBar) convertView.findViewById(R.id.progressBar);
        tip= (TextView) convertView.findViewById(R.id.tip);
        if (SDCardUtil.isExistSDFile("ntp/download/"+name.getText().toString())){//如果文件已下载
            tip.setVisibility(View.VISIBLE);
        }
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.click(v, name.getText().toString(), path.getText().toString(), progressBar, tip);
            }
        });
        return convertView;
    }
}
