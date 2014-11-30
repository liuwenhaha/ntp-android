package com.chzu.ntp.util;

/**
 * 卡片类，代表一个课程信息预览，
 * 包括课程图片预览、课程名称、类型、课程老师
 */
public class CardView {
    private int id;
    private String name;
    private String type;
    private String teacher;

    public CardView(int id, String name, String type, String teacher) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.teacher = teacher;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }
}
