package com.example.newsclient.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.example.newsclient.Model.bean.image.ImageContentBean;
import com.example.newsclient.R;
import com.example.newsclient.presenter.ImageItemPresenter;
import com.example.newsclient.view.fragment.ImageItemFragment;
import com.example.newsclient.view.impl.ImageItemViewImpl;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;

/**
 * Created by Administrator on 2016/5/22.
 */
public class ImageItemActivity extends BaseActivity<ImageItemPresenter> implements ImageItemViewImpl {
    @Bind(R.id.imageitem_viewpager)
    ViewPager viewPager;

    private FragmentManager manager;
    private FragmentPagerAdapter mFragmentAdapter;

    private String type;
    private String page;
    private String itemid;

    private ImageContentBean mData;

    @Override
    protected int getToolBarId() {
        return 0;
    }



    @Override
    protected void init() {
        Intent intent = getIntent();
        type = String.valueOf(intent.getIntExtra("type", 0));
        page = String.valueOf(intent.getIntExtra("page", 0));
        itemid = intent.getStringExtra("itemid");

        Map<String, String> map = new HashMap<>();
        map.put("type", type);
        map.put("page", page);
        map.put("itemid", itemid);
        getPresenter().getImages(map);


        manager = getSupportFragmentManager();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_imageitem;
    }

    @Override
    protected ImageItemPresenter initPresenter() {
        return new ImageItemPresenter();
    }

    @Override
    public void onImagesResult(ImageContentBean data) {
        mData = data;
        if (mFragmentAdapter == null) {
            initAdapter();
        }

    }


    private void initAdapter() {
        if (mData == null) {
            return;
        }
        mFragmentAdapter = new FragmentPagerAdapter(manager) {
            @Override
            public Fragment getItem(int position) {
                Fragment fragment = new ImageItemFragment();
                Bundle bundle = new Bundle();
                bundle.putString("url", mData.getList().get(position).getBig());
                fragment.setArguments(bundle);
                return fragment;
            }

            @Override
            public int getCount() {
                return mData.getList().size();
            }
        };

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        viewPager.setAdapter(mFragmentAdapter);
    }
}
