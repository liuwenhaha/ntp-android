package com.ntp.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * 课件下载记录
 * @author yanxing
 */
public class DownloadHistoryDao {

    private static DBOpenHelper dbOpenHelper;
    private static SQLiteDatabase sqLiteDB;

    public DownloadHistoryDao(Context context){
        dbOpenHelper = new DBOpenHelper(context);
        sqLiteDB = dbOpenHelper.getWritableDatabase();
    }

    /**
     * 获取所有的下载记录
     * @return
     */
    public List<String> findAll(){

        Cursor cursor = sqLiteDB.rawQuery("select * from download_history", null);
        List<String> list = new ArrayList<String>();
        while (cursor.moveToNext()) {
            list.add(cursor.getString(1));
        }
        cursor.close();
        return list;
    }

    /**
     * 保存下载记录
     * @param fileName
     * @return
     */
    public boolean save(String fileName){
        Cursor cursor = sqLiteDB.rawQuery("select * from download_history where name=?", new String[]{fileName});
        if (cursor.getCount()>0){//已经存在
            cursor.close();
            return false;
        }
        ContentValues values = new ContentValues();
        values.put("name", fileName);
        sqLiteDB.insert("download_history", null, values);
        return true;
    }

    /**
     * 删除下载记录
     * @param fileName
     */
    public void delete(String fileName){
        sqLiteDB.delete("download_history", "name=?", new String[]{fileName});
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
