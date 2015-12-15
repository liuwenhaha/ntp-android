package com.ntp.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ntp.model.Course;
import com.ntp.ui.R;
import com.ntp.util.BitmapUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;


/**
 * @author yanxing
 * 课程适配器
 */
public class CourseAdapter extends BaseAdapter {

    private DisplayImageOptions options;//UIL显示图片的配置
    private List<Course> mCourseList;
    private Context context;
    public ImageView imageView;
    private static final String TYPE="类型：";
    private static final String TEACHER="老师：";

    public CourseAdapter(List<Course> mCourseList, Context context) {
        this.mCourseList = mCourseList;
        this.context = context;
        options = new DisplayImageOptions.Builder()
                .cacheOnDisk(true)
                .showImageForEmptyUri(R.drawable.course_default)//设置图片URI为空时默认显示图片
                .showImageOnFail(R.drawable.course_default)//设置图片加载失败时默认显示图片
                .build();
    }

    /**
     * 更新数据
     * @param mCourseList
     */
    public void update (List<Course> mCourseList){
        this.mCourseList=mCourseList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mCourseList.size();
    }

    @Override
    public Object getItem(int position) {
        return mCourseList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.listview_item_course, null);
            holder = new ViewHolder();
            x.view().inject(holder, convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.code.setText(mCourseList.get(position).getCode());
        holder.courseName.setText(mCourseList.get(position).getName());
        holder.courseType.setText(TYPE + mCourseList.get(position).getType());
        holder.teacher.setText(TEACHER + mCourseList.get(position).getTeacher());
        //加载图片
        ImageLoader.getInstance().displayImage(mCourseList.get(position).getImageUri(),holder.imageView, options);
        return convertView;
    }

    //中间变量，标记一个
    private class ViewHolder{
        @ViewInject(R.id.img)
        private ImageView imageView;

        @ViewInject(R.id.code)
        private TextView code;

        @ViewInject(R.id.courseName)
        private TextView courseName;

        @ViewInject(R.id.courseType)
        private TextView courseType;

        @ViewInject(R.id.teacher)
        private TextView teacher;
    }
}
