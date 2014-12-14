package com.chzu.ntp.dao;


import org.json.JSONArray;
import org.json.JSONObject;

/**
 * 对课程操作接口
 */
public interface CourseDao {

    /**
     * 根据课程id获取单个课程
     *
     * @param id 课程id
     * @return 课程json对象
     */
    public JSONObject getCourse(Integer id);

    /**
     * 分页获取课程
     *
     * @param pageSize    每页大小
     * @param currentPage 当前第几页
     */
    public JSONArray getAllCourse(int pageSize, int currentPage);
}
