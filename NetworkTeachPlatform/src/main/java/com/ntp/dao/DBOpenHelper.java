package com.ntp.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * SQLLite管理器，实现创建数据库和表
 */
public class DBOpenHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;// 设置数据库的版本
    private static final String DB_NAME = "ntp.db";// 数据库名称

    public DBOpenHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    // 第一次创建数据库调用
    @Override
    public void onCreate(SQLiteDatabase db) {
        //课程表
        String sql = "create table course_table(_id integer primary key,code varchar(20),name varchar(20),imageUri varchar(20),type varchar(20),username varchar(20))";
        //课程类型表
        String sqlType = "create table coursetype_table(_id integer primary key,type varchar(20))";
        //搜索历史
        String sqlSearch = "create table search_history(_id integer primary key,content varchar(20))";
        //缓存用户信息
        String sqlUser = "create table user(_id integer primary key,name varchar(20),head blob)";
        //下载课件记录表
        String sqlDownload = "create table download_history(_id integer primary key,name varchar(20))";
        db.execSQL(sql);
        db.execSQL(sqlType);
        db.execSQL(sqlSearch);
        db.execSQL(sqlUser);
        db.execSQL(sqlDownload);
    }

    // 更新数据库，当版本变化时系统会调用该回调方法
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "drop table if exists course_table";
        db.execSQL(sql);
        onCreate(db);// 重新创建数据库表,也可以自己根据业务需要创建新的数据表
    }


}
