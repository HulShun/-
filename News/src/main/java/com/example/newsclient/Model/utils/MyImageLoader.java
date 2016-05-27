package com.example.newsclient.Model.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.util.LruCache;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.example.newsclient.view.utils.AppUtil;
import com.jakewharton.disklrucache.DiskLruCache;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by Administrator on 2016-04-11.
 */
public class MyImageLoader {
    private static MyImageLoader myImageLoader;
    private static ImageLoader imageLoader;
    private RequestQueue mQueue;
    private Context mContext;

    public static MyImageLoader newInstance() {
        if (myImageLoader == null) {
            synchronized (MyImageLoader.class) {
                if (myImageLoader == null) {
                    myImageLoader = new MyImageLoader();
                }
            }
        }
        return myImageLoader;
    }

    public static ImageLoader getImageLoader() {
        return imageLoader;
    }

    private MyImageLoader() {

    }

    public void init(Context c) {
        mContext = c;
        mQueue = Volley.newRequestQueue(mContext);
        imageLoader = new ImageLoader(mQueue, new BitmapCache());
    }

    class BitmapCache implements ImageLoader.ImageCache {

        private long diskSize = 100 * 1024 * 1024;
        //内存缓存
        private LruCache<String, Bitmap> lruCache;
        /*磁盘缓存*/
        private DiskLruCache diskLruCache;

        public BitmapCache() {

            int maxSize = (int) (Runtime.getRuntime().maxMemory() / 8);

            lruCache = new LruCache<String, Bitmap>(maxSize) {
                @Override
                protected int sizeOf(String key, Bitmap value) {
                    int size = value.getRowBytes() * value.getHeight();
                    return size;
                }
            };

            String dir = "bitmap";
            String cacheDir;
            if (AppUtil.getInstance().checkExternalStorageState()) {
                cacheDir = mContext.getExternalCacheDir().getAbsolutePath();
            } else {
                cacheDir = mContext.getCacheDir().getAbsolutePath();
            }
            File cacheFile = new File(cacheDir, dir);
            if (!cacheFile.exists()) {
                cacheFile.mkdirs();
            }
            try {
                diskLruCache = DiskLruCache.open(cacheFile, AppUtil.getInstance().getAppVersion(), 1, diskSize);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        @Override
        public Bitmap getBitmap(String url) {
            //内存缓存中获取
            Bitmap bitmap = lruCache.get(url);
            if (bitmap != null) {
                return bitmap;
            }
            //磁盘缓存中获取
            String hashCode = MD5Util.stringMD5(url);
            try {
                DiskLruCache.Snapshot snapshot = diskLruCache.get(hashCode);
                if (snapshot == null) {
                    return null;
                }
                bitmap = BitmapFactory.decodeStream(snapshot.getInputStream(0));
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (bitmap != null) {
                return bitmap;
            }

            return null;
        }

        @Override
        public void putBitmap(String url, Bitmap bitmap) {
            //写入内存缓存
            lruCache.put(url, bitmap);
            String hashCode = MD5Util.stringMD5(url);
            try {
                //写入磁盘
                DiskLruCache.Editor editor = diskLruCache.edit(hashCode);
                OutputStream out = editor.newOutputStream(0);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                editor.commit();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


}



