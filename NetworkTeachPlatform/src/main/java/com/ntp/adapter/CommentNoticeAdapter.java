package com.ntp.adapter;

import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.ntp.model.gson.CommentNoticeGson;
import com.ntp.ui.R;
import com.ntp.view.CircleImageView;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

/**
 * 回帖消息
 * Created by lishuangxiang on 2015/12/21.
 */
public class CommentNoticeAdapter extends BaseAdapter{

    private List<CommentNoticeGson.ForumUsersEntity> forumUsersEntityList;
    private DisplayImageOptions options;//UIL显示图片的配置
    private String name;

    public CommentNoticeAdapter(List<CommentNoticeGson.ForumUsersEntity> forumUsersEntityList,String name){
        this.forumUsersEntityList=forumUsersEntityList;
        this.name=name;
        options = new DisplayImageOptions.Builder()
                .cacheOnDisk(true)
                .showImageForEmptyUri(R.drawable.head_default)//设置图片URI为空时默认显示图片
                .showImageOnFail(R.drawable.head_default)//设置图片加载失败时默认显示图片
                .build();
    }
    @Override
    public int getCount() {
        return forumUsersEntityList.size();
    }

    @Override
    public Object getItem(int position) {
        return forumUsersEntityList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    /**
     * 更新数据
     */
    public void updateCommentNotice(List<CommentNoticeGson.ForumUsersEntity> forumUsersEntityList){
        this.forumUsersEntityList=forumUsersEntityList;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView==null){
            convertView=LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_item_comment_notice,null);
            viewHolder=new ViewHolder();
            x.view().inject(viewHolder,convertView);
            convertView.setTag(viewHolder);
        }else {
            viewHolder= (ViewHolder) convertView.getTag();
        }
        ImageLoader.getInstance().displayImage(forumUsersEntityList.get(position).getUser().getHead(), viewHolder.head, options);
        viewHolder.commentName.setText(forumUsersEntityList.get(position).getUser().getName());
        String time=forumUsersEntityList.get(position).getTime();
        viewHolder.time.setText(time.substring(0, time.lastIndexOf("T")));

        //name设置绿色
        SpannableStringBuilder builder = new SpannableStringBuilder("回复"+name+":"+forumUsersEntityList.get(position).getContent());
        ForegroundColorSpan dark = new ForegroundColorSpan(parent.getContext().getResources().getColor(R.color.green));
        builder.setSpan(dark, 2, name.length() +"回复".length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        viewHolder.commentContent.setText(builder);

        SpannableStringBuilder builder1 = new SpannableStringBuilder(name+":"+forumUsersEntityList.get(position).getForum().getContent());
        ForegroundColorSpan dark1 = new ForegroundColorSpan(parent.getContext().getResources().getColor(R.color.green));
        builder1.setSpan(dark1,0,name.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        viewHolder.forumContent.setText(builder1);
        return convertView;
    }

    private class ViewHolder {
        @ViewInject(R.id.head)
        private CircleImageView head;

        //帖子id，不作显示
        @ViewInject(R.id.id)
        private TextView id;

        //回复人姓名
        @ViewInject(R.id.commentName)
        private TextView commentName;

        @ViewInject(R.id.time)
        private TextView time;

        //回复内容
        @ViewInject(R.id.commentContent)
        private TextView commentContent;

        //原帖子内容
        @ViewInject(R.id.forumContent)
        private TextView forumContent;

    }
}
