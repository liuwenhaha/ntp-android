package com.ntp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ntp.activity.R;
import com.ntp.model.Notice;

import java.util.List;

/**
 * 作业和回贴消息适配器
 * @author yanxing
 */
public class NoticeAdapter extends BaseAdapter{

    private List<Notice> noticeList;
    private Context context;

    public NoticeAdapter(Context context,List<Notice> noticeList) {
        this.noticeList = noticeList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        TextView title,content,time;
        convertView = LayoutInflater.from(context).inflate(R.layout.listview_item_notice, null);
        imageView= (ImageView) convertView.findViewById(R.id.img);
        imageView.setImageResource(noticeList.get(position).getImageId());
        title= (TextView) convertView.findViewById(R.id.title);
        title.setText(noticeList.get(position).getTitle());
        content= (TextView) convertView.findViewById(R.id.courseName);
        content.setText(noticeList.get(position).getContent());
        time= (TextView) convertView.findViewById(R.id.time);
        time.setText(noticeList.get(position).getTime());
        return convertView;
    }
}
