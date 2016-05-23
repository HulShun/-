package com.example.newsclient.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

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
    @Bind(R.id.activity_imageitem_cout_tv)
    TextView cout_tv;
    @Bind(R.id.activity_imageitem_more_ib)
    ImageButton more_btn;
    @Bind(R.id.activity_imageitem_back_ib)
    ImageButton back_btn;
    @Bind(R.id.activity_imageitem_tv_title)
    TextView title_tv;

    private FragmentManager manager;
    private FragmentPagerAdapter mFragmentAdapter;

    private ImageContentBean mData;


    @Override
    protected int getToolBarId() {
        return 0;
    }


    @Override
    protected void init() {
        Intent intent = getIntent();
        String type = intent.getStringExtra("type");
        String itemid = intent.getStringExtra("itemid");
        String page = intent.getStringExtra("page");

        Map<String, String> map = new HashMap<>();
        map.put("type", type);
        map.put("page", page);
        map.put("itemid", itemid);
        getPresenter().getImages(map);

        manager = getSupportFragmentManager();
        cout_tv.setText("");
        title_tv.setText("");
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageItemActivity.this.finish();
            }
        });
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
        title_tv.setText(data.getTitle());

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
                bundle.putString("big_url", mData.getList().get(position).getBig());
                //之前的图片列表已经加载了第一张图
                if (position == 0) {
                    //     byte[] bs = getIntent().getByteArrayExtra("middle_img");
                    //    bundle.putByteArray("middle_img", bs);
                    //  bundle.putString("middle_url", mData.getList().get(position).getMiddle());
                }
                //    bundle.putByteArray("middle_img", imgBytes);
                fragment.setArguments(bundle);
                return fragment;
            }

            @Override
            public int getCount() {
                return mData.getList().size();
            }
        };
        cout_tv.setText(1 + "/" + mData.getList().size());

        viewPager.setAdapter(mFragmentAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                cout_tv.setText(position + 1 + "/" + mData.getList().size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}
