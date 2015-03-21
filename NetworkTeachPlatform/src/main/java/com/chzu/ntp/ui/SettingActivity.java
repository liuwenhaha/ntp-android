package com.chzu.ntp.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chzu.ntp.dao.CourseDao;
import com.chzu.ntp.dao.CourseTypeDao;
import com.chzu.ntp.util.ExitListApplication;
import com.chzu.ntp.widget.MyDialog;
import com.chzu.ntp.widget.MyTitleView;
import com.chzu.ntp.util.PreferenceUtil;

import java.io.File;

/**
 * 软件设置
 */
public class SettingActivity extends Activity implements View.OnClickListener {

    private ImageView back;//退出
    private ImageView switchImg;//用2G3G4G网络播放视频和下载课件开关图片
    private TextView about, exit;//关于、退出
    private RelativeLayout cache;
    private TextView cacheText;
    private CourseTypeDao courseTypeDao;
    private CourseDao courseDao;
    private MyTitleView myTitleView;
    private final static String  TIP="将删除所有缓存的课程信息";
    /**
     * 清除缓存对话框请求码
     */
    private final static int REQUEST=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        back = (ImageView) findViewById(R.id.back);
        switchImg = (ImageView) findViewById(R.id.switchImg);
        about = (TextView) findViewById(R.id.about);
        exit = (TextView) findViewById(R.id.exit);
        cache= (RelativeLayout) findViewById(R.id.cache);
        cacheText= (TextView) findViewById(R.id.cache_text);
        myTitleView= (MyTitleView) findViewById(R.id.myTitleView);
        myTitleView.setTitle("设置");
        back.setOnClickListener(this);
        switchImg.setOnClickListener(this);
        about.setOnClickListener(this);
        exit.setOnClickListener(this);
        cache.setOnClickListener(this);
        courseTypeDao=new CourseTypeDao(getApplicationContext());
        courseDao=new CourseDao(getApplicationContext());
        if (PreferenceUtil.getConfig(getApplicationContext())) {//可以使用移动网络播放视频、下载课件
            switchImg.setImageDrawable(getResources().getDrawable(R.drawable.switch_on));
        } else {
            switchImg.setImageDrawable(getResources().getDrawable(R.drawable.switch_off));
        }
        cacheText.setText(getCache()+"KB");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back://退出
                finish();
                break;
            case R.id.switchImg://设置用2G3G4G网络播放视频和下载课件开关
                if (PreferenceUtil.getConfig(getApplicationContext())) {
                    switchImg.setImageDrawable(getResources().getDrawable(R.drawable.switch_off));
                    PreferenceUtil.saveConfig(getApplicationContext(), false);
                } else {
                    switchImg.setImageDrawable(getResources().getDrawable(R.drawable.switch_on));
                    PreferenceUtil.saveConfig(getApplicationContext(), true);
                }
                break;
            case R.id.cache://清除缓存
                Intent intent=new Intent(getApplicationContext(), MyDialog.class);
                Bundle bundle=new Bundle();
                bundle.putString("tip",TIP);
                intent.putExtras(bundle);
                startActivityForResult(intent,REQUEST);
                break;
            case R.id.about://关于
                Intent intent1 = new Intent(this, AboutActivity.class);
                startActivity(intent1);
                break;
            case R.id.exit://退出
                ExitListApplication.getInstance().exit();
                break;
        }
    }

    /**
     * 获取应用本地缓存大小
     */
    public String getCache(){
        String path="/data/data/"+getApplicationContext().getPackageName()+"/databases";
        String st="0";
        //如果数据库有数据
        if(courseDao.getAllCourse().size()>0){
            File file=new File(path);
            File[] files=file.listFiles();
            double sizeInit=0;
            for (int i=0;i<files.length;i++){
                sizeInit+=files[i].length();
            }
            double size=sizeInit/1024;//以kb为单位
            st=String.valueOf(size).substring(0,String.valueOf(size).indexOf("."));
        }

        return st;
    }

    @Override
    protected void onDestroy() {
        courseDao.close();
        courseTypeDao.close();
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==REQUEST&&resultCode==RESULT_OK){//用户选择确定时
            courseDao.delete();
            courseTypeDao.delete();
            cacheText.setText("0KB");
            Toast.makeText(getApplicationContext(),"清除成功",Toast.LENGTH_LONG).show();
        }
    }
}
