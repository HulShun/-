package com.example.newsclient.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.newsclient.Configuration;
import com.example.newsclient.Model.bean.ImageMainTypeBean;
import com.example.newsclient.Model.bean.ImageTpyeBean;
import com.example.newsclient.MyApplication;
import com.example.newsclient.dao.helper.ImageTypeHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Administrator on 2016-04-14.
 */
public class ImageTypeDao {

    private static ImageTypeDao iamgesDao;
    private ImageTypeHelper mHelper;
    private SQLiteDatabase mdb;

    //线程安全的Integer
    private AtomicInteger atomicInteger;


    public static ImageTypeDao getInstance() {
        if (iamgesDao == null) {
            synchronized (ImageTypeDao.class) {
                if (iamgesDao == null) {
                    iamgesDao = new ImageTypeDao();
                }
            }
        }
        return iamgesDao;
    }

    private ImageTypeDao() {
        Context context = MyApplication.getInstance();
        mHelper = new ImageTypeHelper(context, Configuration.IMAGE_TYPE_DB_NAEM, null, 1);
        atomicInteger = new AtomicInteger();
        //  atomicInteger.incrementAndGet();
    }

    private ContentValues getMainTypeValues(ImageMainTypeBean mainTypeBean) {
        ContentValues cv = new ContentValues();
        cv.put(ImageTypeHelper.MAINNAME, mainTypeBean.getName());
        return cv;
    }

    private ContentValues getTypeValues(String mainName, ImageTpyeBean typeBean) {
        ContentValues cv = new ContentValues();
        cv.put(ImageTypeHelper.ID, typeBean.getId());
        cv.put(ImageTypeHelper.NAME, typeBean.getName());
        cv.put(ImageTypeHelper.MAINNAME, mainName);
        return cv;
    }

    public synchronized void insert(ImageMainTypeBean mainTpyeBean) {
        mdb = getDatabase();
        mdb.insert(Configuration.IMAGE_MAIN_TYPE_TABLE_NAEM, null, getMainTypeValues(mainTpyeBean));
        for (ImageTpyeBean bean : mainTpyeBean.getList()) {
            mdb.insert(Configuration.IMAGE_TYPE_TABLE_NAEM, null, getTypeValues(mainTpyeBean.getName(), bean));
        }

        closeDatabase();
    }

    public synchronized void insert(List<ImageMainTypeBean> list) {
        if (list == null) {
            return;
        }
        for (ImageMainTypeBean bean : list) {
            insert(bean);
        }
        closeDatabase();
    }

    public synchronized List<ImageMainTypeBean> query() {
        mdb = getDatabase();
        String[] columns = {
                ImageTypeHelper._ID,
                ImageTypeHelper.MAINNAME
        };
        //拿到所有的数据

        Cursor c = mdb.query(Configuration.IMAGE_MAIN_TYPE_TABLE_NAEM,
                columns,
                null,
                null,
                null,
                null,
                ImageTypeHelper._ID);

        List<ImageMainTypeBean> beans = new ArrayList<>();

        String selection2 = ImageTypeHelper.MAINNAME + " = ? ";
        String[] columns2 = {
                ImageTypeHelper.ID,
                ImageTypeHelper.NAME
        };


        while (c.moveToNext()) {
            ImageMainTypeBean mainbean = new ImageMainTypeBean();
            mainbean.setName(c.getString(1));
            Cursor c1 = mdb.query(Configuration.IMAGE_TYPE_TABLE_NAEM,
                    columns2,
                    selection2,
                    new String[]{mainbean.getName()},
                    null,
                    null,
                    ImageTypeHelper.ID);
            List<ImageTpyeBean> typeList = new ArrayList<>();
            while (c1.moveToNext()) {
                ImageTpyeBean typeBean = new ImageTpyeBean();
                typeBean.setId(c1.getInt(0));
                typeBean.setName(c1.getString(1));
                typeList.add(typeBean);
            }
            mainbean.setList(typeList);
            beans.add(mainbean);
        }
        c.close();
        closeDatabase();
        return beans;
    }


    public synchronized int deleteAll() {
        mdb = getDatabase();
        int i = mdb.delete(Configuration.IMAGE_MAIN_TYPE_TABLE_NAEM, null, null);
        int m = mdb.delete(Configuration.IMAGE_TYPE_TABLE_NAEM, null, null);
        closeDatabase();
        return i;
    }


    private synchronized SQLiteDatabase getDatabase() {
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
