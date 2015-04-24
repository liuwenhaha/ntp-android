package com.ntp.util;

import android.graphics.Bitmap;
import android.graphics.Matrix;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * 图片工具类
 *
 * @author yanxing
 */
public class BitmapUtil {

    /**
     * 缩放图片
     * @param bitmap  原图片
     * @param newWidth  缩放后图片的宽度，单位像素
     * @param newHeight 缩放后图片的长度，单位像素
     * @return  返回缩放后的图片
     */
    public static Bitmap createBitmapZoop(Bitmap bitmap, int newWidth, int newHeight) {
        if (bitmap == null) {
            return bitmap;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        // 设置想要的大小
        // 计算缩放比例
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 返回新的图片
        return Bitmap.createBitmap(bitmap, 0, 0, width, height,
                matrix, true);
    }

    /**
     * 将图片转换为字节数组,如果图片大于500kb，将压缩
     *
     * @param bitmap 图片对象
     * @return 该图片的字节数组数据
     */
    public static byte[] getBitmapByte(Bitmap bitmap) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
        try {
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return out.toByteArray();
    }
}
