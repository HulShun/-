package com.example.newsclient.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import com.example.newsclient.Configuration;
import com.example.newsclient.Model.bean.image.ImageBean;
import com.example.newsclient.Model.bean.image.ImageContentBean;
import com.example.newsclient.Model.bean.image.ImageJsonBean;
import com.example.newsclient.Model.bean.image.ImageMainTypeBean;
import com.example.newsclient.Model.bean.image.ImageTypeBean;
import com.example.newsclient.MyApplication;
import com.example.newsclient.dao.helper.ImagesHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Administrator on 2016-04-14.
 */
public class ImagesDao {

    private static ImagesDao iamgesDao;
    private ImagesHelper mHelper;
    private SQLiteDatabase mdb;

    //线程安全的Integer
    private AtomicInteger atomicInteger;


    public static ImagesDao getInstance() {
        if (iamgesDao == null) {
            synchronized (ImagesDao.class) {
                if (iamgesDao == null) {
                    iamgesDao = new ImagesDao();
                }
            }
        }
        return iamgesDao;
    }

    private ImagesDao() {
        Context context = MyApplication.getInstance();
        mHelper = new ImagesHelper(context, Configuration.IMAGE_TYPE_DB_NAEM, null, 1);
        atomicInteger = new AtomicInteger();
        //  atomicInteger.incrementAndGet();
    }

    private ContentValues getMainTypeValues(ImageMainTypeBean mainTypeBean) {
        ContentValues cv = new ContentValues();
        cv.put(ImagesHelper.MAINNAME, mainTypeBean.getName());
        return cv;
    }

    private ContentValues getTypeValues(String mainName, ImageTypeBean typeBean) {
        ContentValues cv = new ContentValues();
        cv.put(ImagesHelper.ID, typeBean.getId());
        cv.put(ImagesHelper.NAME, typeBean.getName());
        cv.put(ImagesHelper.MAINNAME, mainName);
        return cv;
    }

    private ContentValues getImageInformValues(ImageContentBean contentBean, int page) {
        ContentValues cv = new ContentValues();
        cv.put(ImagesHelper.TYPE, contentBean.getType());
        cv.put(ImagesHelper.TYPENAME, contentBean.getTypeName());
        cv.put(ImagesHelper.CT, contentBean.getCt());
        cv.put(ImagesHelper.TITLE, contentBean.getTitle());
        cv.put(ImagesHelper.ITEMID, contentBean.getItemId());
        cv.put(ImagesHelper.CURRENTPAGE, page);
        return cv;
    }

    private ContentValues getImageValues(String itemID, ImageBean imageBean) {
        ContentValues cv = new ContentValues();
        cv.put(ImagesHelper.ITEMID, itemID);
        cv.put(ImagesHelper.SMALL, imageBean.getSmall());
        cv.put(ImagesHelper.MIDDLE, imageBean.getMiddle());
        cv.put(ImagesHelper.BIG, imageBean.getBig());
        return cv;
    }


    public synchronized void insertMainType(ImageMainTypeBean mainTpyeBean) {
        mdb = getDatabase();
        mdb.insert(Configuration.IMAGE_MAIN_TYPE_TABLE_NAEM, null, getMainTypeValues(mainTpyeBean));
        for (ImageTypeBean bean : mainTpyeBean.getList()) {
            mdb.insert(Configuration.IMAGE_TYPE_TABLE_NAEM, null, getTypeValues(mainTpyeBean.getName(), bean));
        }

        closeDatabase();
    }

    public synchronized void insertMainType(List<ImageMainTypeBean> list) {
        if (list == null) {
            return;
        }
        for (ImageMainTypeBean bean : list) {
            insertMainType(bean);
        }
        closeDatabase();
    }

    public void insertImageInform(ImageJsonBean jsonbean) {
        mdb = getDatabase();
        int page = jsonbean.getShowapi_res_body().getPagebean().getCurrentPage();
        List<ImageContentBean> contentBeen = jsonbean.getShowapi_res_body().getPagebean().getContentlist();
        for (ImageContentBean bean : contentBeen) {
            mdb.insert(Configuration.IMAGE_INFORM_TABLE_NAEM, null, getImageInformValues(bean, page));
            List<ImageBean> imageBeen = bean.getList();
            for (ImageBean bean2 : imageBeen) {
                mdb.insert(Configuration.IMAGE_TABLE_NAEM, null, getImageValues(bean.getItemId(), bean2));
            }
        }
        closeDatabase();
    }

    public synchronized List<ImageMainTypeBean> queryMainType() {
        mdb = getDatabase();
        String[] columns = {
                ImagesHelper._ID,
                ImagesHelper.MAINNAME
        };
        //拿到所有的数据
        Cursor c = mdb.query(Configuration.IMAGE_MAIN_TYPE_TABLE_NAEM,
                columns,
                null,
                null,
                null,
                null,
                ImagesHelper._ID);

        List<ImageMainTypeBean> beans = new ArrayList<>();

        while (c.moveToNext()) {
            ImageMainTypeBean mainbean = new ImageMainTypeBean();
            mainbean.setName(c.getString(1));

            List<ImageTypeBean> typeList = queryImageTypeBeen(mainbean.getName());
            mainbean.setList(typeList);
            beans.add(mainbean);
        }
        c.close();
        closeDatabase();
        return beans;
    }

    @NonNull
    private List<ImageTypeBean> queryImageTypeBeen(String mainName) {
        String selection2 = ImagesHelper.MAINNAME + " = ? ";
        String[] columns2 = {
                ImagesHelper.ID,
                ImagesHelper.NAME
        };
        Cursor c1 = mdb.query(Configuration.IMAGE_TYPE_TABLE_NAEM,
                columns2,
                selection2,
                new String[]{mainName},
                null,
                null,
                ImagesHelper.ID);
        List<ImageTypeBean> typeList = new ArrayList<>();
        while (c1.moveToNext()) {
            ImageTypeBean typeBean = new ImageTypeBean();
            typeBean.setId(c1.getInt(0));
            typeBean.setName(c1.getString(1));
            typeList.add(typeBean);
        }
        return typeList;
    }

    public ImageJsonBean.ShowapiResBodyBean.PagebeanBean queryImageContent(String type) {
        mdb = getDatabase();
        String[] columns = {
                ImagesHelper.TYPE,
                ImagesHelper.TYPENAME,
                ImagesHelper.CT,
                ImagesHelper.TITLE,
                ImagesHelper.ITEMID,
                ImagesHelper.CURRENTPAGE
        };
        String selection = ImagesHelper.TYPE + " = ? ";
        //拿到所有的数据
        Cursor c = mdb.query(Configuration.IMAGE_INFORM_TABLE_NAEM,
                columns,
                selection,
                new String[]{type},
                null,
                null,
                ImagesHelper.TYPE);
        if (!c.moveToFirst()) {
            c.close();
            closeDatabase();
            return null;
        }
        c.moveToPrevious();

        ImageJsonBean.ShowapiResBodyBean.PagebeanBean pagebean = new ImageJsonBean.ShowapiResBodyBean.PagebeanBean();
        List<ImageContentBean> beans = new ArrayList<>();
        while (c.moveToNext()) {
            ImageContentBean mainbean = new ImageContentBean();
            mainbean.setType(c.getInt(0));
            mainbean.setTypeName(c.getString(1));
            mainbean.setCt(c.getString(2));
            mainbean.setTitle(c.getString(3));
            mainbean.setItemId(c.getString(4));
            pagebean.setCurrentPage(c.getInt(5));
            //图片信息
            List<ImageBean> imageList = queryImageBeen(mainbean.getItemId());
            mainbean.setList(imageList);
            beans.add(mainbean);
        }
        pagebean.setContentlist(beans);
        c.close();
        closeDatabase();
        return pagebean;
    }

    @NonNull
    private List<ImageBean> queryImageBeen(String itemid) {
        String selection2 = ImagesHelper.ITEMID + " = ? ";
        String[] columns2 = {
                ImagesHelper.SMALL,
                ImagesHelper.MIDDLE,
                ImagesHelper.BIG
        };
        Cursor c1 = mdb.query(Configuration.IMAGE_TABLE_NAEM,
                columns2,
                selection2,
                new String[]{itemid},
                null,
                null,
                null);
        List<ImageBean> imageList = new ArrayList<>();
        while (c1.moveToNext()) {
            ImageBean imageBean = new ImageBean();
            imageBean.setSmall(c1.getString(0));
            imageBean.setMiddle(c1.getString(1));
            imageBean.setBig(c1.getString(2));
            imageList.add(imageBean);
        }
        c1.close();
        return imageList;
    }


    public synchronized int deleteAll() {
        mdb = getDatabase();
        int i = mdb.delete(Configuration.IMAGE_MAIN_TYPE_TABLE_NAEM, null, null);
        int m = mdb.delete(Configuration.IMAGE_TYPE_TABLE_NAEM, null, null);
        closeDatabase();
        return i;
    }

    public int deleteImages() {
        mdb = getDatabase();
        int i = mdb.delete(Configuration.IMAGE_INFORM_TABLE_NAEM, null, null);
        mdb.delete(Configuration.IMAGE_TABLE_NAEM, null, null);
        closeDatabase();
        return i;
    }

    public int deleteImage(Map<String, String> map) {
        mdb = getDatabase();
        String itemID = map.get("itemid");
        String type = map.get("type");
        //从图片信息表中删除
        int i = mdb.delete(Configuration.IMAGE_TABLE_NAEM, ImagesHelper.ITEMID + " = ? ", new String[]{itemID});
        //从分类信息表中删除
        int m = mdb.delete(Configuration.IMAGE_INFORM_TABLE_NAEM, ImagesHelper.TYPE + " = ? ", new String[]{type});
        closeDatabase();
        return m;

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
        if (atomicInteger.decrementAndGet() <= 0) {
            mdb.close();
        }
    }


}
