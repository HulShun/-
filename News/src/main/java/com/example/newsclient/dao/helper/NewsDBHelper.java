package com.example.newsclient.dao.helper;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.newsclient.Configuration;

/**
 * Created by Administrator on 2016-04-14.
 */
public class NewsDBHelper extends SQLiteOpenHelper {
    public static final String _ID = "_id";
    public static final String TITLE = "title";
    public static final String ABSTRACTX = "abstractX";
    public static final String URL = "url";
    public static final String DATATIME = "datetime";
    public static final String IMG_URL = "img_url";
    public static final String KEYWORD = "keyword";
    public static final String PAGE = "page";
    public static final String HAS_MORE = "has_more";


    public NewsDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public NewsDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table " +
                Configuration.NEWS_TABLE_NAME + "(" +
                _ID + " INTEGER  primary key AUTOINCREMENT , " +
                TITLE + " varchar(50), " +
                ABSTRACTX + " varchar(100), " +
                URL + " varchar(100), " +
                DATATIME + " varchar(20), " +
                IMG_URL + " varchar(100), " +
                KEYWORD + " varchar(20), " +
                PAGE + " varchar(10), " +
                HAS_MORE + " varchar(5) " +
                ")";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("delete table " + Configuration.NEWS_TABLE_NAME);
        onCreate(db);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }
}
