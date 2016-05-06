package com.example.newsclient.dao.helper;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.newsclient.Configuration;

/**
 * Created by Administrator on 2016-05-06.
 */
public class ImageTypeHelper extends SQLiteOpenHelper {
    public static final String _ID = "_id";
    public static final String MAINNAME = "mainName";
    public static final String NAME = "name";
    public static final String ID = "id";


    public ImageTypeHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public ImageTypeHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table " +
                Configuration.IMAGE_MAIN_TYPE_TABLE_NAEM + " ( " +
                _ID + " INTEGER  primary key AUTOINCREMENT, " +
                MAINNAME + " varchar(50) " +
                ")";
        db.execSQL(sql);

        String sql2 = "create table " +
                Configuration.IMAGE_TYPE_TABLE_NAEM + " ( " +
                _ID + " INTEGER  primary key AUTOINCREMENT, " +
                ID + " INTEGER, " +
                NAME + " varchar(50), " +
                MAINNAME + " varchar(50) " +
                ")";
        db.execSQL(sql2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("delete table " + Configuration.IMAGE_MAIN_TYPE_TABLE_NAEM);
        db.execSQL("delete table " + Configuration.IMAGE_TYPE_TABLE_NAEM);
        onCreate(db);
    }
}
