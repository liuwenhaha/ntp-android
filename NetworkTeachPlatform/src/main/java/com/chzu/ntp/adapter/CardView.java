package com.chzu.ntp.adapter;

/**
 * 课程类，代表一个课程信息预览，
 * 包括课程代码、名称、类型、课程老师
 */
public class CardView {
    private String code;
    private String name;
    private String type;
    private String teacher;

    public CardView(String code,String name, String type, String teacher) {
        this.code=code;
        this.name = name;
        this.type = type;
        this.teacher = teacher;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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
