package com.chzu.ntp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.chzu.ntp.ui.R;

import java.util.List;


/**
 * @author yanxing
 *         自定义ListView适配器,用来装载CardView类
 */
public class CardViewAdapter extends BaseAdapter {
    private List<CardView> cardViewList;
    private Context context;

    public CardViewAdapter(List<CardView> cardViewList, Context context) {
        this.cardViewList = cardViewList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return cardViewList.size();
    }

    @Override
    public Object getItem(int position) {
        return cardViewList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView courseName, courseType, teacher;
//        ImageView imageView;
        convertView = LayoutInflater.from(context).inflate(R.layout.card_layout, null);
//        imageView = (ImageView) convertView.findViewById(R.id.img);
//        imageView.setBackgroundResource(cardViewList.get(position).getId());//拉伸图片，充满ImageView控件
//        imageView.setImageResource(cardViewList.get(position).getId());
        courseName = (TextView) convertView.findViewById(R.id.courseName);
        courseName.setText(cardViewList.get(position).getName());
        courseType = (TextView) convertView.findViewById(R.id.courseType);
        courseType.setText("类型：" + cardViewList.get(position).getType());
        teacher = (TextView) convertView.findViewById(R.id.teacher);
        teacher.setText("老师：" + cardViewList.get(position).getTeacher());
        return convertView;
    }
}
