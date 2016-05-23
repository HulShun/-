package com.example.newsclient.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.newsclient.Configuration;
import com.example.newsclient.Model.bean.video.VideoItemBean;
import com.example.newsclient.Model.bean.video.VideosInFormBean;
import com.example.newsclient.MyApplication;
import com.example.newsclient.dao.helper.VideoDBHelper;
import com.google.gson.Gson;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Administrator on 2016-04-14.
 */
public class VideoDao {

    private static VideoDao videoDao;
    private VideoDBHelper mHelper;
    private SQLiteDatabase mdb;

    //线程安全的Integer
    private AtomicInteger atomicInteger;


    public static VideoDao getInstance() {
        if (videoDao == null) {
            synchronized (VideoDao.class) {
                if (videoDao == null) {
                    videoDao = new VideoDao();
                }
            }
        }
        return videoDao;
    }

    private VideoDao() {
        Context context = MyApplication.getInstance();
        mHelper = new VideoDBHelper(context, Configuration.VIDEO_DB_NAEM, null, 1);
        atomicInteger = new AtomicInteger();
    }

    private ContentValues getContentValues(VideoItemBean data) {
        ContentValues cv = new ContentValues();

        cv.put(VideoDBHelper.VID, data.getId());
        //要到秒就好了
        cv.put(VideoDBHelper.TIME, System.currentTimeMillis() / 1000);
        Gson gson = new Gson();
        cv.put(VideoDBHelper.DATA, gson.toJson(data));
        return cv;
    }

    private ContentValues getVideosContentValues(String title, VideosInFormBean data) {
        ContentValues cv = new ContentValues();

        cv.put(VideoDBHelper.TITLE, title);
        cv.put(VideoDBHelper.PAGE, data.getPage());
        //要到秒就好了
        cv.put(VideoDBHelper.TIME, System.currentTimeMillis() / 1000);
        Gson gson = new Gson();
        cv.put(VideoDBHelper.DATA, gson.toJson(data));
        return cv;
    }


    public synchronized void insertVideoDetail(VideoItemBean data) {
        mdb = getDatabase();
        mdb.insert(Configuration.VIDEOITEM_TABLE_NAEM, null, getContentValues(data));
        closeDatabase();
    }

    public synchronized void insertVideos(String title, VideosInFormBean data) {
        mdb = getDatabase();
        mdb.insert(Configuration.VIDEOS_TABLE_NAEM, null, getVideosContentValues(title, data));
        closeDatabase();
    }


    public synchronized VideoItemBean query(String vid) {
        mdb = getDatabase();
        String[] columns = {
                VideoDBHelper.VID,
                VideoDBHelper.DATA,
                VideoDBHelper.TIME
        };

        String selection = VideoDBHelper.VID + "  = ? ";
        Cursor c = mdb.query(Configuration.VIDEOITEM_TABLE_NAEM,
                columns,
                selection,
                new String[]{vid},
                null,
                null,
                null);
        VideoItemBean data = null;
        if (c.moveToNext()) {
            Gson gson = new Gson();
            String json = c.getString(1);
            data = gson.fromJson(json, VideoItemBean.class);
            data.setUpdataTime(c.getString(2));
        }
        c.close();
        closeDatabase();

        return data;
    }

    public synchronized VideosInFormBean queryVideos(String title, String page) {
        mdb = getDatabase();
        String[] columns = {
                VideoDBHelper.TITLE,
                VideoDBHelper.PAGE,
                VideoDBHelper.DATA,
                VideoDBHelper.TIME
        };

        String selection = VideoDBHelper.TITLE + "  = ?  and " + VideoDBHelper.PAGE + " =? ";
        Cursor c = mdb.query(Configuration.VIDEOS_TABLE_NAEM,
                columns,
                selection,
                new String[]{title, page},
                null,
                null,
                null);
        VideosInFormBean data = null;
        if (c.moveToNext()) {
            Gson gson = new Gson();
            String json = c.getString(2);
            data = gson.fromJson(json, VideosInFormBean.class);
            data.setUpdateTime(c.getString(3));
        }
        c.close();
        closeDatabase();

        return data;
    }

    public synchronized int deleteVideoDetailByKey(String vid) {
        mdb = getDatabase();
        String selection = VideoDBHelper.VID + "  = ? ";
        int i = mdb.delete(Configuration.VIDEOITEM_TABLE_NAEM, selection, new String[]{vid});
        closeDatabase();
        return i;
    }

    public synchronized int deleteVideosByTitle(String title) {
        mdb = getDatabase();
        String selection = VideoDBHelper.TITLE + "  = ? ";
        int i = mdb.delete(Configuration.VIDEOS_TABLE_NAEM, selection, new String[]{title});
        closeDatabase();
        return i;
    }

    private synchronized SQLiteDatabase getDatabase() {
        if (atomicInteger.get() < 0) {
            atomicInteger.set(0);
        }
        //如果是自加之后才是1，就说明之前db没有被占用
        if (atomicInteger.incrementAndGet() == 1) {
            mdb = mHelper.getWritableDatabase();
        }
        return mdb;
    }


    private synchronized void closeDatabase() {
        //如果减到0了，就说明没有用户在连接数据库了
        if (atomicInteger.decrementAndGet() == 0) {
            mdb.close();
        }
    }
}
