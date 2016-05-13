package com.example.newsclient.view.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.newsclient.Configuration;
import com.example.newsclient.Model.bean.image.ImageMainTypeBean;
import com.example.newsclient.Model.bean.video.VideoTypeBean;
import com.example.newsclient.view.fragment.ImageClassifyFragment;
import com.example.newsclient.view.fragment.NewsClassifyFragment;
import com.example.newsclient.view.fragment.VideoClassifyFramgent;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * Created by Administrator on 2016-04-11.
 */
public class MyFragmentAdapter extends FragmentPagerAdapter {
    private static String NEWS_CLASSNAME = NewsClassifyFragment.class.getName();
    // private static String IMAGES_CLASSNAME = ImagesFramgent.class.getName();
    private static String IMAGECLASSIFY = ImageClassifyFragment.class.getName();
    private static String VIDEOCLASSIFY = VideoClassifyFramgent.class.getName();
    private List datas;
    private Class mCz;

    /**
     * @param manager
     * @param list    数据
     * @param cz      framgment类
     */
    public MyFragmentAdapter(FragmentManager manager, List<?> list, Class cz) {
        super(manager);
        datas = list;
        mCz = cz;
    }

    @Override
    public Fragment getItem(int position) {
        String title = null;
        Fragment fragment = null;
        try {
            fragment = (Fragment) mCz.getConstructor().newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        Bundle bundle = new Bundle();
        //新闻列表
        if (mCz.getName().contentEquals(NEWS_CLASSNAME)) {
            title = (String) datas.get(position);
            bundle.putString(Configuration.KEYWORD, title);
            //图片列表
        } else if (mCz.getName().contentEquals(IMAGECLASSIFY)) {
            bundle.putParcelable("type", (ImageMainTypeBean) datas.get(position));
        }//视频列表
        else if (mCz.getName().contentEquals(VIDEOCLASSIFY)) {
            bundle.putParcelable("type", (VideoTypeBean.VideoCategoriesBean) (datas.get(position)));
        }
        if (fragment != null) {
            fragment.setArguments(bundle);
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Object o = datas.get(position);
        String title = null;
        if (mCz.getName().contentEquals(NEWS_CLASSNAME)) {
            title = (String) o;
        } else if (mCz.getName().contentEquals(IMAGECLASSIFY)) {
            title = ((ImageMainTypeBean) o).getName();
        } else if (mCz.getName().contentEquals(VIDEOCLASSIFY)) {
            title = ((VideoTypeBean.VideoCategoriesBean) o).getLabel();
        }
        //   LogUtil.d("type", "tab文字：" + title);
        return title;
    }
}
