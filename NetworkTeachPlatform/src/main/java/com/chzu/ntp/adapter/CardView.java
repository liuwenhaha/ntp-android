package com.chzu.ntp.adapter;

/**
 * 课程类，代表一个课程信息预览，
 * 包括课程名称、类型、课程老师
 */
public class CardView {
    private String name;
    private String type;
    private String teacher;

    public CardView(String name, String type, String teacher) {
        this.name = name;
        this.type = type;
        this.teacher = teacher;
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
