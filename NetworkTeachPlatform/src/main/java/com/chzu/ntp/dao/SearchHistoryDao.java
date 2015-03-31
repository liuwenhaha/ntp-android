package com.chzu.ntp.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.chzu.ntp.model.SearchHistory;

import java.util.ArrayList;
import java.util.List;

/**
 * 课程搜索历史dao
 *
 * @author yanxing
 */
public class SearchHistoryDao {
    private DBOpenHelper dbOpenHelper;
    private SQLiteDatabase sqLiteDatabase;

    public SearchHistoryDao(Context context) {
        dbOpenHelper = new DBOpenHelper(context);
        sqLiteDatabase = dbOpenHelper.getWritableDatabase();
    }

    /**
     * 保存搜索内容
     *
     * @param content
     * @return 如果数据库中已经存在，则不保存，返回false;否则保存返回true
     */
    public Boolean save(String content) {
        if (isExist(content)) {
            return false;
        } else {
            ContentValues values = new ContentValues();
            values.put("content", content);
            sqLiteDatabase.insert("search_history", null, values);
            return true;
        }
    }

    /**
     * 查询搜索内容是否已经存在，精准查找
     *
     * @param content
     * @return 存在返回true, 否则返回false
     */
    public boolean isExist(String content) {
        Cursor cursor = sqLiteDatabase.rawQuery("select * from search_history where content=?", new String[]{content});
        return cursor.getCount() > 0 ? true : false;
    }

    /**
     * 获取搜索历史
     */
    public String[] findAll() {
        Cursor cursor = sqLiteDatabase.rawQuery("select * from search_history", null);
        List<String> list = new ArrayList<String>();
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
     * 清空search_history
     */
    public void delete() {
        sqLiteDatabase.delete("search_history", null, null);
    }

    /**
     * 关闭数据库连接
     */
    public void close() {
        if (dbOpenHelper != null) {
            dbOpenHelper.close();
            dbOpenHelper = null;
        }
        if (sqLiteDatabase != null) {
            sqLiteDatabase.close();
            sqLiteDatabase = null;
        }
    }

}
