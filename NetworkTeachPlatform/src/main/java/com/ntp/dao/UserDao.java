package com.ntp.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ntp.model.User;

/**
 * @author yanxing
 */
public class UserDao {

    private static DBOpenHelper dbOpenHelper;
    private static SQLiteDatabase SQLiteDB;

    public UserDao(Context context) {
        dbOpenHelper = new DBOpenHelper(context);
        SQLiteDB = dbOpenHelper.getWritableDatabase();
    }

    /**
     * 保存用户信息
     */
    public void save(User user) {
        ContentValues values = new ContentValues();
        values.put("name", user.getUsername());
        values.put("email", user.getEmail());
        values.put("sex", user.getSex());
        if (user.getHead() != null) {
            values.put("head", user.getHead());
        }
        SQLiteDB.insert("user", null, values);
    }

    /**
     * 查询用户信息
     *
     * @param name
     */
    public User findByName(String name) {
        Cursor cursor = SQLiteDB.rawQuery("select * from user where name=?",
                new String[]{name});
        User user = new User();
        while (cursor.moveToNext()) {
            user.setUsername(cursor.getString(1));
            user.setEmail(cursor.getString(2));
            user.setSex(cursor.getString(3));
            user.setHead(cursor.getBlob(4));
        }
        cursor.close();
        return user;
    }

    /**
     * 更新用户信息
     */
    public void update(User user) {
        ContentValues values = new ContentValues();
        if (user.getEmail() != null) {
            values.put("email", user.getEmail());
        }
        if (user.getSex() != null) {
            values.put("sex", user.getSex());
        }
        if (user.getHead() != null) {
            values.put("head", user.getHead());
        }
        SQLiteDB.update("user", values, "name=?", new String[]{user.getUsername()});
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
