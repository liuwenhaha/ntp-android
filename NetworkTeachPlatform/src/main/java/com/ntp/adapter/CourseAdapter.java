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
import com.ntp.activity.R;
import com.ntp.util.BitmapUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

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
        View view = convertView;
        final ViewHolder holder;
        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.listview_item_course, null);
            holder = new ViewHolder();
            holder.code= (TextView) view.findViewById(R.id.code);
            holder.courseName = (TextView) view.findViewById(R.id.courseName);
            holder.courseType = (TextView) view.findViewById(R.id.courseType);
            holder.teacher = (TextView) view.findViewById(R.id.teacher);
            holder.imageView = (ImageView) view.findViewById(R.id.img);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.code.setText(mCourseList.get(position).getCode());
        holder.courseName.setText(mCourseList.get(position).getName());
        holder.courseType.setText(TYPE + mCourseList.get(position).getType());
        holder.teacher.setText(TEACHER + mCourseList.get(position).getTeacher());
        //加载图片
        ImageLoader.getInstance().loadImage(mCourseList.get(position).getImageUri(), options, new SimpleImageLoadingListener() {
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                super.onLoadingComplete(imageUri, view, loadedImage);
                holder.imageView.setImageBitmap(BitmapUtil.createBitmapZoop(loadedImage, 120, 70));
                FadeInBitmapDisplayer.animate(imageView, 500);
            }
        });
        return view;
    }

    //中间变量，标记一个
    private class ViewHolder{
        ImageView imageView;
        TextView code,courseName, courseType, teacher;
    }


}
