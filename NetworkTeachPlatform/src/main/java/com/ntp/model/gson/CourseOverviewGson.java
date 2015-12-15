package com.ntp.model.gson;

/**
 * 课程简介
 * Created by lishuangxiang on 2015/12/15.
 */
public class CourseOverviewGson {

    /**
     * overview : C语言是一门通用计算机编程语言，应用广泛。C语言的设计目标是提供一种能以简易的方式编译、处理低级存储器、产生少量的机器码以及不需要任何运行环境支持便能运行的编程语言
     */

    private CourseEntity course;

    public void setCourse(CourseEntity course) {
        this.course = course;
    }

    public CourseEntity getCourse() {
        return course;
    }

    public static class CourseEntity {
        private String overview;

        public void setOverview(String overview) {
            if (overview.equals("null")){
                this.overview="无";
            }else {
                this.overview = overview;
            }
        }

        public String getOverview() {
            return overview;
        }
    }
}
