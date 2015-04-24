package com.ntp.util;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 工具类，在SD上数据存储操作
 */
public class SDCardUtil {
    private static final String SDPATH = Environment.getExternalStorageDirectory() + "/";
    private static final String TAG = "tip";

    /**
     * 获取SD卡根目录
     *
     * @return
     */
    public static String getSDPATH() {
        return SDPATH;
    }

    /**
     * 检查SD是否存在，存在返回true
     *
     * @return
     */
    public static boolean checkSDCard() {
        return android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
    }

    /**
     * 获取内置SD卡路径
     * @return
     */
    public static String getInnerSDCardPath() {
        return Environment.getExternalStorageDirectory().getPath();
    }

    /**
     * 在SD卡上创建文件
     *
     * @throws IOException
     */
    public static File creatSDFile(String fileName) throws IOException {
        File file = new File(SDPATH + fileName);
        if (checkSDCard()) {//SD存在
            file.createNewFile();
        } else {
            Log.i(TAG, "SD卡不存在");
        }
        return file;
    }

    /**
     * 在SD卡是否有文件
     * @param filePath 文件路径 ,不需要加mnt/sdcard
     * @return 存在 返回true 否则false
     */
    public static Boolean isExistSDFile(String filePath){
        File file = new File(SDPATH + filePath);
        if (checkSDCard()) {//SD存在
             return file.exists();
        } else {
            Log.i(TAG, "SD卡不存在");
        }
        return false;
    }

    /**
     * 在SD卡上创建目录
     *
     * @param dirName
     */
    public static File creatSDDir(String dirName) {
        File dir = new File(SDPATH + dirName);
        if (checkSDCard()) {//SD存在
            if (!dir.exists()) {
                Boolean bo=dir.mkdir();
                Log.i(TAG, bo.toString());
            }
        } else {
            Log.i(TAG, "SD卡不存在");
        }
        return dir;
    }

    /**
     * 在内置SD卡上创建目录
     *
     * @param dirName
     */
    public static File createInnerSDDir(String dirName) {
        File dir = new File(getInnerSDCardPath() + dirName);
        if (!dir.exists()) {
            Boolean bo = dir.mkdir();
            Log.i(TAG, bo.toString());
        }
        return dir;
    }

    /**
     * 将一个InputStream里面的数据写入到SD卡中
     *
     * @param path     路径名
     * @param fileName 文件名
     */
    public static File writeSDInput(String path, String fileName, InputStream input) {
        File file = null;
        OutputStream output = null;
        try {
            creatSDDir(path);
            file = creatSDFile(path + fileName);
            output = new FileOutputStream(file);
            byte buffer[] = new byte[4 * 1024];
            while ((input.read(buffer)) != -1) {
                output.write(buffer);
            }
            output.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                output.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return file;
    }
}
