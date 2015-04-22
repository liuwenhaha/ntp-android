package com.chzu.ntp.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.chzu.ntp.model.Course;
import com.chzu.ntp.ui.R;
import com.chzu.ntp.util.BitmapUtil;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView code,courseName, courseType, teacher;
        convertView = LayoutInflater.from(context).inflate(R.layout.listview_item_course, null);
        imageView = (ImageView) convertView.findViewById(R.id.img);
        //加载图片
        imageLoader.loadImage(mCourseList.get(position).getImageUri(),options,new SimpleImageLoadingListener(){
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                super.onLoadingComplete(imageUri, view, loadedImage);
                imageView.setImageBitmap(BitmapUtil.createBitmapZoop(loadedImage,120,70));
                //图片的淡入效果
                FadeInBitmapDisplayer.animate(imageView, 500);
            }
        });
        code= (TextView) convertView.findViewById(R.id.code);
        code.setText(mCourseList.get(position).getCode());
        courseName = (TextView) convertView.findViewById(R.id.courseName);
        courseName.setText(mCourseList.get(position).getName());
        courseType = (TextView) convertView.findViewById(R.id.courseType);
        courseType.setText(TYPE + mCourseList.get(position).getType());
        teacher = (TextView) convertView.findViewById(R.id.teacher);
        teacher.setText(TEACHER + mCourseList.get(position).getTeacher());
        return convertView;
    }


}
