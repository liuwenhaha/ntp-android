package com.chzu.ntp.model;

/**
 * @author yanxing
 * 课程视频，listview上的一个item
 */
public class Coursevideo {
    private String id;
    private String name;
    private int imageId;//图片资源id

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

    /**
     * @param id 视频id
     * @param name 视频名称
     * @param imageId 播放图片
     */
    public Coursevideo(String id, String name, int imageId) {
        this.id = id;
        this.name = name;
        this.imageId = imageId;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    @Override
    public String toString() {
        return "Coursevideo{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
