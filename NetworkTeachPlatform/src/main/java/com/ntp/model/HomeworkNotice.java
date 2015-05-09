package com.ntp.model;

/**
 * 作业消息：图片、标题、所属课程、时间
 * @author yanxing
 */
public class HomeworkNotice {

    private int imageId;//图片id
    private String id;
    private String title;//标题
    private String content;//所属课程
    private String time;//时间


    /**
     * 作业消息
     * @param imageId 图片资源id
     * @param title 标题
     * @param content 所属课程
     * @param time 作业布置时间
     */
    public HomeworkNotice(int imageId, String id, String title, String content, String time) {
        this.id=id;
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
    public HomeworkNotice(String id, String title, String content, String time) {
        this.id=id;
        this.time = time;
        this.content = content;
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
