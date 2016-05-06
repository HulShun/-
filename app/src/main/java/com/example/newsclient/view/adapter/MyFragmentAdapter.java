package com.example.newsclient.view.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.newsclient.Configuration;
import com.example.newsclient.Model.LogUtil;
import com.example.newsclient.Model.bean.ImageMainTpyeBean;
import com.example.newsclient.Model.bean.ImageTpyeBean;
import com.example.newsclient.view.fragment.ImageClassifyFragment;
import com.example.newsclient.view.fragment.ImagesFramgent;
import com.example.newsclient.view.fragment.NewsFragment;

import java.util.List;

/**
 * Created by Administrator on 2016-04-11.
 */
public class MyFragmentAdapter extends FragmentPagerAdapter {
    private static String NEWS_CLASSNAME = NewsFragment.class.getName();
    private static String IMAGES_CLASSNAME = ImagesFramgent.class.getName();
    private static String IMAGECLASSIFY = ImageClassifyFragment.class.getName();
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
        Bundle bundle = new Bundle();
        if (mCz.getName().contentEquals(NEWS_CLASSNAME)) {
            title = (String) datas.get(position);
            fragment = new NewsFragment();
            bundle.putString(Configuration.KEYWORD, title);
        } else if (mCz.getName().contentEquals(IMAGES_CLASSNAME)) {
            fragment = new ImagesFramgent();
            bundle.putParcelable("type", (ImageMainTpyeBean) datas.get(position));
        } else if (mCz.getName().contentEquals(IMAGECLASSIFY)) {
            fragment = new ImageClassifyFragment();
            bundle.putParcelable("type", (ImageMainTpyeBean) datas.get(position));
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
        } else if (mCz.getName().contentEquals(IMAGES_CLASSNAME)) {
            title = ((ImageTpyeBean) o).getName();
        } else if (mCz.getName().contentEquals(IMAGECLASSIFY)) {
            title = ((ImageMainTpyeBean) o).getName();
        }
        LogUtil.d("type", "tab文字：" + title);
        return title;
    }
}
