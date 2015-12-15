package com.ntp.model.gson;

import java.util.List;

/**
 * 课件
 * Created by lishuangxiang on 2015/12/15.
 */
public class CoursewareGson {


    /**
     * name : C语言课程进度表.doc
     * path : 6f3f3067-da0f-4c7b-aca0-b43d9fe10ba5.doc
     * size : 0.04M
     */

    private List<CoursewaresEntity> coursewares;

    public void setCoursewares(List<CoursewaresEntity> coursewares) {
        this.coursewares = coursewares;
    }

    public List<CoursewaresEntity> getCoursewares() {
        return coursewares;
    }

    public static class CoursewaresEntity {
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
