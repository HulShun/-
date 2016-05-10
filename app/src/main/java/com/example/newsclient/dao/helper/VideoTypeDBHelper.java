package com.example.newsclient.dao.helper;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.newsclient.Configuration;

/**
 * Created by Administrator on 2016-05-09.
 */
public class VideoTypeDBHelper extends SQLiteOpenHelper {
    public static final String _ID = "_id";
    public static final String TERM = "term";
    public static final String LABEL = "label";
    public static final String LANG = "lang";
    public static final String KEY = "key";


    public VideoTypeDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public VideoTypeDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql1 = "create table " + Configuration.VIDEO_TYPE_TABLE_NAEM + " ( " +
                _ID + " integer primary key autoincrement , " +
                TERM + " char(10), " +
                LABEL + " char(10), " +
                LANG + " char(10) " +
                ")";
        db.execSQL(sql1);

        String sql2 = "create table " + Configuration.VIDEO_TYPE_SUBTITLE_TABLE_NAEM + " ( " +
                KEY + " char(10) primary key, " +
                TERM + " char(10), " +
                LABEL + " char(10), " +
                LANG + " char(10) " +
                ")";
        db.execSQL(sql2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("delete table" + Configuration.VIDEO_TYPE_TABLE_NAEM);
        db.execSQL("delete table" + Configuration.VIDEO_TYPE_SUBTITLE_TABLE_NAEM);
    }
}
