package com.ntp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ntp.activity.R;
import com.ntp.model.HomeworkNotice;

import java.util.List;

/**
 * 作业消息适配器，图片暂时不用
 * @author yanxing
 */
public class HomeworkNoticeAdapter extends BaseAdapter{

    private List<HomeworkNotice> homeworkNoticeList;
    private Context context;

    public HomeworkNoticeAdapter(Context context, List<HomeworkNotice> homeworkNoticeList) {
        this.homeworkNoticeList = homeworkNoticeList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return homeworkNoticeList.size();
    }

    @Override
    public Object getItem(int position) {
        return homeworkNoticeList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        TextView id,title,content,time;
        convertView = LayoutInflater.from(context).inflate(R.layout.listview_item_notice, null);
        imageView= (ImageView) convertView.findViewById(R.id.img);
        imageView.setImageResource(homeworkNoticeList.get(position).getImageId());
        id= (TextView) convertView.findViewById(R.id.id);
        id.setText(homeworkNoticeList.get(position).getId());
        title= (TextView) convertView.findViewById(R.id.title);
        title.setText(homeworkNoticeList.get(position).getTitle());
        content= (TextView) convertView.findViewById(R.id.courseName);
        content.setText(homeworkNoticeList.get(position).getContent());
        time= (TextView) convertView.findViewById(R.id.time);
        time.setText(homeworkNoticeList.get(position).getTime());
        return convertView;
    }
}
