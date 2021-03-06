package com.ntp.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.ntp.model.User;

/**
 * 用户
 * @author yanxing
 */
public class UserDao {

    private static DBOpenHelper dbOpenHelper;
    private static SQLiteDatabase sqLiteDB;

    public UserDao(Context context) {
        dbOpenHelper = new DBOpenHelper(context);
        sqLiteDB = dbOpenHelper.getWritableDatabase();
    }

    /**
     * 保存用户信息
     */
    public void save(User user) {
        ContentValues values = new ContentValues();
        values.put("name", user.getUsername());
        if (user.getHead() != null) {
            values.put("head", user.getHead());
        }
        sqLiteDB.insert("user", null, values);
    }

    /**
     * 查询用户信息
     *
     * @param name
     */
    public User findByName(String name) {
        Cursor cursor = sqLiteDB.rawQuery("select * from user where name=?",
                new String[]{name});
        if (cursor.getCount()<=0){
            return null;
        }
        User user = new User();
        while (cursor.moveToNext()) {
            user.setUsername(cursor.getString(1));
            user.setHead(cursor.getBlob(2));
        }
        cursor.close();
        return user;
    }

    /**
     * 更新用户信息
     */
    public void update(User user) {
        ContentValues values = new ContentValues();
        if (user.getHead() != null) {
            values.put("head", user.getHead());
        }
        sqLiteDB.update("user", values, "name=?", new String[]{user.getUsername()});
    }

    /**
     * 关闭数据库连接
     */
    public void close() {
        if (dbOpenHelper != null) {
            dbOpenHelper.close();
        }
        if (sqLiteDB != null) {
            sqLiteDB.close();
        }
    }
}
