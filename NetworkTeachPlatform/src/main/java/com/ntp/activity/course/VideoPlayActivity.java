package com.ntp.activity.course;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ntp.activity.R;

import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;

/**
 * 课程视频播放
 *
 * @author yanxing
 */
public class VideoPlayActivity extends Activity implements MediaPlayer.OnInfoListener, MediaPlayer.OnBufferingUpdateListener {
    private VideoView videoView;
    private ProgressBar pb;
    private TextView downloadRateView, loadRateView;//下载速度，加载百分比

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!io.vov.vitamio.LibsChecker.checkVitamioLibs(this)) {
            return;
        }
        setContentView(R.layout.activitity_video_play);
        pb = (ProgressBar) findViewById(R.id.probar);
        downloadRateView = (TextView) findViewById(R.id.download_rate);
        loadRateView = (TextView) findViewById(R.id.load_rate);
        String path = getIntent().getStringExtra("path");
        videoView = (VideoView) findViewById(R.id.surface_view);
        videoView.setVideoPath(path);
        videoView.setMediaController(new MediaController(this));
        videoView.setOnInfoListener(this);//异常、警告信息时调用，例如：开始缓冲、缓冲结束、下载速度变化
        videoView.setOnBufferingUpdateListener(this);//在网络视频流缓冲变化时调用
        videoView.requestFocus();
        videoView.setVideoLayout(2, 0);//画面拉伸显示
        //监听播放完成，关闭Activity
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                finish();
            }
        });

    }

    //提示信息
    @Override
    public boolean onInfo(MediaPlayer arg0, int what, int extra) {
        switch (what) {
            case MediaPlayer.MEDIA_INFO_BUFFERING_START://开始缓存，暂停播放
                if (videoView.isPlaying()) {
                    videoView.pause();
                }
                pb.setVisibility(View.VISIBLE);
                loadRateView.setText(videoView.getBufferPercentage() + "%");
                downloadRateView.setVisibility(View.VISIBLE);
                loadRateView.setVisibility(View.VISIBLE);
                break;
            case MediaPlayer.MEDIA_INFO_BUFFERING_END://缓存完成，继续播放
                videoView.start();
                pb.setVisibility(View.GONE);
                downloadRateView.setVisibility(View.GONE);
                loadRateView.setVisibility(View.GONE);
                break;
            case MediaPlayer.MEDIA_INFO_DOWNLOAD_RATE_CHANGED://显示下载速度
                downloadRateView.setText("" + extra + "kb/s" + "  ");
                break;
        }
        return true;
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        loadRateView.setText(percent + "%");
    }
}
