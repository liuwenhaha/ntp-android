package com.chzu.ntp.model;

/**
 * @author yanxing
 * 课程视频，listview上的一个item
 */
public class Coursevideo {
    private String id;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    /**
     * @param id
     * @param name 视频名称
     */
    public Coursevideo(String id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return "Coursevideo{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
