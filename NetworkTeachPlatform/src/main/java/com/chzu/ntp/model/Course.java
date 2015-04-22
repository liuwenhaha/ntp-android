package com.chzu.ntp.model;

import android.graphics.Bitmap;

/**
 * 课程实体
 */
public class Course {

    private String code;//课程代码
    private String name;//名称
    private String imageUri;//课程图片路径
    private String type;//课程类型
    private String teacher;//代课老师


    public Course() {
    }

    /**
     * imageUri只存图片名称，不存前缀，例如"http://site.com/image.png"，
     * 只存image.png
     * @param code    课程代码
     * @param name    课程名称
     * @param imageUri 课程图片路径
     * @param type    课程类型
     * @param teacher 老师姓名
     */
    public Course(String code, String name, String imageUri,String type, String teacher) {
        this.code = code;
        this.teacher = teacher;
        this.type = type;
        this.name = name;
        this.imageUri = imageUri;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    @Override
    public String toString() {
        return "Course{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", teacher='" + teacher + '\'' +
                ", imageUri='" + imageUri + '\'' +
                '}';
    }
}
