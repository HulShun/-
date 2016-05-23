package com.example.newsclient.dao.helper;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.newsclient.Configuration;

/**
 * Created by Administrator on 2016-05-09.
 */
public class VideoDBHelper extends SQLiteOpenHelper {
    public static final String _ID = "_id";
    public static final String VID = "vid";
    public static final String DATA = "data";
    public static final String TIME = "time";

    public static final String TITLE = "title";
    public static final String PAGE = "page";

    public VideoDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public VideoDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        //视频列表
        String sql = "create table " + Configuration.VIDEOS_TABLE_NAEM + " ( " +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT , " +
                TITLE + " CHAR(10), " +
                PAGE + " CHAR(10), " +
                DATA + " TEXT, " +
                TIME + " CHAR(10) " +
                ")";
        db.execSQL(sql);

        //单个视频的详细信息
        String sql1 = "create table " + Configuration.VIDEOITEM_TABLE_NAEM + " ( " +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT , " +
                VID + " char(10), " +
                DATA + " TEXT, " +
                TIME + " char(10) " +
                ")";
        db.execSQL(sql1);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("delete table" + Configuration.VIDEOITEM_TABLE_NAEM);
        db.execSQL("delete table" + Configuration.VIDEOS_TABLE_NAEM);
        onCreate(db);
    }
}
