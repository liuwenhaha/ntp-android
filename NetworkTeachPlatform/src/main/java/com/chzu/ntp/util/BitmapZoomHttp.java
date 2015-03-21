package com.chzu.ntp.util;

import android.graphics.Bitmap;
import android.graphics.Matrix;

/**
 * 图片缩放工具类
 *
 * @author yanxing
 */
public class BitmapZoomHttp {

    /**
     *
     * @param bitmap  原图片
     * @param newWidth  缩放后图片的宽度，单位像素
     * @param newHeight 缩放后图片的长度，单位像素
     * @return  返回缩放后的图片
     */
    public static Bitmap createBitmapZoop(Bitmap bitmap, int newWidth, int newHeight) {
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

}
