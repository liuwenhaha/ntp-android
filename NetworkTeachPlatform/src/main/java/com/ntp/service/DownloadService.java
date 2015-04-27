package com.ntp.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.ntp.activity.R;
import com.ntp.activity.course.CoursewareFragment;
import com.ntp.dao.PathConstant;
import com.ntp.util.SDCardUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * 下载服务，异步处理
 */
public class DownloadService extends IntentService {
    //下载动作
    private static final String ACTION_DOWNLOAD = "com.ntp.action.download";

    private static final String EXTRA_PARAM1 = "com.ntp.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "com.ntp.extra.PARAM2";
    private static final String EXTRA_PARAM3 = "com.ntp.extra.PARAM3";
    private static final String TAG = "DownLoadService";
    private File saveFile;// 下载的数据保存到的文件
    private boolean downloadPause;//暂停下载标识
    private int fileSize;//文件大小
    private int downloadLength;//已经下载的文件大小
    private String fileName;//文件名
    private static int id=1;
    private NotificationManager manager;
    private NotificationCompat.Builder builder;

    /**
     * 下载文件
     *
     * @param downloadPath 文件下载路径
     * @param newFileName  文件新名称。如果为null，将采用URL文件名称
     * @param fileSaveDir  下载保存路径保存
     */
    public static void startActionDownload(Context context, String downloadPath, String newFileName, String fileSaveDir) {
        Intent intent = new Intent(context, DownloadService.class);
        intent.setAction(ACTION_DOWNLOAD);
        intent.putExtra(EXTRA_PARAM1, downloadPath);
        intent.putExtra(EXTRA_PARAM2, newFileName);
        intent.putExtra(EXTRA_PARAM3, fileSaveDir);
        context.startService(intent);
    }

    public DownloadService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_DOWNLOAD.equals(action)) {
                id+=1;
                final String downloadPath = intent.getStringExtra(EXTRA_PARAM1);
                final String newFileName = intent.getStringExtra(EXTRA_PARAM2);
                final String fileSaveDir = intent.getStringExtra(EXTRA_PARAM3);
                handllerActionDownload(downloadPath, newFileName, fileSaveDir);
            }
        }
    }

    /**
     * @param downloadPath 文件下载路径
     * @param newFileName  文件新名称。如果为null，将采用URL文件名称
     * @param fileSaveDir  下载保存路径保存
     */
    private void handllerActionDownload(String downloadPath, String newFileName, String fileSaveDir) {
        if (!SDCardUtil.checkSDCard()) {
            Log.i(TAG, "SD不存在");
            return;
        }
        try {
            // 获取通知服务
            manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            builder = new NotificationCompat.Builder(
                    getApplicationContext());
            URL url = new URL(downloadPath);// 根据下载路径实例化URL
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5 * 1000);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Charset", "UTF-8");
            conn.setRequestProperty("Connection", "Keep-Alive");
            if (conn.getResponseCode() == 200) {//响应成功
                fileSize = conn.getContentLength();// 根据相应获取文件大小
                fileName = newFileName == null ? downloadPath.substring(downloadPath
                        .lastIndexOf('/') + 1) : newFileName;
                saveFile = new File(fileSaveDir, fileName);
                Log.i(TAG, saveFile.toString());
                InputStream inStream = conn.getInputStream();// 获取远程连接的输入流
                byte[] buffer = new byte[1024];
                int offset = 0;
                RandomAccessFile randomAccessFile = new RandomAccessFile(
                        this.saveFile, "rwd");
                randomAccessFile.seek(0);
                // 当用户没有要求停止下载，同时没有到达请求数据的末尾时候会一直循环读取数据
                while (!isDownloadPause()
                        && (offset = inStream.read(buffer, 0, 1024)) != -1) {
                    randomAccessFile.write(buffer, 0, offset);
                    Log.i(TAG, randomAccessFile.getFilePointer() + "");
                    downloadLength += offset;
                    // 发送进度广播
                    Intent sendIntent = new Intent(CoursewareFragment.ACTION_UPDATE);
                    //下载进度
                    sendIntent.putExtra("fileSize",fileSize);
                    sendIntent.putExtra("downloadLength",downloadLength);
                    Log.i(TAG,"DownloadService id="+id);
                    sendIntent.putExtra("notificationId",id);
                    sendBroadcast(sendIntent);
                    showProgressBar(fileSize,downloadLength);
                }
                // 发送下载完成广播
                Intent sendIntent = new Intent(CoursewareFragment.ACTION_FINISH);
                sendIntent.putExtra("success", true);
                sendBroadcast(sendIntent);
                randomAccessFile.close();
                inStream.close();

            } else {
                Log.i(TAG, "服务器响应异常");
            }


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 通知栏显示下载进度
     * @param fileSize 文件总大小
     * @param downloadLength 当前下载大小
     */
    private void showProgressBar(final int  fileSize, final int downloadLength) {
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.ic_launcher)).setSmallIcon(R.drawable.download)
                .setTicker(fileName+"开始下载...").setContentInfo((downloadLength * 100 / fileSize) + "%")
                .setOngoing(true).setContentTitle("ntp")
                .setContentText(fileName+"正在下载").setAutoCancel(true)
                //点击通知栏取消
                .setContentIntent(PendingIntent.getActivity(getApplicationContext(), 0, new Intent(), 0));
        if (fileSize==downloadLength){//下载完成
            builder.setContentText(fileName+"下载完成");
        }
        builder.setProgress(fileSize, downloadLength, false);
        manager.notify(id, builder.build());
    }


    /**
     * 获取当前下载状态
     *
     * @return true 暂停，false不暂停
     */
    public boolean isDownloadPause() {
        return downloadPause;
    }

    /**
     * 设置暂停下载，false不暂停，true暂停
     *
     * @param downloadPause
     */
    public void setDownloadPause(boolean downloadPause) {
        this.downloadPause = downloadPause;
    }
}
