package com.chzu.ntp.model;

import android.graphics.Bitmap;

/**
 * 课程实体
 */
public class Course {
    private Bitmap bitmap;//课程图片
    private String code;//课程代码
    private String name;//名称
    private String type;//课程类型
    private String teacher;//代课老师

    public Course() {
    }

    /**
     * 不包括课程图片
     * @param code    课程代码
     * @param name    课程名称
     * @param type    课程类型
     * @param teacher 老师姓名
     */
    public Course(String code, String name, String type, String teacher) {
        this.code = code;
        this.teacher = teacher;
        this.type = type;
        this.name = name;
    }


    /**
     * @param bitmap  课程图片，
     * @param code    课程代码
     * @param name    课程名称
     * @param type    课程类型
     * @param teacher 老师姓名
     */
    public Course(Bitmap bitmap, String code, String name, String type, String teacher) {
        this.bitmap = bitmap;
        this.code = code;
        this.name = name;
        this.type = type;
        this.teacher = teacher;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
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

    @Override
    public String toString() {
        return "Course{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", teacher='" + teacher + '\'' +
                '}';
    }
}
