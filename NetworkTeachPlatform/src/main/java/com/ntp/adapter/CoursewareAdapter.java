package com.ntp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ntp.model.Courseware;
import com.ntp.ui.R;
import com.ntp.util.SDCardUtil;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

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
        ViewHolder viewHolder;
        if (convertView==null){
            convertView = LayoutInflater.from(context).inflate(R.layout.listview_item_courseware, null);
            viewHolder=new ViewHolder();
            x.view().inject(viewHolder,convertView);
            convertView.setTag(viewHolder);
        }else {
            viewHolder= (ViewHolder) convertView.getTag();
        }
        viewHolder.path.setText(mCoursewareList.get(position).getPath());
        viewHolder.courseWareName.setText(mCoursewareList.get(position).getName());
        if (mCoursewareList.get(position).getSize().equals("")){
            viewHolder.size.setVisibility(View.INVISIBLE);
        }else {
            viewHolder.size.setText(mCoursewareList.get(position).getSize());
        }
        if (SDCardUtil.isExistSDFile("ntp/download/"+viewHolder.courseWareName.getText().toString())){//如果文件已下载
            viewHolder.tip.setVisibility(View.VISIBLE);
        }
        final ViewHolder finalViewHolder = viewHolder;
        viewHolder.download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.click(v, finalViewHolder.courseWareName.getText().toString(),
                        finalViewHolder.path.getText().toString(), finalViewHolder.progressBar, finalViewHolder.tip);
            }
        });
        return convertView;
    }

    private class ViewHolder{
        @ViewInject(R.id.myDownload)
        private Button download;

        @ViewInject(R.id.coursewarePath)
        private TextView path;

        @ViewInject(R.id.coursewareName)
        private TextView courseWareName;

        @ViewInject(R.id.size)
        private TextView size;

        @ViewInject(R.id.tip)
        private TextView tip;

        @ViewInject(R.id.progressBar)
        private ProgressBar progressBar;
    }
}
