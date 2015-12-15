package com.ntp.model.gson;

import java.util.List;

/**
 * Created by lishuangxiang on 2015/12/15.
 */
public class CourseVideoGson {


    /**
     * name : 第一讲：C语言入门.mp4
     * path : cyuyanrumen.mp4
     * size : null
     */

    private List<VideosEntity> videos;

    public void setVideos(List<VideosEntity> videos) {
        this.videos = videos;
    }

    public List<VideosEntity> getVideos() {
        return videos;
    }

    public static class VideosEntity {
        private String name;
        private String path;
        private String size;

        public void setName(String name) {
            this.name = name;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public void setSize(String size) {
            this.size = size;
        }

        public String getName() {
            return name;
        }

        public String getPath() {
            return path;
        }

        public String getSize() {
            return size;
        }
    }
}
