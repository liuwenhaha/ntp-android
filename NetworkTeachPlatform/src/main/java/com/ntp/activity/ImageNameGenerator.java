package com.ntp.activity;

import com.nostra13.universalimageloader.cache.disc.naming.FileNameGenerator;

/**
 * 自定义Universal Image Loader缓存图片命名规则
 *
 * @author yanxing
 */
public class ImageNameGenerator implements FileNameGenerator {

    @Override
    public String generate(String imageUri) {
        String image[]=imageUri.split("/");
        return image[image.length-1];//使用图片原名称命名缓存的图片
    }
}
