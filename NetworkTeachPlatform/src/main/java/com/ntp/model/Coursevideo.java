package com.ntp.model;

/**
 * @author yanxing
 * 课程视频，listview上的一个item
 */
public class Coursevideo {
    private String id;
    private String name;
    private String path;
    private String size;
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

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    /**
     * @param name 视频显示的名称
     * @param path 视频路径
     * @param size  视频大小
     * @param imageId 播放图片
     */
    public Coursevideo(String name, String path,String size,int imageId) {
        this.imageId = imageId;
        this.size = size;
        this.name = name;
        this.path = path;
    }

    /**
     * @param name 视频显示的名称
     * @param path 视频路径
     * @param size  视频大小
     */
    public Coursevideo(String name, String path,String size) {
        this.size = size;
        this.name = name;
        this.path = path;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

}
