package com.ntp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ntp.ui.R;
import com.ntp.model.HomeworkNotice;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

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
        ViewHolder viewHolder;
        if (convertView==null){
            convertView = LayoutInflater.from(context).inflate(R.layout.listview_item_notice, null);
            viewHolder=new ViewHolder();
            x.view().inject(viewHolder,convertView);
            convertView.setTag(viewHolder);
        }else {
            viewHolder= (ViewHolder) convertView.getTag();
        }
        viewHolder.imageView.setImageResource(homeworkNoticeList.get(position).getImageId());
        viewHolder.id.setText(homeworkNoticeList.get(position).getId());
        viewHolder.title.setText(homeworkNoticeList.get(position).getTitle());
//        viewHolder.content.setText(homeworkNoticeList.get(position).getContent());
        viewHolder.time.setText(homeworkNoticeList.get(position).getTime());
        return convertView;
    }

    private class ViewHolder{
        @ViewInject(R.id.img)
        private ImageView imageView;

        @ViewInject(R.id.id)
        private TextView id;

        @ViewInject(R.id.title)
        private TextView title;

        @ViewInject(R.id.content)
        private TextView content;

        @ViewInject(R.id.time)
        private TextView time;
    }
}
