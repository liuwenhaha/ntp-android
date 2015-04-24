package com.ntp.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


import java.util.ArrayList;
import java.util.List;

/**
 * 对课程类型操作，缓存所有的课程类型到数据库
 * Created by yanxing on 2015/2/22.
 */
public class CourseTypeDao {

    private static DBOpenHelper dbOpenHelper;
    private static SQLiteDatabase SQLiteDB;

    public CourseTypeDao(Context context) {
        dbOpenHelper = new DBOpenHelper(context);
        SQLiteDB = dbOpenHelper.getWritableDatabase();
    }

    /**
     * 获取所有课程类型名称
     */
    public String[] getAllCourseType() {
        Cursor cursor = SQLiteDB.rawQuery("select * from coursetype_table", null);
        List<String> list = new ArrayList<String>();
        Log.i("TAG", String.valueOf(cursor.getCount()));
        while (cursor.moveToNext()) {
            list.add(cursor.getString(1));
        }
        String st[] = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            st[i] = list.get(i);
        }
        cursor.close();
        return st;
    }

    /**
     * 保存课程类型
     */
    public void save(String type) {
        ContentValues values = new ContentValues();
        values.put("type", type);
        SQLiteDB.insert("coursetype_table", null, values);
    }

    /**
     * 清空coursetype_table
     */
    public void delete() {
        SQLiteDB.delete("coursetype_table", null, null);
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
