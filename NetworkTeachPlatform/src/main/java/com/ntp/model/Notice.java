package com.ntp.model;

/**
 * 消息界面（回复和作业），回复：图片、标题、时间
 * 作业：图片、标题、所属课程、时间
 * @author yanxing
 */
public class Notice {

    private int imageId;//图片id
    private String title;//标题
    private String content;//所属课程
    private String time;//时间

    /**
     * 回复消息
     * @param imageId 图片资源id
     * @param title 标题
     * @param time 回复时间
     */
    public Notice(int imageId,String title, String time) {
        this.title = title;
        this.imageId = imageId;
        this.time = time;
    }

    /**
     * 回复消息
     * @param title 标题
     * @param time 回复时间
     */
    public Notice(String title, String time) {
        this.title = title;
        this.time = time;
    }

    /**
     * 作业消息
     * @param imageId 图片资源id
     * @param title 标题
     * @param content 所属课程
     * @param time 作业布置时间
     */
    public Notice(int imageId, String title,String content, String time) {
        this.imageId = imageId;
        this.time = time;
        this.content = content;
        this.title = title;
    }

    /**
     * 作业消息
     * @param title 标题
     * @param content 所属课程
     * @param time 作业布置时间
     */
    public Notice(String title,String content, String time) {
        this.time = time;
        this.content = content;
        this.title = title;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
