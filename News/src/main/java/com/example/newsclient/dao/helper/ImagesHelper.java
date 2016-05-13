package com.example.newsclient.dao.helper;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.newsclient.Configuration;

/**
 * Created by Administrator on 2016-05-06.
 */
public class ImagesHelper extends SQLiteOpenHelper {
    public static final String _ID = "_id";
    public static final String MAINNAME = "mainName";
    public static final String NAME = "name";
    public static final String ID = "id";

    public static final String TYPE = "type";
    public static final String TYPENAME = "typeName";
    public static final String CT = "ct";
    public static final String TITLE = "title";
    public static final String ITEMID = "itemid";
    public static final String CURRENTPAGE = "currentPage";

    public static final String SMALL = "small";
    public static final String MIDDLE = "middle";
    public static final String BIG = "BIG";

    public ImagesHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public ImagesHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //图片主分类表
        String sql = "create table " +
                Configuration.IMAGE_MAIN_TYPE_TABLE_NAEM + " ( " +
                _ID + " INTEGER  primary key AUTOINCREMENT, " +
                MAINNAME + " varchar(50) " +
                ")";
        db.execSQL(sql);

        //图片二级分类表
        String sql2 = "create table " +
                Configuration.IMAGE_TYPE_TABLE_NAEM + " ( " +
                _ID + " INTEGER  primary key AUTOINCREMENT, " +
                ID + " INTEGER, " +
                NAME + " varchar(50), " +
                MAINNAME + " varchar(50) " +
                ")";
        db.execSQL(sql2);

        //图片分页信息表
        String sql3 = "create table " +
                Configuration.IMAGE_INFORM_TABLE_NAEM + " ( " +
                TYPE + " INTEGER, " +
                TYPENAME + "  varchar(50), " +
                CT + " varchar(50), " +
                TITLE + " varchar(50), " +
                ITEMID + " varchar(15), " +
                CURRENTPAGE + " INTEGER " +
                ")";
        db.execSQL(sql3);

        //图片表
        String sql4 = "create table " +
                Configuration.IMAGE_TABLE_NAEM + " ( " +
                ITEMID + " varchar(15) , " +
                SMALL + "  varchar(50), " +
                MIDDLE + " varchar(50), " +
                BIG + " varchar(50) " +
                ")";
        db.execSQL(sql4);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("delete table " + Configuration.IMAGE_MAIN_TYPE_TABLE_NAEM);
        db.execSQL("delete table " + Configuration.IMAGE_TYPE_TABLE_NAEM);
        db.execSQL("delete table " + Configuration.IMAGE_INFORM_TABLE_NAEM);
        onCreate(db);
    }
}
