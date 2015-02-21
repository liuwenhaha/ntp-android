package com.chzu.ntp.model;

/**
 * 课程实体,接受后台json数据，封装成对象
 */
public class Course {
    private String code;//课程代码
    private String name;//名称
    private String type;//课程类型
    private String username;//代课老师

    public Course() {
    }

    /**
     * @param code     课程代码
     * @param name     课程名称
     * @param type     课程类型
     * @param username 老师姓名
     */
    public Course(String code, String name, String type, String username) {
        this.code = code;
        this.username = username;
        this.type = type;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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
                ", username='" + username + '\'' +
                '}';
    }
}
