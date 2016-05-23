package com.example.newsclient.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.newsclient.Configuration;
import com.example.newsclient.Model.bean.NewsBean;
import com.example.newsclient.Model.bean.NewsListBean;
import com.example.newsclient.MyApplication;
import com.example.newsclient.dao.helper.NewsDBHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Administrator on 2016-04-14.
 */
public class NewsDao {

    private static NewsDao newsDao;
    private NewsDBHelper mHelper;
    private SQLiteDatabase mdb;

    //线程安全的Integer
    private AtomicInteger atomicInteger;


    public static NewsDao getInstance() {
        if (newsDao == null) {
            synchronized (NewsDao.class) {
                if (newsDao == null) {
                    newsDao = new NewsDao();
                }
            }
        }
        return newsDao;
    }

    private NewsDao() {
        Context context = MyApplication.getInstance();
        mHelper = new NewsDBHelper(context, Configuration.NEWS_DB_NAME, null, 1);
        atomicInteger = new AtomicInteger();
    }

    private ContentValues getContentValues(Map<String, String> params, NewsBean newsBean) {
        ContentValues cv = new ContentValues();
        String page = params.get("page");
        String keyword = params.get("keyword");
        String has_more = params.get("has_more");

        cv.put("page", page);
        cv.put("keyword", keyword);
        cv.put("has_more", has_more);

        cv.put("title", newsBean.getTitle());
        cv.put("abstractX", newsBean.getAbstractX());
        cv.put("datetime", newsBean.getDatetime());
        cv.put("url", newsBean.getUrl());
        cv.put("img_url", newsBean.getImg_url());
        return cv;
    }

    public synchronized void insert(Map<String, String> params, NewsBean newsBean) {
        mdb = getDatabase();
        mdb.insert(Configuration.NEWS_TABLE_NAME, null, getContentValues(params, newsBean));
        closeDatabase();
    }

    public synchronized void insert(Map<String, String> params, List<NewsBean> list) {
        if (list == null) {
            return;
        }
        mdb = getDatabase();
        for (NewsBean bean : list) {
            mdb.insert(Configuration.NEWS_TABLE_NAME, null, getContentValues(params, bean));
        }
        closeDatabase();
    }

    public synchronized NewsListBean query(Map<String, String> params) {
        mdb = getDatabase();
        String[] columns = {
                NewsDBHelper.TITLE,
                NewsDBHelper.ABSTRACTX,
                NewsDBHelper.DATATIME,
                NewsDBHelper.URL,
                NewsDBHelper.IMG_URL,
                NewsDBHelper.HAS_MORE
        };

        String selection = " keyword = ? and page = ?";
        Cursor c = mdb.query(Configuration.NEWS_TABLE_NAME,
                columns,
                selection,
                new String[]{params.get("keyword"), params.get("page")},
                null,
                null,
                NewsDBHelper._ID);

        List<NewsBean> beans = new ArrayList<>();
        NewsListBean.RetDataBean bean1 = new NewsListBean.RetDataBean();
        while (c.moveToNext()) {
            NewsBean bean = new NewsBean();
            bean.setTitle(c.getString(0));
            bean.setAbstractX(c.getString(1));
            bean.setDatetime(c.getString(2));
            bean.setUrl(c.getString(3));
            bean.setImg_url(c.getString(4));
            beans.add(bean);

            bean1.setHas_more(Integer.valueOf(c.getString(5)));
        }
        c.close();
        closeDatabase();


        bean1.setData(beans);

        NewsListBean list = new NewsListBean();
        list.setRetData(bean1);

        return list;
    }


    public synchronized int deleteByKey(String keyword) {
        mdb = getDatabase();
        String selection = " keyword = ? ";
        int i = mdb.delete(Configuration.NEWS_TABLE_NAME, selection, new String[]{keyword});
        closeDatabase();
        return i;
    }


    private synchronized SQLiteDatabase getDatabase() {
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
