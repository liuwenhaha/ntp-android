package com.chzu.ntp.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * SQLLite管理器，实现创建数据库和表
 */
public class DBOpenHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;// 设置数据库的版本
    private static final String DBNAME = "ntp.db";// 数据库名称

    public DBOpenHelper(Context context) {
        super(context, DBNAME, null, VERSION);
    }

    // 第一次创建数据库调用
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "";
        db.execSQL(sql);
    }

    // 更新数据库，当版本变化时系统会调用该回调方法
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "drop table if exists contact";
        db.execSQL(sql);
        onCreate(db);// 重新创建数据库表,也可以自己根据业务需要创建新的数据表
    }


}
