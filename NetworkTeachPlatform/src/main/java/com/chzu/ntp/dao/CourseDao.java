package com.chzu.ntp.dao;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.chzu.ntp.model.Course;

import java.util.ArrayList;
import java.util.List;

/**
 * 对课程操作,缓存课程信息，最多缓存20条
 */
public class CourseDao {
    private static DBOpenHelper dbOpenHelper;
    private static SQLiteDatabase SQLiteDB;

    public CourseDao(Context context) {
        dbOpenHelper = new DBOpenHelper(context);
        SQLiteDB = dbOpenHelper.getWritableDatabase();
    }

    /**
     * 获取所有课程信息
     */
    public List<Course> getAllCourse() {
        Cursor cursor = SQLiteDB.rawQuery("select * from course_table", null);
        List<Course> list = new ArrayList<Course>();
        while (cursor.moveToNext()) {
            Course course = new Course(null, cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4));
            list.add(course);
        }
        cursor.close();
        return list;
    }

    /**
     * 保存课程信息
     */
    public void save(Course course) {
        ContentValues values = new ContentValues();
        values.put("name", course.getName());
        values.put("code", course.getCode());
        values.put("type", course.getType());
        values.put("username", course.getTeacher());
        SQLiteDB.insert("course_table", null, values);
    }

    /**
     * 清空course_table
     */
    public void delete() {
        SQLiteDB.delete("course_table", null, null);
    }

    /**
     * 关闭数据库连接
     */
    public void close() {
        if (dbOpenHelper != null) {
            dbOpenHelper.close();
            dbOpenHelper = null;
        }
        if (SQLiteDB != null) {
            SQLiteDB.close();
            SQLiteDB = null;
        }
    }

}
