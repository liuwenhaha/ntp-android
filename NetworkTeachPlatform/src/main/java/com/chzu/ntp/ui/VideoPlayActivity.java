package com.chzu.ntp.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;

/**
 * 课程视频播放
 * @author yanxing
 */
public class VideoPlayActivity extends Activity{
    private VideoView videoView;
    private static  final String path="http://www.modrails.com/videos/passenger_nginx.mov";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!io.vov.vitamio.LibsChecker.checkVitamioLibs(this)){
            return;
        }
        setContentView(R.layout.activitity_video_play);
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
