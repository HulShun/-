package com.example.newsclient.dao.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.newsclient.Configuration;
import com.example.newsclient.Model.bean.video.VideoTypeBean;
import com.example.newsclient.MyApplication;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Administrator on 2016-04-14.
 */
public class VideoTypeDao {

    private static VideoTypeDao videoTypeDao;
    private VideoTypeDBHelper mHelper;
    private SQLiteDatabase mdb;

    //线程安全的Integer
    private AtomicInteger atomicInteger;


    public static VideoTypeDao getInstance() {
        if (videoTypeDao == null) {
            synchronized (VideoTypeDao.class) {
                if (videoTypeDao == null) {
                    videoTypeDao = new VideoTypeDao();
                }
            }
        }
        return videoTypeDao;
    }

    private VideoTypeDao() {
        Context context = MyApplication.getInstance();
        mHelper = new VideoTypeDBHelper(context, Configuration.VIDEO_TYPE_DB_NAEM, null, 1);
        atomicInteger = new AtomicInteger();
    }

    /**
     * 外层分类
     *
     * @param mainTypeBean
     * @return
     */
    private ContentValues getTypeValues(VideoTypeBean.VideoCategoriesBean mainTypeBean) {
        ContentValues cv = new ContentValues();
        cv.put(VideoTypeDBHelper.TERM, mainTypeBean.getTerm());
        cv.put(VideoTypeDBHelper.LABEL, mainTypeBean.getLabel());
        cv.put(VideoTypeDBHelper.LANG, mainTypeBean.getLang());
        return cv;
    }

    /**
     * 内层分类
     *
     * @param mainName
     * @param typeBean
     * @return
     */
    private ContentValues getTypeValues(String mainName, VideoTypeBean.VideoCategoriesBean.VideoGenresBean typeBean) {
        ContentValues cv = new ContentValues();
        cv.put(VideoTypeDBHelper.KEY, mainName);
        cv.put(VideoTypeDBHelper.TERM, typeBean.getTerm());
        cv.put(VideoTypeDBHelper.LABEL, typeBean.getLabel());
        cv.put(VideoTypeDBHelper.LANG, typeBean.getLang());
        return cv;
    }

    public synchronized void insert(VideoTypeBean.VideoCategoriesBean mainTpyeBean) {
        mdb = getDatabase();
        mdb.insert(Configuration.VIDEO_TYPE_TABLE_NAEM, null, getTypeValues(mainTpyeBean));
        if (mainTpyeBean.getGenres() != null || !mainTpyeBean.getGenres().isEmpty()) {
            for (VideoTypeBean.VideoCategoriesBean.VideoGenresBean bean : mainTpyeBean.getGenres()) {
                mdb.insert(Configuration.VIDEO_TYPE_SUBTITLE_TABLE_NAEM, null, getTypeValues(mainTpyeBean.getLabel(), bean));
            }
        }
        closeDatabase();
    }

    public synchronized void insert(List<VideoTypeBean.VideoCategoriesBean> list) {
        if (list == null) {
            return;
        }
        for (VideoTypeBean.VideoCategoriesBean bean : list) {
            insert(bean);
        }
        closeDatabase();
    }

    public synchronized List<VideoTypeBean.VideoCategoriesBean> query() {
        mdb = getDatabase();
        String[] columns = {
                VideoTypeDBHelper._ID,
                VideoTypeDBHelper.TERM,
                VideoTypeDBHelper.LABEL,
                VideoTypeDBHelper.LANG
        };
        //拿到所有的数据

        Cursor c = mdb.query(Configuration.VIDEO_TYPE_TABLE_NAEM,
                columns,
                null,
                null,
                null,
                null,
                VideoTypeDBHelper._ID);

        List<VideoTypeBean.VideoCategoriesBean> beans = new ArrayList<>();

        String selection2 = VideoTypeDBHelper.KEY + " = ? ";
        String[] columns2 = {
                VideoTypeDBHelper.TERM,
                VideoTypeDBHelper.LABEL,
                VideoTypeDBHelper.LANG
        };

        while (c.moveToNext()) {
            VideoTypeBean.VideoCategoriesBean mainbean = new VideoTypeBean.VideoCategoriesBean();
            mainbean.setTerm(c.getString(1));
            mainbean.setLabel(c.getString(2));
            mainbean.setLang(c.getString(3));

            Cursor c1 = mdb.query(Configuration.VIDEO_TYPE_SUBTITLE_TABLE_NAEM,
                    columns2,
                    selection2,
                    new String[]{mainbean.getLabel()},
                    null,
                    null,
                    null);
            List<VideoTypeBean.VideoCategoriesBean.VideoGenresBean> typeList = new ArrayList<>();
            while (c1.moveToNext()) {
                VideoTypeBean.VideoCategoriesBean.VideoGenresBean typeBean = new VideoTypeBean.VideoCategoriesBean.VideoGenresBean();
                typeBean.setTerm(c1.getString(0));
                typeBean.setLabel(c1.getString(1));
                typeBean.setLang(c1.getString(2));
                typeList.add(typeBean);
            }
            mainbean.setGenres(typeList);
            beans.add(mainbean);
        }
        c.close();
        closeDatabase();
        return beans;
    }


    public synchronized int deleteAll() {
        mdb = getDatabase();
        int i = mdb.delete(Configuration.VIDEO_TYPE_TABLE_NAEM, null, null);
        int m = mdb.delete(Configuration.VIDEO_TYPE_SUBTITLE_TABLE_NAEM, null, null);
        closeDatabase();
        return i;
    }


    private synchronized SQLiteDatabase getDatabase() {
        if (atomicInteger.get() < 0) {
            atomicInteger.set(0);
        }
        //如果是自加之后才是1，就说明之前db没有被占用
        if (atomicInteger.incrementAndGet() <= 1) {
            mdb = mHelper.getWritableDatabase();
        }
        return mdb;
    }


    private synchronized void closeDatabase() {
        //如果减到0了，就说明没有用户在连接数据库了
        if (atomicInteger.decrementAndGet() >= 0) {
            mdb.close();
        }
    }
}
