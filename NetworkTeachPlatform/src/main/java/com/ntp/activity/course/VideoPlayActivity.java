package com.ntp.activity.course;

import android.app.Activity;
import android.os.Bundle;

import com.ntp.activity.R;

import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;

/**
 * 课程视频播放
 * @author yanxing
 */
public class VideoPlayActivity extends Activity{
    private VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!io.vov.vitamio.LibsChecker.checkVitamioLibs(this)){
            return;
        }
        setContentView(R.layout.activitity_video_play);
        String path=getIntent().getStringExtra("path");
        videoView= (VideoView) findViewById(R.id.surface_view);
        videoView.setVideoPath(path);
        videoView.setMediaController(new MediaController(this));
        videoView.requestFocus();
        videoView.setVideoLayout(2,0);//画面拉伸显示
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
            }
        });

    }
}
