package com.chzu.ntp.ui;

import android.app.Application;

import com.chzu.ntp.model.Course;

import java.util.ArrayList;
import java.util.List;

/**
 * 全局变量list<course>,主界面上拉刷新时，追加新数据，新的数据不缓存到数据库
 * 只在本次有效
 * @author yanxing 2015/3/31.
 */
public class GlobalVariable extends Application{

    private List<Course> list=new ArrayList<Course>();

    public List<Course> getList() {
        return list;
    }

    public void setList(List<Course> list) {
        this.list = list;
    }
}
