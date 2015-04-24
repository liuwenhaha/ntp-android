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

    private ImageLoader imageLoader;//UIL加载图片类
    private DisplayImageOptions options;//UIL显示图片的配置
    private List<Course> mCourseList;
    private Context context;
    public ImageView imageView;
    private static final String TYPE="类型：";
    private static final String TEACHER="老师：";

    public CourseAdapter(List<Course> mCourseList, Context context,ImageLoader imageLoader) {
        this.mCourseList = mCourseList;
        this.context = context;
        this.imageLoader=imageLoader;
        options = new DisplayImageOptions.Builder()
                .cacheOnDisk(true)
                .showImageOnFail(R.drawable.course_default)//不存在默认显示图片
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
            holder.code.setText(mCourseList.get(position).getCode());
            holder.courseName = (TextView) view.findViewById(R.id.courseName);
            holder.courseName.setText(mCourseList.get(position).getName());
            holder.courseType = (TextView) view.findViewById(R.id.courseType);
            holder.courseType.setText(TYPE + mCourseList.get(position).getType());
            holder.teacher = (TextView) view.findViewById(R.id.teacher);
            holder.teacher.setText(TEACHER + mCourseList.get(position).getTeacher());
            holder.imageView = (ImageView) view.findViewById(R.id.img);
            // 预设一个图片,防止因课程图片路径错误下载失败导致无法加载该课程信息，也防止重复加载有图片的课程
            holder.imageView.setImageResource(R.drawable.course_default);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        //加载图片
        imageLoader.loadImage(mCourseList.get(position).getImageUri(),options,new SimpleImageLoadingListener(){
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                super.onLoadingComplete(imageUri, view, loadedImage);
                holder.imageView.setImageBitmap(BitmapUtil.createBitmapZoop(loadedImage,120,70));
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
