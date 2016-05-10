package com.example.newsclient.view.activity;

import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.newsclient.Model.bean.ImageMainTypeBean;
import com.example.newsclient.Model.bean.VideoTypeBean;
import com.example.newsclient.Model.utils.AppUtil;
import com.example.newsclient.R;
import com.example.newsclient.presenter.MainViewPresenter;
import com.example.newsclient.view.Constant;
import com.example.newsclient.view.adapter.MyFragmentAdapter;
import com.example.newsclient.view.fragment.ImageClassifyFragment;
import com.example.newsclient.view.fragment.NewsClassifyFragment;
import com.example.newsclient.view.fragment.VideoClassifyFramgent;
import com.example.newsclient.view.impl.IMainViewImpl;
import com.example.newsclient.view.service.NetWorkBroadcastReceiver;

import java.util.List;

import butterknife.Bind;

public class MainActivity extends BaseActivity<MainViewPresenter> implements IMainViewImpl {


    private FragmentPagerAdapter mFragmentAdapter;
    private FragmentManager mFragmentManager;
    private List<String> mTab_list;

    private NetWorkBroadcastReceiver networkReceiver;
    private int nowMenuItemId;

    @Bind(R.id.tablayout)
    TabLayout tablayout;
    @Bind(R.id.main_viewpager)
    ViewPager mainViewpager;
    @Bind(R.id.main_navi)
    NavigationView mainNavi;
    @Bind(R.id.main_drawer)
    DrawerLayout mainDrawer;

    /*网络无连接提示条布局*/
    @Bind(R.id.main_messageview)
    RelativeLayout messageView;
    /*网络无连接提示条中的文本*/
    @Bind(R.id.main_messagetext)
    TextView messageText;
    /*网络无连接提示条中的按钮*/
    @Bind(R.id.main_messagebtn)
    TextView messageBtn;


    @Override
    protected void init() {
        initViews();
        initDatas();
        register();

    }

    /**
     * 注册网络变化广播
     */
    private void register() {
        networkReceiver = new NetWorkBroadcastReceiver(myHandler);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(networkReceiver, intentFilter);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected int getToolBarId() {
        return R.id.main_toolbar;
    }

    @Override
    protected MainViewPresenter initPresenter() {
        return new MainViewPresenter();
    }

    private void initDatas() {
        mFragmentManager = getSupportFragmentManager();
        loadingBitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_loading);
        nowMenuItemId = R.id.nav_news;
        mainNavi.setCheckedItem(nowMenuItemId);
        mPresenter.loadNewsType(this);
    }

    private void initViews() {
        //不统一icon的颜色
        mainNavi.setItemIconTintList(null);

        //tab文本居中
        tablayout.setTabGravity(TabLayout.GRAVITY_CENTER);
        //tab可滑动
        tablayout.setTabMode(TabLayout.MODE_SCROLLABLE);

        if (!AppUtil.getInstance().isNetWorkConnected()) {
            myHandler.sendEmptyMessage(Constant.UPDATE_NONETWORK);
        }

        messageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myHandler.sendEmptyMessage(Constant.UPDATE_NETWORK);
            }
        });

        //导航栏菜单事件
        mainNavi.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int id = item.getItemId();
                item.setChecked(true);
                switch (id) {
                    //新闻
                    case R.id.nav_news:
                        mainDrawer.closeDrawers();
                        if (nowMenuItemId != id) {
                            mPresenter.loadNewsType(MainActivity.this);
                        }
                        break;
                    //图片
                    case R.id.nav_image:
                        mainDrawer.closeDrawers();
                        if (nowMenuItemId != id) {
                            mPresenter.loadImageTpyes();
                        }
                        break;
                    //视频
                    case R.id.nav_video:
                        mainDrawer.closeDrawers();
                        if (nowMenuItemId != id) {
                            mPresenter.loadVideoTypes();
                        }
                        break;

                }
                nowMenuItemId = id;
                return true;
            }
        });
    }


    @Override
    public void showFaild(String msg) {

    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void showNoNetWork() {
        Toast.makeText(MainActivity.this, "网络连接失败了...", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onImageTabs(List<ImageMainTypeBean> tabs) {
        if (tabs.size() == 0) {
            showFaild("tab获取失败");
            return;
        }
        mFragmentAdapter = new MyFragmentAdapter(mFragmentManager, tabs, ImageClassifyFragment.class);
        setViewpagerAndTablayout();
    }

    @Override
    public void onVideoTabs(List<VideoTypeBean.VideoCategoriesBean> tabs) {
        if (tabs.size() == 0) {
            showFaild("tab获取失败");
            return;
        }

        mFragmentAdapter = new MyFragmentAdapter(mFragmentManager, tabs, VideoClassifyFramgent.class);
        setViewpagerAndTablayout();
    }

    @Override
    public void onNewsTabs(List<String> tabs) {
        mFragmentAdapter = new MyFragmentAdapter(mFragmentManager, tabs, NewsClassifyFragment.class);
        setViewpagerAndTablayout();
    }


    public void setViewpagerAndTablayout() {

        List<Fragment> fragments = mFragmentManager.getFragments();
        if (fragments != null && !fragments.isEmpty()) {
            fragments.clear();
        }
        mainViewpager.setAdapter(mFragmentAdapter);
        tablayout.setupWithViewPager(mainViewpager);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(networkReceiver);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case ArticleActivity.ARTICLE_CODE:
                //从新闻详情页返回，要将tab显示为当前的新闻所在的tab页
                Bundle bundle = data.getBundleExtra("article");
                String key = bundle.getString("key");
                int position = 0;
                for (int i = 0; i < mTab_list.size(); i++) {
                    if (key.equals(mTab_list.get(i))) {
                        position = i;
                    }
                }
                if (mainViewpager != null) {
                    mainViewpager.setCurrentItem(position);
                }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public Bitmap loadingBitmap;

    private Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constant.UPDATE_NETWORK:
                    if (messageView.getVisibility() == View.VISIBLE) {
                        messageView.setVisibility(View.GONE);
                    }
                    break;

                case Constant.UPDATE_NONETWORK:
                    messageView.setVisibility(View.VISIBLE);
                    messageText.setText("没有连接网络...");
            }
        }
    };


}
