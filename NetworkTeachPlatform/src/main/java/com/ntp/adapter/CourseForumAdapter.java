package com.ntp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.ntp.model.gson.CourseForumGson;
import com.ntp.ui.R;
import com.ntp.view.CircleImageView;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

/**
 * 课程问题适配器
 * Created by lishuangxiang on 2015/12/16.
 */
public class CourseForumAdapter extends BaseAdapter{

    private List<CourseForumGson.ForumsEntity> courseForumGsonList;
    private DisplayImageOptions options;

    public CourseForumAdapter(List<CourseForumGson.ForumsEntity> list){
        this.courseForumGsonList=list;
        options = new DisplayImageOptions.Builder()
                .cacheOnDisk(true)
                .showImageForEmptyUri(R.drawable.head_default)//设置图片URI为空时默认显示图片
                .showImageOnFail(R.drawable.head_default)//设置图片加载失败时默认显示图片
                .build();
    }

    @Override
    public int getCount() {
        return courseForumGsonList.size();
    }

    @Override
    public Object getItem(int position) {
        return courseForumGsonList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    /**
     * 更新数据
     * @param list
     */
    public void update(List<CourseForumGson.ForumsEntity> list){
        this.courseForumGsonList=list;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView==null){
            convertView= LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_item_courseforum, null);
            viewHolder=new ViewHolder();
            x.view().inject(viewHolder,convertView);
            convertView.setTag(viewHolder);
        }else {
            viewHolder= (ViewHolder) convertView.getTag();
        }
        viewHolder.id.setText(courseForumGsonList.get(position).getId() + "");
        viewHolder.content.setText(courseForumGsonList.get(position).getContent());
        viewHolder.name.setText(courseForumGsonList.get(position).getUser().getName());
        String time=courseForumGsonList.get(position).getTime();
        viewHolder.time.setText(time.substring(0, time.lastIndexOf("T")));
        viewHolder.reply.setText(courseForumGsonList.get(position).getReplyNumber() == 0 ? "0人回复" : courseForumGsonList.get(position).getReplyNumber() + "人回复");
        String uri=courseForumGsonList.get(position).getUser().getHead();
        ImageLoader.getInstance().displayImage(uri,viewHolder.head,options);
        return convertView;
    }

    private class ViewHolder{

        @ViewInject(R.id.id)
        private TextView id;

        @ViewInject(R.id.head)
        private CircleImageView head;

        @ViewInject(R.id.name)
        private TextView name;

        @ViewInject(R.id.time)
        private TextView time;

        @ViewInject(R.id.content)
        private TextView content;

        @ViewInject(R.id.reply)
        private TextView reply;
    }
}
