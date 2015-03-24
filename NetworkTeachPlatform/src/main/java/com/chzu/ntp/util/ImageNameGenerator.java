package com.chzu.ntp.util;

import android.util.Log;

import com.nostra13.universalimageloader.cache.disc.naming.FileNameGenerator;

/**
 * 自定义Universal Image Loader缓存图片命名规则
 *
 * @author yanxing
 */
public class ImageNameGenerator implements FileNameGenerator {
    private static String fileName;

   public ImageNameGenerator(String filename) {
        this.fileName = filename;
    }

    @Override
    public String generate(String imageUri) {
        return fileName;
    }
}
