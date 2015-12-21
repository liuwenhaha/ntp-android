package com.ntp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ntp.model.gson.HomeworkNoticeGson;
import com.ntp.ui.R;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

/**
 * 作业消息适配器，图片暂时不用
 * @author yanxing
 */
public class HomeworkNoticeAdapter extends BaseAdapter{

    private List<HomeworkNoticeGson.ScoresEntity> scoresEntities;
    private Context context;

    public HomeworkNoticeAdapter(Context context, List<HomeworkNoticeGson.ScoresEntity> scoresEntities) {
        this.scoresEntities = scoresEntities;
        this.context = context;
    }

    @Override
    public int getCount() {
        return scoresEntities.size();
    }

    @Override
    public Object getItem(int position) {
        return scoresEntities.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * 更新数据
     */
    public void updateHomeworkNotice(List<HomeworkNoticeGson.ScoresEntity> scoresEntities){
        this.scoresEntities=scoresEntities;
        notifyDataSetChanged();
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView==null){
            convertView = LayoutInflater.from(context).inflate(R.layout.listview_item_notice, null);
            viewHolder=new ViewHolder();
            x.view().inject(viewHolder,convertView);
            convertView.setTag(viewHolder);
        }else {
            viewHolder= (ViewHolder) convertView.getTag();
        }
//        viewHolder.imageView.setImageResource(scoresEntities.get(position).getId());
        viewHolder.id.setText(scoresEntities.get(position).getId()+"");
        viewHolder.title.setText(scoresEntities.get(position).getExercise().getName());
        String time=scoresEntities.get(position).getExercise().getTime();
        viewHolder.time.setText(time.substring(0, time.lastIndexOf("T")));
        return convertView;
    }

    private class ViewHolder{
        @ViewInject(R.id.img)
        private ImageView imageView;

        @ViewInject(R.id.id)
        private TextView id;

        @ViewInject(R.id.title)
        private TextView title;

        @ViewInject(R.id.time)
        private TextView time;
    }
}
